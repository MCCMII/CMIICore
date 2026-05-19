package com.cmii.cmiicore.exnihilo.texturing;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SpriteColor {
    private final TextureAtlasSprite sprite;
    private final Color color;

    public SpriteColor(TextureAtlasSprite sprite, Color color) {
        this.sprite = sprite;
        this.color = color;
    }

    public TextureAtlasSprite getSprite() {
        return sprite;
    }

    public Color getColor() {
        return color;
    }
}