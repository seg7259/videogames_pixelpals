package controller;

import entity.*;
import controller.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ApplicationController {
    private user users;
    private Connection conn;
    private Scanner in;
    private LoginController login;
    private CollectionController coll;
    private FollowController follow;
    private PlayController play;
    private RatesController rate;
    private SearchController search;
    private StatsController stats;

    public ApplicationController(Connection conn){
        this.conn = conn;
        this.in = new Scanner(System.in);
        this.login = new LoginController();
        this.users = login();

        this.coll = new CollectionController();
        this.follow = new FollowController(conn, users);
        this.play = new PlayController(conn, users);
        this.rate = new RatesController();
        this.search = new SearchController();
        this.stats = new StatsController();
    }

    private user login(){
        for(;;) {
            System.out.print("Would you like to sign in (1) or create an account (2)?: ");
            int input = Integer.parseInt(in.nextLine());
            switch (input) {
                case 1:
                    this.users = login.login(conn);
                    if(this.users != null) return this.users;
                    break;
                case 2:
                    this.users = login.createUser(conn);
                    if(this.users != null) return this.users;
                    break;
                default:
                    System.out.print("Invalid option, please select again: ");
            }
        }
    }
    private void printMenu(){
        System.out.println("""
                \t0: View Option Menu
                \t1: View Collections
                \t2: Add Game to Collection
                \t3: Remove Game from Collection
                \t4: Create Collection
                \t5: Delete Collection
                \t6: Search for Games
                \t7: Add Rating to a Game
                \t8: Update a Rating for a Game
                \t9: Play a Game
                \t10: Follow User
                \t11: Unfollow User
                \t12: Find top games
                \t99: Log Out and Exit Application""");
    }
    public void run(){
        System.out.println("Welcome, " + users.getFirst_name() + "!");
        printMenu();
        for(;;) {
            System.out.print("Please select an option from the menu (0 to view options): ");
            int input = Integer.parseInt(in.nextLine());
            switch(input) {
                case 0: printMenu(); break;
                case 1: coll.viewAllCollections(conn, users.getUid()); break;
                case 2: coll.addGameToCollection(conn, users.getUid()); break;
                case 3: coll.deleteGameFromCollection(conn, users.getUid()); break;
                case 4: coll.createCollection(conn, users.getUid()); break;
                case 5: coll.deleteCollection(conn, users.getUid()); break;
                case 6: search.searchGame(conn, users); break;
                case 7:
                    try {
                        rate.rating(conn, users.getUid());
                    } catch (SQLException e) {
                        System.out.println("Rating failed, please try again");
                    }
                    break;
                case 8:
                    try {
                        rate.updaterating(conn, users.getUid());
                    } catch (SQLException e) {
                        System.out.println("Rating failed, please try again");
                    }
                    break;
                case 9: play.play(); break;
                case 10:
                    follow.findUser();
                    follow.followUser();
                    break;
                case 11:
                    follow.findUser();
                    follow.unfollowUser();
                    break;
                case 12:
                    stats.findTopGames(conn, users);
                    break;
                case 99: return;
                default:
                    System.out.println("Invalid selection");
            }
        }
    }
}
