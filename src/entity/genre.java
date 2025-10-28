package entity;

public class genre {

    /** genre id */
    private int gid;

    /** genre name */
    private String name;


    /**
     * Genre object/entity.
     * @param gid genre id
     * @param name genre name
     */
    public genre(int gid, String name) {
        this.gid = gid;
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
     * Get genre name
     * @return genre name
     */
    public String getName() {
        return name;
    }
}
