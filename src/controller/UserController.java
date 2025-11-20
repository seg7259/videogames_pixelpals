package controller;

import java.sql.*;
import java.util.*;
import entity.*;
public class UserController {
    private Connection conn;
    private int userid;


    public UserController(Connection conn,user users) {
        this.conn = conn;
        this.userid = users.getUid();
    }

    public void getUsercollections(){
        String sql = "select count(*) from collection where uid=?";
        try(PreparedStatement query = conn.prepareStatement(sql)){
            query.setInt(1, userid);
            try(ResultSet rs = query.executeQuery()){
                rs.next();
                int count = rs.getInt(1);
                System.out.println("You have " + count + " collections");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getUserfollowings(){
        String sql = "select count(*) from follows where follower_uid=?";
        try(PreparedStatement query = conn.prepareStatement(sql)){
            query.setInt(1, userid);
            try(ResultSet rs = query.executeQuery()){
                rs.next();
                int count = rs.getInt(1);
                System.out.println("You are following " + count + " people");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void getUserfollowee(){
        String sql = "select count(*) from follows where followee_uid=?";
        try(PreparedStatement query = conn.prepareStatement(sql)){
            query.setInt(1, userid);
            try(ResultSet rs = query.executeQuery()){
                rs.next();
                int count = rs.getInt(1);
                System.out.println("You have " + count + " people following you");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void top10(){
        String sql =
                """
                select g.gaid, g.name, r.ratings 
                from rates r 
                join game g on g.gaid = r.gaid 
                where r.uid=? 
                order by r.ratings desc, g.name asc, g.gaid asc
                limit 10
                """;
        try(PreparedStatement query = conn.prepareStatement(sql)){
            query.setInt(1, userid);
            try(ResultSet rs = query.executeQuery()){
                System.out.println("Top 10 games by ratings");
                int top10 = 1;
                while(rs.next()){
                    System.out.println(top10 + ") " + rs.getString("name") + " ★ " + rs.getInt("ratings"));
                    top10++;
                }
                if(top10 == 1){
                    System.out.println("You didn't give any rating");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
