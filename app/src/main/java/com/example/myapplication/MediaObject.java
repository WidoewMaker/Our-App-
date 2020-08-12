package com.example.myapplication;

public class MediaObject
{
    private String vName, vImgUrL;

    public MediaObject() {

    }

    public MediaObject(String vName, String vImgUrL)
    {
        if (vName.trim().equals(""))
        {
            vName="No Description";
        }
        this.vName = vName;
        this.vImgUrL = vImgUrL;
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
}
