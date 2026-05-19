package com.cmii.cmiicore.exnihilo.compatibility.jei;

import com.cmii.cmiicore.Reference;
import com.cmii.cmiicore.exnihilo.ModBlocks;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.compost.CompostRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.compost.CompostRecipeCategory;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluiditemtransform.FluidItemTransformRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluiditemtransform.FluidItemTransformRecipeCategory;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluidontop.FluidOnTopRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluidontop.FluidOnTopRecipeCategory;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluidtransform.FluidTransformRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.barrel.fluidtransform.FluidTransformRecipeCategory;
import com.cmii.cmiicore.exnihilo.compatibility.jei.crook.CrookRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.crook.CrookRecipeCategory;
import com.cmii.cmiicore.exnihilo.compatibility.jei.crucible.CrucibleHeatSourceRecipeCategory;
import com.cmii.cmiicore.exnihilo.compatibility.jei.crucible.CrucibleRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.crucible.CrucibleRecipeCategory;
import com.cmii.cmiicore.exnihilo.compatibility.jei.crucible.HeatSourcesRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.hammer.HammerRecipe;
import com.cmii.cmiicore.exnihilo.compatibility.jei.hammer.HammerRecipeCategory;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.types.*;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.ItemUtil;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import com.cmii.cmiicore.exnihilo.util.Util;
import com.google.common.collect.Maps;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@JEIPlugin
public class CompatJEI implements IModPlugin {

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new CompostRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidTransformRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidOnTopRecipeCategory(guiHelper));
        registry.addRecipeCategories(new FluidItemTransformRecipeCategory(guiHelper));
        registry.addRecipeCategories(new HammerRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CrookRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CrucibleHeatSourceRecipeCategory(guiHelper));
        registry.addRecipeCategories(new CrucibleRecipeCategory(guiHelper, "cmiicore:crucible_stone"));
        registry.addRecipeCategories(new CrucibleRecipeCategory(guiHelper, "cmiicore:crucible_wood"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void register(@Nonnull IModRegistry registry) {
        LogUtil.info("ModConfig Loaded: " + ModConfig.configsLoaded);

        if (!ModConfig.configsLoaded) {
            ModConfig.loadConfigs();
        }

        registerCompost(registry);
        registerFluidBlockTransform(registry);
        registerFluidOnTop(registry);
        registerFluidTransform(registry);
        registerHammer(registry);
        registerCrook(registry);
        registerHeat(registry);
        registerStoneCrucible(registry);
        registerWoodCrucible(registry);

        for (Item item : Item.REGISTRY) {
            if (ItemUtil.isHammer(item)) {
                registry.addRecipeCatalyst(new ItemStack(item), HammerRecipeCategory.UID);
                LogUtil.info("Registered Hammer Catalyst: " + item.getRegistryName());
            }
            if (ItemUtil.isCrook(item)) {
                registry.addRecipeCatalyst(new ItemStack(item), CrookRecipeCategory.UID);
                LogUtil.info("Registered Crook Catalyst: " + item.getRegistryName());
            }
        }
    }

    // region Compost
    private void registerCompost(@Nonnull IModRegistry registry) {
        List<CompostRecipe> compostRecipes = buildCompostRecipes();

        registry.addRecipes(compostRecipes, CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrel), CompostRecipeCategory.UID);

        LogUtil.info("JEI: Compost Recipes Loaded:         " + compostRecipes.size());
    }

    private List<CompostRecipe> buildCompostRecipes() {
        Map<BlockInfo, List<List<ItemStack>>> outputMap = Maps.newHashMap();
        for (Map.Entry<Ingredient, Compostable> entry : ExNihiloRegistryManager.COMPOST_REGISTRY.getRegistry().entrySet()) {
            BlockInfo output = entry.getValue().getCompostBlock();
            Ingredient ingredient = entry.getKey();
            if (ingredient == null || !output.isValid())
                continue;

            if (!outputMap.containsKey(output)) {
                outputMap.put(output, new ArrayList<>());
            }

            List<ItemStack> inputs = new ArrayList<>();
            for (ItemStack match : ingredient.getMatchingStacks()) {
                if (match.isEmpty())
                    continue;
                ItemStack input = match.copy();
                input.setCount(Util.stepsRequiredToMatch(1.0f, entry.getValue().getValue()));
                inputs.add(input);
            }
            if (!inputs.isEmpty())
                outputMap.get(output).add(inputs);
        }

        List<CompostRecipe> recipes = new ArrayList<>();
        for (Map.Entry<BlockInfo, List<List<ItemStack>>> entry : outputMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i += 21) {
                recipes.add(new CompostRecipe(entry.getKey(),
                        entry.getValue().subList(i, Math.min(i + 21, entry.getValue().size()))));
            }
        }

        return recipes;
    }
    // endregion

    // region Fluid Block Transform + Fluid Item Fluid
    private void registerFluidBlockTransform(@Nonnull IModRegistry registry) {
        List<FluidItemTransformRecipe> recipes = new ArrayList<>();

        for (FluidBlockTransformer recipe : ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY.getRegistry()) {
            recipes.add(new FluidItemTransformRecipe(recipe));
        }
        for (FluidItemFluid recipe : ExNihiloRegistryManager.FLUID_ITEM_FLUID_REGISTRY.getRegistry()) {
            recipes.add(new FluidItemTransformRecipe(recipe));
        }

        registry.addRecipes(recipes, FluidItemTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrel), FluidItemTransformRecipeCategory.UID);

        LogUtil.info("JEI: Fluid Item Transform Recipes Loaded: " + recipes.size());
    }
    // endregion

    // region Fluid On Top
    private void registerFluidOnTop(@Nonnull IModRegistry registry) {
        List<FluidOnTopRecipe> recipes = new ArrayList<>();
        for (FluidFluidBlock recipe : ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY.getRegistry()) {
            recipes.add(new FluidOnTopRecipe(recipe));
        }

        registry.addRecipes(recipes, FluidOnTopRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrel), FluidOnTopRecipeCategory.UID);

        LogUtil.info("JEI: Fluid On Top Recipes Loaded:    " + recipes.size());
    }
    // endregion

    // region Fluid Transform
    private void registerFluidTransform(@Nonnull IModRegistry registry) {
        List<FluidTransformRecipe> recipes = new ArrayList<>();
        for (Map.Entry<String, List<FluidTransformer>> entry : ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.getRegistry().entrySet()) {
            for (FluidTransformer transformer : entry.getValue()) {
                recipes.add(new FluidTransformRecipe(transformer));
            }
        }

        registry.addRecipes(recipes, FluidTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.barrel), FluidTransformRecipeCategory.UID);

        LogUtil.info("JEI: Fluid Transform Recipes Loaded: " + recipes.size());
    }
    // endregion

    // region Hammer
    private void registerHammer(@Nonnull IModRegistry registry) {
        List<HammerRecipe> hammerRecipes = buildHammerRecipes();
        registry.addRecipes(hammerRecipes, HammerRecipeCategory.UID);
        LogUtil.info("JEI: Hammer Recipes Loaded:             " + hammerRecipes.size());
    }

    private List<HammerRecipe> buildHammerRecipes() {
        List<HammerRecipe> recipes = new ArrayList<>();
        for (Map.Entry<Ingredient, List<HammerReward>> entry : ExNihiloRegistryManager.HAMMER_REGISTRY.getRegistry().entrySet()) {
            Ingredient ingredient = entry.getKey();
            if (ingredient == null)
                continue;

            List<ItemStack> rawOutputs = entry.getValue().stream().map(HammerReward::getStack).collect(Collectors.toList());
            List<ItemStack> allOutputs = new ArrayList<>();
            for (ItemStack raw : rawOutputs) {
                boolean alreadyexists = allOutputs.stream().anyMatch(all -> ItemUtil.areStacksEquivalent(all, raw));
                if (!alreadyexists)
                    allOutputs.add(raw);
            }

            List<ItemStack> inputs = Arrays.asList(ingredient.getMatchingStacks());
            for (int i = 0; i < allOutputs.size(); i += 21) {
                final List<ItemStack> outputs = allOutputs.subList(i, Math.min(i + 21, allOutputs.size()));
                recipes.add(new HammerRecipe(inputs, outputs));
            }
        }
        return recipes;
    }
    // endregion

    // region Crook
    private void registerCrook(@Nonnull IModRegistry registry) {
        List<CrookRecipe> crookRecipes = buildCrookRecipes();
        registry.addRecipes(crookRecipes, CrookRecipeCategory.UID);
        LogUtil.info("JEI: Crook Recipes Loaded:              " + crookRecipes.size());
    }

    private List<CrookRecipe> buildCrookRecipes() {
        List<CrookRecipe> recipes = new ArrayList<>();
        for (Map.Entry<Ingredient, List<CrookReward>> entry : ExNihiloRegistryManager.CROOK_REGISTRY.getRegistry().entrySet()) {
            Ingredient ingredient = entry.getKey();
            if (ingredient == null)
                continue;

            List<ItemStack> rawOutputs = entry.getValue().stream().map(CrookReward::getStack).collect(Collectors.toList());
            List<ItemStack> allOutputs = new ArrayList<>();
            for (ItemStack raw : rawOutputs) {
                boolean alreadyexists = allOutputs.stream().anyMatch(all -> ItemUtil.areStacksEquivalent(all, raw));
                if (!alreadyexists)
                    allOutputs.add(raw);
            }

            List<ItemStack> inputs = Arrays.asList(ingredient.getMatchingStacks());
            for (int i = 0; i < allOutputs.size(); i += 21) {
                final List<ItemStack> outputs = allOutputs.subList(i, Math.min(i + 21, allOutputs.size()));
                recipes.add(new CrookRecipe(inputs, outputs));
            }
        }
        return recipes;
    }
    // endregion

    // region Heat Sources
    private void registerHeat(@Nonnull IModRegistry registry) {
        List<HeatSourcesRecipe> heatSources = new ArrayList<>();
        for (Map.Entry<BlockInfo, Integer> entry : ExNihiloRegistryManager.HEAT_REGISTRY.getRegistry().entrySet()) {
            heatSources.add(new HeatSourcesRecipe(entry.getKey(), entry.getValue()));
        }

        registry.addRecipes(heatSources, CrucibleHeatSourceRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleStone, 1, 1), CrucibleHeatSourceRecipeCategory.UID);

        LogUtil.info("JEI: Heat Sources Loaded:             " + heatSources.size());
    }
    // endregion

    // region Stone Crucible
    private void registerStoneCrucible(@Nonnull IModRegistry registry) {
        List<CrucibleRecipe> crucibleRecipes = buildCrucibleRecipes(ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY.getRegistry());

        registry.addRecipes(crucibleRecipes, "cmiicore:crucible_stone");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleStone, 1, 1), "cmiicore:crucible_stone");
        LogUtil.info("JEI: Stone Crucible Recipes Loaded:       " + crucibleRecipes.size());
    }
    // endregion

    // region Wood Crucible
    private void registerWoodCrucible(@Nonnull IModRegistry registry) {
        List<CrucibleRecipe> crucibleRecipes = buildCrucibleRecipes(ExNihiloRegistryManager.CRUCIBLE_WOOD_REGISTRY.getRegistry());

        registry.addRecipes(crucibleRecipes, "cmiicore:crucible_wood");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.crucibleWood, 1, 0), "cmiicore:crucible_wood");
        LogUtil.info("JEI: Wood Crucible Recipes Loaded:        " + crucibleRecipes.size());
    }
    // endregion

    private List<CrucibleRecipe> buildCrucibleRecipes(Map<Ingredient, Meltable> registryMap) {
        Map<Fluid, List<List<ItemStack>>> outputMap = Maps.newHashMap();
        for (Map.Entry<Ingredient, Meltable> entry : registryMap.entrySet()) {
            Fluid output = FluidRegistry.getFluid(entry.getValue().getFluid());
            Ingredient ingredient = entry.getKey();
            if (output == null || ingredient == null)
                continue;

            if (!outputMap.containsKey(output)) {
                outputMap.put(output, new ArrayList<>());
            }

            List<ItemStack> inputs = new ArrayList<>();
            for (ItemStack match : ingredient.getMatchingStacks()) {
                if (match.isEmpty() || match.getItem() == null)
                    continue;
                ItemStack input = match.copy();
                input.setCount((int) Math.max(Math.ceil(Fluid.BUCKET_VOLUME / entry.getValue().getAmount()), 1));
                inputs.add(input);
            }
            if (!inputs.isEmpty())
                outputMap.get(output).add(inputs);
        }

        List<CrucibleRecipe> recipes = new ArrayList<>();
        for (Map.Entry<Fluid, List<List<ItemStack>>> entry : outputMap.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i += 21) {
                recipes.add(new CrucibleRecipe(entry.getKey(),
                        entry.getValue().subList(i, Math.min(i + 21, entry.getValue().size()))));
            }
        }

        return recipes;
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
    }
}