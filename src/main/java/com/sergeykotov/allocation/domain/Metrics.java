package com.sergeykotov.allocation.domain;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private final Map<Actor, ActorMetrics> actorMetricsMap = new HashMap<>();

    public Metrics() {
    }

    public Map<Actor, ActorMetrics> getActorMetricsMap() {
        return actorMetricsMap;
    }
}