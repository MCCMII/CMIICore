package com.cmii.cmiicore.exnihilo.api.registries;

public interface IRegistry<V> {
    void clearRegistry();

    V getRegistry();
}