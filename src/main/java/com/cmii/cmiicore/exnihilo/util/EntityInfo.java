package com.cmii.cmiicore.exnihilo.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityInfo {
    private Class<? extends Entity> entityClass;

    private final String name;

    public EntityInfo(String entityName) {
        this.name = entityName;
        this.entityClass = entityName == null ? null : EntityList.getClass(new ResourceLocation(entityName));
    }

    public static final EntityInfo EMPTY = new EntityInfo(null);

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public String getName() {
        return name;
    }

    public boolean spawnEntityNear(BlockPos pos, int range, World worldIn) {
        if (entityClass == null || name == null)
            return false;

        if (!worldIn.isRemote && worldIn.getDifficulty() != EnumDifficulty.PEACEFUL) {
            Entity entity = EntityList.newEntity(entityClass, worldIn);
            if (entity instanceof EntityLiving) {
                EntityLiving entityLiving = (EntityLiving) entity;

                if (entityLiving instanceof EntitySlime) {
                    ((EntitySlime) entityLiving).setSlimeSize(1, true);
                }

                double dx = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * range + 0.5;
                double dy = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * range;
                double dz = (worldIn.rand.nextDouble() - worldIn.rand.nextDouble()) * range + 0.5;
                BlockPos spawnPos = new BlockPos(pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz);

                entityLiving.setPosition(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());

                boolean canSpawn = worldIn.getCollisionBoxes(entityLiving, entityLiving.getEntityBoundingBox()).isEmpty();
                if (canSpawn) {
                    worldIn.spawnEntity(entityLiving);
                    worldIn.playEvent(2004, pos, 0);
                    entityLiving.spawnExplosionParticle();
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        return false;
    }
}