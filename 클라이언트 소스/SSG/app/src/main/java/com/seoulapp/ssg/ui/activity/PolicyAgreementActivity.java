package com.seoulapp.ssg.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seoulapp.ssg.R;

public class PolicyAgreementActivity extends AppCompatActivity {

    private TextView content;
    private Button confirm;
    private int check;


    public static final int POLICY_AGREEMENT = 0;
    public static final int PERSONAL_INFO= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        Intent intent = getIntent();
        String source;

        if(intent.hasExtra("agreement")){ // 인텐트에 값이 있는지 없는 체크
            check = intent.getExtras().getInt("agreement");
        } else {
            // 없을 때 처리
        }

        if(check == POLICY_AGREEMENT) {
            source = String.valueOf(Html.fromHtml(getResources().getString(R.string.policy_agreement)));

        } else {
            source = String.valueOf(Html.fromHtml(getResources().getString(R.string.personal_info)));
        }

        content = (TextView) findViewById(R.id.txt_content);
        confirm = (Button) findViewById(R.id.btn_confirm);
        content.setText(source);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
