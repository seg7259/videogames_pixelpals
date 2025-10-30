package entity;

public class developer_publisher {

    /** developer/publisher id, auto increment */
    private int devid;

    /** developer.publisher name */
    private String name;

    /**
     * Developer/publisher object.
     * @param name ^
     */
    public  developer_publisher( String name) {
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
     * Set the devid to the value from the data base.
     * @param devid the auto increment devid value
     */
    public void setDevid(int devid) {
        this.devid = devid;
    }

    /**
     * Get developer/publisher name
     * @return name
     */
    public String getName(){
        return name;
    }
}
