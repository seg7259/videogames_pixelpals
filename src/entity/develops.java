package entity;

public class develops {

    /** developer id of the person who developed game */
    private int devid;

    /** game id of the game that was developed */
    private int gaid;

    /**
     * Develops relationship.
     * @param devid ^
     * @param gaid ^
     */
    public develops(int devid, int gaid) {
        this.devid = devid;
        this.gaid = gaid;
    }

    /**
     * Get the developer id
     * @return devid
     */
    public int getDevid() {
        return devid;
    }

    /**
     * Return game id
     * @return gaid
     */
    public int gaid () {
        return gaid;
    }


}
