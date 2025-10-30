package controller;

import entity.rates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RatesController {
    private Integer getgaid(Connection conn, String gamename) throws SQLException {
        String query = "SELECT gaid FROM game WHERE LOWER(name) = LOWER(?)";
        try(PreparedStatement statement = conn.prepareStatement(query)){
            statement.setString(1, gamename);
            try(ResultSet rs = statement.executeQuery()){
                if(rs.next()) {
                    return rs.getInt("gaid");
                }
            }
            return null;
        }
    }

    public rates rating(Connection conn, int uid) throws SQLException {
        String sqli = "INSERT INTO  rates (uid, gaid, ratings) VALUES (?,?, ?)";
        Scanner scanner = new Scanner(System.in);
        String game;
        Integer gaid;
        int stars;
        System.out.println("Enter the game you wish to rate: ");
        game = scanner.nextLine();
        System.out.println("What do you give this game (0-5 stars): ");
        stars = scanner.nextInt();
        gaid = getgaid(conn, game);
        if(gaid == null){
            System.out.println("Game doesn't exist");
            return null;
        }
        rates newrating = new rates(uid, gaid, stars);
        try(PreparedStatement statement = conn.prepareStatement(sqli)){
            statement.setInt(1, uid);
            statement.setInt(2, gaid);
            statement.setInt(3, stars);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return newrating;
    }

    public boolean updaterating(Connection conn, int uid) throws SQLException {
        Integer gaid;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the game you wish to update: ");
        String game = scanner.nextLine();
        System.out.println("What do you want to change it to (0-5 stars): ");
        int stars = scanner.nextInt();
        gaid = getgaid(conn, game);
        if(gaid == null){
            System.out.println("Game doesn't exist");
            return false;
        }
        String sqlu =  "UPDATE rates SET ratings = ? WHERE uid = ? AND gaid = ?";
        try(PreparedStatement statement = conn.prepareStatement(sqlu)){
            statement.setInt(1, stars);
            statement.setInt(2, uid);
            statement.setInt(3, gaid);
            int row = statement.executeUpdate();
            if(row == 1){
                System.out.println("Game rating has been updated");
                return true;
            }else{
                System.out.println("Not updated you have not rated this game yet");
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}

