package com.cmii.cmiicore.exnihilo.barrel.modes.fluid;

import com.cmii.cmiicore.exnihilo.barrel.BarrelFluidHandler;
import com.cmii.cmiicore.exnihilo.barrel.IBarrelMode;
import com.cmii.cmiicore.exnihilo.barrel.modes.transform.BarrelModeFluidTransform;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.types.FluidTransformer;
import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.tiles.TileBarrel;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class BarrelModeFluid implements IBarrelMode {

    private final BarrelItemHandlerFluid handler;

    public BarrelModeFluid() {
        handler = new BarrelItemHandlerFluid(null);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
    }

    @Override
    public boolean isTriggerItemStack(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isTriggerFluidStack(FluidStack stack) {
        return stack != null;
    }

    @Override
    public String getName() {
        return "fluid";
    }

    @Override
    public List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip) {
        if (barrel.getTank().getFluid() != null) {
            currenttip.add(barrel.getTank().getFluid().getLocalizedName());
            currenttip.add("Amount: " + barrel.getTank().getFluidAmount() + "mb");
        } else {
            currenttip.add("Empty");
        }

        return currenttip;
    }

    @Override
    public void onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (barrel.getTank().getFluid() != null && barrel.getTank().getFluidAmount() > 0 && !handler.getStackInSlot(0).isEmpty()
                && player.getHeldItem(hand).isEmpty()) {
            Util.dropItemInWorld(barrel, player, handler.getStackInSlot(0), 0.02);
            handler.setStackInSlot(0, ItemStack.EMPTY);
            barrel.markDirty();
            return;
        }

        if (!barrel.getWorld().isRemote) {
            FluidStack fluidStack = barrel.getTank().getFluid();
            if (fluidStack != null && fluidStack.getFluid() != null && fluidStack.getFluid().canBePlacedInWorld()) {
                BlockPos fluidPos = pos.offset(side);
                IBlockState stateAt = barrel.getWorld().getBlockState(fluidPos);
                Block blockAt = stateAt.getBlock();

                if (stateAt.getMaterial() != Material.WATER && stateAt.getMaterial() != Material.LAVA) {
                    if (!(blockAt instanceof BlockLiquid)) {
                        barrel.getWorld().setBlockToAir(fluidPos);
                    }
                    barrel.getWorld().setBlockState(fluidPos, fluidStack.getFluid().getBlock().getDefaultState());
                }
            } else if (fluidStack != null && fluidStack.getFluid() == null) {
                BlockInfo blockResult = ExNihiloRegistryManager.WITCH_WATER_WORLD_REGISTRY.getResult(fluidStack.getFluid(), 1.0f);
                IBlockState newState = blockResult.getBlockState();
                barrel.getWorld().setBlockState(pos, newState);
            }
        }

        // Fluid Transform
        if (barrel.getTank().getFluid() != null) {
            FluidTransformer transformer = checkForTransform(barrel.getTank().getFluid().getFluid());
            if (transformer != null) {
                barrel.setMode("fluidtransform");
                ((BarrelModeFluidTransform) barrel.getMode()).setTransformer(transformer);
                ((BarrelModeFluidTransform) barrel.getMode()).setInputStack(barrel.getTank().getFluid().copy());
                ((BarrelModeFluidTransform) barrel.getMode()).setOutputStack(new FluidStack(FluidRegistry.getFluid(transformer.getOutputFluid()), 0));
                barrel.getTank().setFluid(null);
                barrel.markDirty();
            }
        }
    }

    private FluidTransformer checkForTransform(Fluid fluid) {
        if (ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.containsKey(fluid.getName())) {
            List<FluidTransformer> transformers = ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.getFluidTransformers(fluid.getName());

            if (transformers != null && !transformers.isEmpty()) {
                return transformers.get(0);
            }
        }

        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
        if (barrel.getTank().getFluid() != null)
            return Util.getTextureFromFluid(barrel.getTank().getFluid().getFluid());
        else
            return Util.getTextureFromBlockState(Blocks.AIR.getDefaultState());
    }

    @Override
    public Color getColorForRender(TileBarrel barrel) {
        if (barrel.getTank().getFluid() != null)
            return Util.getColorFromFluid(barrel.getTank().getFluid().getFluid());
        return Util.whiteColor;
    }

    @Override
    public float getFilledLevelForRender(TileBarrel barrel) {
        float level = ((float) barrel.getTank().getFluidAmount()) / barrel.getTank().getCapacity();

        return level * 0.9375F;
    }

    @Override
    public void update(TileBarrel barrel) {
    }

    @Override
    public void addItem(ItemStack stack, TileBarrel barrel) {
        handler.setBarrel(barrel);
        handler.insertItem(0, stack, false);
    }

    @Override
    public ItemStackHandler getHandler(TileBarrel barrel) {
        handler.setBarrel(barrel);
        return handler;
    }

    @Override
    public FluidTank getFluidHandler(TileBarrel barrel) {
        return new BarrelFluidHandler(barrel.getTank().getFluid(), barrel.getTank().getCapacity());
    }

    @Override
    public boolean canFillWithFluid(TileBarrel barrel) {
        return true;
    }
}