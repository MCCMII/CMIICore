package com.cmii.cmiicore.exnihilo.texturing;

import java.util.Objects;

public class Color {
    public static final Color INVALID_COLOR = new Color(-1, -1, -1, -1);

    private final float red;
    private final float green;
    private final float blue;
    private final float alpha;

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(int color) {
        this(color, false);
    }

    public Color(int color, boolean ignoreAlpha) {
        this.red = ((color >> 16) & 255) / 255f;
        this.green = ((color >> 8) & 255) / 255f;
        this.blue = (color & 255) / 255f;
        this.alpha = ignoreAlpha ? 1f : ((color >> 24) & 255) / 255f;
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
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha() {
        return alpha;
    }

    public boolean shouldApplyColor() {
        return red != -1 && green != -1 && blue != -1;
    }

    public int toInt() {
        int color = 0;
        color |= (int) (this.alpha * 255) << 24;
        color |= (int) (this.red * 255) << 16;
        color |= (int) (this.green * 255) << 8;
        color |= (int) (this.blue * 255);
        return color;
    }

    public int toIntNoAlpha() {
        int color = 0;
        color |= (int) (this.red * 255) << 16;
        color |= (int) (this.green * 255) << 8;
        color |= (int) (this.blue * 255);
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
        return Float.compare(color.red, red) == 0
                && Float.compare(color.green, green) == 0
                && Float.compare(color.blue, blue) == 0
                && Float.compare(color.alpha, alpha) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return "Color{" +
                "r=" + red +
                ", g=" + green +
                ", b=" + blue +
                ", a=" + alpha +
                '}';
    }
}