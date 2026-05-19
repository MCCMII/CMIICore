package com.cmii.cmiicore.exnihilo.barrel.modes.transform;

import com.cmii.cmiicore.exnihilo.barrel.IBarrelMode;
import com.cmii.cmiicore.exnihilo.networking.MessageBarrelModeUpdate;
import com.cmii.cmiicore.exnihilo.networking.PacketHandler;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.types.FluidTransformer;
import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.tiles.TileBarrel;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarrelModeFluidTransform implements IBarrelMode {

    private FluidStack inputStack, outputStack;
    private float progress = 0;
    private FluidTransformer transformer;

    public FluidStack getInputStack() {
        return inputStack;
    }

    public void setInputStack(FluidStack inputStack) {
        this.inputStack = inputStack;
    }

    public FluidStack getOutputStack() {
        return outputStack;
    }

    public void setOutputStack(FluidStack outputStack) {
        this.outputStack = outputStack;
    }

    public float getProgress() {
        return progress;
    }

    public FluidTransformer getTransformer() {
        return transformer;
    }

    public void setTransformer(FluidTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        if (inputStack != null) {
            NBTTagCompound inputTag = inputStack.writeToNBT(new NBTTagCompound());
            tag.setTag("inputTag", inputTag);
        }

        if (outputStack != null) {
            NBTTagCompound outputTag = outputStack.writeToNBT(new NBTTagCompound());
            tag.setTag("outputTag", outputTag);
        }

        tag.setFloat("progress", progress);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("inputTag")) {
            inputStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("inputTag"));
        }
        if (tag.hasKey("outputTag")) {
            outputStack = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("outputTag"));
        }
        if (tag.hasKey("progress")) {
            progress = tag.getFloat("progress");
        }
    }

    @Override
    public boolean isTriggerItemStack(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isTriggerFluidStack(FluidStack stack) {
        return false;
    }

    @Override
    public String getName() {
        return "fluidtransform";
    }

    @Override
    public void onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
        if (outputStack != null && outputStack.getFluid() != null)
            return Util.getTextureFromFluid(outputStack.getFluid());
        if (inputStack != null && inputStack.getFluid() != null)
            return Util.getTextureFromFluid(inputStack.getFluid());
        return Util.getTextureFromBlockState(Blocks.AIR.getDefaultState());
    }

    @Override
    public Color getColorForRender(TileBarrel barrel) {
        if (outputStack != null && outputStack.getFluid() != null)
            return Util.getColorFromFluid(outputStack.getFluid());
        if (inputStack != null && inputStack.getFluid() != null)
            return Util.getColorFromFluid(inputStack.getFluid());
        return Util.whiteColor;
    }

    @Override
    public float getFilledLevelForRender(TileBarrel barrel) {
        float inLevel = inputStack != null ? ((float) inputStack.amount) / Fluid.BUCKET_VOLUME : 0f;
        float outLevel = outputStack != null ? ((float) outputStack.amount) / Fluid.BUCKET_VOLUME : 0f;

        return (1f - (inLevel - outLevel)) * 0.9375F;
    }

    @Override
    public List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip) {
        if (outputStack != null && outputStack.getFluid() != null) {
            currenttip.add("Transforming into " + outputStack.getFluid().getLocalizedName(null));
            currenttip.add(Math.round(progress * 100) + "% complete");

            FluidStack stack = barrel.getTank().getFluid();
            if (stack != null)
                currenttip.add(stack.getLocalizedName());
        }
        return currenttip;
    }

    @Override
    public void update(TileBarrel barrel) {
        if (transformer == null && inputStack != null) {
            List<FluidTransformer> transformers = ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY.getFluidTransformers(inputStack.getFluid().getName());
            if (transformers != null && !transformers.isEmpty()) {
                transformer = transformers.get(0);
            }
        }

        if (transformer != null && outputStack != null) {
            float rate = (float) inputStack.amount / transformer.getDuration();

            inputStack.amount -= Math.ceil(rate);
            outputStack.amount += Math.ceil(rate);

            if (inputStack.amount <= 0) {
                barrel.getTank().setFluid(outputStack);
                barrel.setMode("fluid");
                PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("fluid", barrel.getPos()), barrel);
                barrel.markDirty();
            }

            // Check for blocks transforming
            if (transformer.getTransformingBlocks() != null) {
                List<BlockInfo> transformingBlocks = new ArrayList<>(Arrays.asList(transformer.getTransformingBlocks()));
                List<BlockInfo> blocksToSpawn = new ArrayList<>(Arrays.asList(transformer.getBlocksToSpawn()));

                for (int i = 0; i < transformingBlocks.size(); i++) {
                    BlockInfo transformBlock = transformingBlocks.get(i);
                    BlockInfo spawnBlock = blocksToSpawn.size() > i ? blocksToSpawn.get(i) : blocksToSpawn.get(0);

                    IBlockState checkState = transformBlock.getBlockState();
                    BlockPos checkPos = barrel.getPos().down();
                    IBlockState worldState = barrel.getWorld().getBlockState(checkPos);

                    if (worldState == checkState || (transformBlock.isWildcard() && worldState.getBlock() == checkState.getBlock())) {
                        barrel.getWorld().setBlockState(checkPos, spawnBlock.getBlockState());
                    }
                }
            }
        }

        if (outputStack != null && inputStack != null && inputStack.amount > 0) {
            progress = 1f - ((float) inputStack.amount / Fluid.BUCKET_VOLUME);
        }
    }

    @Override
    public void addItem(ItemStack stack, TileBarrel barrel) {
    }

    @Override
    public ItemStackHandler getHandler(TileBarrel barrel) {
        return null;
    }

    @Override
    public FluidTank getFluidHandler(TileBarrel barrel) {
        return null;
    }

    @Override
    public boolean canFillWithFluid(TileBarrel barrel) {
        return false;
    }
}