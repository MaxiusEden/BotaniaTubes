package org.filip.botaniatubes;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;

public class PetalApothecaryFluidHandler implements IFluidHandler {

    private final PetalApothecaryBlockEntity apothecaryEntity;

    public PetalApothecaryFluidHandler(PetalApothecaryBlockEntity apothecaryEntity) {
        this.apothecaryEntity = apothecaryEntity;
    }

    @Override
    public int getTanks() {
        return 1; // Apenas um tanque para WATER ou LAVA
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        Fluid fluid = switch (apothecaryEntity.getFluid()) {
            case WATER -> Fluids.WATER;
            case LAVA -> Fluids.LAVA;
            default -> Fluids.EMPTY;
        };

        return fluid != Fluids.EMPTY ? new FluidStack(fluid, 1000) : FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 1000; // Capacidade de um balde
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return stack.getFluid() == Fluids.WATER || stack.getFluid() == Fluids.LAVA;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (apothecaryEntity.getFluid() != vazkii.botania.api.block.PetalApothecary.State.EMPTY) {
            return 0; // O tanque está cheio
        }

        if (resource.getFluid() == Fluids.WATER) {
            if (action.execute()) {
                apothecaryEntity.setFluid(vazkii.botania.api.block.PetalApothecary.State.WATER);
            }
            return 1000; // Tamanho do balde
        } else if (resource.getFluid() == Fluids.LAVA) {
            if (action.execute()) {
                apothecaryEntity.setFluid(vazkii.botania.api.block.PetalApothecary.State.LAVA);
            }
            return 1000;
        }

        return 0; // Fluído não suportado
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        Fluid currentFluid = getFluidInTank(0).getFluid();
        if (resource.getFluid() != currentFluid) {
            return FluidStack.EMPTY;
        }

        return drain(1000, action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        Fluid currentFluid = getFluidInTank(0).getFluid();
        if (currentFluid == Fluids.EMPTY || maxDrain < 1000) {
            return FluidStack.EMPTY;
        }

        FluidStack drained = new FluidStack(currentFluid, 1000);

        if (action.execute()) {
            apothecaryEntity.setFluid(vazkii.botania.api.block.PetalApothecary.State.EMPTY);
        }

        return drained;
    }
}