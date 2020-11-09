package com.mostafabor3e.insta.Model;

public class Notifaction {
    private String userId;
    private  String postId;
    private String text;
    private boolean ispost;

    public Notifaction() {
    }

    public Notifaction(String userId, String postId, String text, boolean ispost) {
        this.userId = userId;
        this.postId = postId;
        this.text = text;
        this.ispost = ispost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
