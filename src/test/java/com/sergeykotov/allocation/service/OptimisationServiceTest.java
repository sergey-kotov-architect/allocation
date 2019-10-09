package com.sergeykotov.allocation.service;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Edge;
import com.sergeykotov.allocation.domain.Vertex;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class OptimisationServiceTest {
    private OptimisationService optimisationService;
    private Allocation allocation11;
    private Allocation allocation12;
    private Allocation allocation13;
    private Allocation allocation21;
    private Allocation allocation22;
    private Allocation allocation23;
    private List<Allocation> allocations;

    @Before
    public void setUp() {
        optimisationService = new OptimisationService();

        Actor actor1 = new Actor();
        actor1.setId(1L);
        actor1.setName("1");
        actor1.setSpeed(1.0);

        Actor actor2 = new Actor();
        actor2.setId(2L);
        actor2.setName("2");
        actor2.setSpeed(2.0);

        Vertex vertex1 = new Vertex();
        vertex1.setId(1L);
        vertex1.setName("1");
        vertex1.setRank(1.0);

        Vertex vertex2 = new Vertex();
        vertex2.setId(2L);
        vertex2.setName("2");
        vertex2.setRank(2.0);

        Vertex vertex3 = new Vertex();
        vertex3.setId(3L);
        vertex3.setName("3");
        vertex3.setRank(3.0);

        Edge edge12 = new Edge();
        edge12.setId(1L);
        edge12.setName("12");
        edge12.setSpeedLimit(1.0);
        edge12.setDistance(1.0);
        edge12.setSource(vertex1);
        edge12.setDestination(vertex2);

        Edge edge21 = new Edge();
        edge21.setId(2L);
        edge21.setName("21");
        edge21.setSpeedLimit(1.0);
        edge21.setDistance(1.0);
        edge21.setSource(vertex2);
        edge21.setDestination(vertex1);

        Edge edge23 = new Edge();
        edge23.setId(3L);
        edge23.setName("23");
        edge23.setSpeedLimit(1.0);
        edge23.setDistance(2.0);
        edge23.setSource(vertex2);
        edge23.setDestination(vertex3);

        Edge edge32 = new Edge();
        edge32.setId(1L);
        edge32.setName("32");
        edge32.setSpeedLimit(1.0);
        edge32.setDistance(2.0);
        edge32.setSource(vertex3);
        edge32.setDestination(vertex2);

        vertex1.setEdges(new ArrayList<>());
        vertex1.getEdges().add(edge12);

        vertex2.setEdges(new ArrayList<>());
        vertex2.getEdges().add(edge21);
        vertex2.getEdges().add(edge23);

        vertex3.setEdges(new ArrayList<>());
        vertex3.getEdges().add(edge32);

        allocation11 = new Allocation();
        allocation11.setId(1L);
        allocation11.setActor(actor1);
        allocation11.setVertex(vertex1);
        allocation11.setActorRank(1.0);

        allocation12 = new Allocation();
        allocation12.setId(2L);
        allocation12.setActor(actor1);
        allocation12.setVertex(vertex2);
        allocation12.setActorRank(1.0);

        allocation13 = new Allocation();
        allocation13.setId(3L);
        allocation13.setActor(actor1);
        allocation13.setVertex(vertex3);
        allocation13.setActorRank(1.0);

        allocation21 = new Allocation();
        allocation21.setId(4L);
        allocation21.setActor(actor2);
        allocation21.setVertex(vertex1);
        allocation21.setActorRank(1.0);

        allocation22 = new Allocation();
        allocation22.setId(5L);
        allocation22.setActor(actor2);
        allocation22.setVertex(vertex2);
        allocation22.setActorRank(2.0);

        allocation23 = new Allocation();
        allocation23.setId(6L);
        allocation23.setActor(actor2);
        allocation23.setVertex(vertex3);
        allocation23.setActorRank(1.0);

        allocations = new ArrayList<>();
        allocations.add(allocation11);
        allocations.add(allocation12);
        allocations.add(allocation13);
        allocations.add(allocation21);
        allocations.add(allocation22);
        allocations.add(allocation23);
    }

    @Test
    public void test() {
        List<Allocation> expected = new ArrayList<>();
        expected.add(allocation11);
        expected.add(allocation22);

        optimisationService.evaluateVertexRanks(allocations);
        optimisationService.makeStableMatches(allocations);

        List<Allocation> active = allocations.stream().filter(Allocation::isActive).collect(Collectors.toList());

        assertEquals(expected, active);
    }
}