package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.CreativeTabExNihilo;
import com.cmii.cmiicore.exnihilo.barrel.IBarrelMode;
import com.cmii.cmiicore.exnihilo.barrel.modes.block.BarrelModeBlock;
import com.cmii.cmiicore.exnihilo.barrel.modes.compost.BarrelModeCompost;
import com.cmii.cmiicore.exnihilo.barrel.modes.fluid.BarrelModeFluid;
import com.cmii.cmiicore.exnihilo.barrel.modes.transform.BarrelModeFluidTransform;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.tiles.TileBarrel;
import com.cmii.cmiicore.exnihilo.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class BlockBarrel extends BlockBase implements ITileEntityProvider {

    private final AxisAlignedBB boundingBox = new AxisAlignedBB(0.0625f, 0, 0.0625f, 0.9375f, 1f, 0.9375f);
    private final int tier;

    public BlockBarrel(int tier, Material material) {
        super(material, "block_barrel" + tier);
        this.tier = tier;
        this.setHardness(2.0f);
        this.setCreativeTab(CreativeTabExNihilo.tabExNihilo);
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            TileBarrel barrel = (TileBarrel) te;

            if (barrel.getMode() != null && barrel.getMode().getName().equals("block")) {
                IItemHandler barrelCap = barrel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (barrelCap != null) {
                    ItemStack stack = barrelCap.getStackInSlot(0);
                    if (!stack.isEmpty())
                        Util.dropItemInWorld(te, null, stack, 0);
                }
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getLightValue(@Nonnull IBlockState state, IBlockAccess world, @Nonnull BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);

        if (te instanceof TileBarrel && ModConfig.misc.enableBarrelLighting) {
            TileBarrel tile = (TileBarrel) te;
            if (tile.getMode() instanceof BarrelModeBlock) {
                BarrelModeBlock mode = (BarrelModeBlock) tile.getMode();

                if (mode.getBlock() != null) {
                    return mode.getBlock().getBlockState().getLightValue();
                }
            } else if (tile.getMode() instanceof BarrelModeFluid) {
                BarrelModeFluid mode = (BarrelModeFluid) tile.getMode();

                if (mode.getFluidHandler(tile).getFluidAmount() > 0) {
                    return Util.getLightValue(mode.getFluidHandler(tile).getFluid());
                }
            } else if (ModConfig.misc.enableBarrelTransformLighting) {
                if (tile.getMode() instanceof BarrelModeCompost) {
                    BarrelModeCompost mode = (BarrelModeCompost) tile.getMode();

                    if (mode.getCompostState() != null) {
                        int value = mode.getCompostState().getLightValue() / 2;

                        return Math.round(Util.weightedAverage((float) value / 2, value, mode.getProgress()));
                    }
                } else if (tile.getMode() instanceof BarrelModeFluidTransform) {
                    BarrelModeFluidTransform mode = (BarrelModeFluidTransform) tile.getMode();

                    int inputValue = Util.getLightValue(mode.getInputStack());
                    int outputValue = Util.getLightValue(mode.getOutputStack());

                    return Math.round(Util.weightedAverage(outputValue, inputValue, mode.getProgress()));
                }
            }
        }

        return super.getLightValue(state, world, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return worldIn.isRemote || worldIn.getTileEntity(pos) == null || ((TileBarrel) worldIn.getTileEntity(pos)).onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);

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

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileBarrel(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    // Barrels will attempt to milk entities
    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileBarrel) {
            ((TileBarrel) te).entityOnTop(worldIn, entityIn);
        }
    }

    public int getTier() {
        return tier;
    }
}
