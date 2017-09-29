package com.seoulapp.ssg.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.seoulapp.ssg.SsgApplication;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Dongyoon on 2016. 10. 23..
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        SsgApplication.setCurrentActivity(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
