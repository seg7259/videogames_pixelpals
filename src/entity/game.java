package entity;

public class game {

    /** game id */
    private int gaid;

    /** game name */
    private String name;

    /** esrb rating of game */
    private char esrb_rating;

    /**
     * Game object
     * @param gaid game id
     * @param name game name
     * @param esrb_rating esrb rating of game
     */
    public game(int gaid, String name, char esrb_rating) {
        this.gaid = gaid;
        this.name = name;
        this.esrb_rating = esrb_rating;
    }


    /**
     * Get game name
     * @return name of game
     */
    public String getName() {
        return name;
    }

    /**
     * Get game esrb rating.
     * @return game esrb rating
     */
    public char getEsrb_rating() {
        return esrb_rating;
    }

    /**
     * Get game id
     * @return game id
     */
    public int getGaid() {
        return gaid;
    }
}
