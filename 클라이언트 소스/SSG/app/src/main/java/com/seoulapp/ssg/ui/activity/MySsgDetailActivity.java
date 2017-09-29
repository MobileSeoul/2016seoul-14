package com.seoulapp.ssg.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.Model;
import com.seoulapp.ssg.model.Ssg;
import com.seoulapp.ssg.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySsgDetailActivity extends BaseActivity implements View.OnClickListener {

    ImageView ivSsgPicture;
    TextView tvSpotDetail, tvSsgDate, tvSsgComment;
    Button btnRemove;

    Ssg ssg;


    private boolean isUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ssg_detail);
        Intent i = getIntent();
        if (i.hasExtra("ssg")) {
            ssg = i.getParcelableExtra("ssg");
        }
        ivSsgPicture = (ImageView) findViewById(R.id.iv_picture);
        tvSpotDetail = (TextView) findViewById(R.id.tv_spot_detail);
        tvSsgDate = (TextView) findViewById(R.id.tv_ssg_date);
        tvSsgComment = (TextView) findViewById(R.id.tv_ssg_comment);
        btnRemove = (Button) findViewById(R.id.btn_remove);
//        btnAdjust = (Button) findViewById(R.id.btn_adjust);
        btnRemove.setOnClickListener(this);
//        btnAdjust.setOnClickListener(this);

        tvSpotDetail.setText(ssg.spotDetail);
        tvSsgDate.setText(ssg.date);
        tvSsgComment.setText(ssg.comment);

        Glide
                .with(this)
                .load(ssg.picture)
                .fitCenter()
                .into(ivSsgPicture);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
//            case R.id.btn_adjust:
//                Intent i = new Intent(MySsgDetailActivity.this, ReportActivity.class);
//                i.putExtra("ssg", ssg);
//                startActivity(i);
//                finish();
//                break;
            case R.id.btn_remove:
                AlertDialog dialog = createDeleteDialog();
                dialog.show();
                break;
        }
    }

    private AlertDialog createDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("쓱 삭제");
        builder.setMessage("주의하세요. 쓱을 삭제하면 실행취소할 수 없습니다.");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int arg1) {

                SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
                Call<Model> call = service.deleteSsg(ssg.ssgId, PropertyManager.getInstance().getUserId());
                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getCode() == 600) { // 삭제 성공
                                Toast.makeText(MySsgDetailActivity.this, "삭제되었습니다. ", Toast.LENGTH_SHORT).show();
                                ssg.setIsDelete((byte) 0);
                                finish();

                            } else if (response.body().getCode() == 404) {
                                Toast.makeText(MySsgDetailActivity.this, "이미 삭제된 게시물 입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {

                    }
                });
                isUpdated = true;
                ssg.setIsDelete((byte) 1);
                finish();

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

    @Override
    public void finish() {
        if (isUpdated) {
            Intent intent = new Intent();
            intent.putExtra("item", ssg);
            setResult(RESULT_OK, intent);
        }
        super.finish();
    }
}
