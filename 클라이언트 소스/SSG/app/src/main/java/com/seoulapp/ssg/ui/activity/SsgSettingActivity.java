package com.seoulapp.ssg.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.seoulapp.ssg.R;

/**
 * Created by win7-64 on 2016-10-22.
 */

public class SsgSettingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SsgSettingActivity.class.getSimpleName();


    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.activity_setting);
        //이름 바꿈 profileButton > btn_profile_change
        Button btn_profile_change = (Button) findViewById(R.id.btn_profile_change);
        Button btn_policy_agreement = (Button) findViewById(R.id.btn_policy_agreement);
        Button btn_personal_info = (Button) findViewById(R.id.btn_personal_info);
        btn_profile_change.setOnClickListener(this);
        btn_policy_agreement.setOnClickListener(this);
        btn_personal_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        switch (id) {
            case R.id.btn_profile_change:
                intent = new Intent(SsgSettingActivity.this, ProfileChActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_policy_agreement:
                intent = new Intent(SsgSettingActivity.this, PolicyAgreementActivity.class);
                intent.putExtra("agreement", PolicyAgreementActivity.POLICY_AGREEMENT);
                startActivity(intent);
                break;

            case R.id.btn_personal_info:
                intent = new Intent(SsgSettingActivity.this, PolicyAgreementActivity.class);
                intent.putExtra("agreement", PolicyAgreementActivity.PERSONAL_INFO);
                startActivity(intent);
                break;
        }

    }
}


