package com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluiditemtransform;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.cmii.cmiicore.exnihilo.registries.types.FluidBlockTransformer;
import com.cmii.cmiicore.exnihilo.registries.types.FluidItemFluid;
import com.cmii.cmiicore.exnihilo.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FluidItemTransformRecipe implements IRecipeWrapper {

    @Nonnull
    private final FluidStack inputFluid;
    @Nonnull
    private final ItemStack inputBucket;
    @Nonnull
    private final List<ItemStack> inputStacks;

    @Nullable
    private final FluidStack outputFluid;
    @Nonnull
    private final ItemStack outputStack;

    public FluidItemTransformRecipe(FluidBlockTransformer recipe) {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getFluidName()), 1000);

        inputBucket = Util.getBucketStack(inputFluid.getFluid());

        inputStacks = Arrays.asList(recipe.getInput().getMatchingStacks());
        outputStack = recipe.getOutput().getItemStack();
        outputFluid = null;
    }

    public FluidItemTransformRecipe(FluidItemFluid recipe) {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getInputFluid()), 1000);
        inputBucket = Util.getBucketStack(inputFluid.getFluid());
        inputStacks = Collections.singletonList(recipe.getReactant().getItemStack());

        outputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getOutput()), 1000);
        outputStack = Util.getBucketStack(outputFluid.getFluid());
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, getInputs());
        ingredients.setInputs(VanillaTypes.FLUID, getFluidInputs());

        ingredients.setOutput(VanillaTypes.ITEM, outputStack);
        if (outputFluid != null) {
            ingredients.setOutput(VanillaTypes.FLUID, outputFluid);
        }
    }

    public List<List<ItemStack>> getInputs() {
        return ImmutableList.of(ImmutableList.of(inputBucket), inputStacks);
    }

    public List<FluidStack> getFluidInputs() {
        return ImmutableList.of(inputFluid);
    }

    public ItemStack getOutput() {
        return outputStack;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    @Nonnull
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Lists.newArrayList();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}