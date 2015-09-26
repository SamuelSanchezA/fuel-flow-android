package com.heavyconnect.heavyconnect.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by andremenezes on 8/4/15.
 */
public class User implements Serializable {
    private int id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String username;
    private String password;
    private String token;
    private String email;

    @SerializedName("parent_user")
    private boolean parentUser = false;

    public User(){

    }

    public User(String firstName, String lastName, String username, String password, String email){
        this.firstName = firstName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.lastName = lastName;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.firstName = "";
        this.email = "";
        this.lastName = "";
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) { this.email = email; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() { return lastName; }

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken(){
        return token;
    }

    public boolean isEmployee(){
        return parentUser;
    }

    public void setIsEmployee(boolean b){
        this.parentUser = b;
    }

    public int getId(){
        return id;
    }
}
