package controller;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import entity.user;

import static java.util.Objects.hash;
//Bm00001!#40

public class LoginController {
    String errorMsg;
    user userr;

    public LoginController(){
        errorMsg = "User not found";

    }
    public user login(Connection conn) {
        String userName;
        String password;
        String userPassword = "";
        String first_name = "", last_name = "", creation_date = "", email_address = "", last_access_date = "";
        int uid = 0;
        boolean u = false;
        ResultSet res;
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter username: ");
        userName = in.nextLine();
        try (Statement smt = conn.createStatement()) {
            res = smt.executeQuery("select * from users where username='" + userName + "'");
                while (res.next()) {
                    userPassword = res.getString("password");
                    first_name = res.getString("first_name");
                    last_name = res.getString("last_name");
                    creation_date = res.getString("creation_date");
                    email_address = res.getString("email_address");
                    last_access_date = res.getString("last_access_date");
                    uid = Integer.parseInt(res.getString("uid"));
                    u = true;
                }
                if (u){
                    System.out.print("Please enter password: ");
                    password = in.nextLine();
                    String sv = getSaltedValue(password, creation_date);
                    int hashValue = hashPassword(sv);
                    if(String.valueOf(hashValue).equals(userPassword)){
                        System.out.println("Correct password");
                        userr = new user(uid, first_name, last_name, creation_date, email_address, userName, userPassword);
                    }
                    else{
                        System.out.println("Incorrect password");
                    }
                }
                else{
                    System.out.println(errorMsg);
                    System.out.println("Create new user?\n1. Yes\n2. No");
                    int ans = Integer.parseInt(in.nextLine());
                    if (ans == 1)
                        createUser(conn);
                }

        } catch (SQLException e) {
            System.out.println(errorMsg);
            e.printStackTrace();
        }
        return userr;
    }
    public user createUser(Connection conn){
        LocalDate today = LocalDate.now();
        LocalTime time = LocalTime.now();
        Timestamp creation_date = Timestamp.valueOf(today + " " + time);
        Timestamp last_access_date = Timestamp.valueOf(today + " " + time);
        String userName;
        String password = "";
        String first_name = "", last_name = "", email_address = "";
        Scanner in = new Scanner(System.in);
        System.out.print("Please enter new username: ");
        userName = in.nextLine();
        try (Statement smt = conn.createStatement()) {
            ResultSet res;
            res = smt.executeQuery("select username from users where username='" + userName + "'");
            if(res.next()){
                System.out.println("User name already taken");
            }
            else {
                boolean notSet = true;
                while (notSet) {
                    System.out.print("Enter email address: ");
                    email_address = in.nextLine();
                    if (email_address.length() > 60) {
                        System.out.println("Email address cannot be over 60 characters");
                    } else {
                        notSet = false;
                    }
                }
                res = smt.executeQuery("select email_address from users where email_address='" + email_address + "'");
                if (res.next()) {
                    System.out.println("Email address already taken");
                } else {
                    notSet = true;
                    while (notSet) {
                        System.out.print("Enter password: ");
                        password = in.nextLine();
                        if (password.length() > 30) {
                            System.out.println("Password cannot be over 30 characters");
                        } else {
                            notSet = false;
                        }
                        password = getSaltedValue(password, String.valueOf(creation_date));
                        password = String.valueOf(hashPassword(password));
                    }
                    notSet = true;
                    while (notSet) {
                        System.out.print("Enter first name: ");
                        first_name = in.nextLine();
                        if (first_name.length() > 30) {
                            System.out.println("First name cannot be over 30 characters");
                        } else {
                            notSet = false;
                        }
                    }

                    notSet = true;
                    while (notSet) {
                        System.out.print("Enter last name: ");
                        last_name = in.nextLine();
                        if (last_name.length() > 30) {
                            System.out.println("Last name cannot be over 30 characters");
                        } else {
                            notSet = false;
                        }
                    }


                    String sql = "INSERT INTO users (username, first_name, last_name, creation_date, email_address, password, last_access_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, userName);
                        ps.setString(2, first_name);
                        ps.setString(3, last_name);
                        ps.setTimestamp(4, creation_date);
                        ps.setString(5, email_address);
                        ps.setString(6, password);
                        ps.setTimestamp(7, last_access_date);

                        int affected = ps.executeUpdate(); // returns number of rows inserted
                        if (affected == 0) {
                            throw new SQLException("Creating user failed, no rows affected.");
                        }

                        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
//                                System.out.println(generatedKeys.getLong(8));
                            } else {
                                throw new SQLException("Creating user failed, no ID obtained.");
                            }
                        }


                    } catch (SQLException e) {
                        System.out.println(errorMsg);
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(errorMsg);
            e.printStackTrace();
        }

        return userr;
    }
    public int hashPassword(String saltedPassword){
        return hash(saltedPassword);
    }
    public String getSaltedValue(String password, String date){
        return password + date;
    }
}