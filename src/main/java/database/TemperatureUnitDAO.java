package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TemperatureUnitDAO {
    public List<String> getAllUnitNames() {
        List<String> units = new ArrayList<>();
        String sql = "SELECT name FROM temperature_unit ORDER BY id";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                units.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }
}