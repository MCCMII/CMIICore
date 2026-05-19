package com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluidontop;

import com.cmii.cmiicore.Reference;
import com.cmii.cmiicore.exnihilo.ModBlocks;
import com.cmii.cmiicore.exnihilo.client.renderers.RenderUtils;
import com.cmii.cmiicore.exnihilo.util.TankUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class FluidOnTopRecipeCategory implements IRecipeCategory<FluidOnTopRecipe> {
    public static final String UID = "cmiicore:fluid_on_top";
    private static final ResourceLocation texture = new ResourceLocation(Reference.MOD_ID, "textures/gui/jei_barrel.png");

    private final IDrawableStatic background;
    private final IDrawable arrow;

    public FluidOnTopRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(texture, 0, 44, 128, 36);
        arrow = guiHelper.createDrawable(texture, 128, 44, 30, 36);
    }

    @Override
    @Nonnull
    public String getUid() {
        return UID;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return "Fluid On Top";
    }

    @Override
    @Nonnull
    public String getModName() {
        return Reference.MOD_ID;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull FluidOnTopRecipe recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getFluidStacks().init(0, true, 4, 2, 12, 33, 1000, true, null);
        recipeLayout.getFluidStacks().set(0, ingredients.getInputs(FluidStack.class).get(0));

        recipeLayout.getFluidStacks().init(1, true, 18, 2, 12, 33, 1000, true, null);
        recipeLayout.getFluidStacks().set(1, ingredients.getInputs(FluidStack.class).get(1));

        recipeLayout.getItemStacks().init(0, true, 45, 1);
        recipeLayout.getItemStacks().init(1, true, 65, 1);
        recipeLayout.getItemStacks().init(2, true, 45, 19);
        recipeLayout.getItemStacks().init(3, false, 110, 10);

        recipeLayout.getItemStacks().set(0, new ItemStack(ModBlocks.barrel));
        recipeLayout.getItemStacks().set(1, recipeWrapper.getInputs().get(0));
        recipeLayout.getItemStacks().set(2, recipeWrapper.getInputs().get(1));
        recipeLayout.getItemStacks().set(3, recipeWrapper.getOutputs().get(0));

        recipeLayout.getItemStacks().addTooltipCallback((i, input, stack, tooltips) -> {
            tooltips.clear();
        });
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft) {
        arrow.draw(minecraft, 48, 1);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }
}
