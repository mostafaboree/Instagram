package com.mostafabor3e.insta.Model;

public class User {
    private String username;
    private String fullname;
    private  String id;
    private  String bio;
    private  String image;

    public User(String username, String fullname, String id, String bio, String image) {
        this.username = username;
        this.fullname = fullname;
        this.id = id;
        this.bio = bio;
        this.image = image;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
