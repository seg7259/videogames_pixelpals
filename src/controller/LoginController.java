package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LoginController {
    String errorMsg;

    public LoginController(){
        errorMsg = "Login Failed";

    }
    public void login(Connection conn) {
        String userName;
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter username: ");
        userName = in.nextLine();
        try (Statement smt = conn.createStatement()) {
            ResultSet res = smt.executeQuery("select * from users where username='" + userName + "'");
            while (res.next()) {
                System.out.println(res.getString("first_name"));
                System.out.println(res.getString("last_name"));
                System.out.println(res.getString("creation_date"));
                System.out.println(res.getString("email_address"));
                System.out.println(res.getString("last_access_date"));
            }
        } catch (SQLException e) {
            System.out.println(errorMsg);
            e.printStackTrace();
        }
    }
}