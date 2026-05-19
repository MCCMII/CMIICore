package com.cmii.cmiicore.exnihilo.texturing;

import java.util.Objects;

public class Color {
    public static final Color INVALID_COLOR = new Color(-1, -1, -1, -1);

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color(float red, float green, float blue, float alpha) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.a = alpha;
    }

    public Color(int red, int green, int blue) {
        this(red / 255f, green / 255f, blue / 255f, 1f);
    }

    public Color(int color) {
        this(color, true);
    }

    public Color(int color, boolean ignoreAlpha) {
        this.r = ((color >> 16) & 255) / 255f;
        this.g = ((color >> 8) & 255) / 255f;
        this.b = (color & 255) / 255f;
        this.a = ignoreAlpha ? 1f : ((color >> 24) & 255) / 255f;
    }

    public Color(String hex) {
        this(Integer.parseInt(hex.replace("#", ""), 16), hex.length() < 8);
    }

    public static Color average(Color colorA, Color colorB, float percentage) {
        float averageR = colorA.getRed() + (colorB.getRed() - colorA.getRed()) * percentage;
        float averageG = colorA.getGreen() + (colorB.getGreen() - colorA.getGreen()) * percentage;
        float averageB = colorA.getBlue() + (colorB.getBlue() - colorA.getBlue()) * percentage;
        float averageA = colorA.getAlpha() + (colorB.getAlpha() - colorA.getAlpha()) * percentage;
        return new Color(averageR, averageG, averageB, averageA);
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public float getAlpha() {
        return a;
    }

    public boolean shouldApplyColor() {
        return r != -1 && g != -1 && b != -1;
    }

    public int toInt() {
        int color = 0;
        color |= (int) (this.a * 255) << 24;
        color |= (int) (this.r * 255) << 16;
        color |= (int) (this.g * 255) << 8;
        color |= (int) (this.b * 255);
        return color;
    }

    public int toIntNoAlpha() {
        int color = 0;
        color |= (int) (this.r * 255) << 16;
        color |= (int) (this.g * 255) << 8;
        color |= (int) (this.b * 255);
        return color;
    }

    public String getAsHexNoAlpha() {
        return Integer.toHexString(toIntNoAlpha());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Float.compare(color.r, r) == 0
                && Float.compare(color.g, g) == 0
                && Float.compare(color.b, b) == 0
                && Float.compare(color.a, a) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, a);
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }
}
