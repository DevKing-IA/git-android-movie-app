package com.evilkingmediabeta.chat;

public class MemberData {
    private String name;
    private String image;
    private int color;

    public MemberData(String name, String image, int color) {
        this.name = name;
        this.color = color;
        this.image = image;
    }

    public MemberData() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getColor(){
        return color;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
