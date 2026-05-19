package com.cmii.cmiicore.exnihilo.api.registries;

import java.util.List;

public interface IRegistryList<V> extends IRegistry<List<V>> {
    void register(V value);
}