package com.cmii.cmiicore.exnihilo.api.registries;

public interface IBarrelLiquidBlacklistRegistry extends IRegistryMappedList<Integer, String> {
    boolean isBlacklisted(int level, String fluid);

    void register(int level, String fluid);

    @Override
    void register(Integer key, java.util.List<String> value);
}