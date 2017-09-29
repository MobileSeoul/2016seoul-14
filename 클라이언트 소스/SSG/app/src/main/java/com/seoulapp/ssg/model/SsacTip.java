package com.seoulapp.ssg.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Dongyoon on 2016. 10. 1..
 */
public class SsacTip implements Parcelable {
    public int code;
    public String msg;

    public int tid;
    public String title;
    @SerializedName("subtitle")
    public String subTitle;
    public String category;
    public String thumbnail;

    @SerializedName("tip_detail")
    public ArrayList<SsacTipContents> tipDetail;

    public SsacTip() {
    }

    public SsacTip(Parcel source) {
        title = source.readString();
        subTitle = source.readString();
        category = source.readString();
        thumbnail = source.readString();
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(category);
        dest.writeString(thumbnail);

    }

    public static Creator<SsacTip> CREATOR = new Creator<SsacTip>() {
        @Override
        public SsacTip createFromParcel(Parcel source) {
            return new SsacTip(source);
        }

        @Override
        public SsacTip[] newArray(int size) {
            return new SsacTip[size];
        }
    };
}
