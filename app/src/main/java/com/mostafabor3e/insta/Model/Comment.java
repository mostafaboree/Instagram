package com.mostafabor3e.insta.Model;

public class Comment {
    private String publisher;
    private String id;
    private String comment;

    public Comment() {
    }

    public Comment(String publisher, String id, String comment) {
        this.publisher = publisher;
        this.id = id;
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
