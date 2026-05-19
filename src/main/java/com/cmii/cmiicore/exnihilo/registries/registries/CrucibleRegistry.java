package com.cmii.cmiicore.exnihilo.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cmii.cmiicore.exnihilo.api.registries.ICrucibleRegistry;
import com.cmii.cmiicore.exnihilo.json.CustomBlockInfoJson;
import com.cmii.cmiicore.exnihilo.json.CustomIngredientJson;
import com.cmii.cmiicore.exnihilo.json.CustomMeltableJson;
import com.cmii.cmiicore.exnihilo.registries.ingredient.IngredientUtil;
import com.cmii.cmiicore.exnihilo.registries.ingredient.OreIngredientStoring;
import com.cmii.cmiicore.exnihilo.registries.manager.IDefaultRecipeProvider;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistryMap;
import com.cmii.cmiicore.exnihilo.registries.types.Meltable;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import com.cmii.cmiicore.exnihilo.util.StackInfo;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.io.FileReader;
import java.util.*;

public class CrucibleRegistry extends BaseRegistryMap<Ingredient, Meltable> implements ICrucibleRegistry {
    public CrucibleRegistry(List<? extends IDefaultRecipeProvider> defaultRecipeProviders) {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .registerTypeAdapter(Ingredient.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(OreIngredientStoring.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(Meltable.class, CustomMeltableJson.INSTANCE)
                        .enableComplexMapKeySerialization()
                        .create(),
                new TypeToken<Map<Ingredient, Meltable>>() {}.getType(),
                defaultRecipeProviders
        );
    }

    public void register(StackInfo item, Fluid fluid, int amount) {
        register(item.getItemStack(), fluid, amount);
    }

    public void register(StackInfo item, Meltable meltable) {
        register(item.getItemStack(), meltable);
    }

    @Override
    public void register(String name, Fluid fluid, int amount, BlockInfo block) {
        register(name, new Meltable(fluid.getName(), amount, block));
    }

    public void register(ItemStack stack, Fluid fluid, int amount) {
        register(stack, new Meltable(fluid.getName(), amount));
    }

    public void register(ItemStack stack, Meltable meltable) {
        if (stack.isEmpty() || !FluidRegistry.isFluidRegistered(meltable.getFluid())) return;
        if (registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stack)))
            LogUtil.warn("Crucible entry for " + stack.getDisplayName() + " with meta " + stack.getMetadata() + " already exists, skipping.");
        else register(CraftingHelper.getIngredient(stack), meltable);
    }

    public void register(String name, Fluid fluid, int amount) {
        register(name, new Meltable(fluid.getName(), amount));
    }

    public void register(String name, Meltable meltable) {
        Ingredient ingredient = new OreIngredientStoring(name);
        if (!FluidRegistry.isFluidRegistered(meltable.getFluid()))
            return;

        if (registry.keySet().stream().anyMatch(entry -> IngredientUtil.ingredientEquals(entry, ingredient)))
            LogUtil.error("Crucible Ore Entry for " + name + " already exists, skipping.");
        else registry.put(ingredient, meltable);
    }

    public boolean canBeMelted(ItemStack stack) {
        return registry.keySet().stream().anyMatch(entry -> entry.test(stack));
    }

    public boolean canBeMelted(StackInfo info) {
        return canBeMelted(info.getItemStack());
    }

    @Nonnull
    public Meltable getMeltable(ItemStack stack) {
        Ingredient ingredient = registry.keySet().stream().filter(entry -> entry.test(stack)).findFirst().orElse(null);

        if (ingredient != null) {
            return registry.get(ingredient);
        } else {
            return Meltable.EMPTY;
        }
    }

    @Nonnull
    public Meltable getMeltable(StackInfo info) {
        return getMeltable(info.getItemStack());
    }

    @Nonnull
    public Meltable getMeltable(Item item, int meta) {
        return getMeltable(new ItemStack(item, meta));
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        Map<String, Meltable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Meltable>>() {}.getType());

        gsonInput.forEach((key, value) -> {
            Ingredient ingredient = IngredientUtil.parseFromString(key);

            if (registry.keySet().stream().anyMatch(entry -> IngredientUtil.ingredientEquals(ingredient, entry)))
                LogUtil.error("Crucible JSON Entry for " + Arrays.toString(ingredient.getMatchingStacks()) + " already exists, skipping.");

            registry.put(ingredient, value);
        });
    }

    @Override
    public Map<Ingredient, Meltable> getRegistry() {
        return registry;
    }

    @Override
    public List<?> getRecipeList() {
        return new ArrayList<>();
    }
}