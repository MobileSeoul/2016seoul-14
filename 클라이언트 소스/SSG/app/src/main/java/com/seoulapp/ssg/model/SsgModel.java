package com.seoulapp.ssg.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dongyoon on 2016. 10. 19..
 */

public class SsgModel {

    public int code;
    public String msg;
    public boolean last;

    @SerializedName(value = "ssgs", alternate = {"my_ssg", "gallery_list"})
    public ArrayList<Ssg> ssgs;
}
