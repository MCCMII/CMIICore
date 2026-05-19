package com.cmii.cmiicore.exnihilo.registries.types;

import net.minecraft.item.ItemStack;

import java.util.Objects;

public class CrookReward {
    private final ItemStack stack;
    private final float chance;
    private final float fortuneChance;

    public CrookReward(ItemStack stack, float chance, float fortuneChance) {
        this.stack = stack;
        this.chance = chance;
        this.fortuneChance = fortuneChance;
    }

    public CrookReward() {
        this(ItemStack.EMPTY, 0.0f, 0.0f);
    }

    public ItemStack getStack() {
        return stack;
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
        CrookReward that = (CrookReward) o;
        return Float.compare(that.chance, chance) == 0 &&
                Float.compare(that.fortuneChance, fortuneChance) == 0 &&
                Objects.equals(stack, that.stack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack, chance, fortuneChance);
    }

    @Override
    public String toString() {
        return "CrookReward{" +
                "stack=" + stack +
                ", chance=" + chance +
                ", fortuneChance=" + fortuneChance +
                '}';
    }
}