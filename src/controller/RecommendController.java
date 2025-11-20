package controller;

import entity.user;
import java.sql.*;
import java.util.*;

public class RecommendController {

    public void recommendGames(Connection conn, user currentUser) {
        int uid = currentUser.getUid();
        try {
            List<Integer> userGameIds = getUserGames(conn, uid);
            if (userGameIds.isEmpty()) {
                System.out.println("No played or collected games found. Unable to provide recommendations.");
                return;
            }
            Set<Integer> preferredGenres = getPreferredGenres(conn, userGameIds);
            Set<Integer> preferredDevelopers = getPreferredDevelopers(conn, userGameIds);
            List<String> contentBased = getContentRecommendations(
                    conn, userGameIds, preferredGenres, preferredDevelopers);
            List<String> collaborative = getCollaborativeRecommendations(
                    conn, userGameIds, preferredGenres, preferredDevelopers);
            LinkedHashSet<String> finalRecommendations = new LinkedHashSet<>();
            finalRecommendations.addAll(contentBased);
            finalRecommendations.addAll(collaborative);
            System.out.println("\nRecommended Games For You");
            if (finalRecommendations.isEmpty()) {
                System.out.println("No recommendations available at this time.");
            } else {
                finalRecommendations.forEach(System.out::println);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getUserGames(Connection conn, int uid) throws SQLException {
        List<Integer> games = new ArrayList<>();
        String sql = """
            SELECT DISTINCT gaid
            FROM plays
            WHERE uid = ?
            UNION
            SELECT ch.gaid
            FROM collection_has ch
            JOIN collection c ON c.cid = ch.cid
            WHERE c.uid = ?
        """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setInt(2, uid);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                games.add(rs.getInt("gaid"));
        }
        return games;
    }

    private Set<Integer> getPreferredGenres(Connection conn, List<Integer> userGames) throws SQLException {
        Set<Integer> genres = new HashSet<>();
        String sql = "SELECT DISTINCT gid FROM game_genre WHERE gaid = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int gaid : userGames) {
                ps.setInt(1, gaid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    genres.add(rs.getInt("gid"));
                }
            }
        }
        return genres;
    }

    private Set<Integer> getPreferredDevelopers(Connection conn, List<Integer> userGames) throws SQLException {
        Set<Integer> devs = new HashSet<>();
        String sql = "SELECT DISTINCT devid FROM develops WHERE gaid = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int gaid : userGames) {
                ps.setInt(1, gaid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    devs.add(rs.getInt("devid"));
                }
            }
        }
        return devs;
    }

    private List<String> getContentRecommendations(
            Connection conn,
            List<Integer> userGames,
            Set<Integer> genres,
            Set<Integer> developers) throws SQLException {
        List<String> recs = new ArrayList<>();
        if (genres.isEmpty() && developers.isEmpty())
            return recs;
        String sql = """
            SELECT DISTINCT g.name, g.gaid, AVG(r.ratings) AS avg_rating
            FROM game g
            LEFT JOIN game_genre gg ON g.gaid = gg.gaid
            LEFT JOIN develops d ON g.gaid = d.gaid
            LEFT JOIN rates r ON g.gaid = r.gaid
            WHERE (gg.gid IN (%s) OR d.devid IN (%s))
              AND g.gaid NOT IN (%s)
            GROUP BY g.name, g.gaid
            ORDER BY avg_rating DESC NULLS LAST
            LIMIT 10
        """;
        String genreList = genres.isEmpty() ? "NULL" : joinInts(genres);
        String developerList = developers.isEmpty() ? "NULL" : joinInts(developers);
        String userGameList = joinInts(userGames);
        sql = String.format(sql, genreList, developerList, userGameList);
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString("name");
                double rating = rs.getDouble("avg_rating");
                recs.add(name + " (rating: " + String.format("%.2f", rating) + ")");
            }
        }
        return recs;
    }

    private List<String> getCollaborativeRecommendations(
            Connection conn,
            List<Integer> userGames,
            Set<Integer> genres,
            Set<Integer> developers) throws SQLException {
        List<String> recs = new ArrayList<>();
        String sql = """
            SELECT DISTINCT g.name, g.gaid
            FROM plays p
            JOIN game g ON p.gaid = g.gaid
            LEFT JOIN game_genre gg ON g.gaid = gg.gaid
            LEFT JOIN develops d ON g.gaid = d.gaid
            WHERE p.uid != ?
              AND (gg.gid IN (%s) OR d.devid IN (%s))
              AND g.gaid NOT IN (%s)
            LIMIT 10
        """;
        String genreList = genres.isEmpty() ? "NULL" : joinInts(genres);
        String developerList = developers.isEmpty() ? "NULL" : joinInts(developers);
        String userGameList = joinInts(userGames);
        sql = String.format(sql, genreList, developerList, userGameList);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userGames.get(0)); // prevents null – not used in SQL anyway
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                recs.add(rs.getString("name"));
            }
        }
        return recs;
    }

    private String joinInts(Collection<Integer> ints) {
        StringBuilder sb = new StringBuilder();
        for (int i : ints) sb.append(i).append(",");
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
