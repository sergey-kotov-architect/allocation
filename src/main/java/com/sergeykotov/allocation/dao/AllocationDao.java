package com.sergeykotov.allocation.dao;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Edge;
import com.sergeykotov.allocation.domain.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AllocationDao {
    private static final String CREATE_CMD = "insert into allocation (actor_id, vertex_id, actor_rank, active, note) " +
            "values (?, ?, ?, ?, ?);";
    private static final String GET_CMD = "select a.id, a.actor_id, a2.name as a_name, a2.note as a_note, a2.speed, " +
            "a.vertex_id, v.name as v_name, v.note as v_note, v.rank, a.actor_rank, a.active, a.note " +
            "from allocation a join actor a2 on a.actor_id = a2.id join vertex v on a.vertex_id = v.id;";
    private static final String UPDATE_CMD = "update allocation set actor_id = ?, vertex_id = ?, actor_rank = ?, " +
            "active = ?, note = ? where id = ?";
    private static final String DELETE_CMD = "delete from allocation where id = ?";

    private final EdgeDao edgeDao;

    @Autowired
    public AllocationDao(EdgeDao edgeDao) {
        this.edgeDao = edgeDao;
    }

    public boolean create(Allocation allocation) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            preparedStatement.setLong(1, allocation.getActor().getId());
            preparedStatement.setLong(2, allocation.getVertex().getId());
            preparedStatement.setDouble(3, allocation.getActorRank());
            preparedStatement.setBoolean(4, allocation.isActive());
            preparedStatement.setString(5, allocation.getNote());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public List<Allocation> getAll() throws SQLException {
        List<Edge> edges = edgeDao.getAll();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Allocation> allocations = new ArrayList<>();
            while (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getLong("actor_id"));
                actor.setName(resultSet.getString("a_name"));
                actor.setNote(resultSet.getString("a_note"));
                actor.setSpeed(resultSet.getDouble("speed"));

                Vertex vertex = new Vertex();
                vertex.setId(resultSet.getLong("vertex_id"));
                vertex.setName(resultSet.getString("v_name"));
                vertex.setNote(resultSet.getString("v_note"));
                vertex.setRank(resultSet.getDouble("rank"));
                vertex.setEdges(edges.stream().filter(e -> e.getSource().equals(vertex)).collect(Collectors.toList()));

                Allocation allocation = new Allocation();
                allocation.setId(resultSet.getLong("id"));
                allocation.setActor(actor);
                allocation.setVertex(vertex);
                allocation.setActorRank(resultSet.getDouble("actor_rank"));
                allocation.setActive(resultSet.getBoolean("active"));
                allocation.setNote(resultSet.getString("note"));
                allocations.add(allocation);
            }
            return allocations;
        }
    }

    public boolean update(Allocation allocation) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            preparedStatement.setLong(1, allocation.getActor().getId());
            preparedStatement.setLong(2, allocation.getVertex().getId());
            preparedStatement.setDouble(3, allocation.getActorRank());
            preparedStatement.setBoolean(4, allocation.isActive());
            preparedStatement.setString(5, allocation.getNote());
            preparedStatement.setLong(6, allocation.getId());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public boolean update(List<Allocation> allocations) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            for (Allocation allocation : allocations) {
                preparedStatement.setLong(1, allocation.getActor().getId());
                preparedStatement.setLong(2, allocation.getVertex().getId());
                preparedStatement.setDouble(3, allocation.getActorRank());
                preparedStatement.setBoolean(4, allocation.isActive());
                preparedStatement.setString(5, allocation.getNote());
                preparedStatement.setLong(6, allocation.getId());
                preparedStatement.addBatch();
            }
            return Arrays.stream(preparedStatement.executeBatch()).sum() == allocations.size();
        }
    }

    public boolean deleteById(long id) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CMD)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() == 1;
        }
    }
}