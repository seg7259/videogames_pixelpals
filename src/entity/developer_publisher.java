package entity;

public class developer_publisher {

    /** developer/publisher id */
    private int devid;

    /** developer.publisher name */
    private String name;

    /**
     * Developer/publisher object.
     * @param devid ^
     * @param name ^
     */
    public  developer_publisher(int devid, String name) {
        this.devid = devid;
        this.name = name;
    }

    /**
     * Get developer/publisher id
     * @return devid
     */
    public int getDevid() {
        return devid;
    }

    /**
     * Get developer/publisher name
     * @return name
     */
    public String getName(){
        return name;
    }
}
