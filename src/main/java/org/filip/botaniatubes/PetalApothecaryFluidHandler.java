package org.filip.botaniatubes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.block.PetalApothecary.State;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;

import java.util.Optional;

public class PetalApothecaryFluidHandler implements IFluidHandler {

    private static final int BUCKET = FluidType.BUCKET_VOLUME;

    private final PetalApothecaryBlockEntity apothecary;
    private final FluidLevelAnimation animation = new FluidLevelAnimation();

    private Fluid pendingFluid = Fluids.EMPTY;
    private int pendingAmount;

    public PetalApothecaryFluidHandler(PetalApothecaryBlockEntity apothecary) {
        this.apothecary = apothecary;
    }

    public static Optional<PetalApothecaryFluidHandler> of(BlockEntity blockEntity) {
        return blockEntity instanceof ApothecaryHandlerHolder holder
                ? Optional.of(holder.botaniatubes$handler())
                : Optional.empty();
    }

    private static Fluid toFluid(State state) {
        return switch (state) {
            case WATER -> Fluids.WATER;
            case LAVA -> Fluids.LAVA;
            default -> Fluids.EMPTY;
        };
    }

    private static State toState(Fluid fluid) {
        if (fluid.isSame(Fluids.WATER)) {
            return State.WATER;
        }
        if (fluid.isSame(Fluids.LAVA)) {
            return State.LAVA;
        }
        return State.EMPTY;
    }

    private boolean isServer() {
        Level level = apothecary.getLevel();
        return level != null && !level.isClientSide;
    }

    private State currentState() {
        State state = apothecary.getFluid();
        if (state != State.EMPTY && isServer()) {
            pendingFluid = Fluids.EMPTY;
            pendingAmount = 0;
        }
        return state;
    }

    private void setPending(Fluid fluid, int amount) {
        Fluid newFluid = amount > 0 ? fluid : Fluids.EMPTY;
        if (amount == pendingAmount && newFluid == pendingFluid) {
            return;
        }
        pendingAmount = amount;
        pendingFluid = newFluid;
        if (isServer()) {
            BlockState blockState = apothecary.getBlockState();
            apothecary.getLevel().sendBlockUpdated(apothecary.getBlockPos(), blockState, blockState, Block.UPDATE_CLIENTS);
        }
    }

    public boolean rejects(Fluid incoming) {
        State state = toState(incoming);
        return state != State.EMPTY && pendingAmount > 0 && toState(pendingFluid) != state;
    }

    public static boolean rejects(BlockEntity blockEntity, Fluid incoming) {
        return of(blockEntity).map(handler -> handler.rejects(incoming)).orElse(false);
    }

    public FluidLevelAnimation animate() {
        State state = apothecary.getFluid();
        if (state != State.EMPTY) {
            animation.update(state, 1F);
        } else {
            animation.update(toState(pendingFluid), pendingAmount / (float) BUCKET);
        }
        return animation;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        State state = currentState();
        if (state != State.EMPTY) {
            return new FluidStack(toFluid(state), BUCKET);
        }
        return pendingAmount > 0 ? new FluidStack(pendingFluid, pendingAmount) : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return BUCKET;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return toState(stack.getFluid()) != State.EMPTY;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || currentState() != State.EMPTY) {
            return 0;
        }

        State target = toState(resource.getFluid());
        if (target == State.EMPTY || (pendingAmount > 0 && toState(pendingFluid) != target)) {
            return 0;
        }

        int accepted = Math.min(resource.getAmount(), BUCKET - pendingAmount);
        if (action.execute()) {
            if (pendingAmount + accepted == BUCKET) {
                apothecary.setFluid(target);
                setPending(Fluids.EMPTY, 0);
            } else {
                setPending(toFluid(target), pendingAmount + accepted);
            }
            apothecary.setChanged();
        }
        return accepted;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack current = getFluidInTank(0);
        if (resource.isEmpty() || current.isEmpty() || toState(resource.getFluid()) != toState(current.getFluid())) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        State state = currentState();
        if (maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        if (state != State.EMPTY) {
            Fluid fluid = toFluid(state);
            int drained = Math.min(maxDrain, BUCKET);
            if (action.execute()) {
                apothecary.setFluid(State.EMPTY);
                setPending(fluid, BUCKET - drained);
                apothecary.setChanged();
            }
            return new FluidStack(fluid, drained);
        }

        if (pendingAmount == 0) {
            return FluidStack.EMPTY;
        }

        Fluid fluid = pendingFluid;
        int drained = Math.min(maxDrain, pendingAmount);
        if (action.execute()) {
            setPending(fluid, pendingAmount - drained);
            apothecary.setChanged();
        }
        return new FluidStack(fluid, drained);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        if (apothecary.getFluid() == State.EMPTY && pendingAmount > 0) {
            tag.putString("Fluid", toState(pendingFluid) == State.LAVA ? "lava" : "water");
            tag.putInt("Amount", pendingAmount);
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        pendingAmount = Mth.clamp(tag.getInt("Amount"), 0, BUCKET - 1);
        pendingFluid = pendingAmount > 0 ? toFluid("lava".equals(tag.getString("Fluid")) ? State.LAVA : State.WATER) : Fluids.EMPTY;
    }
}
