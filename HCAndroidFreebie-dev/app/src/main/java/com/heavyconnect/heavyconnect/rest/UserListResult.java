package com.heavyconnect.heavyconnect.rest;

import com.heavyconnect.heavyconnect.entities.Employee;

import java.util.ArrayList;

/**
 * EmployeeList result class.
 */
public class UserListResult {
    public static final int OK = 0;

    private boolean success = false;
    private int count;
    private Employee[] results;

    private int code = OK;

    /**
     * Get result status.
     *
     * @return the text.
     */
    public int getStatus() {
        return code;
    }

    public ArrayList<Employee> getUsers() {
        if (code != OK || results == null)
            return null;

        ArrayList<Employee> result = new ArrayList<Employee>();
        for(int i = 0; i < results.length; i++)
            result.add(results[i]);

        return result;
    }
}
