package com.cmii.cmiicore.catalyst.items;

import com.cmii.cmiicore.catalyst.CreativeTabCatalyst;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemMineralite extends Item implements IHasModel {

    public ItemMineralite() {
        setTranslationKey("item_mineralite");
        setRegistryName("item_mineralite");
        setCreativeTab(CreativeTabCatalyst.tabCatalyst);
        setHasSubtypes(true);
        Data.ITEMS.add(this);
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        EnumMineraliteType type = EnumMineraliteType.getByMeta(stack.getMetadata());
        if (type != null) {
            return getTranslationKey() + "." + type.name;
        }
        return getTranslationKey() + ".unknown";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            for (EnumMineraliteType type : EnumMineraliteType.values()) {
                list.add(new ItemStack(this, 1, type.meta));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel(ModelRegistryEvent event) {
        for (EnumMineraliteType type : EnumMineraliteType.values()) {
            ModelLoader.setCustomModelResourceLocation(
                this, type.meta,
                new ModelResourceLocation(getRegistryName(), "type=" + type.name));
        }
    }
}
