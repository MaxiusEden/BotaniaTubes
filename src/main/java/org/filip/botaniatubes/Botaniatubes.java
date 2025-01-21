package org.filip.botaniatubes;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Botaniatubes.MOD_ID)
public class Botaniatubes {
    public static final String MOD_ID = "botaniatubes";

    public Botaniatubes() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlockEntities.BLOCK_ENTITIES.register(bus);
        MinecraftForge.EVENT_BUS.register(this);
    }
}