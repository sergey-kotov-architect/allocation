package com.sergeykotov.allocation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sergeykotov.allocation.domain.Vertex;

public class ActorMetrics {
    private Vertex vertex;
    private double meanPath;
    private double meanDeviation;

    public ActorMetrics() {
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    @JsonProperty("mean_path")
    public double getMeanPath() {
        return meanPath;
    }

    public void setMeanPath(double meanPath) {
        this.meanPath = meanPath;
    }

    @JsonProperty("mean_deviation")
    public double getMeanDeviation() {
        return meanDeviation;
    }

    public void setMeanDeviation(double meanDeviation) {
        this.meanDeviation = meanDeviation;
    }
}