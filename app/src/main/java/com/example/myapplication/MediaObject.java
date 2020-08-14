package com.example.myapplication;

public class MediaObject {
    private String vName, vImgUrL;



    MediaObject(String vName, String vImgUrL) {
        if (vName.trim().equals("")) {
            vName = "No Description";
        }
        this.vName = vName;
        this.vImgUrL = vImgUrL;

    }

    String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    String getvImgUrL() {
        return vImgUrL;
    }

    public void setvImgUrL(String vImgUrL) {
        this.vImgUrL = vImgUrL;
    }


    }



