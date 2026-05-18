package com.cmii.cmiicore.api;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registry for extra items that can appear in Loonuim loot generation.
 * Other mods or modpack scripts can call {@link #addExtraLoot(ItemStack)}
 * to inject custom items into the Loonuim's loot pool.
 */
public final class LoonuimExtraProducts {

    private static final List<ItemStack> EXTRA_LOOT = new ArrayList<>();

    private LoonuimExtraProducts() {
    }

    /**
     * Register an extra item that has a chance to appear when the Loonuim
     * generates loot. The item is added to the shuffled pool alongside
     * the vanilla dungeon chest loot table entries.
     *
     * @param stack the ItemStack to add
     */
    public static void addExtraLoot(ItemStack stack) {
        EXTRA_LOOT.add(stack.copy());
    }

    /**
     * Remove a previously registered extra loot item.
     *
     * @param stack the ItemStack to remove (matched by Item and metadata)
     */
    public static void removeExtraLoot(ItemStack stack) {
        EXTRA_LOOT.removeIf(
            existing -> ItemStack.areItemStacksEqual(existing, stack)
        );
    }

    /**
     * Clear all registered extra loot items.
     */
    public static void clearExtraLoot() {
        EXTRA_LOOT.clear();
    }

    /**
     * Get an unmodifiable view of all registered extra loot items.
     *
     * @return unmodifiable list of extra ItemStacks
     */
    public static List<ItemStack> getExtraLoot() {
        return Collections.unmodifiableList(EXTRA_LOOT);
    }

    /**
     * Get a mutable copy of all registered extra loot items.
     * Used internally by the mixin to append to the generated loot pool.
     *
     * @return mutable copy of extra ItemStacks
     */
    public static List<ItemStack> getExtraLootCopy() {
        List<ItemStack> copy = new ArrayList<>(EXTRA_LOOT.size());
        for (ItemStack stack : EXTRA_LOOT) {
            copy.add(stack.copy());
        }
        return copy;
    }
}