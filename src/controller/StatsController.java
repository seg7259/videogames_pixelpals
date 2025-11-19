package controller;

import entity.user;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StatsController {
    String errorMsg = "Error";
    Scanner in;
    public StatsController(){
        in = new Scanner(System.in);
    }
    public void findTopGames(Connection conn, user us){

        Scanner in = new Scanner(System.in);
        int ans = 0;
        System.out.println("Choose an option:\n1. Find top 20 most popular video games " +
                "in the last 90 days\n2. Find top 20 most popular video games that is owned by " + us.getFirst_name() +
                "\n3. Top 5 new releases of the month");
        ans = Integer.parseInt(in.nextLine());
        if (ans == 1){
            findByLastFewMonths(conn);
        }
        else if (ans == 2){
            findByOwned(conn, us);
        }
        else{
            findByNewReleases(conn);
        }
    }
    /*Find top 20 games from the past 90 days. The way we measuring which games are most popular is by the amount of
    * time played, ie, which games have the most playtime. We are finding the sum of the playtime between all. We need
    * to select a limit of 20 games in descending order where plays and game id are the same, and we need to do that for
    * every game */
    public void findByLastFewMonths(Connection conn){
        ResultSet res;
        try (Statement smt = conn.createStatement()) {
            String top =
                    "SELECT g.name, " +
                            "       SUM(EXTRACT(EPOCH FROM (p.end_date_time - p.start_date_time))) AS total_playtime " +
                            "FROM game g " +
                            "JOIN plays p ON g.gaid = p.gaid " +
                            "WHERE p.start_date_time >= NOW() - INTERVAL '90 days' " +
                            "GROUP BY g.gaid, g.name " +
                            "ORDER BY total_playtime DESC " +
                            "LIMIT 20;";
            res = smt.executeQuery(top);
            System.out.println("\n---Top 20 most popular games---");
            int count = 0;
            while (res.next()) {
                count++;
                System.out.println(count + ". " + res.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println(errorMsg);
            e.printStackTrace();
        }
    }
    public void findByOwned(Connection conn, user us){
        ResultSet res;
        int userID = us.getUid();
        try (Statement smt = conn.createStatement()) {
            String top =
                    "SELECT g.name, " +
                            "       SUM(EXTRACT(EPOCH FROM (p.end_date_time - p.start_date_time))) AS total_playtime " +
                            "FROM game g " +
                            "JOIN plays p ON g.gaid = p.gaid " +
                            "JOIN collection_has ch ON ch.gaid = g.gaid " +
                            "JOIN collection c ON ch.cid = c.cid " +
                            "WHERE c.uid = " + userID + " " +
                            "GROUP BY g.gaid, g.name " +
                            "ORDER BY total_playtime DESC " +
                            "LIMIT 20;";
            res = smt.executeQuery(top);
            System.out.println("\n---Top 20 most popular games---");
            int count = 0;
            while (res.next()) {
                count++;
                System.out.println(count + ". " + res.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println(errorMsg);
            e.printStackTrace();
        }
    }
    public void findByNewReleases(Connection conn){
        ResultSet res;
        try (Statement smt = conn.createStatement()) {
            String top =
                    "SELECT g.name, " +
                            "       SUM(EXTRACT(EPOCH FROM (p.end_date_time - p.start_date_time))) AS total_playtime " +
                            "FROM game g " +
                            "JOIN plays p ON g.gaid = p.gaid " +
                            "JOIN game_runs_on gro ON g.gaid = gro.gaid " +
                            "WHERE gro.release_date >= NOW() - INTERVAL '30 days' " +
                            "GROUP BY g.gaid, g.name " +
                            "ORDER BY total_playtime DESC " +
                            "LIMIT 5;";
            res = smt.executeQuery(top);
            System.out.println("\n---Top 5 New Releases in the Past Month---");
            int count = 0;
            while (res.next()) {
                count++;
                System.out.println(count + ". " + res.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println(errorMsg);
            e.printStackTrace();
        }
    }
}
