// FluidHandlerProvider.java
package org.filip.botaniatubes;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidHandlerProvider implements ICapabilityProvider {

    private final LazyOptional<IFluidHandler> fluidHandler;

    public FluidHandlerProvider(LazyOptional<IFluidHandler> fluidHandler) {
        this.fluidHandler = fluidHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        }
        return LazyOptional.empty();
    }
}