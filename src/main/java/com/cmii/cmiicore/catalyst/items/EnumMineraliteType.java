package com.cmii.cmiicore.catalyst.items;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public enum EnumMineraliteType {
    BASE_METAL(0, "base_metal"),
    PRECIOUS_METAL(1, "precious_metal"),
    GEM(2, "gem"),
    FUEL(3, "fuel"),
    RARE_EARTH(4, "rare_earth"),
    ESSENTIA(5, "essentia");

    public final int meta;
    public final String name;

    EnumMineraliteType(int meta, String name) {
        this.meta = meta;
        this.name = name;
    }

    private static final Int2ObjectMap<EnumMineraliteType> BY_META = new Int2ObjectOpenHashMap<>();

    static {
        for (EnumMineraliteType type : values()) {
            BY_META.put(type.meta, type);
        }
    }

    public static EnumMineraliteType getByMeta(int meta) {
        return BY_META.get(meta);
    }
}
