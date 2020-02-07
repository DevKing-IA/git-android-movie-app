package com.evilkingmediabeta.sports.ekmsportsembed.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfig {

    @SerializedName("menu")
    @Expose
    private String menu;
    @SerializedName("program_guide_enable")
    @Expose
    private boolean programEnable;

    @SerializedName("mandatory_login")
    @Expose
    private boolean loginMandatory;

    @SerializedName("genre_visible")
    @Expose
    private boolean genreVisible;

    @SerializedName("country_visible")
    @Expose
    private boolean countryVisible;

    public boolean isGenreVisible() {
        return genreVisible;
    }

    public void setGenreVisible(boolean genreVisible) {
        this.genreVisible = genreVisible;
    }

    public boolean isCountryVisible() {
        return countryVisible;
    }

    public void setCountryVisible(boolean countryVisible) {
        this.countryVisible = countryVisible;
    }

    public boolean isProgramEnable() {
        return programEnable;
    }

    public void setProgramEnable(boolean programEnable) {
        this.programEnable = programEnable;
    }

    public boolean isLoginMandatory() {
        return loginMandatory;
    }

    public void setLoginMandatory(boolean loginMandatory) {
        this.loginMandatory = loginMandatory;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }



}
