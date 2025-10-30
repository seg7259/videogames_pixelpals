import com.jcraft.jsch.*;
import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Scanner;

public class PostgresSSH {

    public static void main(String[] args) throws SQLException {

        int lport = 5432;
        String rhost = "starbug.cs.rit.edu";
        int rport = 5432;
        String user;
        String password;
        try{
            Ini ini = new Ini(new File("DBUser/dbInfo.ini"));
            user = ini.get("Database", "username");
            password = ini.get("Database", "password");
        } catch(IOException e) {
            return;
        }
        String databaseName = "p320_30"; //change to your database name

        String driverName = "org.postgresql.Driver";
        Connection conn = null;
        Session session = null;
        try {
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            session = jsch.getSession(user, rhost, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.setConfig("PreferredAuthentications","publickey,keyboard-interactive,password");
            session.connect();
            System.out.println("Connected");
            int assigned_port = session.setPortForwardingL(lport, "127.0.0.1", rport);
            System.out.println("Port Forwarded");

            // Assigned port could be different from 5432 but rarely happens
            String url = "jdbc:postgresql://127.0.0.1:"+ assigned_port + "/" + databaseName;

            System.out.println("database Url: " + url);
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            Class.forName(driverName);
            conn = DriverManager.getConnection(url, props);
            System.out.println("Database connection established");

            // temp: figuring out how to actually query db
            Scanner in = new Scanner(System.in);
            System.out.print("Enter username: ");
            String uname = in.nextLine();
            System.out.println("Hello, " + uname);
            try(Statement smt = conn.createStatement()) {
                ResultSet res = smt.executeQuery("select * from users where username='" + uname +"'");
                while(res.next()){
                    System.out.println(res.getString("first_name"));
                    System.out.println(res.getString("last_name"));
                    System.out.println(res.getString("creation_date"));
                    System.out.println(res.getString("email_address"));
                    System.out.println(res.getString("last_access_date"));
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Closing Database Connection");
                conn.close();
            }
            if (session != null && session.isConnected()) {
                System.out.println("Closing SSH Connection");
                session.disconnect();
            }
        }
    }

}