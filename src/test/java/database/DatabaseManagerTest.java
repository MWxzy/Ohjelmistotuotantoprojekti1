package database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DatabaseManagerTest {

    @BeforeEach
    void useInMemoryDatabase() {
        System.setProperty("h2.url",
                "jdbc:h2:mem:dbmanager" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
    }

    @AfterEach
    void clearDatabaseOverride() {
        System.clearProperty("h2.url");
    }

    @Test
    void testGetConnection() throws SQLException {
        DatabaseManager.initializeDatabase();

        try (Connection connection = DatabaseManager.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }
}
