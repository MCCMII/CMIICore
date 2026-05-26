package com.cmii.cmiicore.mixin.botania;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to remove the artificial Blaze from Garden of Glass.
 * In GoG, placing a Fel Pumpkin on top of a 2-high pillar of Iron Bars
 * would remove the blocks and spawn a Blaze.
 * This mixin prevents that by cancelling the onBlockAdded method entirely.
 */
@Mixin(targets = "vazkii.botania.common.block.BlockFelPumpkin", remap = false)
public class MixinGardenOfGlass {

    /**
     * Cancel the onBlockAdded method to prevent the Fel Pumpkin structure
     * from spawning a Blaze when placed on iron bars.
     * The method checked for iron bars below and spawned an EntityBlaze.
     */
    @Inject(method = "onBlockAdded", at = @At("HEAD"), cancellable = true, remap = false)
    private void onBlockAdded(World world, BlockPos pos, IBlockState state, CallbackInfo ci) {
        ci.cancel();
    }
}