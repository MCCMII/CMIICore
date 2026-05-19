package com.cmii.cmiicore.exnihilo.registries.types;

import com.cmii.cmiicore.exnihilo.util.BlockInfo;
import com.cmii.cmiicore.exnihilo.util.EntityInfo;
import net.minecraft.item.crafting.Ingredient;

import java.util.Objects;

public class FluidBlockTransformer {
    private final String fluidName;
    private final Ingredient input;
    private final BlockInfo output;
    private final EntityInfo toSpawn;
    private final int spawnCount;
    private final int spawnRange;

    public FluidBlockTransformer(String fluidName, Ingredient input, BlockInfo output,
                                  EntityInfo toSpawn, int spawnCount, int spawnRange) {
        this.fluidName = fluidName;
        this.input = input;
        this.output = output;
        this.toSpawn = toSpawn;
        this.spawnCount = spawnCount;
        this.spawnRange = spawnRange;
    }

    public FluidBlockTransformer(String fluidName, Ingredient input, BlockInfo output,
                                  String entityName, int spawnCount, int spawnRange) {
        this(fluidName, input, output,
                entityName == null ? EntityInfo.EMPTY : new EntityInfo(entityName),
                spawnCount, spawnRange);
    }

    public FluidBlockTransformer(String fluidName, Ingredient input, BlockInfo output) {
        this(fluidName, input, output, EntityInfo.EMPTY, 4, 4);
    }

    public String getFluidName() {
        return fluidName;
    }

    public Ingredient getInput() {
        return input;
    }

    public BlockInfo getOutput() {
        return output;
    }

    public EntityInfo getToSpawn() {
        return toSpawn;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public int getSpawnRange() {
        return spawnRange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidBlockTransformer that = (FluidBlockTransformer) o;
        return spawnCount == that.spawnCount &&
                spawnRange == that.spawnRange &&
                Objects.equals(fluidName, that.fluidName) &&
                Objects.equals(input, that.input) &&
                Objects.equals(output, that.output) &&
                Objects.equals(toSpawn, that.toSpawn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluidName, input, output, toSpawn, spawnCount, spawnRange);
    }

    @Override
    public String toString() {
        return "FluidBlockTransformer{" +
                "fluidName='" + fluidName + '\'' +
                ", input=" + input +
                ", output=" + output +
                ", toSpawn=" + toSpawn +
                ", spawnCount=" + spawnCount +
                ", spawnRange=" + spawnRange +
                '}';
    }
}