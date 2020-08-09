package com.example.myapplication.myapplication;

public class Grp_cous_adptr
{
    private String date;
    private String msg;
    private String name;
    private String time;

    public Grp_cous_adptr()
    {


    }

    public Grp_cous_adptr(String date,String msg,String name,String time) {
        this.date = date;
        this.msg = msg;
        this.name = name;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
