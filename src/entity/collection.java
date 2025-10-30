package entity;

public class collection {

    /** collection id, auto increment so dont assign a value */
    private int cid;


    /** user id */
    private int uid;

    /** name of the collection */
    private String name;

    /**
     * Collection object.
     * @param uid <- user id
     * @param name <- collection name
     */
    public  collection(int uid, String name) {
        this.uid = uid;
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
     * Change the name of a collection.
     * @param name new name to change to
     */
    public void changeName(String name) {
        this.name = name;
    }

    /**
     * Get collection id
     * @return collection id
     */
    public int getCid() {
        return cid;
    }

    /**
     * Set the new cid from the auto generated id
     * @param cid to set
     */
    public void setCid(int cid) {
        this.cid = cid;
    }


    /**
     * Get user id
     * @return user id
     */
    public int getUid() {
        return uid;
    }
}
