package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class FollowController {

    private final Connection conn;

    public FollowController(Connection conn) {
        this.conn = conn;
    }

    public boolean followUser(int followerId, int followedId) {
        if (followerId == followedId) {
            System.out.println("You cannot follow yourself.");
            return false;
        }
        String sql = "INSERT INTO follows (follower_id, followed_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            stmt.executeUpdate();
            System.out.println("You now follow." + followedId);
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("You already follow this user.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unfollowUser(int followerId, int followedId) {
        String sql = "DELETE FROM follows WHERE follower_id = ? AND followed_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("You unfollowed." + followedId);
                return true;
            } else {
                System.out.println("You don't unfollow this user.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int findUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("user_id");
                System.out.println("User with this email: ID = " + id);
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