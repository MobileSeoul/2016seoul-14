package com.seoulapp.ssg.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Boram Moon on 2016-10-05.
 */
public class User {

    private int code;

    private String msg;

    @SerializedName("uname")
    private String name;

    @SerializedName("uphone")
    private String phone_num;

    @SerializedName("apply")
    private int joinCheck;

    @SerializedName("uid")
    private int user_num;

    @SerializedName("email")
    private String user_email;

    private String nickname;

    private String profile;

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


    public User() {
    }

    public void setUser_num(int user_num) {
        this.user_num = user_num;
    }

    public void setJoinCheck(int joinCheck) {
        this.joinCheck = joinCheck;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public int getUser_num() {
        return user_num;
    }

    public int getJoinCheck() {
        return joinCheck;
    }

    public String getName() {
        return name;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
