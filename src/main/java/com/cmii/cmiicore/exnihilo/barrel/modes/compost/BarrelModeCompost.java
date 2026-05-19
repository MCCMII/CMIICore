package com.cmii.cmiicore.exnihilo.barrel.modes.compost;

import com.cmii.cmiicore.exnihilo.barrel.IBarrelMode;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.networking.MessageBarrelModeUpdate;
import com.cmii.cmiicore.exnihilo.networking.MessageCompostUpdate;
import com.cmii.cmiicore.exnihilo.networking.PacketHandler;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.registries.types.Compostable;
import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.tiles.TileBarrel;
import com.cmii.cmiicore.exnihilo.util.ItemInfo;
import com.cmii.cmiicore.exnihilo.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class BarrelModeCompost implements IBarrelMode {

    private final Color whiteColor = new Color(1f, 1f, 1f, 1f);
    private final BarrelItemHandlerCompost handler;
    private float fillAmount = 0;
    private Color color = new Color("EEA96D");
    private Color originalColor;
    private float progress = 0;
    private IBlockState compostState;

    public BarrelModeCompost() {
        handler = new BarrelItemHandlerCompost(null);
    }

    public float getFillAmount() {
        return fillAmount;
    }

    public void setFillAmount(float fillAmount) {
        this.fillAmount = fillAmount;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getOriginalColor() {
        return originalColor;
    }

    public void setOriginalColor(Color originalColor) {
        this.originalColor = originalColor;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public IBlockState getCompostState() {
        return compostState;
    }

    @Override
    public void onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (fillAmount == 0) {
            if (!player.getHeldItem(hand).isEmpty()) {
                ItemInfo info = new ItemInfo(player.getHeldItem(hand));
                if (ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(info)) {
                    Compostable comp = ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(info);
                    compostState = comp.getCompostBlock().getBlockState();
                    PacketHandler.sendNBTUpdate(barrel);
                }
            }
        }
        if (fillAmount < 1 && compostState != null) {
            if (!player.getHeldItem(hand).isEmpty()) {
                ItemStack stack = player.getHeldItem(hand);
                ItemInfo info = new ItemInfo(player.getHeldItem(hand));
                Compostable comp = ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(info);

                if (!comp.getCompostBlock().isValid()) return;

                IBlockState testState = comp.getCompostBlock().getBlockState();

                if (ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(info) && compostState.equals(testState)) {
                    Compostable compost = ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(info);
                    Color compColor = compost.getColor();

                    boolean isFirst = fillAmount == 0;

                    fillAmount += compost.getValue();
                    if (fillAmount > 1)
                        fillAmount = 1;
                    if (!player.capabilities.isCreativeMode) {
                        player.getHeldItem(hand).shrink(1);
                    }
                    PacketHandler.sendToAllAround(new MessageCompostUpdate(this.fillAmount, compColor, stack, this.progress, comp.getValue(), barrel.getPos(), isFirst), barrel);
                    barrel.markDirty();
                }
            }
        } else if (progress >= 1) {
            Util.dropItemInWorld(barrel, player, new ItemStack(compostState == null ? Blocks.AIR : compostState.getBlock(), 1, compostState == null ? 0 : compostState.getBlock().getMetaFromState(compostState)), 0.02f);
            removeItem(barrel);
        }
    }

    public void removeItem(TileBarrel barrel) {
        progress = 0;
        fillAmount = 0;
        color = new Color("EEA96D");
        handler.setStackInSlot(0, ItemStack.EMPTY);
        compostState = null;
        PacketHandler.sendToAllAround(new MessageCompostUpdate(this.fillAmount, this.color, ItemStack.EMPTY, this.progress, 0.0f, barrel.getPos(), false), barrel);
        barrel.setMode("null");
        IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
        PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
        barrel.getWorld().setBlockState(barrel.getPos(), state);
    }

    public void addItem(ItemStack stack, TileBarrel barrel) {
        if (fillAmount < 1) {
            if (stack != null && !stack.isEmpty()) {
                ItemInfo info = new ItemInfo(stack);
                Compostable comp = ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(info);
                IBlockState testState = comp.getCompostBlock().getBlockState();

                if (ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(info) && compostState == null) {
                    compostState = testState;
                }

                if (ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(info) && compostState.equals(testState)) {
                    Compostable compost = ExNihiloRegistryManager.COMPOST_REGISTRY.getItem(info);

                    boolean isFirst = fillAmount == 0;
                    fillAmount += compost.getValue();
                    if (fillAmount > 1)
                        fillAmount = 1;
                    PacketHandler.sendToAllAround(new MessageCompostUpdate(this.fillAmount, comp.getColor(), stack, this.progress, comp.getValue(), barrel.getPos(), isFirst), barrel);
                    barrel.markDirty();
                }
            }
        }
    }

    @Override
    public void update(TileBarrel barrel) {
        if (fillAmount >= 1 && progress < 1) {
            if (progress == 0) {
                originalColor = color;
            }

            progress += 1.0 / ModConfig.composting.ticksToFormDirt;
            color = Color.average(originalColor, whiteColor, progress);

            PacketHandler.sendToAllAround(new MessageCompostUpdate(this.fillAmount, this.color, ItemStack.EMPTY, this.progress, 0.0f, barrel.getPos(), false), barrel);

            barrel.markDirty();
        }

        if (progress >= 1 && compostState != null) {
            barrel.setMode("block");
            PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("block", barrel.getPos()), barrel);

            barrel.getMode().addItem(new ItemStack(compostState.getBlock(), 1, compostState.getBlock().getMetaFromState(compostState)), barrel);
        }
    }

    @Override
    public String getName() {
        return "compost";
    }

    @Override
    public List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip) {
        if (compostState != null)
            currenttip.add("Composting " + compostState.getBlock().getLocalizedName());
        if (progress == 0) {
            currenttip.add(Math.round(fillAmount * 100) + "% full");
        } else {
            currenttip.add(Math.round(progress * 100) + "% complete");
        }
        return currenttip;
    }

    @Override
    public boolean isTriggerItemStack(ItemStack stack) {
        return ExNihiloRegistryManager.COMPOST_REGISTRY.containsItem(stack);
    }

    @Override
    public boolean isTriggerFluidStack(FluidStack stack) {
        return false;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setFloat("fillAmount", fillAmount);
        tag.setInteger("color", color.toInt());
        if (originalColor != null)
            tag.setInteger("originalColor", originalColor.toInt());
        tag.setFloat("progress", progress);
        if (compostState != null) {
            tag.setString("block", Block.REGISTRY.getNameForObject(compostState.getBlock()).toString());
            tag.setInteger("meta", compostState.getBlock().getMetaFromState(compostState));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        fillAmount = tag.getFloat("fillAmount");
        this.color = new Color(tag.getInteger("color"));
        if (tag.hasKey("originalColor"))
            this.originalColor = new Color(tag.getInteger("originalColor"));
        this.progress = tag.getFloat("progress");
        if (tag.hasKey("block")) {
            Block block = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("block")));
            compostState = block.getStateFromMeta(tag.getInteger("meta"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
        if (compostState == null)
            return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                    .getTexture(Blocks.DIRT.getDefaultState());
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
                .getTexture(compostState);
    }

    @Override
    public float getFilledLevelForRender(TileBarrel barrel) {
        return fillAmount * 0.9375F;
    }

    @Override
    public Color getColorForRender(TileBarrel barrel) {
        return color;
    }

    @Override
    public ItemStackHandler getHandler(TileBarrel barrel) {
        handler.setBarrel(barrel);
        return handler;
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