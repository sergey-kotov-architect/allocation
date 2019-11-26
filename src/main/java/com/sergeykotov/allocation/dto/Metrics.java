package com.sergeykotov.allocation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sergeykotov.allocation.domain.Actor;

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