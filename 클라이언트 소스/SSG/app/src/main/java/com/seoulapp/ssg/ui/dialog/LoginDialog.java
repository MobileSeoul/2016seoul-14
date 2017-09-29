package com.seoulapp.ssg.ui.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.UserService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.User;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.activity.MainActivity;
import com.seoulapp.ssg.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by win7-64 on 2016-10-15.
 */
public class LoginDialog extends DialogFragment {
    private static final String TAG = LoginDialog.class.getSimpleName();

    String mName, mProfile, mEmail, mSocialId, mJoinType, mAccessToken;
    EditText editName, editEmail;
    Button btnLogin;

    public interface OnDismissListener{
        void onDismiss();
    }

    private OnDismissListener mCallback;

    public void setOnDismissListener(OnDismissListener callback){
        mCallback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSocialId = bundle.getString("social_id");
            mName = bundle.getString("name");
            mProfile = bundle.getString("profile");
            mEmail = bundle.getString("email");
            mJoinType = bundle.getString("join_type");
            mAccessToken = bundle.getString("token");
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() == null)
            return;

        int dialogWidth = Utils.getScreenWidth(getActivity()) - (int) getResources().getDimension(R.dimen.login_dialog_horizontal_margin);
        int dialogHeight = Utils.getScreenHeight(getActivity()) - (int) getResources().getDimension(R.dimen.login_dialog_vertical_margin);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_login, container, false);


        ImageView ivProfile;

        btnLogin = (Button) v.findViewById(R.id.edit_login);
        editName = (EditText) v.findViewById(R.id.edit_name);
        editName.setText(mName);
        editEmail = (EditText) v.findViewById(R.id.edit_email);
        editEmail.setText(mEmail);
        ivProfile = (ImageView) v.findViewById(R.id.profile_picture);

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() > 0) {
                    mName = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.length() > 0) {
                    mEmail = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Glide.with(this).load(mProfile).into(ivProfile);

        Button SignupButton = (Button) v.findViewById(R.id.edit_login);
        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "name : " + mName + " email : " + mEmail + "socialid + " + mSocialId);
                signIn();
            }
        });

        return v;
    }


    private void signIn() {
        UserService service = ServiceGenerator.getInstance().createService(UserService.class);

        if (mName == null || TextUtils.isEmpty(mName) || mEmail == null || TextUtils.isEmpty(mEmail)) {
            Toast.makeText(getActivity(), "이름과 이메일은 필수 입력 사항입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<User> call = service.signUp(mSocialId, mEmail, mName, mProfile, mJoinType, mAccessToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) { // 회원가입 성공 or 인증된 유저
                        Log.e(TAG, "onResponse: success");
                        User user = response.body();
                        PropertyManager.getInstance().setUserId(String.valueOf(user.getUser_num()));
                        PropertyManager.getInstance().setUserNickname(user.getNickname());
                        PropertyManager.getInstance().setLoginFlag(true);
                        PropertyManager.getInstance().setUserPhoto(mProfile);

                        redirectMainActivity();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        dismiss();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mCallback != null){
            mCallback.onDismiss();
        }
    }
}


