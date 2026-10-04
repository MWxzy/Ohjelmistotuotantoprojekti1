package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:./data/tempconverter;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS temperature_unit (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    symbol VARCHAR(10) NOT NULL
                )
            """);

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS temperature_record (
                id INT AUTO_INCREMENT PRIMARY KEY,
                input_value DECIMAL(10,2) NOT NULL,
                from_unit_id INT NOT NULL,
                to_unit_id INT NOT NULL,
                result DECIMAL(10,2) NOT NULL,
                recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (from_unit_id) REFERENCES temperature_unit(id),
                FOREIGN KEY (to_unit_id) REFERENCES temperature_unit(id)
            )
""");

            stmt.execute("MERGE INTO temperature_unit (id, name, symbol) KEY(id) VALUES (1, 'Celsius', '°C')");
            stmt.execute("MERGE INTO temperature_unit (id, name, symbol) KEY(id) VALUES (2, 'Fahrenheit', '°F')");
            stmt.execute("MERGE INTO temperature_unit (id, name, symbol) KEY(id) VALUES (3, 'Kelvin', 'K')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}