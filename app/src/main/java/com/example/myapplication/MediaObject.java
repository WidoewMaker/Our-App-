package com.example.myapplication;

public class MediaObject {
    private String vName, vImgUrL, proImgUrl;

    public MediaObject() {

    }

    public MediaObject(String vName, String vImgUrL) {
        if (vName.trim().equals("")) {
            vName = "No Description";
        }
        this.vName = vName;
        this.vImgUrL = vImgUrL;
        this.proImgUrl = proImgUrl;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getvImgUrL() {
        return vImgUrL;
    }

    public void setvImgUrL(String vImgUrL) {
        this.vImgUrL = vImgUrL;
    }

    public String getProImgUrl() {
        return proImgUrl;
    }

    public void setProImgUrl(String proImgUrl) {
        this.proImgUrl = proImgUrl;
    }
}
