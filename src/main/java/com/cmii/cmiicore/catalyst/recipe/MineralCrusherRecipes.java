package com.cmii.cmiicore.catalyst.recipe;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MineralCrusherRecipes {

    private static final List<CrusherRecipe> RECIPES = new ArrayList<>();

    public static void addRecipe(ItemStack input, WeightedOutput... outputs) {
        List<WeightedOutput> list = new ArrayList<>();
        for (WeightedOutput wo : outputs) {
            if (!wo.output.isEmpty()) {
                list.add(wo);
            }
        }
        if (!list.isEmpty()) {
            removeRecipe(input);
            RECIPES.add(new CrusherRecipe(input, list));
        }
    }

    public static void removeRecipe(ItemStack input) {
        RECIPES.removeIf(r -> ItemStack.areItemsEqual(r.input, input));
    }

    public static int getRecipeCount() {
        return RECIPES.size();
    }

    public static class CrusherRecipe {
        public final ItemStack input;
        public final List<WeightedOutput> outputs;

        public CrusherRecipe(ItemStack input, List<WeightedOutput> outputs) {
            this.input = input;
            this.outputs = outputs;
        }
    }
}
