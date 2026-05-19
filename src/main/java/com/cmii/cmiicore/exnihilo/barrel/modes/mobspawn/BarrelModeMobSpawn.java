package com.cmii.cmiicore.exnihilo.barrel.modes.mobspawn;

import com.cmii.cmiicore.exnihilo.barrel.IBarrelMode;
import com.cmii.cmiicore.exnihilo.items.ItemDoll;
import com.cmii.cmiicore.exnihilo.networking.MessageBarrelModeUpdate;
import com.cmii.cmiicore.exnihilo.networking.PacketHandler;
import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.tiles.TileBarrel;
import com.cmii.cmiicore.exnihilo.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class BarrelModeMobSpawn implements IBarrelMode {

    private float progress = 0;

    private ItemStack dollStack;

    public void setDollStack(ItemStack dollStack) {
        this.dollStack = dollStack;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setFloat("progress", progress);

        if (dollStack != null) {
            NBTTagCompound dollTag = dollStack.writeToNBT(new NBTTagCompound());
            tag.setTag("doll", dollTag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        progress = tag.getFloat("progress");

        if (tag.hasKey("doll")) {
            dollStack = new ItemStack(tag.getCompoundTag("doll"));
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
        return "mobspawn";
    }

    @Override
    public void onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
        if (dollStack == null || dollStack.isEmpty())
            return null;

        ItemDoll doll = (ItemDoll) dollStack.getItem();

        return Util.getTextureFromFluid(doll.getSpawnFluid(dollStack));
    }

    @Override
    public Color getColorForRender(TileBarrel barrel) {
        if (dollStack == null || dollStack.isEmpty())
            return Util.whiteColor;

        ItemDoll doll = (ItemDoll) dollStack.getItem();

        return Util.getColorFromFluid(doll.getSpawnFluid(dollStack));
    }

    @Override
    public float getFilledLevelForRender(TileBarrel barrel) {
        return progress * 0.9375F;
    }

    @Override
    public List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip) {
        if (dollStack != null && !dollStack.isEmpty()) {
            currenttip.add("Spawning " + dollStack.getDisplayName() + " (" + Math.round(progress * 100) + "%)");
        }
        return currenttip;
    }

    @Override
    public void update(TileBarrel barrel) {
        if (dollStack != null && !dollStack.isEmpty()) {
            progress += 1.0f / 1200f; // 60 seconds default

            if (progress >= 1) {
                ItemDoll doll = (ItemDoll) dollStack.getItem();
                doll.spawnMob(dollStack, barrel.getWorld(), barrel.getPos().up());
                progress = 0;
                dollStack = ItemStack.EMPTY;
                barrel.setMode("null");
                PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
                barrel.markDirty();
            }
        }
    }

    @Override
    public void addItem(ItemStack stack, TileBarrel barrel) {
        if (dollStack == null || dollStack.isEmpty()) {
            ItemDoll doll = (ItemDoll) stack.getItem();
            if (doll.getSpawnFluid(stack) != null) {
                dollStack = stack.copy();
                dollStack.setCount(1);
            }
        }
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