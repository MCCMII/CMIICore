package com.cmii.cmiicore.catalyst.blocks;

import com.cmii.cmiicore.catalyst.ModCatalystItems;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class BlockMineraliteOre extends Block implements IHasModel {

    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 5);

    public BlockMineraliteOre() {
        super(Material.ROCK);
        setTranslationKey("block_mineralite_ore");
        setRegistryName("block_mineralite_ore");
        setHardness(3.0F);
        setResistance(5.0F);
        setSoundType(SoundType.STONE);
        setDefaultState(blockState.getBaseState().withProperty(TYPE, 0));
        Data.BLOCKS.add(this);
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        int meta = getMetaFromState(state);
        NonNullList<ItemStack> drops = NonNullList.create();
        int count = 1 + (fortune > 0 ? new Random().nextInt(fortune + 1) : 0);
        drops.add(new ItemStack(ModCatalystItems.mineralite, count, meta));
        return drops;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, Math.min(Math.max(meta, 0), 5));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE);
    }
}
