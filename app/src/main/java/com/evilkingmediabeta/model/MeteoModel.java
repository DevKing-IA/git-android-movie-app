package com.evilkingmediabeta.model;

public class MeteoModel {

    private String Title;
    private String Url;
    private String image;

    public MeteoModel() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "MeteoModel{" +
                "Title='" + Title + '\'' +
                ", Url='" + Url + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
