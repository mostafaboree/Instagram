package com.mostafabor3e.insta.Model;

public class Story {
    private String imageuri;
    private  String userid;
    private String storyid;
    private long starttime;
    private long endtime;

    public Story() {
    }

    public Story(String imageuri, String userid, String storyid, long starttime, long endtime) {
        this.imageuri = imageuri;
        this.userid = userid;
        this.storyid = storyid;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getStoryid() {
        return storyid;
    }

    public void setStoryid(String storyid) {
        this.storyid = storyid;
    }

    public long getStarttime() {
        return starttime;
    }

    public void setStarttime(long starttime) {
        this.starttime = starttime;
    }

    public long getEndtime() {
        return endtime;
    }

    public void setEndtime(long endtime) {
        this.endtime = endtime;
    }
}
