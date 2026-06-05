package rvt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAO {
    public int createCategory(String name) throws SQLException {
        String sql = "INSERT INTO categories(name) VALUES(?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name.trim());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Kategorijas izveide neizdevās.");
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Kategorijas ID netika iegūts.");
            }
        }
    }

    public List<Category> findAll() throws SQLException {
        String sql = "SELECT id, name FROM categories ORDER BY id";
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categories.add(new Category(resultSet.getInt("id"), resultSet.getString("name")));
            }
        }
        return categories;
    }

    public Optional<Category> findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Category(resultSet.getInt("id"), resultSet.getString("name")));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Category> findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM categories WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Category(resultSet.getInt("id"), resultSet.getString("name")));
                }
            }
        }
        return Optional.empty();
    }
}
