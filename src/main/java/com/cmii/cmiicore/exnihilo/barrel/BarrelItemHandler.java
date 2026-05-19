package com.cmii.cmiicore.exnihilo.barrel;

import com.cmii.cmiicore.exnihilo.networking.MessageBarrelModeUpdate;
import com.cmii.cmiicore.exnihilo.networking.PacketHandler;
import com.cmii.cmiicore.exnihilo.registries.registries.BarrelModeRegistry;
import com.cmii.cmiicore.exnihilo.registries.registries.BarrelModeRegistry.TriggerType;
import com.cmii.cmiicore.exnihilo.tiles.TileBarrel;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;

public class BarrelItemHandler extends ItemStackHandler {

    private final TileBarrel barrel;

    public BarrelItemHandler(TileBarrel barrel) {
        super(1);
        this.barrel = barrel;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null) {
            return barrel.getMode().getHandler(barrel).getStackInSlot(slot);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (barrel.getMode() != null && barrel.getMode().isTriggerItemStack(stack)) {
            barrel.getMode().addItem(stack, barrel);
            barrel.markDirty();

            IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
            barrel.getWorld().setBlockState(barrel.getPos(), state);
        } else if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null) {
            barrel.getMode().getHandler(barrel).setStackInSlot(slot, stack);
        } else if (barrel.getMode() == null) {
            List<IBarrelMode> modes = BarrelModeRegistry.getModes(TriggerType.ITEM);

            if (modes != null) {
                for (IBarrelMode possibleMode : modes) {
                    if (possibleMode.isTriggerItemStack(stack)) {
                        barrel.setMode(possibleMode.getName());
                        PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(barrel.getMode().getName(), barrel.getPos()), barrel);

                        barrel.getMode().addItem(stack, barrel);
                        barrel.markDirty();

                        IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
                        barrel.getWorld().setBlockState(barrel.getPos(), state);
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null) {
            return barrel.getMode().getHandler(barrel).extractItem(slot, amount, simulate);
        }

        return ItemStack.EMPTY;
    }
}