package entity;

public class game_runs_on {

    /** platform id */
    private int pid;

    /** game id */
    private int gaid;

    /** release date of game on certain platform */
    private String release_date;

    /** price of game on platform */
    private int price;

    /**
     * Game runs on relationship, for a game that runs on a certain platform.
     * @param pid <-
     * @param gaid <-
     * @param release_date <-
     * @param price <-
     */
    public game_runs_on(int pid, int gaid, String release_date, int price) {
        this.pid = pid;
        this.gaid = gaid;
        this.release_date = release_date;
        this.price = price;
    }

    /**
     * Get price of game
     * @return price of game
     */
    public int getPrice() {
        return price;
    }

    /**
     * Get release date of game
     * @return release date
     */
    public String getRelease_date() {
        return release_date;
    }

    /**
     * Get the game id
     * @return game id
     */
    public int getGaid() {
        return gaid;
    }

    /**
     * Return platform id
     * @return platform id
     */
    public int getPid() {
        return pid;
    }
}
