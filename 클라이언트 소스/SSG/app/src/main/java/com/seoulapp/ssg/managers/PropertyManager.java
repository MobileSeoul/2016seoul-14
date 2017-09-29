package com.seoulapp.ssg.managers;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.seoulapp.ssg.SsgApplication;

/**
 * Created by Dongyoon on 15. 6. 25..
 */
public class PropertyManager {
    private static PropertyManager instance;

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(SsgApplication.getContext());
        mEditor = mPrefs.edit();
    }

    // GCM REGISTRATION ID
    private static final String FIELD_REG_ID = "regId";

    public void setRegistrationId(String regId) {
        mEditor.putString(FIELD_REG_ID, regId);
        mEditor.commit();
    }

    public String getRegistrationId() {
        return mPrefs.getString(FIELD_REG_ID, "");
    }

    // ID
    private static final String USER_ID = "userId";

    public void setUserId(String userId) {
        mEditor.putString(USER_ID, userId);
        mEditor.commit();
    }

    public String getUserId() {
        return mPrefs.getString(USER_ID, "");
    }

    // NICKNAME
    private static final String USER_NICKNAME = "userNickname";

    public void setUserNickname(String userNickname) {
        mEditor.putString(USER_NICKNAME, userNickname);
        mEditor.commit();
    }

    public String getUserNickname() {
        return mPrefs.getString(USER_NICKNAME, "");
    }

    // PROFILE PHOTO
    private static final String USER_PHOTO = "userPhoto";

    public void setUserPhoto(String userPhoto) {
        mEditor.putString(USER_PHOTO, userPhoto);
        mEditor.commit();
    }

    public String getUserPhoto() {
        return mPrefs.getString(USER_PHOTO, "");
    }

    // LOGIN CHECK 1 TIME OVER
    private static final String LOGIN_FLAG = "login";


    public void setLoginFlag(boolean isLogin) {
        mEditor.putBoolean(LOGIN_FLAG, isLogin);
        mEditor.commit();
    }

    public boolean getLoginFlag() {
        return mPrefs.getBoolean(LOGIN_FLAG, false);
    }

    private static final String REGISTRAION_COMPLETE = "registrationComplete";

    public void setRegistComplete(boolean isRegist){
        mEditor.putBoolean(REGISTRAION_COMPLETE, isRegist);
        mEditor.commit();
    }

    public boolean getRegistFlag(){
        return mPrefs.getBoolean(REGISTRAION_COMPLETE, false);
    }
}
