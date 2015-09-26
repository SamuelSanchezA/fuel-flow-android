package com.heavyconnect.heavyconnect.entities;

import java.io.Serializable;

/**
 * This class represents an Employee.
 */
public class Employee implements Serializable{

    private int id;
    private User user;
    private int company_id;
    private int language;
    private int manager;

    /**
     * Gets employee user.
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets employee's company id.
     * @return
     */
    public int getCompany_id() {
        return company_id;
    }

    /**
     * Gets employee's language.
     * @return - Language ID.
     */
    public int getLanguage() {
        return language;
    }

    /**
     * Gets employee's manager id.
     * @return
     */
    public int getManager() {
        return manager;
    }

    /**
     * Gets employee's id.
     * @return - Employee's id.
     */
    public int getId(){
        return id;
    }
}
