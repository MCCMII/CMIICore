package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.CreativeTabExNihilo;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.tiles.TileInfestedLeaves;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockInfestedLeaves extends BlockInfestingLeaves {

    public BlockInfestedLeaves() {
        super(InfestedType.INFESTED);
        this.setTranslationKey("block_infested_leaves");
        this.setRegistryName("block_infested_leaves");
        this.setCreativeTab(CreativeTabExNihilo.tabExNihilo);
        Data.BLOCKS.add(this);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false));
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void randomTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        this.updateTick(world, pos, state, rand);
        if (!world.isRemote) {
            if (state.getValue(NEARBYLEAVES)) {
                NonNullList<BlockPos> nearbyLeaves = Util.getNearbyLeaves(world, pos);
                if (nearbyLeaves.isEmpty()) {
                    world.setBlockState(pos, state.withProperty(NEARBYLEAVES, false), 7);
                } else {
                    nearbyLeaves.stream().filter(leaves -> rand.nextFloat() <= ModConfig.infested_leaves.leavesSpreadChanceFloat).findAny().ifPresent(blockPos -> BlockInfestingLeaves.infestLeafBlock(world, world.getBlockState(blockPos), blockPos));
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileInfestedLeaves();
    }

}