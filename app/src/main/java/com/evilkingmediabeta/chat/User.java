package com.evilkingmediabeta.chat;

public class User {
    private String userid;
    private String username;
    private String useremail;
    private String userimage;
    private String usertype;
    private String usertoken;
    private boolean block;

    public User(){}

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean isBlock() {
        return block;
    }

    public void setUsertoken(String usertoken) {
        this.usertoken = usertoken;
    }

    public String getUsertoken() {
        return usertoken;
    }
}
