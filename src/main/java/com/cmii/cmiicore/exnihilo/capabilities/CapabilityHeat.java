package com.cmii.cmiicore.exnihilo.capabilities;

public class CapabilityHeat implements ICapabilityHeat {
    private int heatRate;

    public CapabilityHeat() {
    }

    public CapabilityHeat(int heatRate) {
        this.heatRate = heatRate;
    }

    @Override
    public int getHeatRate() {
        return heatRate;
    }

    @Override
    public void setHeatRate(int heatRate) {
        this.heatRate = heatRate;
    }
}
