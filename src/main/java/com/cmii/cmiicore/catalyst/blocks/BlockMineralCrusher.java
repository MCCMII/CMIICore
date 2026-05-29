package com.cmii.cmiicore.catalyst.blocks;

import com.cmii.cmiicore.catalyst.CreativeTabCatalyst;
import com.cmii.cmiicore.catalyst.recipe.MineralCrusherRecipes;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class BlockMineralCrusher extends Block implements IHasModel {

    public BlockMineralCrusher() {
        super(Material.ROCK);
        setRegistryName("block_mineral_crusher");
        setTranslationKey("cmiicore.block_mineral_crusher");
        setCreativeTab(CreativeTabCatalyst.tabCatalyst);
        setHardness(2.0F);
        setResistance(6.0F);
        setHarvestLevel("pickaxe", 0);
        Data.BLOCKS.add(this);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        ItemStack held = player.getHeldItem(hand);
        if (held.isEmpty()) {
            return false;
        }

        List<ItemStack> results = MineralCrusherRecipes.getResults(held, world.rand);
        if (results.isEmpty()) {
            return false;
        }

        held.shrink(1);
        player.setHeldItem(hand, held);

        BlockPos spawnPos = pos.offset(facing);
        for (ItemStack stack : results) {
            spawnAsEntity(world, spawnPos, stack);
        }

        return true;
    }

    private static void spawnAsEntity(World world, BlockPos pos, ItemStack stack) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        EntityItem entity = new EntityItem(world, x, y, z, stack);
        entity.motionX = 0.0D;
        entity.motionY = 0.0D;
        entity.motionZ = 0.0D;
        world.spawnEntity(entity);
    }
}
