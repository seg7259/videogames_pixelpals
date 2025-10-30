package controller;

import entity.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class SearchController {
    final String MENU = "Search game by:\n1. Name\n2. Platform\n3. Release Date" +
            "\n4. Developers\n5. Price\n6. Genre";
    Scanner in;
    public SearchController(){
        in = new Scanner(System.in);
    }
    public game searchGame(Connection conn, user us){
        int ans;
        game g = null;
        System.out.println(MENU);
        ans = Integer.parseInt(in.nextLine());
        game temp = null;
        List<game> games= new LinkedList<>();
        ResultSet res = null;
        try (Statement smt = conn.createStatement()) {
            switch (ans) {
                case 1:
                    System.out.println("Enter game's name: ");
                    String gameName = in.nextLine();
                    res = smt.executeQuery("select * from game where name like '" + gameName + "%'");
                    while (res.next()) {
                        temp = new game(res.getString("name"), res.getString("esrb_rating"));
                        temp.setGaid(Integer.parseInt(res.getString("gaid")));
                        games.add(temp);
                    }
                    break;
                case 2:
                    System.out.println("Enter game's platform: ");
                    String gamePlatform = in.nextLine();
                    List<platforms> ps = new LinkedList<>();
                    platforms temp2 = null;
                    ResultSet res2 = smt.executeQuery("select * from platforms where name like '" + gamePlatform + "%'");
                    while(res2.next()){
                        temp2 = new platforms(res2.getString("name"));
                        temp2.setPid(Integer.parseInt(res2.getString("pid")));
                        ps.add(temp2);
                    }
                    ResultSet res3;
                    ResultSet res4;
                    game t;
                    List<game_runs_on> gaids = new LinkedList<>();
                    for (platforms p : ps) {
                        res3 = smt.executeQuery("select * from game_runs_on where pid=" + p.getPid());
                        while (res3.next()) {
                            gaids.add(new game_runs_on(Integer.parseInt(res3.getString("pid")), Integer.parseInt(res3.getString("gaid")), res3.getString("release_date"), Integer.parseInt(res3.getString("price"))));
                        }
                        for (game_runs_on gaid : gaids) {
                            res4 = smt.executeQuery("select * from game where gaid=" + gaid.getGaid());
                            while (res4.next()) {
                                t = new game(res4.getString("name"), res4.getString("esrb_rating"));
                                t.setGaid(Integer.parseInt(res4.getString("gaid")));
                                games.add(t);
                            }
                        }
                    }

                    break;
                case 3:
                    //insert
                    break;
                case 4:
                    System.out.println("Enter game's Developer: ");
                    String gameDev = in.nextLine();
                    List<developer_publisher> ds = new LinkedList<>();
                    developer_publisher temp3 = null;
                    ResultSet res10 = smt.executeQuery("select * from developer_publisher where name like '" + gameDev + "%'");
                    while(res10.next()){
                        temp3 = new developer_publisher(res10.getString("name"));
                        temp3.setDevid(Integer.parseInt(res10.getString("devid")));
                        ds.add(temp3);
                    }
                    game te;
                    List<develops> devids = new LinkedList<>();
                    for (developer_publisher d : ds) {
                        res3 = smt.executeQuery("select * from develops where devid=" + d.getDevid());
                        while (res3.next()) {
                            devids.add(new develops(Integer.parseInt(res3.getString("devid")), Integer.parseInt(res3.getString("gaid"))));
                        }
                        for (develops dd : devids) {
                            res4 = smt.executeQuery("select * from game where gaid=" + dd.gaid());
                            while (res4.next()) {
                                te = new game(res4.getString("name"), res4.getString("esrb_rating"));
                                te.setGaid(Integer.parseInt(res4.getString("gaid")));
                                games.add(te);
                            }
                        }
                    }
                    break;
                case 5:
                    System.out.println("Enter game's price: ");
                    String gamePrice = in.nextLine();
                    List<game_runs_on> p = new LinkedList<>();
                    game_runs_on temp4 = null;
                    res2 = smt.executeQuery("select * from game_runs_on where price=" + gamePrice);
                    while(res2.next()){
                        temp4 = new game_runs_on(Integer.parseInt(res2.getString("pid")), Integer.parseInt(res2.getString("gaid")), res2.getString("release_date"), Integer.parseInt(res2.getString("price")));
                        p.add(temp4);
                    }
                    game tem;

                    for (game_runs_on gaid : p) {
                        res4 = smt.executeQuery("select * from game where gaid=" + gaid.getGaid());
                        while (res4.next()) {
                            tem = new game(res4.getString("name"), res4.getString("esrb_rating"));
                            tem.setGaid(Integer.parseInt(res4.getString("gaid")));
                            games.add(tem);
                        }
                    }

                    break;
                default:
                    //res = smt.executeQuery("select * from game where name like '" + gameName + "%'");
                    break;
            }
            ResultSet result5;
            HashSet<String> pid = new HashSet<>();
            HashSet<String> devid = new HashSet<>();
            HashSet<String> pevid = new HashSet<>();
            for (game gs : games){
                // GET PLATFORMS
                pid = new HashSet<>();
                System.out.println("Game: " + gs.getName());
                System.out.println("Platforms: ");
                result5 = smt.executeQuery("select pid from game_runs_on where gaid=" + gs.getGaid());
                while(result5.next()){
                    pid.add(result5.getString("pid"));
                }
                for(String p : pid){
                    result5 = smt.executeQuery("select name from platforms where pid=" + p);
                    while(result5.next())
                        System.out.println(result5.getString("name"));
                } // END GET PLATFORMS

                //GET DEVELOPERS/PUBLISHERs
                devid = new HashSet<>();
                System.out.println("Developers: ");
                result5 = smt.executeQuery("select devid from develops where gaid=" + gs.getGaid());
                while(result5.next()){
                    devid.add(result5.getString("devid"));
                }
                for(String d : devid){
                    result5 = smt.executeQuery("select name from developer_publisher where devid=" + d);
                    while(result5.next())
                        System.out.println(result5.getString("name"));
                }
                System.out.println("Publishers: ");
                result5 = smt.executeQuery("select devid from publishes where gaid=" + gs.getGaid());
                while(result5.next()){
                    pevid.add(result5.getString("devid"));
                }
                for(String p : pevid){
                    result5 = smt.executeQuery("select name from developer_publisher where devid=" + p);
                    while(result5.next())
                        System.out.println(result5.getString("name"));
                } //END GET PUBLISHERS/DEVELOPERS

                // GET PLAYTIME
                pid = new HashSet<>();
                System.out.print("Play Time: ");
                result5 = smt.executeQuery("select start_date_time from plays where gaid=" + gs.getGaid() + " and uid=" + us.getUid());
                while(result5.next()){
                    System.out.println(result5.getString("start_date_time"));
                } // END GET PLAYTIME

                // GET RATING
                pid = new HashSet<>();
                System.out.println("Rating: ");
                result5 = smt.executeQuery("select ratings from rates where gaid=" + gs.getGaid() + " and uid=" + us.getUid());
                while(result5.next()){
                    System.out.print(result5.getString("ratings"));
                } // END GET RATING

                System.out.println("ESRB Rating: " + gs.getEsrb_rating());
                System.out.println();
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return g;
    }


}
