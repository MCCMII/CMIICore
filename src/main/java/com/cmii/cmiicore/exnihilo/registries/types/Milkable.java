package com.cmii.cmiicore.exnihilo.registries.types;

import java.util.Objects;

public class Milkable {
    private final String entityOnTop;
    private final String result;
    private final int amount;
    private final int coolDown;

    public Milkable(String entityOnTop, String result, int amount, int coolDown) {
        this.entityOnTop = entityOnTop;
        this.result = result;
        this.amount = amount;
        this.coolDown = coolDown;
    }

    public String getEntityOnTop() {
        return entityOnTop;
    }

    public String getResult() {
        return result;
    }

    public int getAmount() {
        return amount;
    }

    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Milkable milkable = (Milkable) o;
        return amount == milkable.amount &&
                coolDown == milkable.coolDown &&
                Objects.equals(entityOnTop, milkable.entityOnTop) &&
                Objects.equals(result, milkable.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityOnTop, result, amount, coolDown);
    }

    @Override
    public String toString() {
        return "Milkable{" +
                "entityOnTop='" + entityOnTop + '\'' +
                ", result='" + result + '\'' +
                ", amount=" + amount +
                ", coolDown=" + coolDown +
                '}';
    }
}