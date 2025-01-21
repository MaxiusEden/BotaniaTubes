package org.filip.botaniatubes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.block.block_entity.PetalApothecaryBlockEntity;

@Mod.EventBusSubscriber(modid = Botaniatubes.MOD_ID)
public class BlockAttachHandler {

    private BlockAttachHandler() {
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<BlockEntity> event) {
        BlockEntity blockEntity = event.getObject();

        // Verifica se é um Petal Apothecary
        if (blockEntity instanceof PetalApothecaryBlockEntity apothecaryEntity) {
            // Cria um LazyOptional<IFluidHandler>, especificando o tipo correto
            LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> new PetalApothecaryFluidHandler(apothecaryEntity));

            // Adiciona a capacidade de manipular fluidos ao Apothecary
            event.addCapability(
                    new ResourceLocation(Botaniatubes.MOD_ID, "fluid_handler"),
                    new FluidHandlerProvider(fluidHandler)
            );
        }
    }
}