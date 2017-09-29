package com.seoulapp.ssg.ui.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.UserService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.User;
import com.seoulapp.ssg.network.ServiceGenerator;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by win7-64 on 2016-10-24.
 */

public class ProfileChActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = ProfileChActivity.class.getSimpleName();

    TextView tvUserName;
    ImageView ivProfile;
    EditText et_user_email;
    EditText et_key;

    Button btn_email_send;
    Button btn_email_check;
    Button btn_profile_ok;
    Button btn_profile_cancel;

    User user;
    UserService service;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ch);

        ivProfile = (ImageView) findViewById(R.id.iv_profile_ch);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        et_user_email = (EditText) findViewById(R.id.et_user_email);
        et_key = (EditText) findViewById(R.id.et_key);

        btn_email_send = (Button) findViewById(R.id.btn_email_send);
        btn_email_check = (Button) findViewById(R.id.btn_email_check);
        btn_profile_ok = (Button) findViewById(R.id.btn_profile_ok);
        btn_profile_cancel = (Button) findViewById(R.id.btn_profile_cancel);

        btn_email_send.setOnClickListener(this);
        btn_email_check.setOnClickListener(this);
        btn_profile_ok.setOnClickListener(this);
        btn_profile_cancel.setOnClickListener(this);

        service = ServiceGenerator.getInstance().createService(UserService.class);
        Call<User> call = service.getMyProfile(PropertyManager.getInstance().getUserId());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    tvUserName.setText(response.body().getNickname());
                    et_user_email.setHint(response.body().getUser_email());
                    user = response.body();
                    Glide.with(ProfileChActivity.this)
                            .load(response.body().getProfile())
                            .bitmapTransform(new CropCircleTransformation(ProfileChActivity.this))
                            .placeholder(R.drawable.ic_profile_none)
                            .into(ivProfile);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_email_send:
                onSendEmail();
                break;
            case R.id.btn_email_check:
                onCheckingEmail();
                break;
            case R.id.btn_profile_ok:
                finish();
                break;
            case R.id.btn_profile_cancel:
                finish();
                break;
        }
    }

    private void onCheckingEmail() {
        String changed_email = et_user_email.getText().toString();
        String auth_key = et_key.getText().toString();

        Call<User> call = service.changeEmail(PropertyManager.getInstance().getUserId(), changed_email, auth_key);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 700) {
                        final Dialog dialog_info = new Dialog(ProfileChActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.dialog_profile_change, null);
                        TextView tv_profile_changed = (TextView) view.findViewById(R.id.tv_profile_changed);
                        Button btn_info_ok = (Button) view.findViewById(R.id.btn_profile_changed);

                        tv_profile_changed.setText("인증번호가 확인되었습니다.");

                        dialog_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_info.setContentView(view);
                        btn_info_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_info.dismiss();
                            }
                        });
                        dialog_info.show();

                    } else if (response.body().getCode() == 801) {
                        final Dialog dialog_info = new Dialog(ProfileChActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.dialog_profile_change, null);
                        TextView tv_profile_changed = (TextView) view.findViewById(R.id.tv_profile_changed);
                        Button btn_info_ok = (Button) view.findViewById(R.id.btn_profile_changed);

                        tv_profile_changed.setText("인증번호를 다시 입력해주세요");

                        dialog_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog_info.setContentView(view);
                        btn_info_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_info.dismiss();
                            }
                        });
                        dialog_info.show();
                    } else if (response.body().getCode() == 701) {
                        final Dialog dialog_info = new Dialog(ProfileChActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.dialog_profile_change, null);
                        TextView tv_profile_changed = (TextView) view.findViewById(R.id.tv_profile_changed);
                        Button btn_info_ok = (Button) view.findViewById(R.id.btn_profile_changed);

                        tv_profile_changed.setText("이메일 변경에 실패하였습니다");

                        dialog_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_info.setContentView(view);

                        btn_info_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_info.dismiss();
                            }
                        });
                        dialog_info.show();
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    /*
        public void onChangedEmail() {

        }
    */
    public void onSendEmail() {
        int user_id = user.getUser_num();
        String changed_email = et_user_email.getText().toString();
        Log.d(TAG, "email 전송, 네트워크전:" + user_id + " " + changed_email);

        Call<User> call = service.sendEmail(PropertyManager.getInstance().getUserId(), changed_email);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 700) {
                        Log.d(TAG, "onSucces: " + "700, 다이얼로그를 만듦");
                        final Dialog dialog_info = new Dialog(ProfileChActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.dialog_profile_change, null);
                        //TextView tv_profile_changed = (TextView) view.findViewById(R.id.tv_profile_changed);
                        Button btn_info_ok = (Button) view.findViewById(R.id.btn_profile_changed);

                        dialog_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_info.setContentView(view);

                        btn_info_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_info.dismiss();
                            }
                        });
                        dialog_info.show();

                    } else {
                        final Dialog dialog_info = new Dialog(ProfileChActivity.this);
                        View view = getLayoutInflater().inflate(R.layout.dialog_profile_change, null);
                        TextView tv_profile_changed = (TextView) view.findViewById(R.id.tv_profile_changed);
                        Button btn_info_ok = (Button) view.findViewById(R.id.btn_profile_changed);

                        tv_profile_changed.setText("이메일을 다시 입력해주세요");
                        dialog_info.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog_info.setContentView(view);

                        btn_info_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_info.dismiss();
                            }
                        });
                        dialog_info.show();
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
