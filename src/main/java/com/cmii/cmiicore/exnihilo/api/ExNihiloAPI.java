package com.cmii.cmiicore.exnihilo.api;

import com.cmii.cmiicore.exnihilo.api.registries.*;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;

public final class ExNihiloAPI {

    private ExNihiloAPI() {
    }

    //---------------------------------------------------------------
    //                        ALL REGISTRIES
    //---------------------------------------------------------------

    public static final ICompostRegistry COMPOST_REGISTRY = ExNihiloRegistryManager.COMPOST_REGISTRY;
    public static final ICrookRegistry CROOK_REGISTRY = ExNihiloRegistryManager.CROOK_REGISTRY;
    public static final IHammerRegistry HAMMER_REGISTRY = ExNihiloRegistryManager.HAMMER_REGISTRY;
    public static final IHeatRegistry HEAT_REGISTRY = ExNihiloRegistryManager.HEAT_REGISTRY;
    public static final IBarrelLiquidBlacklistRegistry BARREL_LIQUID_BLACKLIST_REGISTRY = ExNihiloRegistryManager.BARREL_LIQUID_BLACKLIST_REGISTRY;
    public static final IFluidOnTopRegistry FLUID_ON_TOP_REGISTRY = ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY;
    public static final IFluidTransformRegistry FLUID_TRANSFORM_REGISTRY = ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY;
    public static final IFluidBlockTransformerRegistry FLUID_BLOCK_TRANSFORMER_REGISTRY = ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY;
    public static final IFluidItemFluidRegistry FLUID_ITEM_FLUID_REGISTRY = ExNihiloRegistryManager.FLUID_ITEM_FLUID_REGISTRY;
    public static final ICrucibleRegistry CRUCIBLE_STONE_REGISTRY = ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY;
    public static final ICrucibleRegistry CRUCIBLE_WOOD_REGISTRY = ExNihiloRegistryManager.CRUCIBLE_WOOD_REGISTRY;
    public static final IMilkEntityRegistry MILK_ENTITY_REGISTRY = ExNihiloRegistryManager.MILK_ENTITY_REGISTRY;
    public static final IWitchWaterWorldRegistry WITCH_WATER_WORLD_REGISTRY = ExNihiloRegistryManager.WITCH_WATER_WORLD_REGISTRY;

}