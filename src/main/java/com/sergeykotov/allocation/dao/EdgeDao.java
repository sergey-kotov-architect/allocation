package com.sergeykotov.allocation.dao;

import com.sergeykotov.allocation.domain.Edge;
import com.sergeykotov.allocation.domain.Vertex;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EdgeDao {
    private static final String CREATE_CMD =
            "insert into edge (name, note, speed_limit, distance, source_id, destination_id) values (?, ?, ?, ?, ?, ?);";
    private static final String GET_CMD = "select e.id, e.name, e.note, e.speed_limit, e.distance, e.source_id, " +
            "e.destination_id from edge e;";
    private static final String UPDATE_CMD = "update edge set name = ?, note = ?, speed_limit = ?, distance = ?, " +
            "source_id = ?, destination_id = ? where id = ?";
    private static final String DELETE_CMD = "delete from edge where id = ?";

    public boolean create(Edge edge) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            preparedStatement.setString(1, edge.getName());
            preparedStatement.setString(2, edge.getNote());
            preparedStatement.setDouble(3, edge.getSpeedLimit());
            preparedStatement.setDouble(4, edge.getDistance());
            preparedStatement.setLong(5, edge.getSource().getId());
            preparedStatement.setLong(6, edge.getDestination().getId());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public List<Edge> getAll() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Edge> edges = new ArrayList<>();
            while (resultSet.next()) {
                Vertex source = new Vertex();
                source.setId(resultSet.getLong("source_id"));
                //

                Vertex destination = new Vertex();
                destination.setId(resultSet.getLong("destination_id"));
                //

                Edge edge = new Edge();
                edge.setId(resultSet.getLong("id"));
                edge.setName(resultSet.getString("name"));
                edge.setNote(resultSet.getString("note"));
                edge.setSpeedLimit(resultSet.getDouble("speed_limit"));
                edge.setDistance(resultSet.getDouble("distance"));
                edge.setSource(source);
                edge.setDestination(destination);
                edges.add(edge);
            }
            return edges;
        }
    }

    public boolean update(Edge edge) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            preparedStatement.setString(1, edge.getName());
            preparedStatement.setString(2, edge.getNote());
            preparedStatement.setDouble(3, edge.getSpeedLimit());
            preparedStatement.setDouble(4, edge.getDistance());
            preparedStatement.setDouble(5, edge.getSource().getId());
            preparedStatement.setDouble(6, edge.getDestination().getId());
            preparedStatement.setLong(7, edge.getId());
            return preparedStatement.executeUpdate() == 1;
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