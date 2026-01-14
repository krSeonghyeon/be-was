package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:h2:mem:testdb",
                    "sa",
                    ""
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
