package entity;

public class rates {

    /** id of user rating the game */
    private int uid;

    /** game id of the game being rated */
    private int gaid;

    /** rating of game */
    private int ratings;

    /**
     * Rates relationship.
     * @param uid <-
     * @param gaid <-
     * @param ratings <-
     */
    public rates(int uid, int gaid, int ratings) {
        this.uid = uid;
        this.gaid = gaid;
        this.ratings = ratings;

    }

    /**
     * Get the ratings.
     * @return the ratings if a specific game
     */
    public int getRatings() {
        return ratings;
    }

    /**
     * Change the rating for a game if the new rating is within  a range
     * @param ratings new rate
     */
    public void setRatings(int ratings) {
        if (ratings >= 0 && ratings <= 5) {
            this.ratings = ratings;
        }
    }

    /**
     * Get game id
     * @return game id
     */
    public int getGaid() {
        return gaid;
    }

    /**
     * Get user id
     * @return user id
     */
    public int getUid() {
        return uid;
    }
}
