package org.filip.botaniatubes.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.filip.botaniatubes.FluidLevelAnimation;
import org.filip.botaniatubes.PetalApothecaryFluidHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.block.PetalApothecary.State;
import vazkii.botania.client.render.block_entity.PetalApothecaryBlockEntityRenderer;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;

@Mixin(value = PetalApothecaryBlockEntityRenderer.class, remap = false)
public abstract class PetalApothecaryRendererMixin {

    @Unique
    private static final String botaniatubes$RENDER = "render(Lvazkii/botania/common/block/block_entity/PetalApothecaryBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V";

    @Unique
    private static final float botaniatubes$BASIN_DEPTH = 3F / 16F - 0.01F;

    @Unique
    private State botaniatubes$state = State.EMPTY;

    @Unique
    private float botaniatubes$level;

    @Inject(method = botaniatubes$RENDER, at = @At("HEAD"))
    private void botaniatubes$updateLevel(PetalApothecaryBlockEntity apothecary, float partialTicks, PoseStack poseStack,
                                          MultiBufferSource buffers, int light, int overlay, CallbackInfo ci) {
        FluidLevelAnimation animation = PetalApothecaryFluidHandler.of(apothecary).map(PetalApothecaryFluidHandler::animate).orElse(null);
        if (animation == null) {
            botaniatubes$state = apothecary.getFluid();
            botaniatubes$level = 1F;
        } else {
            botaniatubes$state = animation.state();
            botaniatubes$level = animation.level();
        }
    }

    @Redirect(method = botaniatubes$RENDER, at = @At(value = "INVOKE",
            target = "Lvazkii/botania/common/block/block_entity/PetalApothecaryBlockEntity;getFluid()Lvazkii/botania/api/block/PetalApothecary$State;"))
    private State botaniatubes$displayedFluid(PetalApothecaryBlockEntity apothecary) {
        return botaniatubes$state;
    }

    @ModifyConstant(method = botaniatubes$RENDER, constant = @Constant(floatValue = -0.3125F))
    private float botaniatubes$surfaceHeight(float original) {
        return original - (1F - botaniatubes$level) * botaniatubes$BASIN_DEPTH;
    }
}
