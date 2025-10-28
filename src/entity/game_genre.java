package entity;

//TODO: games can have multiple genre
//TODO: could be a list in the game class
public class game_genre {

    /** game id */
    private int gaid;

    /** genre id */
    private int gid;

    /**
     * Game has what genre relationship.
     * @param gaid game id
     * @param gid genre id
     */
    public game_genre(int gaid, int gid) {
        this.gaid = gaid;
        this.gid = gid;
    }

    /**
     * Get game id
     * @return game id
     */
    public int getGaid() {
        return gaid;
    }

    /**
     * Get genre id
     * @return genre id
     */
    public int getGid() {
        return gid;
    }

    /**
     *
     * @return message that tells us a game has a certain genre
     */
    public String gameGenreMessage(){
        //TODO: message that says game genre
        //TODO: games can have multiple genre
        return "Game has genre is genre";
    }


}
