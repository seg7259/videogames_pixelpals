package controller;

import entity.user;

import java.sql.*;
import java.util.Scanner;

public class FollowController {

    private final Connection conn;
    private int followerId;
    private int followedId;

    public FollowController(Connection conn, user users) {
        this.conn = conn;
        this.followerId = users.getUid();
    }

    public void findUser(){
        Scanner in = new Scanner(System.in);
        System.out.print("Search for a user by email: ");
        for(;;) {
            String user = in.nextLine();
            this.followedId = findUserIdByEmail(user);
            if(this.followedId != -1) return;
            System.out.print("Invalid email, please try again: ");
        }
    }

    public boolean followUser() {
        if (followerId == followedId) {
            System.out.println("You cannot follow yourself.");
            return false;
        }
        String sql = "INSERT INTO follows (follower_uid, followee_uid) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            stmt.executeUpdate();
            System.out.println("You now follow this user.");
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("You already follow this user.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unfollowUser() {
        String sql = "DELETE FROM follows WHERE follower_uid = ? AND followee_uid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("You unfollowed this user.");
                return true;
            } else {
                System.out.println("You did not unfollow this user, please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int findUserIdByEmail(String email) {
        String sql = "SELECT uid FROM users WHERE email_address = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("uid");
                return id;
            } else {
                System.out.println("User with this email not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}