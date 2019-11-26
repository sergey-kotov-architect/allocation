package com.sergeykotov.allocation.dto;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.domain.Vertex;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class Path {
    @NotNull
    private Actor actor;

    @NotNull
    private Vertex source;

    @NotNull
    private Vertex destination;

    private List<Vertex> vertices = new ArrayList<>();

    private double value;

    public Path() {
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    public Vertex getDestination() {
        return destination;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}