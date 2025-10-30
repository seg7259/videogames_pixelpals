package entity;

public class user_platform {
    /** user id */
     private int uid;
     /** platform id */
     private int pid;

    /**
     * User platform relationship to see which platforms a user is on.
     * @param uid <-
     * @param pid <-
     */
     public user_platform(int uid, int pid) {
         this.uid = uid;
         this.pid = pid;
     }

    /**
     * Get user id
     * @return uid
     */
    public int getUid() {
         return uid;
     }

    /**
     * Get platform id.
     * @return pid
     */
    public int getPid() {
         return pid;
     }
}
