package com.cmii.cmiicore.exnihilo.registries.types;

import com.cmii.cmiicore.exnihilo.util.BlockInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WitchWaterWorld {
    private final List<BlockInfo> results;
    private final List<Integer> weights;
    private int totalWeight;

    public WitchWaterWorld(List<BlockInfo> results, List<Integer> weights) {
        this.results = results;
        this.weights = weights;
        this.totalWeight = 0;
        for (int w : weights) {
            this.totalWeight += w;
        }
    }

    public WitchWaterWorld(List<BlockInfo> results) {
        this.results = results;
        this.weights = new ArrayList<>(results.size());
        for (int i = 0; i < results.size(); i++) {
            this.weights.add(1);
        }
        this.totalWeight = results.size();
    }

    public List<BlockInfo> getResults() {
        return results;
    }

    public List<Integer> getWeights() {
        return weights;
    }

    public Map<BlockInfo, Integer> toMap() {
        Map<BlockInfo, Integer> mapping = new HashMap<>();
        for (int idx = 0; idx < results.size(); idx++) {
            mapping.put(results.get(idx), weights.get(idx));
        }
        return mapping;
    }

    public void add(BlockInfo result, int weight) {
        results.add(result);
        weights.add(weight);
        totalWeight += weight;
    }

    public BlockInfo getResult(int chance) {
        if (chance > totalWeight || chance < 1)
            return results.get(results.size() - 1);
        int remainder = chance;
        int idx = -1;
        while (remainder > 0) {
            idx++;
            remainder -= weights.get(idx);
        }
        return results.get(idx);
    }

    public BlockInfo getResult(float chance) {
        return getResult((int) (chance * this.totalWeight) + 1);
    }

    public static WitchWaterWorld fromMap(Map<BlockInfo, Integer> mapping) {
        List<BlockInfo> keys = new ArrayList<>(mapping.keySet());
        List<Integer> vals = new ArrayList<>(keys.size());
        for (BlockInfo key : keys) {
            Integer val = mapping.get(key);
            vals.add(val != null ? val : 0);
        }
        return new WitchWaterWorld(keys, vals);
    }
}