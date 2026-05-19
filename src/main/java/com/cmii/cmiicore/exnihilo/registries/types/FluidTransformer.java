package com.cmii.cmiicore.exnihilo.registries.types;

import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import java.util.Arrays;
import java.util.Objects;

public class FluidTransformer {
    private final String inputFluid;
    private final String outputFluid;
    private final int duration;
    private final BlockInfo[] transformingBlocks;
    private final BlockInfo[] blocksToSpawn;

    public FluidTransformer(String inputFluid, String outputFluid, int duration,
                             BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
        this.inputFluid = inputFluid;
        this.outputFluid = outputFluid;
        this.duration = duration;
        this.transformingBlocks = transformingBlocks;
        this.blocksToSpawn = blocksToSpawn;
    }

    public String getInputFluid() {
        return inputFluid;
    }

    public String getOutputFluid() {
        return outputFluid;
    }

    public int getDuration() {
        return duration;
    }

    public BlockInfo[] getTransformingBlocks() {
        return transformingBlocks;
    }

    public BlockInfo[] getBlocksToSpawn() {
        return blocksToSpawn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidTransformer that = (FluidTransformer) o;
        return duration == that.duration &&
                Objects.equals(inputFluid, that.inputFluid) &&
                Objects.equals(outputFluid, that.outputFluid) &&
                Arrays.equals(transformingBlocks, that.transformingBlocks) &&
                Arrays.equals(blocksToSpawn, that.blocksToSpawn);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(inputFluid, outputFluid, duration);
        result = 31 * result + Arrays.hashCode(transformingBlocks);
        result = 31 * result + Arrays.hashCode(blocksToSpawn);
        return result;
    }

    @Override
    public String toString() {
        return "FluidTransformer{" +
                "inputFluid='" + inputFluid + '\'' +
                ", outputFluid='" + outputFluid + '\'' +
                ", duration=" + duration +
                ", transformingBlocks=" + Arrays.toString(transformingBlocks) +
                ", blocksToSpawn=" + Arrays.toString(blocksToSpawn) +
                '}';
    }
}