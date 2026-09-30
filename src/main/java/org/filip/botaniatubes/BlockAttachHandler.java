package org.filip.botaniatubes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Botaniatubes.MOD_ID)
public final class BlockAttachHandler {

    private static final ResourceLocation FLUID_HANDLER_ID = new ResourceLocation(Botaniatubes.MOD_ID, "fluid_handler");

    private BlockAttachHandler() {
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        if (!(event.getObject() instanceof ApothecaryHandlerHolder holder)) {
            return;
        }

        FluidHandlerProvider provider = new FluidHandlerProvider(holder.botaniatubes$handler());
        event.addCapability(FLUID_HANDLER_ID, provider);
        event.addListener(provider::invalidate);
    }
}
