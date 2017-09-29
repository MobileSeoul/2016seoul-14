package com.seoulapp.ssg;

/**
 * Created by win7-64 on 2016-10-09.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.kakao.auth.KakaoSDK;
import com.seoulapp.ssg.ui.adapter.KakaoSDKAdapter;
import com.tsengvn.typekit.Typekit;

/**
 * 이미지를 캐시를 앱 수준에서 관리하기 위한 애플리케이션 객체이다.
 * 로그인 기반 샘플앱에서 사용한다.
 *
 * @author MJ
 */
public class SsgApplication extends Application {
    private static volatile SsgApplication instance = null;
    private static volatile Activity currentActivity = null;
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = this;
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this,"SeoulNamsanL.otf"))
                .addCustom1(Typekit.createFromAsset(this, "SeoulHangangM.otf"))
                .addCustom2(Typekit.createFromAsset(this, "SeoulNamsanL.otf"))
                .addCustom3(Typekit.createFromAsset(this, "SeoulNamsanM.otf"))
                .addCustom4(Typekit.createFromAsset(this, "SeoulNamsanEB.otf"))
                .addCustom5(Typekit.createFromAsset(this, "SeoulHangangL.otf"));

        KakaoSDK.init(new KakaoSDKAdapter());

    }



    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        SsgApplication.currentActivity = currentActivity;
    }

    public static Context getContext() {
        return mContext;
    }


    /**
     * singleton 애플리케이션 객체를 얻는다.
     *
     * @return singleton 애플리케이션 객체
     */
    public static SsgApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

}
