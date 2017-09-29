package com.seoulapp.ssg.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.VolunteerApiService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.Model;
import com.seoulapp.ssg.model.Ssac;
import com.seoulapp.ssg.model.User;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.adapter.VolunteerPagerAdapter;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Boram Moon on 2016-10-05.
 */
public class VolunteerActivity extends BaseActivity {

    private static final String TAG = VolunteerActivity.class.getSimpleName();

    private Button btn_volunteer_join;
    private ArrayAdapter<String> adapter;
    private ViewPager mPager;
    private VolunteerPagerAdapter v_pageAdapter;
    private CircleIndicator circleIndicator;
    private String name, phone;

    private String[] v_title = {"제목", "날짜", "봉사시간", "활동장소", "집합장소", "모집인원", "상세설명"};
    Ssac volParcel;
    User user;

    TextView tv_volunteer_total_content;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("");
        }

        Intent intent = getIntent();
        volParcel = intent.getParcelableExtra("volunteerParcel");
        user = new User();


        Log.d(TAG, "onCreate: volId " + volParcel.getVolunteerId());

        v_pageAdapter = new VolunteerPagerAdapter(VolunteerActivity.this);

        TextView tv_volunteer_title = (TextView) findViewById(R.id.tv_volunteer_title);
        TextView tv_volunteer_title_content = (TextView) findViewById(R.id.tv_volunteer_title_content);
        TextView tv_volunteer_date = (TextView) findViewById(R.id.tv_volunteer_date);
        TextView tv_volunteer_date_content = (TextView) findViewById(R.id.tv_volunteer_date_content);
        TextView tv_volunteer_schedule = (TextView) findViewById(R.id.tv_volunteer_schedule);
        TextView tv_volunteer_schedule_content = (TextView) findViewById(R.id.tv_volunteer_schedule_content);
        TextView tv_volunteer_place = (TextView) findViewById(R.id.tv_volunteer_place);
        TextView tv_volunteer_place_content = (TextView) findViewById(R.id.tv_volunteer_place_content);
        TextView tv_volunteer_meetingpoint = (TextView) findViewById(R.id.tv_volunteer_meetingpoint);
        TextView tv_volunteer_meetingpoint_content = (TextView) findViewById(R.id.tv_volunteer_meetingpoint_content);
        TextView tv_volunteer_total = (TextView) findViewById(R.id.tv_volunteer_total);
        tv_volunteer_total_content = (TextView) findViewById(R.id.tv_volunteer_total_content);
        TextView tv_volunteer_detail = (TextView) findViewById(R.id.tv_volunteer_detail);
        TextView tv_volunteer_detail_content = (TextView) findViewById(R.id.tv_volunteer_detail_content);

        btn_volunteer_join = (Button) findViewById(R.id.btn_volunteer_join);
        mPager = (ViewPager) findViewById(R.id.pager_volunteer);
        mPager.setAdapter(v_pageAdapter);

        tv_volunteer_title.append(v_title[0]);
        tv_volunteer_date.append(v_title[1]);
        tv_volunteer_schedule.append(v_title[2]);
        tv_volunteer_place.append(v_title[3]);
        tv_volunteer_meetingpoint.append(v_title[4]);
        tv_volunteer_total.append(v_title[5]);
        tv_volunteer_detail.append(v_title[6]);

        tv_volunteer_title_content.append(volParcel.getVolunteerTitle());
        tv_volunteer_date_content.append(volParcel.getSchedule());
        tv_volunteer_schedule_content.append(volParcel.getTime());
        tv_volunteer_place_content.append(volParcel.getSpot());
        tv_volunteer_meetingpoint_content.append(volParcel.getMeeting_location());
        Log.d(TAG, "onCreate: " + volParcel.getTotal_volunteer() + " / " + volParcel.getRecruitment());
        tv_volunteer_total_content.append(volParcel.getTotal_volunteer() + " / " + volParcel.getRecruitment());
        tv_volunteer_detail_content.append(volParcel.getDetail_info());

        btn_volunteer_join.setOnClickListener(new View.OnClickListener() {
                                                  @Override

                                                  public void onClick(View v) {
                                                      Log.d(TAG, "onClick: " + user.getJoinCheck());
                                                      if (user.getJoinCheck() == 1) {
                                                          createCancelSsacJoinDialog().show();
                                                      } else {
                                                          final Dialog dialog_check = new Dialog(VolunteerActivity.this);
                                                          dialog_check.setContentView(R.layout.join_agreement);
                                                          dialog_check.setTitle("주의사항");
                                                          // set the custom dialog components - text, image and button
                                                          TextView text = (TextView) dialog_check.findViewById(R.id.txt_dialog);
                                                          text.setText(getResources().getString(R.string.alert_ssac_join_msg));
                                                          Button dialogButton = (Button) dialog_check.findViewById(R.id.btn_dialog_OK);
                                                          // if button is clicked, close the custom dialog
                                                          dialogButton.setOnClickListener(new View.OnClickListener() {
                                                                                              @Override
                                                                                              public void onClick(View v) {
                                                                                                  // custom dialog
                                                                                                  dialog_check.dismiss();
                                                                                                  final Dialog dialog_info = new Dialog(VolunteerActivity.this);
                                                                                                  View view = getLayoutInflater().inflate(R.layout.join_info, null);
                                                                                                  final EditText edit_name = (EditText) view.findViewById(R.id.edit_name);
                                                                                                  final EditText edit_phone = (EditText) view.findViewById(R.id.edit_phone);
                                                                                                  final EditText edit_email = (EditText) view.findViewById(R.id.edit_email);

                                                                                                  Button btn_info_ok = (Button) view.findViewById(R.id.btn_join_OK);
                                                                                                  Button btn_info_cancel = (Button) view.findViewById(R.id.btn_join_Cancel);

                                                                                                  dialog_info.setContentView(view);
                                                                                                  dialog_info.setTitle("봉사활동 신청 양식");

                                                                                                  // set the custom dialog components - text, image and button

                                                                                                  edit_name.addTextChangedListener(new TextWatcher() {
                                                                                                      @Override
                                                                                                      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                                                                      }

                                                                                                      @Override
                                                                                                      public void onTextChanged(CharSequence s, int i, int i1, int count) {
                                                                                                          if (s.length() > 0) {
                                                                                                              name = s.toString();
                                                                                                          } else {
                                                                                                              name = "";
                                                                                                          }
                                                                                                      }

                                                                                                      @Override
                                                                                                      public void afterTextChanged(Editable editable) {

                                                                                                      }
                                                                                                  });

                                                                                                  edit_phone.addTextChangedListener(new TextWatcher() {
                                                                                                      @Override
                                                                                                      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                                                                      }

                                                                                                      @Override
                                                                                                      public void onTextChanged(CharSequence s, int i, int i1, int count) {
                                                                                                          if (s.length() > 0) {
                                                                                                              phone = s.toString();
                                                                                                          } else {
                                                                                                              phone = "";
                                                                                                          }
                                                                                                      }

                                                                                                      @Override
                                                                                                      public void afterTextChanged(Editable editable) {

                                                                                                      }
                                                                                                  });

                                                                                                  btn_info_ok.setOnClickListener(new View.OnClickListener() {


                                                                                                                                     @Override
                                                                                                                                     public void onClick(View v) {
                                                                                                                                         if (name == null || TextUtils.isEmpty(name) ||
                                                                                                                                                 phone == null || TextUtils.isEmpty(phone)) {
                                                                                                                                             Toast.makeText(VolunteerActivity.this, "이름과 연락처는 필수 입력사항압니다.", Toast.LENGTH_SHORT).show();
                                                                                                                                             return;
                                                                                                                                         } else {
                                                                                                                                             requestJoinSsac(name, phone);
                                                                                                                                             dialog_info.dismiss();
                                                                                                                                         }
                                                                                                                                     }
                                                                                                                                 }

                                                                                                  );

                                                                                                  btn_info_cancel.setOnClickListener(new View.OnClickListener() {
                                                                                                                                         @Override
                                                                                                                                         public void onClick(View v) {
                                                                                                                                             dialog_info.dismiss();
                                                                                                                                         }
                                                                                                                                     }

                                                                                                  );

                                                                                                  dialog_info.show();
                                                                                              }
                                                                                          }

                                                          );

                                                          dialog_check.show();

                                                      }


                                                  }
                                              }

        );

        VolunteerApiService service = ServiceGenerator.getInstance().createService(VolunteerApiService.class);
        Call<Ssac> call = service.getSsacPictures(volParcel.getVolunteerId(), PropertyManager.getInstance().getUserId());

        call.enqueue(new Callback<Ssac>() {
            @Override
            public void onResponse(Call<Ssac> call, Response<Ssac> response) {
                if (response.isSuccessful()) {
                    v_pageAdapter.additems(response.body().getPictures());
                    circleIndicator.setViewPager(mPager);

                    if (response.body().volunteer_apply == 1) {
                        user.setJoinCheck(1);
                        setJoinButton(true);
                    } else {
                        user.setJoinCheck(0);
                        setJoinButton(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Ssac> call, Throwable t) {

            }
        });
    }

    private void requestJoinSsac(String name, String phone) {
        VolunteerApiService service = ServiceGenerator.getInstance().createService(VolunteerApiService.class);
        Call<Model> call = service.joinVolunteer(volParcel.getVolunteerId(), PropertyManager.getInstance().getUserId(), name, phone);
        call.enqueue(new Callback<Model>() {
                         @Override
                         public void onResponse(Call<Model> call, Response<Model> response) {
                             if (response.isSuccessful()) {
                                 if (response.body().getCode() == 200) {
                                     volParcel.setTotal_volunteer(volParcel.getTotal_volunteer() + 1);
                                     user.setJoinCheck(1);
                                     setJoinButton(true);

                                 } else if (response.body().getCode() == 600) {
                                     volParcel.setTotal_volunteer(volParcel.getTotal_volunteer() - 1);
                                     user.setJoinCheck(0);
                                     setJoinButton(false);
                                 } else {
                                     Toast.makeText(VolunteerActivity.this, "신청 마감된 봉사활동 입니다.", Toast.LENGTH_SHORT).show();
                                 }
                             }
                         }


                         @Override
                         public void onFailure(Call<Model> call, Throwable t) {

                         }
                     }

        );

    }

    private AlertDialog createCancelSsacJoinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("봉사활동 참가 취소");
        builder.setMessage("참가 취소하시겠습니까?");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int arg1) {
                Log.d(TAG, "cancel dialog onClick: " + true);
                requestJoinSsac(name, phone);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // 삭제 취소
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    private void setJoinButton(boolean b) {
        if (b) {
            btn_volunteer_join.setText("봉사활동 신청 취소할래요");
            btn_volunteer_join.setBackgroundColor(getResources().getColor(R.color.btnChanged));
        } else {
            btn_volunteer_join.setText("봉사활동 참여할래요");
            btn_volunteer_join.setBackgroundColor(getResources().getColor(R.color.bntNormal));
        }

        setRequirementTextView();
    }

    private void setRequirementTextView() {
        tv_volunteer_total_content.setText(volParcel.getTotal_volunteer() + " / " + volParcel.getRecruitment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
