package com.sergeykotov.allocation.domain;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

public class Vertex {
    private long id;

    @Size(max = 255)
    @NotEmpty
    private String name;

    @Size(max = 4000)
    private String note;

    @Positive
    private double rank;

    private transient double normalisedRank = 1.0;
    private transient List<Edge> edges;
    private transient Actor actor;
    private transient double path;
    private transient boolean visited;
    private transient Vertex source;

    public Vertex() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public double getNormalisedRank() {
        return normalisedRank;
    }

    public void setNormalisedRank(double normalisedRank) {
        this.normalisedRank = normalisedRank;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public double getPath() {
        return path;
    }

    public void setPath(double path) {
        this.path = path;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Vertex getSource() {
        return source;
    }

    public void setSource(Vertex source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return Objects.equals(getName(), vertex.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return getName();
    }
}