package com.cmii.cmiicore.exnihilo.blocks;

import com.cmii.cmiicore.exnihilo.ModFluids;
import com.cmii.cmiicore.exnihilo.config.ModConfig;
import com.cmii.cmiicore.exnihilo.registries.manager.ExNihiloRegistryManager;
import com.cmii.cmiicore.exnihilo.util.Data;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;

public class BlockFluidWitchwater extends BlockFluidClassic {

    private static final ResourceLocation PRIEST = new ResourceLocation("minecraft:priest");
    private static final ResourceLocation BUTCHER = new ResourceLocation("minecraft:butcher");
    private static final ResourceLocation LIBRARIAN = new ResourceLocation("minecraft:librarian");

    public BlockFluidWitchwater() {
        super(ModFluids.fluidWitchwater, Material.WATER);
        this.setRegistryName("witchwater");
        this.setTranslationKey("witchwater");

        Data.BLOCKS.add(this);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (world.isRemote || entity.isDead)
            return;

        if (entity instanceof EntitySkeleton) {
            replaceMob(world, (EntityLivingBase) entity, new EntityWitherSkeleton(world));
        } else if (entity instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) entity;
            if (!creeper.getPowered()) {
                creeper.onStruckByLightning(new EntityLightningBolt(world, creeper.posX, creeper.posY, creeper.posZ, true));
                creeper.setHealth(creeper.getMaxHealth());
            }
        } else if (entity instanceof EntitySlime && !(entity instanceof EntityMagmaCube)) {
            EntitySlime slime = (EntitySlime) entity;
            EntityMagmaCube newEntity = new EntityMagmaCube(world);
            newEntity.setSlimeSize(slime.getSlimeSize(), true);
            replaceMob(world, slime, newEntity);
        } else if (entity instanceof EntitySpider && !(entity instanceof EntityCaveSpider)) {
            replaceMob(world, (EntityLivingBase) entity, new EntityCaveSpider(world));
        } else if (entity instanceof EntitySquid) {
            replaceMob(world, (EntityLivingBase) entity, new EntityGhast(world));
        } else if (entity instanceof EntityVillager) {
            EntityVillager villager = (EntityVillager) entity;
            ResourceLocation prof = villager.getProfessionForge().getRegistryName();

            EntityLivingBase spawnEntity;
            if (PRIEST.equals(prof)) {
                spawnEntity = new EntityWitch(world);
            } else if (BUTCHER.equals(prof)) {
                spawnEntity = new EntityVindicator(world);
            } else if (LIBRARIAN.equals(prof)) {
                spawnEntity = new EntityEvoker(world);
            } else {
                EntityZombieVillager zv = new EntityZombieVillager(world);
                zv.setForgeProfession(villager.getProfessionForge());
                spawnEntity = zv;
            }

            replaceMob(world, villager, spawnEntity);
        } else if (entity instanceof EntityCow && !(entity instanceof EntityMooshroom)) {
            replaceMob(world, (EntityLivingBase) entity, new EntityMooshroom(world));
        } else if (entity instanceof EntityAnimal) {
            entity.onStruckByLightning(new EntityLightningBolt(world, entity.posX, entity.posY, entity.posZ, true));
        } else if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            applyPotion(player, new PotionEffect(MobEffects.BLINDNESS, 210, 0));
            applyPotion(player, new PotionEffect(MobEffects.WEAKNESS, 210, 2));
            applyPotion(player, new PotionEffect(MobEffects.WITHER, 210, 0));
            applyPotion(player, new PotionEffect(MobEffects.SLOWNESS, 210, 0));
        }
    }

    /**
     * Renew's a potion effect if the time would be increased by more than 20 ticks
     */
    private void applyPotion(EntityPlayer player, PotionEffect potionEffect) {
        PotionEffect active = player.getActivePotionEffect(potionEffect.getPotion());
        int currentDuration = active != null ? active.getDuration() : 0;
        if (currentDuration <= potionEffect.getDuration() - 20)
            player.addPotionEffect(potionEffect);
    }

    private void replaceMob(World world, EntityLivingBase toKill, EntityLivingBase toSpawn) {
        toSpawn.setLocationAndAngles(toKill.posX, toKill.posY, toKill.posZ, toKill.rotationYaw, toKill.rotationPitch);
        toSpawn.renderYawOffset = toKill.renderYawOffset;
        toSpawn.setHealth(toSpawn.getMaxHealth() * toKill.getHealth() / toKill.getMaxHealth());

        toKill.setDead();
        world.spawnEntity(toSpawn);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighbourPos) {
        super.neighborChanged(state, world, pos, neighborBlock, neighbourPos);

        interactWithAdjacent(world, pos);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);

        interactWithAdjacent(world, pos);
    }

    private void interactWithAdjacent(World world, BlockPos pos) {
        if (!ModConfig.witchwater.enableWitchWaterBlockForming)
            return;
        Fluid otherFluid = null;

        for (EnumFacing side : EnumFacing.VALUES) {
            if (side == EnumFacing.DOWN)
                continue;
            IBlockState offset = world.getBlockState(pos.offset(side));
            if (offset.getMaterial().isLiquid() && !(offset.getBlock() instanceof BlockFluidWitchwater) &&
                    (offset.getBlock() instanceof BlockFluidBase || offset.getBlock() instanceof BlockLiquid)) {
                otherFluid = FluidRegistry.lookupFluidForBlock(offset.getBlock());
                break;
            }
        }
        if (otherFluid == null)
            return;
        if (ExNihiloRegistryManager.WITCH_WATER_WORLD_REGISTRY.contains(otherFluid)) {
            boolean isCold = otherFluid.getTemperature() <= 300;
            IBlockState newState = ExNihiloRegistryManager.WITCH_WATER_WORLD_REGISTRY.getResult(otherFluid, world.rand.nextFloat()).getBlockState();

            world.setBlockState(pos, newState);

            net.minecraft.util.SoundEvent sound = isCold ? SoundEvents.BLOCK_GRAVEL_BREAK : SoundEvents.BLOCK_STONE_BREAK;
            world.playSound(null, pos, sound, SoundCategory.BLOCKS, 0.5f, 2.6f + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8f);

            for (int i = 0; i < 10; i++) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX() + world.rand.nextDouble(), pos.getY() + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), 0.0, 0.0, 0.0);
            }
        }
    }
}