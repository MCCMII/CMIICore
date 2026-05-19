package com.cmii.cmiicore.exnihilo.compatibility.jei.crucible;

import com.cmii.cmiicore.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CrucibleHeatSourceRecipeCategory implements IRecipeCategory<HeatSourcesRecipe> {
    public static final String UID = "cmiicore:heat_sources";
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei_mid.png");

    private final IDrawableStatic background;

    public CrucibleHeatSourceRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 168, 166, 58);
    }

    @Override
    @Nonnull
    public String getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return "Heat Sources";
    }

    @Override
    @Nonnull
    public String getModName() {
        return Reference.MOD_ID;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull HeatSourcesRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 0, 40);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(net.minecraft.item.ItemStack.class));
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }
}