package rvt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {
    private static final String DATA_DIRECTORY = "data";
    private static final String DATABASE_FILE = "products.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + DATA_DIRECTORY + "/" + DATABASE_FILE;

    static {
        try {
            initializeDatabase();
        } catch (SQLException | IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseConnection() {
        // Utility class
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    private static void initializeDatabase() throws SQLException, IOException {
        Path dataDirectory = Paths.get(DATA_DIRECTORY);
        if (Files.notExists(dataDirectory)) {
            Files.createDirectories(dataDirectory);
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute("CREATE TABLE IF NOT EXISTS categories ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL UNIQUE"
                    + ")");
            statement.execute("CREATE TABLE IF NOT EXISTS products ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT NOT NULL, "
                    + "price REAL NOT NULL CHECK(price > 0), "
                    + "category_id INTEGER NOT NULL, "
                    + "FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE RESTRICT"
                    + ")");
        }
    }
}
