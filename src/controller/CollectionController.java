package controller;

import entity.collection;

import java.sql.*;
import java.util.Scanner;

public class CollectionController {

    public collection createCollection(Connection conn, int currentUID) {
        String sql = "INSERT INTO collection (name, uid) VALUES (?, ?)";
        String collectionName;

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter collection name: ");
        collectionName = sc.nextLine();
        collection newCollection = new collection(currentUID, collectionName);


        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            stmt.setString(1, collectionName);
            stmt.setInt(2, currentUID);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(2);
                    newCollection.setCid(generatedId);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return newCollection;
    }

    public void viewAllCollections(Connection conn, int currentUID) {
        //getting the name of the collections a user has
        String sqlCollections = "SELECT * FROM collection WHERE uid = ? ORDER BY name ASC";

        try (PreparedStatement stmt = conn.prepareStatement(sqlCollections)) {
            stmt.setInt(1, currentUID);
            ResultSet resCollections = stmt.executeQuery();
            while (resCollections.next()) {
                int numberOfGames = 0;
                int totalSeconds = 0;

                int collectionId = resCollections.getInt("cid");
                String collectionName = resCollections.getString("name");

                //get the game count in a collection
                String sqlCount = "SELECT COUNT(*) AS game_count FROM collection_has WHERE cid = ?";
                try (PreparedStatement stmt1 = conn.prepareStatement(sqlCount)) {
                    stmt1.setInt(1, collectionId);
                    ResultSet resCount = stmt1.executeQuery();
                    if (resCount.next()) {
                        numberOfGames = resCount.getInt("game_count");
                    }
                }
                // Getting total playtime
                String sqlPlayTime = "SELECT SUM(EXTRACT(EPOCH FROM (end_date_time - start_date_time))) AS total_seconds " +
                        "FROM plays p " +
                        "JOIN collection_has ch ON p.gaid = ch.gaid " +
                        "WHERE ch.cid = ? AND p.uid = ?";
                try (PreparedStatement stmt2 = conn.prepareStatement(sqlPlayTime)) {
                    stmt2.setInt(1, collectionId);
                    stmt2.setInt(2, currentUID);
                    ResultSet resTime = stmt2.executeQuery();
                    if (resTime.next()) {
                        totalSeconds = resTime.getInt("total_seconds");
                    }
                }

                // Convert time
                int hours = totalSeconds / 3600;
                int minutes = (totalSeconds % 3600) / 60;

                //print
                System.out.println("Collection: " + collectionName);
                System.out.println("Number of video games: " + numberOfGames);
                System.out.println("Total Playtime: " + hours + " hours " + minutes + " minutes");
                System.out.println("__________________________________________");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean userHasCollection(Connection conn, int collectionID, int userID) {
        String sql = "SELECT 1 FROM collection WHERE uid = ? AND cid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, userID);
            stmt.setInt(2, collectionID);
            ResultSet res = stmt.executeQuery();
            return res.next();
        } catch (SQLException e ){
            System.out.println(e.getMessage());
            return false;
        }

    }

    public void deleteGameFromCollection(Connection conn, int currentUID) {
        String sqlDelete = "DELETE FROM collection_has WHERE cid = ? AND gaid = ?";

        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("Give the collection ID for the collection you would like to delete game from!");
        int collectionID = Integer.parseInt(sc.nextLine());
        if (userHasCollection(conn, collectionID, currentUID)) {
            System.out.println("Give the game ID for the game you would like to delete!");
            int gameID = Integer.parseInt(sc.nextLine());

            try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
                stmt.setInt(1, collectionID);
                stmt.setInt(2, gameID);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Game removed from collection successfully.");
                } else {
                    System.out.println("No game found in the collection with the given ID.");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("User does not have access to this collection.");
        }

    }


    public boolean doesUserHavePlatform(Connection conn, int gameID, int userID) {
        String sql = "SELECT 1 " +
                "FROM user_platform up " +
                "JOIN game_runs_on gro ON gro.platformid = up.platformid " +
                "WHERE up.userid = ? AND gro.gaid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, userID);
            stmt.setInt(2, gameID);
            ResultSet res = stmt.executeQuery();
            return res.next();
        } catch (SQLException e){
            System.out.println();
        }

        return false;
    }



    public void addGameToCollection(Connection conn, int currentUID) {
        String sqlAdd = "INSERT INTO collection_has (cid, gaid) VALUES (?, ?)";

        Scanner sc = new Scanner(System.in);

        System.out.println();
        System.out.println("Give the collection ID for the collection you would like to add game to!");

        int collectionID = Integer.parseInt(sc.nextLine());

        if (userHasCollection(conn, collectionID, currentUID)) {
            System.out.println("Give the game ID for the game you would like to add!");
            int gameID = Integer.parseInt(sc.nextLine());

            //check platform
            if (!doesUserHavePlatform(conn, gameID, currentUID)) {
                System.out.println("User does not own the game the platform is on!");
                System.out.println("Would you like to continue? (Y/N)");

                if (sc.nextLine().equalsIgnoreCase("Y")) {
                    try (PreparedStatement stmt = conn.prepareStatement(sqlAdd)) {
                        stmt.setInt(1, collectionID);
                        stmt.setInt(2, gameID);

                        int rowsAffected = stmt.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Game add to collection successfully.");
                        } else {
                            System.out.println("Error adding game to collection.");
                        }

                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    try {
                        conn.close();
                    } catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }

        } else {
            System.out.println("User does not have access to this collection.");
        }
    }

    public void deleteCollection(Connection conn, int currentUID) {
        String sqlDelete = "DELETE FROM collection WHERE uid = ? AND cid = ?";

        Scanner sc = new Scanner(System.in);
        System.out.println("Give the collection ID for the collection you would like to delete!");
        int collectionID = Integer.parseInt(sc.nextLine());

        if (userHasCollection(conn, collectionID, currentUID)) {
            try(PreparedStatement stmt = conn.prepareStatement(sqlDelete)){
                stmt.setInt(1,currentUID);
                stmt.setInt(2, collectionID);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Collection deleted from collection successfully.");
                } else  {
                    System.out.println("Error removing collection!");
                }
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("User does not own the collection.");
        }
    }
}
