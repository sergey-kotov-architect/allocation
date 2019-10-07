package com.sergeykotov.allocation.dao;

import com.sergeykotov.allocation.domain.Vertex;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VertexDao {
    private static final String CREATE_CMD = "insert into vertex (name, note, rank) values (?, ?, ?);";
    private static final String GET_CMD = "select v.id, v.name, v.note, v.rank from vertex v;";
    private static final String UPDATE_CMD = "update vertex set name = ?, note = ?, rank = ? where id = ?";
    private static final String DELETE_CMD = "delete from vertex where id = ?";

    public boolean create(Vertex vertex) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            preparedStatement.setString(1, vertex.getName());
            preparedStatement.setString(2, vertex.getNote());
            preparedStatement.setDouble(3, vertex.getRank());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public List<Vertex> getAll() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Vertex> vertices = new ArrayList<>();
            while (resultSet.next()) {
                Vertex vertex = new Vertex();
                vertex.setId(resultSet.getLong("id"));
                vertex.setName(resultSet.getString("name"));
                vertex.setNote(resultSet.getString("note"));
                vertex.setRank(resultSet.getDouble("rank"));
                //
                vertices.add(vertex);
            }
            return vertices;
        }
    }

    public boolean update(Vertex vertex) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            preparedStatement.setString(1, vertex.getName());
            preparedStatement.setString(2, vertex.getNote());
            preparedStatement.setDouble(3, vertex.getRank());
            preparedStatement.setLong(4, vertex.getId());
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