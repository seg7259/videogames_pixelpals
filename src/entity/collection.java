package entity;

public class collection {

    /** collection id */
    private int cid;

    /** name of the collection */
    private String name;

    /**
     * Collection object.
     * @param cid <- collection id
     * @param name <- collection name
     */
    public  collection(int cid, String name) {
        this.cid = cid;
        this.name = name;
    }


    /**
     * Get collection name
     * @return collection name
     */
    public String getName() {
        return name;
    }

    /**
     * Get collection id
     * @return collection id
     */
    public int getCid() {
        return cid;
    }
}
