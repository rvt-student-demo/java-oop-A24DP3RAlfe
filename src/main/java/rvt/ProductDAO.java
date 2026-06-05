package rvt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public int createProduct(String name, double price, int categoryId) throws SQLException {
        String sql = "INSERT INTO products(name, price, category_id) VALUES(?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name.trim());
            statement.setDouble(2, price);
            statement.setInt(3, categoryId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Produkta izveide neizdevās.");
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Produkta ID netika iegūts.");
            }
        }
    }

    public List<Product> findAll() throws SQLException {
        String sql = "SELECT p.id, p.name, p.price, p.category_id, c.name AS category_name "
                + "FROM products p LEFT JOIN categories c ON p.category_id = c.id ORDER BY p.id";
        return fetchProducts(sql, null);
    }

    public List<Product> findByCategoryId(int categoryId) throws SQLException {
        String sql = "SELECT p.id, p.name, p.price, p.category_id, c.name AS category_name "
                + "FROM products p LEFT JOIN categories c ON p.category_id = c.id "
                + "WHERE p.category_id = ? ORDER BY p.id";
        return fetchProducts(sql, statement -> statement.setInt(1, categoryId));
    }

    public List<Product> findByCategoryName(String categoryName) throws SQLException {
        String sql = "SELECT p.id, p.name, p.price, p.category_id, c.name AS category_name "
                + "FROM products p LEFT JOIN categories c ON p.category_id = c.id "
                + "WHERE c.name = ? ORDER BY p.id";
        return fetchProducts(sql, statement -> statement.setString(1, categoryName.trim()));
    }

    private List<Product> fetchProducts(String sql, SqlPreparer preparer) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (preparer != null) {
                preparer.prepare(statement);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(new Product(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("category_name")
                    ));
                }
            }
        }
        return products;
    }

    @FunctionalInterface
    private interface SqlPreparer {
        void prepare(PreparedStatement statement) throws SQLException;
    }
}
