package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionManager {

    static {
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                    CREATE TABLE USERS (
                        user_id VARCHAR(50) PRIMARY KEY,
                        password VARCHAR(100),
                        name VARCHAR(100),
                        email VARCHAR(100))
                    """);
            stmt.execute("""
                    CREATE TABLE ARTICLES (
                        article_id VARCHAR(50) PRIMARY KEY,
                        content CLOB,
                        image_url VARCHAR(255))
                    """);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                    "sa",
                    ""
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
