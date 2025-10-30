package controller;

import entity.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class PlayController {
    private user users;
    private Scanner in;
    private Connection conn;

    public PlayController(Connection conn, user users){
        this.in = new Scanner(System.in);
        this.users = users;
        this.conn = conn;
    }

    public void play(){
        String input;
        int flag = 0;
        game game = null;
        System.out.print("Please select an option: \nPlay specific game (1) or choose random game from a collection (2)?: ");
        input = in.nextLine();
        for(;;) {
            if(flag==1) break; //already played a game
            if(input.equals("1")) {
                System.out.print("What is the name of the game you played?: ");
                for(;;){
                    String name = in.nextLine();
                    game = nameToGame(name);
                    if(game!=null){
                        markAsPlayed(game);
                        flag = 1;
                        break;
                    }
                    System.out.print("Please enter a valid game title: ");
                }
            }
            else if(input.equals("2")) {
                game = selectRandom();
                markAsPlayed(game);
                break;
            }
            else {
                System.out.print("Please enter a valid option (1/2): ");
                input = in.nextLine();
            }
        }
    }
    private game selectRandom(){
        Random rand = new Random();
        List<Integer> gameIds = new ArrayList<>();
        int chosen = 0;
        System.out.print("Enter name of collection to choose from: ");
        for(;;) {
            String name = in.nextLine();
            try (Statement smt = conn.createStatement()) {
                ResultSet res = smt.executeQuery("select cid from collection where name='" + name + "' and uid=" + this.users.getUid());
                int flag = 0;
                while(res.next()){
                    int cid = res.getInt("cid");
                    gameIds = findGames(cid);
                    chosen = gameIds.get(rand.nextInt(gameIds.size()));
                    flag = 1;
                }
                if(flag==1) break;
                else System.out.print("Please enter a valid collection name: ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        game game = idToGame(chosen);
        System.out.println("The chosen game was: " + game.getName());
        return game;
    }
    private List<Integer> findGames(int cid){
        List<Integer> gameIds = new ArrayList<>();
        try(Statement smt = conn.createStatement()) {
            ResultSet games = smt.executeQuery("select gaid from collection_has where cid=" + cid);
            while (games.next()) {
                gameIds.add(games.getInt("gaid"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return gameIds;
    }
    private game idToGame(int gaid){
        String name = "";
        String esrb = "";
        try(Statement smt = conn.createStatement()) {
            ResultSet res = smt.executeQuery("select name, esrb_rating from game where gaid=" + gaid);
            while (res.next()) {
                name = res.getString("name");
                esrb = res.getString("esrb_rating");
            }
        } catch(SQLException e){
            e.printStackTrace();

        }
        return new game(gaid, name, esrb);
    }
    private game nameToGame(String name){
        int gaid = 0;
        String esrb = "";
        try(Statement smt = conn.createStatement()) {
            ResultSet res = smt.executeQuery("select gaid, esrb_rating from game where name='" + name + "'");
            while (res.next()) {
                gaid = res.getInt("gaid");
                esrb = res.getString("esrb_rating");
            }
            if(gaid == 0){
                return null;
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return new game(gaid, name, esrb);
    }
    private void markAsPlayed(game game){
        Timestamp start;
        Timestamp end;
        System.out.print("When did you start playing this game (24-hour format) (YYYY-MM-DD HH:mm:ss.SSS): ");
        for(;;) {
            String str = in.nextLine();
            try {
                start = Timestamp.valueOf(str);
                break;
            } catch (IllegalArgumentException e){
                System.out.print("Please enter date again: ");
            }
        }

        System.out.print("When did you stop playing this game (24-hour format) (YYYY-MM-DD HH:mm:ss.SSS): ");
        for(;;) {
            String str = in.nextLine();
            try {
                end = Timestamp.valueOf(str);
                if (!end.after(start)) {
                    System.out.print("Date must occur after start date: ");
                }
                else break;
            } catch (IllegalArgumentException e){
                System.out.print("Please enter date again: ");
            }
        }

        String insert = "insert into plays (uid, gaid, start_date_time, end_date_time) values (?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(insert)){
            ps.setInt(1, this.users.getUid());
            ps.setInt(2, game.getGaid());
            ps.setTimestamp(3, start);
            ps.setTimestamp(4, end);
            int affected = ps.executeUpdate();
            if(affected==0){
                throw new SQLException("Recording playtime failed");
            }
            System.out.println("Playtime recorded");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
