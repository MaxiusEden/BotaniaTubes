// ModBlockEntities.java
package org.filip.botaniatubes;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.block.BotaniaBlocks;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Botaniatubes.MOD_ID);

    // Construtor privado para impedir a instanciação
    private ModBlockEntities() {
        throw new UnsupportedOperationException("Esta classe não pode ser instanciada.");
    }

    /**
     * Retorna uma lista de variantes válidas dos blocos "Petal Apothecary".
     * Este método é útil em processos que envolvem registro ou comportamento em massa
     * desses blocos.
     * Embora não esteja em uso no momento, foi planejado para facilitar futuras implementações.
     */
    private static Block[] getPetalApothecaryVariants() {
        return new Block[]{
                BotaniaBlocks.defaultAltar,
                BotaniaBlocks.forestAltar,
                BotaniaBlocks.plainsAltar,
                BotaniaBlocks.mountainAltar,
                BotaniaBlocks.fungalAltar,
                BotaniaBlocks.swampAltar,
                BotaniaBlocks.desertAltar,
                BotaniaBlocks.taigaAltar,
                BotaniaBlocks.mesaAltar,
                BotaniaBlocks.mossyAltar,
                BotaniaBlocks.livingrockAltar,
                BotaniaBlocks.deepslateAltar
        };
    }
}