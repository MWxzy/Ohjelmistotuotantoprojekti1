package database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TempRecordDAOTest {

    @BeforeEach
    void setUp() {
        System.setProperty("h2.url",
                "jdbc:h2:mem:records" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
        DatabaseManager.initializeDatabase();
    }

    @AfterEach
    void clearDatabaseOverride() {
        System.clearProperty("h2.url");
    }

    @Test
    void saveRecordStoresAllValues() throws SQLException {
        TempRecordDAO dao = new TempRecordDAO();
        dao.saveRecord(100.0, 1, 2, 212.0);

        String sql = "SELECT input_value, from_unit_id, to_unit_id, result, recorded_at "
                + "FROM temperature_record ORDER BY id";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            assertTrue(resultSet.next());
            assertEquals(100.0, resultSet.getDouble("input_value"), 0.001);
            assertEquals(1, resultSet.getInt("from_unit_id"));
            assertEquals(2, resultSet.getInt("to_unit_id"));
            assertEquals(212.0, resultSet.getDouble("result"), 0.001);
            assertTrue(resultSet.getTimestamp("recorded_at") != null);
            assertFalse(resultSet.next());
        }
    }

    @Test
    void saveRecordStoresMultipleConversions() throws SQLException {
        TempRecordDAO dao = new TempRecordDAO();
        dao.saveRecord(0.0, 1, 2, 32.0);
        dao.saveRecord(32.0, 2, 1, 0.0);

        String sql = "SELECT COUNT(*) AS total FROM temperature_record";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            assertTrue(resultSet.next());
            assertEquals(2, resultSet.getInt("total"));
        }
    }
}
