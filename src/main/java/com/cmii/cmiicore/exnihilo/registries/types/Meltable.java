package com.cmii.cmiicore.exnihilo.registries.types;

import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import java.util.Objects;

public class Meltable {
    public static final Meltable EMPTY = new Meltable("", 0);

    private final String fluid;
    private int amount;
    private BlockInfo textureOverride;

    public Meltable(String fluid, int amount, BlockInfo textureOverride) {
        this.fluid = fluid;
        this.amount = amount;
        this.textureOverride = textureOverride;
    }

    public Meltable(String fluid, int amount) {
        this(fluid, amount, BlockInfo.EMPTY);
    }

    public String getFluid() {
        return fluid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BlockInfo getTextureOverride() {
        return textureOverride;
    }

    public void setTextureOverride(BlockInfo textureOverride) {
        this.textureOverride = textureOverride;
    }

    public Meltable setTextureOverrideChain(BlockInfo textureOverride) {
        this.textureOverride = textureOverride;
        return this;
    }

    public Meltable copy() {
        return new Meltable(fluid, amount, textureOverride);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meltable meltable = (Meltable) o;
        return amount == meltable.amount &&
                Objects.equals(fluid, meltable.fluid) &&
                Objects.equals(textureOverride, meltable.textureOverride);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fluid, amount, textureOverride);
    }

    @Override
    public String toString() {
        return "Meltable{" +
                "fluid='" + fluid + '\'' +
                ", amount=" + amount +
                ", textureOverride=" + textureOverride +
                '}';
    }
}