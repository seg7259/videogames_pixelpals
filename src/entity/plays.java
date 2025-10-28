package entity;

public class plays {

    /** foreign key of user id */
    private int uid;

    /** foreign key of game id */
    private int gaid;

    /** the time the user started playing the game */
    private String start_date_time;

    /** the time the user ended playing the game */
    private String end_date_time;


    /**
     * Plays object.
     * @param uid user id
     * @param gaid game id
     * @param start_date_time start time
     * @param end_date_time end time
     */
    public plays(int uid, int gaid, String start_date_time, String end_date_time) {
        this.uid = uid;
        this.gaid = gaid;
        this.start_date_time = start_date_time;
        this.end_date_time = end_date_time;
    }

    /**
     * Gets the total playtime of a single game from the start time and end time.
     * @param star_date_time <-
     * @param end_date_time <-
     * @return amount of time a game was played for
     */
    public int getPlayTime(int star_date_time, int end_date_time) {
//        TODO: create functions
        return 0;
    }

    /**
     * Tells us the game that was played.
     * @param gaid game id
     * @return game that was played
     */
    public String getGamePlayed(int gaid) {
        //        TODO: create functions
        return null;
    }
}
