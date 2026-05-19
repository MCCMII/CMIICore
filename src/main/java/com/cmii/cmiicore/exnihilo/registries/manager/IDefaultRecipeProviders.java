package com.cmii.cmiicore.exnihilo.registries.manager;

import com.cmii.cmiicore.exnihilo.registries.registries.*;
import com.cmii.cmiicore.exnihilo.registries.registries.prefab.BaseRegistry;

public interface IDefaultRecipeProvider<T extends BaseRegistry<?>> {
    void registerRecipeDefaults(T registry);
}

interface IFluidBlockDefaultRegistryProvider extends IDefaultRecipeProvider<FluidBlockTransformerRegistry> {}
interface IFluidItemFluidDefaultRegistryProvider extends IDefaultRecipeProvider<FluidItemFluidRegistry> {}
interface IFluidOnTopDefaultRegistryProvider extends IDefaultRecipeProvider<FluidOnTopRegistry> {}
interface IFluidTransformDefaultRegistryProvider extends IDefaultRecipeProvider<FluidTransformRegistry> {}
interface IHammerDefaultRegistryProvider extends IDefaultRecipeProvider<HammerRegistry> {}
interface IHeatDefaultRegistryProvider extends IDefaultRecipeProvider<HeatRegistry> {}
interface IMilkEntityDefaultRegistryProvider extends IDefaultRecipeProvider<MilkEntityRegistry> {}
interface ICrucibleWoodDefaultRegistryProvider extends IDefaultRecipeProvider<CrucibleRegistry> {}
interface ICrucibleStoneDefaultRegistryProvider extends IDefaultRecipeProvider<CrucibleRegistry> {}
interface ICrookDefaultRegistryProvider extends IDefaultRecipeProvider<CrookRegistry> {}
interface ICompostDefaultRegistryProvider extends IDefaultRecipeProvider<CompostRegistry> {}
interface IBarrelLiquidBlacklistDefaultRegistryProvider extends IDefaultRecipeProvider<BarrelLiquidBlacklistRegistry> {}
interface IWitchWaterWorldDefaultRegistryProvider extends IDefaultRecipeProvider<WitchWaterWorldRegistry> {}