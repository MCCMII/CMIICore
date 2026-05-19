package com.cmii.cmiicore.exnihilo;

import com.cmii.cmiicore.Reference;
import com.cmii.cmiicore.exnihilo.blocks.BlockFluidWitchwater;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModFluids {

    public static Fluid fluidWitchwater;

    public static void init() {
        fluidWitchwater = new Fluid("witchwater",
                new ResourceLocation(Reference.MOD_ID, "blocks/witchwater_still"),
                new ResourceLocation(Reference.MOD_ID, "blocks/witchwater_flow"));
        FluidRegistry.registerFluid(fluidWitchwater);
        FluidRegistry.addBucketForFluid(fluidWitchwater);

        ModBlocks.fluidWitchwater = new BlockFluidWitchwater();
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    }
}
