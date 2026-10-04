package database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitDAOTest {

    @BeforeEach
    void setUp() {
        System.setProperty("h2.url", "jdbc:h2:mem:test" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
        DatabaseManager.initializeDatabase();
    }

    @Test
    void testGetAllUnitNames() throws SQLException {
        TemperatureUnitDAO dao = new TemperatureUnitDAO();
        var units = dao.getAllUnitNames();
        assertEquals(3, units.size());
        assertTrue(units.contains("Celsius"));
    }
}