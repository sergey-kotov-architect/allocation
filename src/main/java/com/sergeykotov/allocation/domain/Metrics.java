package com.sergeykotov.allocation.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private final Map<Actor, ActorMetrics> actorMetricsMap = new HashMap<>();

    public Metrics() {
    }

    @JsonProperty("actor_metrics_map")
    public Map<Actor, ActorMetrics> getActorMetricsMap() {
        return actorMetricsMap;
    }
}