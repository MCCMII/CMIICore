package com.cmii.cmiicore.api;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import vazkii.botania.api.subtile.SubTileFunctional;

/**
 * Purifying Flower (净化花).
 * A functional flower that consumes mana from a linked mana pool
 * to gradually cleanse Thaumcraft 6 flux from the chunk it resides in.
 *
 * <ul>
 *   <li>Mana cost per operation: 500</li>
 *   <li>Max mana buffer: 1000</li>
 *   <li>Operation interval: 100 ticks (5 seconds)</li>
 *   <li>Flux removed per operation: 1.0</li>
 *   <li>Redstone signal disables the flower</li>
 * </ul>
 */
public class SubTilePurifyingFlower extends SubTileFunctional {

    private static final int MANA_COST = 500;
    private static final int MAX_MANA = 1000;
    private static final int CLEAN_INTERVAL = 100;
    private static final float FLUX_REMOVED_PER_OP = 1.0F;
    private static final int COLOR = 0xE0FFFF;

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (getWorld().isRemote) {
            return;
        }

        if (redstoneSignal > 0) {
            return;
        }

        if (ticksExisted % CLEAN_INTERVAL != 0) {
            return;
        }

        if (mana < MANA_COST) {
            return;
        }

        World world = getWorld();
        BlockPos pos = getPos();

        boolean cleaned = TC6FluxHelper.drainFlux(
            world, pos, FLUX_REMOVED_PER_OP);
        if (cleaned) {
            mana -= MANA_COST;
            sync();
        }
    }

    @Override
    public int getColor() {
        return COLOR;
    }

    @Override
    public int getMaxMana() {
        return MAX_MANA;
    }

    @Override
    public boolean acceptsRedstone() {
        return true;
    }
}