package entity;

public class platforms {

    /** platform id */
    private int pid;

    /** platform name */
    private String name;

    /**
     * Platform/s object.
     * @param pid <-
     * @param name <-
     */
    public platforms(int pid, String name) {
        this.pid = pid;
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
     * Get platform name.
     * @return platform name
     */
    public String getName() {
        return name;
    }

}
