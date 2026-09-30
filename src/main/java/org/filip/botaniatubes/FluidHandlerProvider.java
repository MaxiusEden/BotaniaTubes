package org.filip.botaniatubes;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidHandlerProvider implements ICapabilityProvider {

    private final LazyOptional<IFluidHandler> fluidHandler;

    public FluidHandlerProvider(IFluidHandler handler) {
        this.fluidHandler = LazyOptional.of(() -> handler);
    }

    public void invalidate() {
        fluidHandler.invalidate();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        return ForgeCapabilities.FLUID_HANDLER.orEmpty(capability, fluidHandler);
    }
}
