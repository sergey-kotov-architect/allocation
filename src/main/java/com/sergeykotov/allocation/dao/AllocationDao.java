package com.sergeykotov.allocation.dao;

import com.sergeykotov.allocation.domain.Actor;
import com.sergeykotov.allocation.domain.Allocation;
import com.sergeykotov.allocation.domain.Vertex;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class AllocationDao {
    private static final String CREATE_CMD = "insert into allocation (actor_id, vertex_id, actor_rank, active, note) " +
            "values (?, ?, ?, ?, ?);";
    private static final String GET_CMD = "select a.id, a.actor_id, a.vertex_id, a.actor_rank, a.active, a.note " +
            "from allocation a;";
    private static final String UPDATE_CMD = "update allocation set actor_id = ?, vertex_id = ?, actor_rank = ?, " +
            "active = ?, note = ? where id = ?";
    private static final String DELETE_CMD = "delete from allocation where id = ?";

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
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Allocation> allocations = new ArrayList<>();
            while (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getLong("actor_id"));
                //

                Vertex vertex = new Vertex();
                vertex.setId(resultSet.getLong("vertex_id"));
                //

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