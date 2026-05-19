package com.cmii.cmiicore.exnihilo.registries.types;

import com.cmii.cmiicore.exnihilo.texturing.Color;
import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import java.util.Objects;

public class Compostable {
    public static final Compostable EMPTY = new Compostable(0f, new Color(0), BlockInfo.EMPTY);

    private final float value;
    private final Color color;
    private final BlockInfo compostBlock;

    public Compostable(float value, Color color, BlockInfo compostBlock) {
        this.value = value;
        this.color = color;
        this.compostBlock = compostBlock;
    }

    public float getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public BlockInfo getCompostBlock() {
        return compostBlock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compostable that = (Compostable) o;
        return Float.compare(that.value, value) == 0 &&
                Objects.equals(color, that.color) &&
                Objects.equals(compostBlock, that.compostBlock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, color, compostBlock);
    }

    @Override
    public String toString() {
        return "Compostable{" +
                "value=" + value +
                ", color=" + color +
                ", compostBlock=" + compostBlock +
                '}';
    }
}