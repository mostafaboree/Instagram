package com.mostafabor3e.insta.Model;

public class Post {
    private String postId;
    private String postimage;
    private String descraption;
    private String publisher;

    public Post() {

    }

    public Post(String postId, String postimage, String descraption, String publisher) {
        this.postId = postId;
        this.postimage = postimage;
        this.descraption = descraption;
        this.publisher = publisher;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescraption() {
        return descraption;
    }

    public void setDescraption(String descraption) {
        this.descraption = descraption;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}