package com.cmii.cmiicore.exnihilo.registries.manager;

import com.cmii.cmiicore.exnihilo.registries.registries.*;

public class ExNihiloDefaultRecipes {
    private static final CompatDefaultRecipes compat = new CompatDefaultRecipes();

    public static void registerDefaults() {
        ExNihiloRegistryManager.registerHammerDefaultRecipeHandler(new HammerDefaults());
        ExNihiloRegistryManager.registerCompostDefaultRecipeHandler(new CompostDefaults());
        ExNihiloRegistryManager.registerCrookDefaultRecipeHandler(new CrookDefaults());
        ExNihiloRegistryManager.registerHeatDefaultRecipeHandler(new HeatDefaults());
        ExNihiloRegistryManager.registerBarrelLiquidBlacklistDefaultHandler(new BarrelLiquidBlacklistDefaults());
        ExNihiloRegistryManager.registerFluidOnTopDefaultRecipeHandler(new FluidOnTopDefaults());
        ExNihiloRegistryManager.registerFluidTransformDefaultRecipeHandler(new FluidTransformDefaults());
        ExNihiloRegistryManager.registerFluidBlockDefaultRecipeHandler(new FluidBlockTransformDefaults());
        ExNihiloRegistryManager.registerFluidItemFluidDefaultHandler(new FluidItemFluidDefaults());
        ExNihiloRegistryManager.registerCrucibleStoneDefaultRecipeHandler(new CrucibleStoneDefaults());
        ExNihiloRegistryManager.registerCrucibleWoodDefaultRecipeHandler(new CrucibleWoodDefaults());
        ExNihiloRegistryManager.registerMilkEntityDefaultRecipeHandler(new MilkEntityDefaults());
        ExNihiloRegistryManager.registerWitchWaterWorldDefaultRecipeHandler(new WitchWaterWorldDefaults());
    }

    private static class CompostDefaults implements ICompostDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(CompostRegistry registry) {
            compat.registerCompost(registry);
        }
    }

    private static class CrookDefaults implements ICrookDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(CrookRegistry registry) {
            compat.registerCrook(registry);
        }
    }

    private static class HammerDefaults implements IHammerDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(HammerRegistry registry) {
            compat.registerHammer(registry);
        }
    }

    private static class HeatDefaults implements IHeatDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(HeatRegistry registry) {
            compat.registerHeat(registry);
        }
    }

    private static class BarrelLiquidBlacklistDefaults implements IBarrelLiquidBlacklistDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(BarrelLiquidBlacklistRegistry registry) {
            compat.registerBarrel(registry);
        }
    }

    private static class FluidOnTopDefaults implements IFluidOnTopDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(FluidOnTopRegistry registry) {
            compat.registerFluidOnTop(registry);
        }
    }

    private static class FluidTransformDefaults implements IFluidTransformDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(FluidTransformRegistry registry) {
            compat.registerFluidTransform(registry);
        }
    }

    private static class FluidBlockTransformDefaults implements IFluidBlockDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(FluidBlockTransformerRegistry registry) {
            compat.registerFluidBlockTransform(registry);
        }
    }

    private static class FluidItemFluidDefaults implements IFluidItemFluidDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(FluidItemFluidRegistry registry) {
            compat.registerFluidItemFluid(registry);
        }
    }

    private static class CrucibleStoneDefaults implements ICrucibleStoneDefaultRegistryProvider {

        @Override
        public void registerRecipeDefaults(CrucibleRegistry registry) {
            compat.registerCrucibleStone(registry);
        }
    }

    private static class CrucibleWoodDefaults implements ICrucibleWoodDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(CrucibleRegistry registry) {
            compat.registerCrucibleWood(registry);
        }
    }

    public static class MilkEntityDefaults implements IMilkEntityDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(MilkEntityRegistry registry) {
            compat.registerMilk(registry);
        }
    }

    public static class WitchWaterWorldDefaults implements IWitchWaterWorldDefaultRegistryProvider {
        @Override
        public void registerRecipeDefaults(WitchWaterWorldRegistry registry) {
            compat.registerWitchWaterWorld(registry);
        }
    }
}