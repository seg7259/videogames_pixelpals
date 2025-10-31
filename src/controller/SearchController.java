package controller;

import entity.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class SearchController {
    final String MENU = "Search game by:\n1. Name\n2. Platform\n3. Release Date" +
            "\n4. Developers\n5. Price\n6. Genre";
    final String OPTIONS = "1. Select game\n2. Sort by video game name (ascending)\n" +
            "3. Sort by video game name (descending)\n4. Sort by price (ascending)\n5. " +
            "Sort by genre (ascending) \n6. Sort by genre (descending)\n7. Sort by released " +
            "year (ascending)\n8.Sort by released year";
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
                    res = smt.executeQuery("select * from game where name like '" + gameName + "%'"  + " order by name ASC");
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
                            res4 = smt.executeQuery("select * from game where gaid=" + gaid.getGaid()  + " order by name ASC");
                            while (res4.next()) {
                                t = new game(res4.getString("name"), res4.getString("esrb_rating"));
                                t.setGaid(Integer.parseInt(res4.getString("gaid")));
                                games.add(t);
                            }
                        }
                    }

                    break;

                case 3:
                    System.out.println("Enter game's release date: ");
                    String gameReleaseDate = in.nextLine();
                    List<game_runs_on> grs = new LinkedList<>();
                    game_runs_on temp6 = null;
                    game t3;
                    res2 = smt.executeQuery("select * from game_runs_on where release_date='" + LocalDate.parse(gameReleaseDate) + "'");
                    while(res2.next()){
                        temp6 = new game_runs_on(Integer.parseInt(res2.getString("pid")), Integer.parseInt(res2.getString("gaid")), res2.getString("release_date"), Integer.parseInt(res2.getString("price")));
                        grs.add(temp6);

                    }
                    for (game_runs_on gaid : grs) {
                        res4 = smt.executeQuery("select * from game where gaid=" + gaid.getGaid()  + " order by name ASC");
                        while (res4.next()) {
                            t3 = new game(res4.getString("name"), res4.getString("esrb_rating"));
                            t3.setGaid(Integer.parseInt(res4.getString("gaid")));
                            games.add(t3);
                        }
                    }

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
                            res4 = smt.executeQuery("select * from game where gaid=" + dd.gaid()  + " order by name ASC");
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
                        res4 = smt.executeQuery("select * from game where gaid=" + gaid.getGaid()  + " order by name ASC");
                        while (res4.next()) {
                            tem = new game(res4.getString("name"), res4.getString("esrb_rating"));
                            tem.setGaid(Integer.parseInt(res4.getString("gaid")));
                            games.add(tem);
                        }
                    }

                    break;
                default:
                    System.out.println("Enter game's genre: ");
                    String gameGenre = in.nextLine();
                    List<genre> genres = new LinkedList<>();
                    genre temp5 = null;
                    res2 = smt.executeQuery("select * from genre where name like '" + gameGenre + "%'");
                    while(res2.next()){
                        temp5 = new genre(res2.getString("name"));
                        temp5.setGid(Integer.parseInt(res2.getString("gid")));
                        genres.add(temp5);
                    }
                    game t2;
                    List<game_genre> gids = new LinkedList<>();
                    for (genre gen : genres) {
                        res3 = smt.executeQuery("select * from game_genre where gid=" + gen.getGid());
                        while (res3.next()) {
                            gids.add(new game_genre(Integer.parseInt(res3.getString("gaid")), Integer.parseInt(res3.getString("gid"))));
                        }
                        for (game_genre gid : gids) {
                            res4 = smt.executeQuery("select * from game where gaid=" + gid.getGaid() + " order by name ASC");
                            while (res4.next()) {
                                t2 = new game(res4.getString("name"), res4.getString("esrb_rating"));
                                t2.setGaid(Integer.parseInt(res4.getString("gaid")));
                                games.add(t2);
                            }
                        }
                    }
                    break;
            }
            displayGames(games, smt, us);
            System.out.println(OPTIONS);
            ans = Integer.parseInt(in.nextLine());
            switch (ans) {
                case 1:

            }


        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return g;
    }

    public void displayGames(List<game> games, Statement smt, user us) throws SQLException {
        if(games == null || games.isEmpty()) {
            System.out.println("No Result");
            return;
        }

        System.out.println(OPTIONS);
        int choice = 2;
        try {
            choice = Integer.parseInt(new java.util.Scanner(System.in).nextLine());
        }catch (NumberFormatException ignored){}

        Map<Integer, Integer> prices = new HashMap<>();
        Map<Integer, String> genres = new HashMap<>();
        Map<Integer, Integer> years = new HashMap<>();

        for(game g : games) {
            try(ResultSet rs = smt.executeQuery("select MIN(price) as price from game_runs_on where gaid=" + g.getGaid())) {
                if(rs.next()) {
                    int price =  rs.getInt("price");
                    prices.put(g.getGaid(), price);
                }
            }
            try(ResultSet rs = smt.executeQuery("select MIN(release_date) as date from  game_runs_on where gaid=" + g.getGaid())) {
                if(rs.next()) {
                    java.sql.Date date = rs.getDate("date");
                    years.put(g.getGaid(), date.toLocalDate().getYear());
                }
            }
            try(ResultSet rs = smt.executeQuery("SELECT MIN(ge.name) AS gname FROM game_genre gg JOIN genre ge ON ge.gid = gg.gid WHERE gg.gaid=" + g.getGaid())) {
                if(rs.next()) {
                    String genre = rs.getString("gname");
                    genres.put(g.getGaid(), genre);
                }
            }
        }

        Comparator<game> byName = Comparator.comparing(game::getName).thenComparingInt(game::getGaid);
        Comparator<game> byNamedesc = byName.reversed();
        Comparator<game> price = Comparator.comparing((game g) -> prices.get(g.getGaid())).thenComparing(byName);
        Comparator<game> genre = Comparator.comparing((game g) -> genres.get(g.getGaid())).thenComparing(byName);
        Comparator<game> genredesc = genre.reversed();
        Comparator<game> year = Comparator.comparing((game g) -> years.get(g.getGaid())).thenComparing(byName);
        Comparator<game> yeardesc = year.reversed();
        Comparator<game> compare = switch (choice){
            case 2 -> byName;
            case 3 -> byNamedesc;
            case 4 -> price;
            case 5 -> genre;
            case 6 -> genredesc;
            case 7 -> year;
            case 8 -> yeardesc;
            default -> byName;
        };
        games.sort(compare);
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
    }
}
