package com.heavyconnect.heavyconnect.rest;

import com.google.gson.annotations.SerializedName;
import com.heavyconnect.heavyconnect.entities.User;

/**
 * This class represents the RegisterResult.
 */
public class LoginResult {
    public static final int OK = 0;
    public static final int INVALID_REQUEST = 1;
    public static final int INVALID_INFO = 2;
    public static final int INVALID_USERNAME_OR_PASSWORD = 3;
    public static final int NEED_TO_ACTIVATE = 4;

    private boolean success = false;
    private String username;
    private String first_name;
    private String last_name;
    private boolean parent_user = false;

    @SerializedName("api-token")
    private String token;
    private int code = OK;

    /**
     * Get result status.
     *
     * @return the text.
     */
    public int getStatus() {
        return code;
    }

    public User getUser() {
        if (code != OK)
            return null;

        User result = new User();
        result.setFirstName(first_name);
        result.setLastName(last_name);
        result.setUsername(username);
        result.setToken(token);
        result.setIsEmployee(parent_user);

        return result;
    }
}
