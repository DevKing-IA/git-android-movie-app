package com.evilkingmediabeta.model;

import java.io.Serializable;

public class SportsModel implements Serializable{

    private String date;
    private String time;
    private String url;
    private String category;
    private String channel;
    private String team1;
    private String team2;
    private String title;
    private String currentUrl;
    private String id;
    private String linkNodeString;

    public String getLinkNodeString() {
        return linkNodeString;
    }

    public void setLinkNodeString(String linkNodeString) {
        this.linkNodeString = linkNodeString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SportsModel{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", channel='" + channel + '\'' +
                ", team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", title='" + title + '\'' +
                ", currentUrl='" + currentUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
