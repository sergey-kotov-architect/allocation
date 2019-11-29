package com.sergeykotov.allocation.dao;

import com.sergeykotov.allocation.domain.Actor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ActorDao {
    private static final String CREATE_CMD = "insert into actor (name, note, speed) values (?, ?, ?);";
    private static final String GET_CMD = "select a.id, a.name, a.note, a.speed from actor a;";
    private static final String UPDATE_CMD = "update actor set name = ?, note = ?, speed = ? where id = ?";
    private static final String DELETE_CMD = "delete from actor where id = ?";

    public boolean create(Actor actor) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_CMD)) {
            preparedStatement.setString(1, actor.getName());
            preparedStatement.setString(2, actor.getNote());
            preparedStatement.setDouble(3, actor.getSpeed());
            return preparedStatement.executeUpdate() == 1;
        }
    }

    public List<Actor> getAll() throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CMD);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<Actor> actors = new ArrayList<>();
            while (resultSet.next()) {
                Actor actor = new Actor();
                actor.setId(resultSet.getLong("id"));
                actor.setName(resultSet.getString("name"));
                actor.setNote(resultSet.getString("note"));
                actor.setSpeed(resultSet.getDouble("speed"));
                actors.add(actor);
            }
            return actors;
        }
    }

    public boolean updateById(long id, Actor actor) throws SQLException {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CMD)) {
            preparedStatement.setString(1, actor.getName());
            preparedStatement.setString(2, actor.getNote());
            preparedStatement.setDouble(3, actor.getSpeed());
            preparedStatement.setLong(4, id);
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