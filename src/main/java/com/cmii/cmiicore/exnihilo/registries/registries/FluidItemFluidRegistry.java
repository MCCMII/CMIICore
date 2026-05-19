package com.cmii.cmiicore.exnihilo.registries.registries;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.cmii.cmiicore.exnihilo.api.registries.IFluidItemFluidRegistry;
import com.cmii.cmiicore.exnihilo.json.CustomBlockInfoJson;
import com.cmii.cmiicore.exnihilo.json.CustomItemInfoJson;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistryList;
import com.cmii.cmiicore.exnihilo.registries.types.FluidItemFluid;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.ItemInfo;
import com.cmii.cmiicore.exnihilo.util.StackInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FluidItemFluidRegistry extends BaseRegistryList<FluidItemFluid> implements IFluidItemFluidRegistry {

    public FluidItemFluidRegistry() {
        super(
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ItemInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(StackInfo.class, CustomItemInfoJson.INSTANCE)
                        .registerTypeAdapter(BlockInfo.class, CustomBlockInfoJson.INSTANCE)
                        .create(),
                ExNihiloRegistryManager.FLUID_ITEM_FLUID_DEFAULT_REGISTRY_PROVIDERS
        );
    }

    public void register(@NotNull String inputFluid, @NotNull StackInfo reactant, @NotNull String outputFluid) {
        registry.add(new FluidItemFluid(inputFluid, reactant, outputFluid));
    }

    public void register(@NotNull Fluid inputFluid, @NotNull StackInfo reactant, @NotNull Fluid outputFluid) {
        registry.add(new FluidItemFluid(inputFluid.getName(), reactant, outputFluid.getName()));
    }

    public String getFLuidForTransformation(@NotNull Fluid fluid, @NotNull ItemStack stack) {
        ItemInfo info = new ItemInfo(stack);

        for (FluidItemFluid transformer : registry) {
            if (fluid.getName().equals(transformer.getInputFluid()) && info.equals(transformer.getReactant())) {
                return transformer.getOutput();
            }
        }

        return null;
    }

    @Override
    protected void registerEntriesFromJSON(FileReader fr) {
        List<FluidItemFluid> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidItemFluid>>() {}.getType());
        registry.addAll(gsonInput);
    }

    @Override
    public List<?> getRecipeList() {
        return new ArrayList<>();
    }
}