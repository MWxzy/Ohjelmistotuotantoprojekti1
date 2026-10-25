package database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureUnitDAOTest {

    @BeforeEach
    void setUp() {
        System.setProperty("h2.url",
                "jdbc:h2:mem:units" + System.nanoTime() + ";DB_CLOSE_DELAY=-1");
        DatabaseManager.initializeDatabase();
    }

    @AfterEach
    void clearDatabaseOverride() {
        System.clearProperty("h2.url");
    }

    @Test
    void testGetAllUnitNames() {
        TemperatureUnitDAO dao = new TemperatureUnitDAO();
        List<String> units = dao.getAllUnitNames();

        assertEquals(3, units.size());
        assertEquals(List.of("Celsius", "Fahrenheit", "Kelvin"), units);
        assertTrue(units.contains("Celsius"));
    }
}
