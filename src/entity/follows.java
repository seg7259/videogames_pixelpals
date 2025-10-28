package entity;

public class follows {

    /** the user id of the person who follows the followee */
    private int follower_uid;

    /** the user id of the person being followed */
    private int followee_uid;

    /**
     * Follows relationship.
     * @param follower_uid <-
     * @param followee_uid <-
     */
    public follows (int follower_uid, int followee_uid) {
        this.follower_uid = follower_uid;
        this.followee_uid = followee_uid;
    }

    /**
     * Get follower uid
     * @return follower uid
     */
    public int getFollower_uid() {
        return follower_uid;
    }

    /**
     * Get followee user id
     * @return followee uid
     */
    public int getFollowee_uid() {
        return followee_uid;
    }

    /**
     * Message to happens when a user follows another user.
     * @return follower message
     */
    public String followingMessage(){
        //TODO: get the usernames from the uid
        return "follower started following followee";
    }
}
