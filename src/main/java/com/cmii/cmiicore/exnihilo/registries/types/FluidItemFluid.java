package com.cmii.cmiicore.exnihilo.registries.types;

import com.cmii.cmiicore.exnihilo.util.StackInfo;

import java.util.Objects;

public class FluidItemFluid {
    private final String inputFluid;
    private final StackInfo reactant;
    private final String output;

    public FluidItemFluid(String inputFluid, StackInfo reactant, String output) {
        this.inputFluid = inputFluid;
        this.reactant = reactant;
        this.output = output;
    }

    public FluidItemFluid() {
        this(null, null, null);
    }

    public String getInputFluid() {
        return inputFluid;
    }

    public StackInfo getReactant() {
        return reactant;
    }

    public String getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FluidItemFluid that = (FluidItemFluid) o;
        return Objects.equals(inputFluid, that.inputFluid) &&
                Objects.equals(reactant, that.reactant) &&
                Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputFluid, reactant, output);
    }

    @Override
    public String toString() {
        return "FluidItemFluid{" +
                "inputFluid='" + inputFluid + '\'' +
                ", reactant=" + reactant +
                ", output='" + output + '\'' +
                '}';
    }
}