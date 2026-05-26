package com.cmii.cmiicore.mixin.botania;

import com.cmii.cmiicore.api.LoonuimExtraProducts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.block.subtile.functional.SubTileLoonuim;

import java.util.Collections;
import java.util.List;

/**
 * Mixin for SubTileLoonuim (Loonium / 聚宝花).
 * <ul>
 *   <li>Reduces mana cost from 35000 to 1000.</li>
 *   <li>Reduces cooldown from 100 ticks (5s) to 20 ticks (1s).</li>
 *   <li>Replaces mob spawning with direct item drops.</li>
 *   <li>Drops items into adjacent IInventory containers (chests, hoppers, etc.)
 *       before falling back to world drops.</li>
 *   <li>Injects extra loot from {@link LoonuimExtraProducts} into the loot pool.</li>
 * </ul>
 */
@Mixin(value = SubTileLoonuim.class, remap = false)
public class MixinSubTileLoonuim {

    /**
     * Lower the mana cost from 35000 to 1000.
     * The constant 35000 appears twice in onUpdate() (mana check and deduction);
     * both are modified here.
     */
    @ModifyConstant(method = "onUpdate", constant = @Constant(intValue = 35000))
    private int modifyManaCost(int original) {
        return 1000;
    }

    /**
     * Lower the cooldown from 100 ticks to 20 ticks (1 second).
     * Only the first occurrence (ordinal 0) of int value 100 is targeted,
     * which is the {@code ticksExisted % 100} check. The other two 100s
     * (potion effect duration for creepers) are left unchanged.
     */
    @ModifyConstant(method = "onUpdate", constant = @Constant(intValue = 100, ordinal = 0))
    private int modifyCooldown(int original) {
        return 20;
    }

    /**
     * Intercept the {@link Collections#shuffle} call to inject extra loot items
     * before the shuffle happens. This gives custom items a fair chance
     * alongside vanilla dungeon loot table entries.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Redirect(
        method = "onUpdate",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Collections;shuffle(Ljava/util/List;)V"
        )
    )
    private void injectExtraLootAndShuffle(List list) {
        List<ItemStack> extras = LoonuimExtraProducts.getExtraLootCopy();
        if (!extras.isEmpty()) {
            list.addAll(extras);
        }
        Collections.shuffle(list);
    }

    /**
     * Hijack the {@code World.spawnEntity(mob)} call and replace it with
     * direct {@link EntityItem} spawning.
     * The generated ItemStack is extracted from the mob's entity data
     * (stored under {@code botania:looniumItemStackToDrop}).
     * Adjacent IInventory containers (chests, hoppers, etc.) are checked
     * in the four horizontal directions; items are inserted into them
     * first. Only leftovers that cannot fit are dropped into the world.
     */
    @Redirect(
        method = "onUpdate",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
        )
    )
    private boolean redirectMobSpawnToItemDrop(World world, Entity entity) {
        NBTTagCompound entityData = entity.getEntityData();
        if (entityData.hasKey("botania:looniumItemStackToDrop")) {
            NBTTagCompound itemTag = entityData.getCompoundTag("botania:looniumItemStackToDrop");
            ItemStack stack = new ItemStack(itemTag);
            if (!stack.isEmpty()) {
                stack = tryInsertToAdjacentContainers(world, entity, stack);
                if (!stack.isEmpty()) {
                    EntityItem entityItem = new EntityItem(
                        world,
                        entity.posX,
                        entity.posY,
                        entity.posZ,
                        stack
                    );
                    world.spawnEntity(entityItem);
                }
            }
        }
        return true;
    }

    /**
     * Check the four horizontal directions around the entity's position
     * for {@link IInventory} tile entities and attempt to insert the stack
     * into them. Returns whatever could not fit.
     */
    private static ItemStack tryInsertToAdjacentContainers(
            World world, Entity entity, ItemStack stack) {
        BlockPos origin = new BlockPos(
            entity.posX, entity.posY, entity.posZ);
        EnumFacing[] directions = {
            EnumFacing.NORTH, EnumFacing.SOUTH,
            EnumFacing.WEST, EnumFacing.EAST
        };
        for (EnumFacing facing : directions) {
            BlockPos adjacent = origin.offset(facing);
            TileEntity tile = world.getTileEntity(adjacent);
            if (tile instanceof IInventory) {
                stack = tryInsertToInventory((IInventory) tile, stack);
                if (stack.isEmpty()) {
                    break;
                }
            }
        }
        return stack;
    }

    /**
     * Insert an ItemStack into an IInventory.
     * First pass: merge into existing stacks of the same item.
     * Second pass: fill empty valid slots.
     * Returns the leftovers that could not be placed.
     */
    private static ItemStack tryInsertToInventory(
            IInventory inventory, ItemStack stack) {
        // First pass — merge with identical existing stacks
        for (int i = 0; i < inventory.getSizeInventory() && !stack.isEmpty(); i++) {
            ItemStack slot = inventory.getStackInSlot(i);
            if (!slot.isEmpty()
                    && slot.getItem() == stack.getItem()
                    && slot.getItemDamage() == stack.getItemDamage()
                    && ItemStack.areItemStackTagsEqual(slot, stack)) {
                int limit = Math.min(slot.getMaxStackSize(),
                    inventory.getInventoryStackLimit());
                int free = limit - slot.getCount();
                if (free > 0) {
                    int add = Math.min(free, stack.getCount());
                    stack.shrink(add);
                    slot.grow(add);
                    inventory.markDirty();
                }
            }
        }
        // Second pass — fill empty valid slots
        for (int i = 0; i < inventory.getSizeInventory() && !stack.isEmpty(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()
                    && inventory.isItemValidForSlot(i, stack)) {
                int limit = Math.min(stack.getMaxStackSize(),
                    inventory.getInventoryStackLimit());
                int count = Math.min(limit, stack.getCount());
                ItemStack inserted = stack.splitStack(count);
                inventory.setInventorySlotContents(i, inserted);
                inventory.markDirty();
            }
        }
        return stack;
    }
}