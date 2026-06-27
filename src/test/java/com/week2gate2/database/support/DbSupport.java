package com.week2gate2.database.support;

import com.week2gate2.database.config.DatabaseConfig;
import com.week2gate2.database.model.OrderRow;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DbSupport {
    private final DatabaseConfig config;

    public DbSupport(DatabaseConfig config) {
        this.config = config;
    }

    public boolean isReachable() throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT 1");
             ResultSet result = statement.executeQuery()) {
            return result.next() && result.getInt(1) == 1;
        }
    }

    public OrderRow findOrder(long orderId) throws SQLException {
        String sql = """
        SELECT id, order_number, status, total, user_id, created_at
        FROM orders
        WHERE id = ?
        """;

        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, orderId);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                return new OrderRow(
                        result.getLong("id"),
                        result.getString("order_number"),
                        result.getString("status"),
                        result.getBigDecimal("total"),
                        result.getString("user_id"),
                        result.getTimestamp("created_at").toInstant()
                );
            }
        }
    }

    public int deleteOrder(long orderId) throws SQLException {
        try (Connection connection = openConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM orders WHERE id = ?")) {
            statement.setLong(1, orderId);
            return statement.executeUpdate();
        }
    }

    private Connection openConnection() throws SQLException {
        System.out.println(config.jdbcUrl() + config.username() + config.password());
        return DriverManager.getConnection(config.jdbcUrl(), config.username(), config.password());
    }
}
