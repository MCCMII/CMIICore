package com.cmii.cmiicore.exnihilo.registries.types;

import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import java.util.Objects;

public class FluidFluidBlock {
    private final String fluidInBarrel;
    private final String fluidOnTop;
    private final BlockInfo result;

    public FluidFluidBlock(String fluidInBarrel, String fluidOnTop, BlockInfo result) {
        this.fluidInBarrel = fluidInBarrel;
        this.fluidOnTop = fluidOnTop;
        this.result = result;
    }

    public FluidFluidBlock() {
        this(null, null, null);
    }

    public String getFluidInBarrel() {
        return fluidInBarrel;
    }

    public String getFluidOnTop() {
        return fluidOnTop;
    }

    public BlockInfo getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidFluidBlock that = (FluidFluidBlock) o;
        return Objects.equals(fluidInBarrel, that.fluidInBarrel) &&
                Objects.equals(fluidOnTop, that.fluidOnTop) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluidInBarrel, fluidOnTop, result);
    }

    @Override
    public String toString() {
        return "FluidFluidBlock{" +
                "fluidInBarrel='" + fluidInBarrel + '\'' +
                ", fluidOnTop='" + fluidOnTop + '\'' +
                ", result=" + result +
                '}';
    }
}