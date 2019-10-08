package com.sergeykotov.allocation.domain;

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

    public double getMeanPath() {
        return meanPath;
    }

    public void setMeanPath(double meanPath) {
        this.meanPath = meanPath;
    }

    public double getMeanDeviation() {
        return meanDeviation;
    }

    public void setMeanDeviation(double meanDeviation) {
        this.meanDeviation = meanDeviation;
    }
}