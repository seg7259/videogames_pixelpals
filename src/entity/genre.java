package entity;

public class genre {

    /** genre id, auto increment value */
    private int gid;

    /** genre name */
    private String name;


    /**
     * Genre object/entity.
     * @param name genre name
     */
    public genre(String name) {
        this.name = name;
    }

    /**
     * Return genre id
     * @return gid
     */
    public int getGid() {
        return gid;
    }

    /**
     * Set the genre id to the one from the database.
     * @param gid genre id to get from database
     */
    public void setGid(int gid) {
        this.gid = gid;
    }

    /**
     * Get genre name
     * @return genre name
     */
    public String getName() {
        return name;
    }
}
