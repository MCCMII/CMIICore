package com.cmii.cmiicore.mixin.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.BotaniaAPI;

@Mixin(value = BotaniaAPI.class, remap = false)
public class MixinBotaniaAPI {

    /**
     * After BotaniaAPI's static initializer populates oreWeights,
     * clear all entries and replace with Thaumcraft 6 element crystals.
     */
    @Inject(method = "<clinit>", at = @At("RETURN"))
    private static void onPostInit(CallbackInfo ci) {
        BotaniaAPI.oreWeights.clear();

        BotaniaAPI.oreWeights.put("oreCrystalEarth", 8000);
        BotaniaAPI.oreWeights.put("oreCrystalAir", 7000);
        BotaniaAPI.oreWeights.put("oreCrystalWater", 5000);
        BotaniaAPI.oreWeights.put("oreCrystalFire", 3500);
        BotaniaAPI.oreWeights.put("oreCrystalOrder", 2000);
        BotaniaAPI.oreWeights.put("oreCrystalEntropy", 1000);
    }

    /**
     * Block other mods (and Botania itself) from registering additional ore weights,
     * ensuring the Orechid only generates Thaumcraft 6 crystals.
     */
    @Inject(method = "registerOreWeight", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onRegisterOreWeight(String oreDict, int weight, CallbackInfo ci) {
        ci.cancel();
    }
}