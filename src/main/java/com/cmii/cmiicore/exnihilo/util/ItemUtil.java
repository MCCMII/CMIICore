package com.cmii.cmiicore.exnihilo.util;

import com.cmii.cmiicore.exnihilo.items.tools.ICrook;
import com.cmii.cmiicore.exnihilo.items.tools.IHammer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class ItemUtil {

    public static boolean isCrook(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return false;

        if (stack.getItem() == Items.AIR)
            return false;

        if (stack.getItem() instanceof ICrook)
            return ((ICrook) stack.getItem()).isCrook(stack);

        // Inspirations compatibility
        // Using ToolClass is the recommended method for compatible mods.
        return stack.getItem().getToolClasses(stack).contains("crook");
    }

    public static boolean isCrook(Item item) {
        return isCrook(new ItemStack(item));
    }

    public static boolean isHammer(@Nullable ItemStack stack) {
        if (stack == null)
            return false;

        if (stack.getItem() == Items.AIR)
            return false;

        if (stack.getItem() instanceof IHammer)
            return ((IHammer) stack.getItem()).isHammer(stack);

        return false;

    }

    public static boolean isHammer(Item item) {
        return isHammer(new ItemStack(item));
    }

    /**
     * Compares Items, Damage, and NBT
     */
    public static boolean areStacksEquivalent(ItemStack left, ItemStack right) {
        return ItemStack.areItemsEqual(right, left) && ItemStack.areItemStackTagsEqual(left, right);
    }
}