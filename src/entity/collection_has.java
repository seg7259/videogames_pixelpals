package entity;

//TODO: should be able to get a list of games in the collection
//TODO: could possible be done in another class, "like a script"
//TODO: user could have a MAP where the key is the name of collection and value a list of game in collection
public class collection_has {

    /** collection id */
    private int cid;

    /** game id */
    private int gaid;

    /**
     * Collection has relationship.
     * @param cid collection id
     * @param gaid game id
     */
    public collection_has(int cid, int gaid) {
        this.cid = cid;
        this.gaid = gaid;
    }

    /**
     * Gets game id
     * @return game id
     */
    public int getGaid() {
        return gaid;
    }

    /**
     * Gets collection id
     * @return collection id
     */
    public int getCid() {
        return cid;
    }
}
