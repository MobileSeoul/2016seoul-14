package com.seoulapp.ssg.ui.activity;

/**
 * Created by win7-64 on 2016-10-09.
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.ui.dialog.LoginDialog;
import com.seoulapp.ssg.widget.CustomLoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginDialog.OnDismissListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private CallbackManager mCallbackManager;
    private SessionCallback kakaoCallback;
    private TextView agreement;
    private ClickableSpan clickSpan, clickSpan2;
    private LoginDialog loginDialog;
    private CustomLoginButton btnFacebook, btnCustomKakaoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.)
        mCallbackManager = CallbackManager.Factory.create();  // 페이스북 로그인 응답을 처리할 콜백 관리자

        LoginManager.getInstance().registerCallback(mCallbackManager, mCallback);
        setContentView(R.layout.activity_login);

        if (PropertyManager.getInstance().getLoginFlag()) {
            redirectMainActivity();
        }

        btnFacebook = (CustomLoginButton) findViewById(R.id.btn_facebook_login);
        btnFacebook.setOnClickListener(this);
        btnCustomKakaoLogin = (CustomLoginButton) findViewById(R.id.btn_kakao_login);
        btnCustomKakaoLogin.setOnClickListener(this);

        kakaoCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(kakaoCallback);
        clickSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Intent i = new Intent(LoginActivity.this, PolicyAgreementActivity.class);
                i.putExtra("agreement", PolicyAgreementActivity.POLICY_AGREEMENT);
                startActivity(i);
            }
        };

        clickSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent i = new Intent(LoginActivity.this, PolicyAgreementActivity.class);
                i.putExtra("agreement", PolicyAgreementActivity.PERSONAL_INFO);
                startActivity(i);
            }
        };

        SpannableStringBuilder sp = new SpannableStringBuilder(
                "회원가입을 함으로써 쓱싹의 이용약관 및 개인정보 취급방침에 동의합니다.");

        sp.setSpan(clickSpan, 15, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(clickSpan2, 22, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        agreement = (TextView) findViewById(R.id.tvLinkify);
        agreement.setMovementMethod(LinkMovementMethod.getInstance());
        agreement.setText(sp);


    }

    private void redirectMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e(TAG, "onSessionClosed: " + errorResult.getErrorMessage());
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    Bundle bundle = new Bundle();
                    String mName = userProfile.getNickname();
                    String mProfile = userProfile.getThumbnailImagePath();
                    String mKakaoId = String.valueOf(userProfile.getId());
                    bundle.putString("social_id", mKakaoId);
                    bundle.putString("name", mName);
                    bundle.putString("profile", mProfile);
                    bundle.putString("join_type", "K");

                    LoginDialog loginDialog = new LoginDialog();
                    loginDialog.setOnDismissListener(LoginActivity.this);
                    loginDialog.setArguments(bundle);
                    loginDialog.show(getSupportFragmentManager(), null);
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_facebook_login:
                openFacebookSession();
                break;
            case R.id.btn_kakao_login:
                Session.getCurrentSession().open(AuthType.KAKAO_TALK, this);
                break;
        }
    }

    private void openFacebookSession() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {

            Log.e(TAG, "onSuccess: accessToken = " + loginResult.getAccessToken().getToken());
            final String mProfile = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=normal";

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    if (response.getError() != null) {
                        //handle error
                        Log.e(TAG, "onCompleted: geError " + response.getError().toString());
                    } else {
                        Bundle bundle = new Bundle();
                        String mEmail = object.optString("email");
                        String mFacebookid = object.optString("id");
                        String mName = object.optString("name");
                        String mToken = loginResult.getAccessToken().getToken();

                        bundle.putString("social_id", mFacebookid);
                        bundle.putString("profile", mProfile);
                        bundle.putString("name", mName);
                        bundle.putString("email", mEmail);
                        bundle.putString("join_type", "F");
                        bundle.putString("token", mToken);


                        loginDialog = new LoginDialog();
                        loginDialog.setOnDismissListener(LoginActivity.this);
                        loginDialog.setArguments(bundle);
                        loginDialog.show(getSupportFragmentManager(), null);
                    }
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "name, email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    public void onDismiss() { //다이얼로그 닫혔을 때
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(kakaoCallback);
    }
}