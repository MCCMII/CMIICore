package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.CreativeTabExNihilo;
import com.cmii.cmiicore.exnihilo.tiles.TileCrucibleBase;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public abstract class BlockCrucibleBase extends Block implements IHasModel {

    public static final PropertyBool THIN = PropertyBool.create("thin");


    public BlockCrucibleBase(String name, Material material) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);

        setCreativeTab(CreativeTabExNihilo.tabExNihilo);

        Data.BLOCKS.add(this);
    }

    @Override
    public abstract TileEntity createTileEntity(@Nonnull World worldIn, @Nonnull IBlockState state);

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        TileCrucibleBase te = (TileCrucibleBase) world.getTileEntity(pos);

        if (te != null) {
            IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
            return te.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ, fluidHandler);
        } else {
            return true;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

}