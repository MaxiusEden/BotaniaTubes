package org.filip.botaniatubes.mixin;

import net.minecraft.nbt.CompoundTag;
import org.filip.botaniatubes.PetalApothecaryFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import vazkii.botania.common.block.block_entity.SimpleInventoryBlockEntity;

@Mixin(value = SimpleInventoryBlockEntity.class, remap = false)
public abstract class SimpleInventoryBlockEntityMixin {

    @Unique
    private static final String botaniatubes$KEY = "botaniatubes:pending";

    @Inject(method = "writePacketNBT", at = @At("TAIL"))
    private void botaniatubes$writePending(CompoundTag tag, CallbackInfo ci) {
        if ((Object) this instanceof PetalApothecaryBlockEntity apothecary) {
            PetalApothecaryFluidHandler.of(apothecary).ifPresent(handler -> tag.put(botaniatubes$KEY, handler.save()));
        }
    }

    @Inject(method = "readPacketNBT", at = @At("TAIL"))
    private void botaniatubes$readPending(CompoundTag tag, CallbackInfo ci) {
        if ((Object) this instanceof PetalApothecaryBlockEntity apothecary) {
            PetalApothecaryFluidHandler.of(apothecary).ifPresent(handler -> handler.load(tag.getCompound(botaniatubes$KEY)));
        }
    }
}
