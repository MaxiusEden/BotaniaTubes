package org.filip.botaniatubes;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import vazkii.botania.api.block.PetalApothecary.State;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import vazkii.botania.common.item.BotaniaItems;

@Mod.EventBusSubscriber(modid = Botaniatubes.MOD_ID)
public final class ApothecaryInteractHandler {

    private ApothecaryInteractHandler() {
    }

    public static Fluid fluidOf(ItemStack stack) {
        if (stack.is(BotaniaItems.waterRod) || stack.getItem() instanceof MobBucketItem) {
            return Fluids.WATER;
        }
        return FluidUtil.getFluidContained(stack).map(FluidStack::getFluid).orElse(Fluids.EMPTY);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        ItemStack held = event.getItemStack();
        if (!(level.getBlockEntity(event.getPos()) instanceof PetalApothecaryBlockEntity apothecary)
                || apothecary.getFluid() != State.EMPTY) {
            return;
        }

        if (PetalApothecaryFluidHandler.rejects(apothecary, fluidOf(held))) {
            cancel(event, InteractionResult.FAIL);
            return;
        }

        if (!FluidUtil.getFluidHandler(held).isPresent()) {
            return;
        }

        if (level.isClientSide) {
            cancel(event, InteractionResult.SUCCESS);
            return;
        }

        IFluidHandler target = PetalApothecaryFluidHandler.of(apothecary).orElse(null);
        if (target == null) {
            return;
        }

        Player player = event.getEntity();
        IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
        FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(held, target, inventory, Integer.MAX_VALUE, player, true);
        if (!result.isSuccess()) {
            result = FluidUtil.tryFillContainerAndStow(held, target, inventory, Integer.MAX_VALUE, player, true);
        }

        if (result.isSuccess()) {
            player.setItemInHand(event.getHand(), result.getResult());
            cancel(event, InteractionResult.CONSUME);
        }
    }

    private static void cancel(PlayerInteractEvent.RightClickBlock event, InteractionResult result) {
        event.setCanceled(true);
        event.setCancellationResult(result);
    }
}
