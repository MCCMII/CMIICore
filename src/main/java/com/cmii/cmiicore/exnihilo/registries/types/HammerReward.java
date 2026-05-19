package com.cmii.cmiicore.exnihilo.registries.types;

import net.minecraft.item.ItemStack;

import java.util.Objects;

public class HammerReward {
    private final ItemStack stack;
    private final int miningLevel;
    private final float chance;
    private final float fortuneChance;

    public HammerReward(ItemStack stack, int miningLevel, float chance, float fortuneChance) {
        this.stack = stack;
        this.miningLevel = miningLevel;
        this.chance = chance;
        this.fortuneChance = fortuneChance;
    }

    public HammerReward() {
        this(ItemStack.EMPTY, 0, 0.0f, 0.0f);
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public float getChance() {
        return chance;
    }

    public float getFortuneChance() {
        return fortuneChance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HammerReward that = (HammerReward) o;
        return miningLevel == that.miningLevel &&
                Float.compare(that.chance, chance) == 0 &&
                Float.compare(that.fortuneChance, fortuneChance) == 0 &&
                Objects.equals(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack, miningLevel, chance, fortuneChance);
    }

    @Override
    public String toString() {
        return "HammerReward{" +
                "stack=" + stack +
                ", miningLevel=" + miningLevel +
                ", chance=" + chance +
                ", fortuneChance=" + fortuneChance +
                '}';
    }
}