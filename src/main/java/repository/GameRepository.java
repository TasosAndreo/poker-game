package repository;

import database.DatabaseConnection;

import java.sql.*;

public class GameRepository {

    public int createGame() {
        String sql = "INSERT INTO games DEFAULT VALUES";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void saveResult(int gameId, int playerId, String result, int chipsChange) {
        String sql = """
            INSERT INTO game_results(game_id, player_id, result, chips_change)
            VALUES(?,?,?,?)
        """;

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);
            stmt.setInt(2, playerId);
            stmt.setString(3, result);
            stmt.setInt(4, chipsChange);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}