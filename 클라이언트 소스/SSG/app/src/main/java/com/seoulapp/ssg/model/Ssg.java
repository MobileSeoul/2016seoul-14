package com.seoulapp.ssg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dongyoon on 2016. 10. 19..
 */

public class Ssg implements Parcelable{
    @SerializedName("gid")
    public int ssgId;
    @SerializedName("pname")
    public String spotName;
    @SerializedName("detail_location")
    public String spotDetail;

    public String comment;
    @SerializedName("total_like")
    public int likeCount;
    @SerializedName("total_declare")
    public int declareCount;
    @SerializedName("create_date")
    public String date;

    public double lat;
    public double lng;
    public String picture;
    public User user;

    public Like like;

    public Declare declare;

    @SerializedName("declare_cnt")
    public byte isDeclare;

    @SerializedName("like_cnt")
    public byte isLike;

    private byte isDelete;

    public boolean isDelete() {
        return isDelete != 0;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }

    public Ssg(){
    }

    public Ssg(Parcel source){
        ssgId = source.readInt();
        picture = source.readString();
        spotName = source.readString();
        spotDetail = source.readString();
        comment = source.readString();
        lat = source.readDouble();
        lng = source.readDouble();
        date = source.readString();
        isDelete = source.readByte();

    }


    public class Like {
        public int like_cnt;
    }

    public class Declare {
        public int declare_cnt;
    }

    public boolean isDeclare() {
        return declare != null && declare.declare_cnt == 1 || isDeclare == 1;
    }

    public boolean isLike() {
        return like != null && like.like_cnt == 1 || isLike == 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ssgId);
        dest.writeString(picture);
        dest.writeString(spotName);
        dest.writeString(spotDetail);
        dest.writeString(comment);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(date);
        dest.writeByte(isDelete);
    }

    public static Creator<Ssg> CREATOR = new Creator<Ssg>() {
        @Override
        public Ssg createFromParcel(Parcel source) {
            return new Ssg(source);
        }

        @Override
        public Ssg[] newArray(int size) {
            return new Ssg[size];
        }
    };
}
/*
"gallery_list": [
        {
        "gid": 9,
        "picture": "http://52.78.115.250:9000/1/gyeongbok.jpg",
        "pname": "경복궁",
        "detail_location": "작은 기둥",
        "comment": "경복궁 낙서",
        "total_like": 0,
        "total_declare": 0,
        "create_date": "2016-09-06",
        "user": {
        "nickname": "Jay Park"
        }
        }
        */
