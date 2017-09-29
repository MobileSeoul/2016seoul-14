package com.seoulapp.ssg.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by win7-64 on 2016-09-10.
 */
public class Model {
    private int code;

    private String msg;

    @SerializedName("tip_list")
    public ArrayList<SsacTip> ssacTips;


    @SerializedName(value = "ssacs", alternate = {"volunteer_list", "my_sak"})
    public ArrayList<Ssac> ssacs;
    public User user;
/*
    @SerializedName("my_ssg")
    public ArrayList<Ssg> my_ssg_history;
*/
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}

