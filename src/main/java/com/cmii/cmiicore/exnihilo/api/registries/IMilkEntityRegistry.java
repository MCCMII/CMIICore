package com.cmii.cmiicore.exnihilo.api.registries;

import com.cmii.cmiicore.exnihilo.registries.types.Milkable;
import net.minecraft.entity.Entity;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;

public interface IMilkEntityRegistry extends IRegistryList<Milkable> {

    void register(Entity entityOnTop, Fluid result, int amount, int coolDown);

    void register(String entityOnTop, String result, int amount, int coolDown);

    boolean isValidRecipe(@Nullable Entity entityOnTop);

    boolean isValidRecipe(@Nullable String entityOnTop);

    @Nullable
    Milkable getMilkable(@Nullable Entity entityOnTop);

    @Nullable
    String getResult(@Nullable Entity entityOnTop);

    int getAmount(@Nullable Entity entityOnTop);

    int getCoolDown(@Nullable Entity entityOnTop);
}