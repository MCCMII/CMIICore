package com.cmii.cmiicore.exnihilo.registries.manager;

import com.cmii.cmiicore.exnihilo.registries.RegistryReloadedEvent;
import com.cmii.cmiicore.exnihilo.registries.registries.*;

import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ExNihiloRegistryManager {

    //region >>>> DEFAULT RECIPE PROVIDERS
    public static final List<IHammerDefaultRegistryProvider> HAMMER_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICompostDefaultRegistryProvider> COMPOST_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICrookDefaultRegistryProvider> CROOK_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICrucibleStoneDefaultRegistryProvider> CRUCIBLE_STONE_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<ICrucibleWoodDefaultRegistryProvider> CRUCIBLE_WOOD_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IFluidBlockDefaultRegistryProvider> FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IFluidOnTopDefaultRegistryProvider> FLUID_ON_TOP_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IFluidTransformDefaultRegistryProvider> FLUID_TRANSFORM_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IFluidItemFluidDefaultRegistryProvider> FLUID_ITEM_FLUID_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IHeatDefaultRegistryProvider> HEAT_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IMilkEntityDefaultRegistryProvider> MILK_ENTITY_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IBarrelLiquidBlacklistDefaultRegistryProvider> BARREL_LIQUID_BLACKLIST_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    public static final List<IWitchWaterWorldDefaultRegistryProvider> WITCH_WATER_WORLD_DEFAULT_REGISTRY_PROVIDERS = new ArrayList<>();
    //endregion

    public static final CompostRegistry COMPOST_REGISTRY = new CompostRegistry();
    public static final CrookRegistry CROOK_REGISTRY = new CrookRegistry();
    public static final HammerRegistry HAMMER_REGISTRY = new HammerRegistry();
    public static final HeatRegistry HEAT_REGISTRY = new HeatRegistry();
    public static final BarrelLiquidBlacklistRegistry BARREL_LIQUID_BLACKLIST_REGISTRY = new BarrelLiquidBlacklistRegistry();
    public static final FluidOnTopRegistry FLUID_ON_TOP_REGISTRY = new FluidOnTopRegistry();
    public static final FluidTransformRegistry FLUID_TRANSFORM_REGISTRY = new FluidTransformRegistry();
    public static final FluidBlockTransformerRegistry FLUID_BLOCK_TRANSFORMER_REGISTRY = new FluidBlockTransformerRegistry();
    public static final FluidItemFluidRegistry FLUID_ITEM_FLUID_REGISTRY = new FluidItemFluidRegistry();
    public static final CrucibleRegistry CRUCIBLE_STONE_REGISTRY = new CrucibleRegistry(CRUCIBLE_STONE_DEFAULT_REGISTRY_PROVIDERS);
    public static final CrucibleRegistry CRUCIBLE_WOOD_REGISTRY = new CrucibleRegistry(CRUCIBLE_WOOD_DEFAULT_REGISTRY_PROVIDERS);
    public static final MilkEntityRegistry MILK_ENTITY_REGISTRY = new MilkEntityRegistry();
    public static final WitchWaterWorldRegistry WITCH_WATER_WORLD_REGISTRY = new WitchWaterWorldRegistry();

    //region >>>> DEFAULT RECIPE REGISTERS

    public static void registerHammerDefaultRecipeHandler(IHammerDefaultRegistryProvider provider) {
        HAMMER_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCompostDefaultRecipeHandler(ICompostDefaultRegistryProvider provider) {
        COMPOST_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCrookDefaultRecipeHandler(ICrookDefaultRegistryProvider provider) {
        CROOK_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCrucibleStoneDefaultRecipeHandler(ICrucibleStoneDefaultRegistryProvider provider) {
        CRUCIBLE_STONE_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerCrucibleWoodDefaultRecipeHandler(ICrucibleWoodDefaultRegistryProvider provider) {
        CRUCIBLE_WOOD_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerFluidBlockDefaultRecipeHandler(IFluidBlockDefaultRegistryProvider provider) {
        FLUID_BLOCK_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerFluidTransformDefaultRecipeHandler(IFluidTransformDefaultRegistryProvider provider) {
        FLUID_TRANSFORM_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerFluidItemFluidDefaultHandler(IFluidItemFluidDefaultRegistryProvider provider) {
        FLUID_ITEM_FLUID_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerFluidOnTopDefaultRecipeHandler(IFluidOnTopDefaultRegistryProvider provider) {
        FLUID_ON_TOP_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerHeatDefaultRecipeHandler(IHeatDefaultRegistryProvider provider) {
        HEAT_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerBarrelLiquidBlacklistDefaultHandler(IBarrelLiquidBlacklistDefaultRegistryProvider provider) {
        BARREL_LIQUID_BLACKLIST_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerMilkEntityDefaultRecipeHandler(IMilkEntityDefaultRegistryProvider provider) {
        MILK_ENTITY_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }

    public static void registerWitchWaterWorldDefaultRecipeHandler(IWitchWaterWorldDefaultRegistryProvider provider) {
        WITCH_WATER_WORLD_DEFAULT_REGISTRY_PROVIDERS.add(provider);
    }
    //endregion

    //region >>>> JSON CONFIG LOADING

    /**
     * Load all JSON registry files from the given directory.
     * Called during init() to ensure cross-mod compatibility (e.g. Forestry bee templates).
     */
    public static void loadAllRegistries(File configDir) {
        COMPOST_REGISTRY.loadJson(new File(configDir, "CompostRegistry.json"));
        CROOK_REGISTRY.loadJson(new File(configDir, "CrookRegistry.json"));
        HAMMER_REGISTRY.loadJson(new File(configDir, "HammerRegistry.json"));
        HEAT_REGISTRY.loadJson(new File(configDir, "HeatRegistry.json"));
        BARREL_LIQUID_BLACKLIST_REGISTRY.loadJson(new File(configDir, "BarrelLiquidBlacklistRegistry.json"));
        FLUID_ON_TOP_REGISTRY.loadJson(new File(configDir, "FluidOnTopRegistry.json"));
        FLUID_TRANSFORM_REGISTRY.loadJson(new File(configDir, "FluidTransformRegistry.json"));
        FLUID_BLOCK_TRANSFORMER_REGISTRY.loadJson(new File(configDir, "FluidBlockTransformerRegistry.json"));
        FLUID_ITEM_FLUID_REGISTRY.loadJson(new File(configDir, "FluidItemFluidRegistry.json"));
        CRUCIBLE_STONE_REGISTRY.loadJson(new File(configDir, "CrucibleStoneRegistry.json"));
        CRUCIBLE_WOOD_REGISTRY.loadJson(new File(configDir, "CrucibleWoodRegistry.json"));
        MILK_ENTITY_REGISTRY.loadJson(new File(configDir, "MilkEntityRegistry.json"));
        WITCH_WATER_WORLD_REGISTRY.loadJson(new File(configDir, "WitchWaterWorldRegistry.json"));

        MinecraftForge.EVENT_BUS.post(new RegistryReloadedEvent());
    }

    //endregion
}