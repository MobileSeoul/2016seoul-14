package com.seoulapp.ssg.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seoulapp.ssg.R;

public class PersonalInfoActivity extends BaseActivity {
    private static final String TAG = PersonalInfoActivity.class.getSimpleName();

    private TextView content;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        Intent intent = getIntent();

        content = (TextView) findViewById(R.id.txt_content);
        confirm = (Button) findViewById(R.id.btn_confirm);

        content.setText(Html.fromHtml(getResources().getString(R.string.personal_info))); // 내용 나오면 바꾸기!
        //flag 부분이 이해가 잘 안됨. 24api 이상부터는 fromhtml(String) 대체

        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
