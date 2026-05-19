package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.ModBlocks;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.items.tools.ICrook;
import com.cmii.cmiicore.exnihilo.tiles.BaseTileEntity;
import com.cmii.cmiicore.exnihilo.tiles.ITileLeafBlock;
import com.cmii.cmiicore.exnihilo.tiles.TileInfestingLeaves;
import com.cmii.cmiicore.exnihilo.util.Data;
import com.cmii.cmiicore.exnihilo.util.IHasModel;
import com.cmii.cmiicore.exnihilo.util.ItemUtil;
import com.cmii.cmiicore.exnihilo.util.LogUtil;
import com.cmii.cmiicore.exnihilo.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockInfestingLeaves extends BlockLeaves implements ITileEntityProvider, IHasModel {

    public static final PropertyBool NEARBYLEAVES = PropertyBool.create("nearby_leaves");

    public static final IUnlistedProperty<IBlockState> LEAFBLOCK = new IUnlistedProperty<IBlockState>() {
        @Override
        public String getName() {
            return "LeafBlock";
        }

        @Override
        public boolean isValid(IBlockState value) {
            return true;
        }

        @Override
        public Class<IBlockState> getType() {
            return IBlockState.class;
        }

        @Override
        public String valueToString(IBlockState value) {
            return value.toString();
        }
    };

    public BlockInfestingLeaves() {
        this(InfestedType.INFESTING);
        this.setTranslationKey("block_infesting_leaves");
        this.setRegistryName("block_infesting_leaves");
        Data.BLOCKS.add(this);
        this.setDefaultState(
                this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false).withProperty(NEARBYLEAVES, true));
    }

    protected BlockInfestingLeaves(InfestedType type) {
        super();
        this.setTickRandomly(true);
        this.leavesFancy = true;
    }

    /**
     * Sets a normal leave to infested
     */
    public static void infestLeafBlock(World world, IBlockState state, BlockPos pos) {
        IBlockState leafState;
        //Prevents a crash with forestry using the new model system
        if (Block.REGISTRY.getNameForObject(state.getBlock()).getNamespace().equalsIgnoreCase("forestry")) {
            leafState = Blocks.LEAVES.getDefaultState();
        } else {
            leafState = state;
        }

        Boolean check_decay = leafState.getPropertyKeys().contains(CHECK_DECAY) ? leafState.getValue(CHECK_DECAY) : false;
        Boolean decayable = leafState.getPropertyKeys().contains(DECAYABLE) ? leafState.getValue(DECAYABLE) : false;

        world.setBlockState(pos,
                ModBlocks.infestingLeaves.getDefaultState()
                        .withProperty(CHECK_DECAY, check_decay)
                        .withProperty(DECAYABLE, decayable),
                3);
        if (world.getTileEntity(pos) != null)
            ((ITileLeafBlock) world.getTileEntity(pos)).setLeafBlock(leafState);
    }

    /**
     * Used to set an existing infesting leaves block to infested
     *
     * @param world     The world this is happening in
     * @param pos       The position of the block
     * @param leafState The leaf state captured by the infesting leaves block block
     */
    public static void setInfested(World world, BlockPos pos, IBlockState leafState) {
        IBlockState block = world.getBlockState(pos);
        if (block.getBlock() instanceof BlockInfestingLeaves) {
            IBlockState retval = ModBlocks.infestedLeaves.getDefaultState()
                    .withProperty(CHECK_DECAY, block.getValue(CHECK_DECAY))
                    .withProperty(DECAYABLE, block.getValue(DECAYABLE));
            retval = ((IExtendedBlockState) retval).withProperty(BlockInfestedLeaves.LEAFBLOCK, leafState);

            world.setBlockState(pos, retval, 0b111);

            if (world.getTileEntity(pos) != null) {
                ((ITileLeafBlock) world.getTileEntity(pos)).setLeafBlock(leafState);
                ((BaseTileEntity) world.getTileEntity(pos)).markDirtyClient();
            }
        } else if (Util.isLeaves(block) && !(block.getBlock() instanceof BlockInfestedLeaves)) {
            LogUtil.error("Sent leaf change to wrong method, redirecting");
            infestLeafBlock(world, block, pos);
        }
    }

    public static void spread(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote && state != null) {
            if (state.getValue(NEARBYLEAVES)) {
                NonNullList<BlockPos> nearbyLeaves = Util.getNearbyLeaves(world, pos);
                if (nearbyLeaves.isEmpty()) {
                    world.setBlockState(pos, state.withProperty(NEARBYLEAVES, false), 7);
                } else {
                    TileEntity te = world.getTileEntity(pos);
                    if (te instanceof TileInfestingLeaves && ((TileInfestingLeaves) te).getProgress() > ModConfig.infested_leaves.leavesSpreadPercent) {
                        nearbyLeaves.stream().filter(leaves -> rand.nextFloat() <= ModConfig.infested_leaves.leavesSpreadChanceFloat).findAny().ifPresent(blockPos -> BlockInfestingLeaves.infestLeafBlock(world, world.getBlockState(blockPos), blockPos));
                    }
                }
            }
        }
    }

    public static IBlockState getLeafState(IBlockState state) {
        if (state instanceof IExtendedBlockState) {
            return ((IExtendedBlockState) state).getValue(BlockInfestedLeaves.LEAFBLOCK);
        }

        return state;
    }

    @Override
    public void randomTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        this.updateTick(world, pos, state, rand);
        spread(world, pos, state, rand);
    }

    @Override
    public int tickRate(World worldIn) {
        return ModConfig.infested_leaves.leavesUpdateFrequency;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote && Util.isLeaves(worldIn.getBlockState(fromPos)))
            worldIn.setBlockState(pos, state.withProperty(NEARBYLEAVES, true), 7);
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @Nonnull
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        if (state instanceof IExtendedBlockState) {
            IExtendedBlockState retval = (IExtendedBlockState) state;
            IBlockState leafState;
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof ITileLeafBlock) {
                leafState = ((ITileLeafBlock) te).getLeafBlock();
            } else {
                leafState = Blocks.LEAVES.getDefaultState();
            }

            return retval.withProperty(LEAFBLOCK, leafState);
        }
        return state;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        IProperty<?>[] listedProperties = {CHECK_DECAY, DECAYABLE, NEARBYLEAVES};
        IUnlistedProperty<?>[] unlistedProperties = {LEAFBLOCK};
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CHECK_DECAY, (meta & 0b100) != 0)
                .withProperty(DECAYABLE, (meta & 0b010) != 0)
                .withProperty(NEARBYLEAVES, (meta & 0b001) != 0);
    }

    /**
     * 3 bits for data:
     * 0               0                0
     * checkDecay      decayable       nearbyLeaves
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int checkDecay = state.getValue(CHECK_DECAY) ? 1 : 0;
        int decayable = state.getValue(DECAYABLE) ? 1 : 0;
        int nearbyLeaves = state.getValue(NEARBYLEAVES) ? 1 : 0;

        return checkDecay << 2 | decayable << 1 | nearbyLeaves;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        return new ArrayList<>();
    }

    @Override
    @Nonnull
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        ret.add(new ItemStack(this));
        return ret;
    }

    @Override
    @Nonnull
    public EnumType getWoodType(int meta) {
        return EnumType.OAK;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileInfestingLeaves();
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote && !player.isCreative()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile != null) {
                if (tile instanceof ITileLeafBlock) {
                    ITileLeafBlock leaves = (ITileLeafBlock) tile;

                    if (ItemUtil.isCrook(player.getHeldItemMainhand())) {
                        if (world.rand.nextFloat() < leaves.getProgress() * ModConfig.crooking.stringChance / 100.0) {
                            Util.dropItemInWorld(tile, player,
                                    new ItemStack(Items.STRING, world.rand.nextInt(ModConfig.crooking.maxStringDrop) + 1, 0), 0.02f);
                        }
                    } else if (world.rand.nextFloat() * 100.0 < leaves.getProgress() * ModConfig.crooking.stringChance / 4.0d / 100.0) {
                        Util.dropItemInWorld(tile, player, new ItemStack(Items.STRING, 1, 0), 0.02f);

                    }
                }

                world.removeTileEntity(pos);
            }
        }
    }

    enum InfestedType {
        INFESTING,
        INFESTED
    }
}