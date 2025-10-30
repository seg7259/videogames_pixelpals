package entity;

public class platforms {

    /** platform id */
    private int pid;

    /** platform name */
    private String name;

    /**
     * Platform/s object.
     * @param name <-
     */
    public platforms(String name) {
        this.name = name;

    }

    /**
     * Get platform id
     * @return pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * Set the id to the database id.
     * @param pid the id to the one from the database
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
     * Get platform name.
     * @return platform name
     */
    public String getName() {
        return name;
    }

}
