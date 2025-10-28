package entity;

public class publishes {

    /** id of developer/publisher */
    private int devid;

    /** id of the game that was developed or published */
    private int gaid;

    /**
     * Publishes relationship.
     * @param devid developer/publisher id, who made/publish game
     * @param gaid game published/made
     */
    public  publishes(int devid, int gaid) {
        this.devid = devid;
        this.gaid = gaid;
    }

    /**
     * Get developer/publisher id
     * @return developer/publisher id
     */
    public int getDevid() {
        return devid;
    }

    /**
     * Get game id
     * @return game id
     */
    public int getGaid() {
        return gaid;
    }
}
