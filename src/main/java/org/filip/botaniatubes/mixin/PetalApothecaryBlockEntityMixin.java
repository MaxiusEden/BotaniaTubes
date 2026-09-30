package org.filip.botaniatubes.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.filip.botaniatubes.ApothecaryHandlerHolder;
import org.filip.botaniatubes.ApothecaryInteractHandler;
import org.filip.botaniatubes.PetalApothecaryFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.block.PetalApothecary.State;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;

@Mixin(value = PetalApothecaryBlockEntity.class, remap = false)
public abstract class PetalApothecaryBlockEntityMixin implements ApothecaryHandlerHolder {

    @Unique
    private PetalApothecaryFluidHandler botaniatubes$handler;

    @Override
    public PetalApothecaryFluidHandler botaniatubes$handler() {
        if (botaniatubes$handler == null) {
            botaniatubes$handler = new PetalApothecaryFluidHandler((PetalApothecaryBlockEntity) (Object) this);
        }
        return botaniatubes$handler;
    }

    @Inject(method = "collideEntityItem", at = @At("HEAD"), cancellable = true)
    private void botaniatubes$rejectOtherFluid(ItemEntity item, CallbackInfoReturnable<Boolean> cir) {
        PetalApothecaryBlockEntity apothecary = (PetalApothecaryBlockEntity) (Object) this;
        if (apothecary.getFluid() == State.EMPTY
                && PetalApothecaryFluidHandler.rejects(apothecary, ApothecaryInteractHandler.fluidOf(item.getItem()))) {
            cir.setReturnValue(false);
        }
    }
}
