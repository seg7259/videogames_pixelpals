package entity;

public class user {

    /** entity.user id */
    private int uid;

    /** entity.user first name */
    private String first_name;
    /** entity.user last name */
    private String last_name;

    /** date account was created */
    private String creation_date;

    /** entity.user email address */
    private String email_address;

    /** entity.user's username */
    private String username;

    /** entity.user's password */
    private String password;


    /**
     * User object.
     * @param uid user id
     * @param first_name first name
     * @param last_name last name
     * @param creation_date <-
     * @param email_address <-
     * @param username <-
     * @param password users password
     */
    public user(int uid, String first_name, String last_name, String creation_date, String email_address, String username, String password) {
        this.uid = uid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.creation_date = creation_date;
        this.email_address = email_address;
        this.username = username;
        this.password = password;

    }


    /**
     * Get password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get email address
     * @return email address
     */
    public String getEmail_address() {
        return email_address;
    }

    /**
     * Get creation date
     * @return creation date
     */
    public String getCreation_date() {
        return creation_date;
    }

    /**
     * Get last name
     * @return last name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * Get first name
     * @return first name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * Get uid
     * @return user id, uid
     */
    public int getUid() {
        return uid;
    }
}
