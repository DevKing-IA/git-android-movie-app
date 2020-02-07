package com.evilkingmediabeta.demand;

public class Utils {

    public String title;
    public int img_resource;


    public Utils(String p_title, int p_img_resource) {
        title = p_title;
        img_resource = p_img_resource;
    }

    public int getImg_resource() {
        return img_resource;
    }

    public void setImg_resource(int img_resource) {
        this.img_resource = img_resource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
