package database;

import java.sql.*;

public class TempRecordDAO {
    public void saveRecord(double value, int fromUnitId, int toUnitId, double result) {
        String sql = "INSERT INTO temperature_record (input_value, from_unit_id, to_unit_id, result) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, value);
            pstmt.setInt(2, fromUnitId);
            pstmt.setInt(3, toUnitId);
            pstmt.setDouble(4, result);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}