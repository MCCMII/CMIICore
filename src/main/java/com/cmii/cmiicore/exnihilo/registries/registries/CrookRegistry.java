package com.cmii.cmiicore.exnihilo.registries.registries;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cmii.cmiicore.exnihilo.api.registries.ICrookRegistry;
import com.cmii.cmiicore.exnihilo.json.CustomIngredientJson;
import com.cmii.cmiicore.exnihilo.json.CustomItemStackJson;
import com.cmii.cmiicore.exnihilo.registries.ingredient.IngredientUtil;
import com.cmii.cmiicore.exnihilo.registries.ingredient.OreIngredientStoring;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistryMap;
import com.cmii.cmiicore.exnihilo.registries.types.CrookReward;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class CrookRegistry extends BaseRegistryMap<Ingredient, List<CrookReward>> implements ICrookRegistry {

    public CrookRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemStack.class, CustomItemStackJson.INSTANCE)
                        .registerTypeAdapter(Ingredient.class, CustomIngredientJson.INSTANCE)
                        .registerTypeAdapter(OreIngredientStoring.class, CustomIngredientJson.INSTANCE)
                        .enableComplexMapKeySerialization()
                        .create(),
                new TypeToken<Map<Ingredient, List<CrookReward>>>() {}.getType(),
                ExNihiloRegistryManager.CROOK_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(Block block, int meta, ItemStack reward, float chance, float fortuneChance) {
        register(new BlockInfo(block, meta), reward, chance, fortuneChance);
    }

    public void register(IBlockState state, ItemStack reward, float chance, float fortuneChance) {
        register(new BlockInfo(state), reward, chance, fortuneChance);
    }

    public void register(BlockInfo info, ItemStack reward, float chance, float fortuneChance) {
        register(Ingredient.fromStacks(info.getItemStack()), new CrookReward(reward, chance, fortuneChance));
    }

    public void register(String name, ItemStack reward, float chance, float fortuneChance) {
        register(new OreIngredientStoring(name), new CrookReward(reward, chance, fortuneChance));
    }

    public void register(Ingredient ingredient, CrookReward reward) {
        Ingredient search = registry.keySet()
                .stream()
                .filter(entry -> IngredientUtil.ingredientEquals(entry, ingredient))
                .findAny()
                .orElse(null);


        if (search != null) {
            registry.get(search).add(reward);
        } else {
            NonNullList<CrookReward> drops = NonNullList.create();
            drops.add(reward);
            registry.put(ingredient, drops);
        }
    }

    public NonNullList<CrookReward> getRewards(Ingredient ingredient) {
        NonNullList<CrookReward> drops = NonNullList.create();
        registry.entrySet().stream().filter(entry -> entry.getKey() == ingredient).forEach(entry -> drops.addAll(entry.getValue()));
        return drops;
    }

    public boolean isRegistered(Block block) {
        ItemStack stack = new ItemStack(block);
        return registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stack));
    }

    public boolean isRegistered(IBlockState state) {
        return isRegistered(new BlockInfo(state));
    }

    public boolean isRegistered(BlockInfo stackInfo) {
        return registry.keySet().stream().anyMatch(ingredient -> ingredient.test(stackInfo.getItemStack()));
    }

    public List<CrookReward> getRewards(IBlockState state) {
        BlockInfo info = new BlockInfo(state);
        ArrayList<CrookReward> list = new ArrayList<>();

        registry.entrySet()
                .stream()
                .filter(ingredient -> ingredient.getKey().test(info.getItemStack()))
                .forEach(ingredient -> list.addAll(ingredient.getValue()));

        return list;
    }

    @Override
    public void registerEntriesFromJSON(FileReader fr) {
        HashMap<String, NonNullList<CrookReward>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, NonNullList<CrookReward>>>() {}.getType());

        gsonInput.forEach((key, value) -> {
            Ingredient ingredient = IngredientUtil.parseFromString(key);

            if (ingredient != null) {
                List<CrookReward> list = registry.getOrDefault(ingredient, NonNullList.create());
                list.addAll(value);
                registry.put(ingredient, list);
            }
        });
    }

    @Override
    public List<?> getRecipeList() {
        return Lists.newLinkedList();
    }
}