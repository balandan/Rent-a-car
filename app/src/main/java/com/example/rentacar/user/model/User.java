package com.example.rentacar.user.model;

public class User
{
   public Boolean banned;
   public String email;
   public String password;
   public String userID;
   public String username;
   public String verifiedCode;
   public Boolean isVerified;

    public User() {
    }

    public User(Boolean banned, String email, String password, String userID, String username, String verifiedCode, Boolean isVerified) {
        this.banned = banned;
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.username = username;
        this.verifiedCode = verifiedCode;
        this.isVerified = isVerified;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerifiedCode() {
        return verifiedCode;
    }

    public void setVerifiedCode(String verifiedCode) {
        this.verifiedCode = verifiedCode;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }
}
