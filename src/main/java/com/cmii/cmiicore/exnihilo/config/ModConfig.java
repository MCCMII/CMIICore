package com.cmii.cmiicore.exnihilo.config;

import com.cmii.cmiicore.Reference;
import com.cmii.cmiicore.exnihilo.items.tools.EnumCrook;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@Config(modid = Reference.MOD_ID, name = "cmiicore/exnihilo", category = "exnihilo")
public class ModConfig {

    public static boolean configsLoaded = false;

    public static void loadConfigs() {
        ConfigManager.load(Reference.MOD_ID, Config.Type.INSTANCE);
        configsLoaded = true;
    }

    public static final Misc misc = new Misc();
    @Config.Comment("These configs can be changed client-side without causing server connection problems.")
    public static final Client client = new Client();
    public static final Mechanics mechanics = new Mechanics();
    public static final Composting composting = new Composting();
    public static final InfestedLeaves infested_leaves = new InfestedLeaves();
    public static final Crooking crooking = new Crooking();
    public static final Crucible crucible = new Crucible();
    public static final World world = new World();
    public static final WitchWater witchwater = new WitchWater();

    // --- Misc ---
    public static class Misc {
        @Config.Comment({"Barrel light up if their contents do,",
                "disabling this makes `B:enableBarrelTransformLighting` do nothing"})
        public boolean enableBarrelLighting = true;
        @Config.Comment("Barrel lighting can change during transformations")
        public boolean enableBarrelTransformLighting = true;

        @Config.RequiresMcRestart
        @Config.Comment({"Enable this to to load the JSON files.",
                "Keeping this on false will only load recipes registered in code",
                "This is recommended to enable for Packmakers",
                "For players just playing with this without wanting to configure something this is recommended to keep disabled",
                "If you are having trouble reading custom json files I suggest using https://jsonlint.com/ to validate your files."})
        public boolean enableJSONLoading = false;

        public boolean oredictVanillaItems = true;
        @Config.Comment("Add Ex Nihilo seeds to the listAllSeeds oredict")
        public boolean oredictExNihiloSeeds = true;

        @Config.Comment({"When attempting to reuse an existing item from the oredict, what modids should be given preference."})
        public String[] oreDictPreferenceOrder = {"thermalfoundation", "magneticraft", "immersiveengineering", "nuclearcraft", "techreborn"};

        @Config.Comment({"Possible results of using a rubber seed."})
        public String[] rubberSeeds = {"techreborn:rubber_sapling", "ic2:sapling"};
    }

    public static class Client {
        @Config.Comment("This enables the thin crucible model which is similar to the one in the 1.7 Ex Nihilo.")
        public boolean thinCrucibleModel = false;
    }

    // --- Mechanics ---
    public static class Mechanics {
        public boolean enableBarrels = true;
        public boolean enableCrucible = true;
        public boolean shouldBarrelsFillWithRain = true;

        @Config.Comment({"Default max fluid temp allowed in the barrel. Does nothing if JSON configs are used.",
        "Set to a high number to disable any blacklisting. Water = 300, Lava = 1300"})
        @Config.RangeInt(min = -1)
        public int woodBarrelMaxTemp = 301;
    }

    // --- Composting ---
    public static class Composting {
        @Config.RangeInt(min = 1)
        public int ticksToFormDirt = 600;
    }

    // --- Infested Leaves ---
    public static class InfestedLeaves {
        @Config.RangeInt(min = 1)
        public int ticksToTransform = 600;
        @Config.Comment("How many ticks to wait before getting ticked again, already fully infested leaves spread much slower due to waiting for world ticks.")
        @Config.RangeInt(min = 0)
        public int leavesUpdateFrequency = 5;
        @Config.Comment("Minimum percentage to spread")
        @Config.RangeInt(min = 0, max = 100)
        public int leavesSpreadPercent = 15;
        @Config.Comment("Chance to spread if it got ticked")
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public float leavesSpreadChanceFloat = 0.5f;
    }

    // --- Crooking ---
    public static class Crooking {
        @Config.Comment("Durability for each of the default crooks.")
        public Map<String, Integer> durability = new HashMap<>();

        Crooking() {
            for (EnumCrook crook : EnumCrook.values()) {
                durability.put(crook.getRegistryName(), crook.getDefaultDurability());
            }
        }

        @Config.RangeDouble(min = 0.0, max = 1.0)
        public double stringChance = 1.0;
        @Config.RangeInt(min = 0)
        public int maxStringDrop = 2;
        @Config.RangeDouble(min = 0.0, max = 1.0)
        public double stringFortuneChance = 1.0;
        @Config.RangeInt(min = 0)
        public int numberOfTimesToTestVanillaDrops = 3;
        @Config.Comment("Disable the ExNihilo Crooks, useful if another mod adds compatible crooks.")
        public boolean disableCrookCrafting = false;
    }

    // --- Crucible ---
    public static class Crucible {
        @Config.RangeInt(min = 1)
        public int woodenCrucibleSpeed = 4;
    }

    // --- World ---
    public static class World {
        public boolean isSkyWorld = true;
        public int normalDropPercent = 100;
    }

    // --- Witch Water ---
    public static class WitchWater {
        @Config.Comment("Enable custom fluid mixing")
        public boolean enableWitchWaterBlockForming = true;
    }

    // --- Configuration Reload Handler ---
    @Mod.EventBusSubscriber
    static class ConfigurationHolder {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Reference.MOD_ID)) {
                ConfigManager.load(Reference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
