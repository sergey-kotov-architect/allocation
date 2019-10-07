package com.sergeykotov.allocation.domain;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "allocation")
public class Allocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "actor_id", nullable = false)
    private Actor actor;

    @Column(name = "vertex_id", nullable = false)
    private Vertex vertex;

    @Positive
    @Column(name = "actor_rank", nullable = false)
    private double actorRank;

    @Column(name = "active")
    private boolean active;

    @Column(name = "note", length = 4000)
    private String note;

    private transient double vertexRank;

    private transient boolean proposed;

    public Allocation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public double getActorRank() {
        return actorRank;
    }

    public void setActorRank(double actorRank) {
        this.actorRank = actorRank;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getVertexRank() {
        return vertexRank;
    }

    public void setVertexRank(double vertexRank) {
        this.vertexRank = vertexRank;
    }

    public boolean isProposed() {
        return proposed;
    }

    public void setProposed(boolean proposed) {
        this.proposed = proposed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Allocation that = (Allocation) o;
        return Objects.equals(getActor(), that.getActor()) &&
                Objects.equals(getVertex(), that.getVertex());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getActor(), getVertex());
    }

    @Override
    public String toString() {
        return "(" + getActor() + ", " + getVertex() + ")";
    }
}