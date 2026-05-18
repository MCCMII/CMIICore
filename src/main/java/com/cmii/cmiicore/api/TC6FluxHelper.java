package com.cmii.cmiicore.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.Method;

/**
 * Reflection-based bridge to Thaumcraft 6 flux system.
 * Uses public AuraHandler.drainFlux / getFlux for safety and forward compat.
 */
public final class TC6FluxHelper {

    private static boolean available;
    private static Method drainFluxMethod;
    private static Method getFluxMethod;

    static {
        try {
            Class<?> auraHandler = Class.forName(
                "thaumcraft.common.world.aura.AuraHandler");

            drainFluxMethod = auraHandler.getMethod(
                "drainFlux", World.class, BlockPos.class,
                float.class, boolean.class);
            getFluxMethod = auraHandler.getMethod(
                "getFlux", World.class, BlockPos.class);

            available = true;
        } catch (Exception e) {
            available = false;
        }
    }

    private TC6FluxHelper() {
    }

    /**
     * Reduce the flux at the given position by the specified amount.
     *
     * @param world   the world
     * @param pos     the position (determines the chunk)
     * @param amount  amount of flux to attempt to remove
     * @return true if flux was actually drained (flux > 0 before drain)
     */
    public static boolean drainFlux(
            World world, BlockPos pos, float amount) {
        if (!available || world.isRemote) {
            return false;
        }
        try {
            float drained = (float) drainFluxMethod.invoke(
                null, world, pos, amount, true);
            return drained > 0.0F;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the current flux level at the given position.
     *
     * @param world  the world
     * @param pos    the position (determines the chunk)
     * @return the current flux value, or 0 if unavailable
     */
    public static float getFlux(World world, BlockPos pos) {
        if (!available || world.isRemote) {
            return 0.0F;
        }
        try {
            return (float) getFluxMethod.invoke(null, world, pos);
        } catch (Exception e) {
            return 0.0F;
        }
    }

    /**
     * Check if the TC6 flux bridge is available.
     *
     * @return true if TC6 flux classes were found via reflection
     */
    public static boolean isAvailable() {
        return available;
    }
}