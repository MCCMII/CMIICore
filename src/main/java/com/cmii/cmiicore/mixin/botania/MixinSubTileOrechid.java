package com.cmii.cmiicore.mixin.botania;

import com.cmii.cmiicore.catalyst.ModCatalystBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

@Mixin(value = SubTileOrechid.class, remap = false)
public class MixinSubTileOrechid {

    @Redirect(
        method = "onUpdate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z"
        )
    )
    private boolean redirectSetBlockState(World world, BlockPos pos, IBlockState state, int flags) {
        Block block = state.getBlock();
        Integer meta = ModCatalystBlocks.ORE_TO_MINERALITE_META.get(block);
        if (meta != null) {
            IBlockState mineraliteState = ModCatalystBlocks.mineraliteOre.getStateFromMeta(meta);
            return world.setBlockState(pos, mineraliteState, flags);
        }
        return world.setBlockState(pos, state, flags);
    }
}
