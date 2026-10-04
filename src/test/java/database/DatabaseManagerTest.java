package database;

import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @Test
    void testGetConnection() throws SQLException {
        DatabaseManager.initializeDatabase();
        assertNotNull(DatabaseManager.getConnection());
    }
}