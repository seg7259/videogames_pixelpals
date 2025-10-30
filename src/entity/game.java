package entity;

public class game {

    /** game id, auto increment so doesn't get set */
    private int gaid;

    /** game name */
    private String name;

    /** esrb rating of game */
    private String esrb_rating;

    /**
     * Game object
     * @param gaid game id
     * @param name game name
     * @param esrb_rating esrb rating of game
     */
    public game(int gaid, String name, String esrb_rating) {
        this.gaid = gaid;
        this.name = name;
        this.esrb_rating = esrb_rating;
    }

    /**
     * Game object
     * @param name game name
     * @param esrb_rating esrb rating of game
     */
    public game(String name, String esrb_rating) {
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
    public String getEsrb_rating() {
        return esrb_rating;
    }

    /**
     * Get game id
     * @return game id
     */
    public int getGaid() {
        return gaid;
    }

    /**
     * Set the game id from the value from the data.
     * @param gaid set the gaid from auto increment value
     */
    public void setGaid(int gaid) {
        this.gaid = gaid;
    }
}
