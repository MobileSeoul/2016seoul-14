package com.seoulapp.ssg.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.model.SsacTip;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.adapter.SsacTipDetailPagerAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SsacTipActivity extends AppCompatActivity {

    int tid, totalNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssac_tip);
        Intent i = getIntent();
        if (i.hasExtra("tid")) {
            tid = i.getExtras().getInt("tid");
        }

        final ViewPager mPager = (ViewPager) findViewById(R.id.viewpager);
        final SsacTipDetailPagerAdapter mAdapter = new SsacTipDetailPagerAdapter(this);
        final TextView tvPageNumber = (TextView) findViewById(R.id.tv_page_number);
        mPager.setAdapter(mAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPageNumber.setText(position + 1 + " / " + totalNumber);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
        service.getSsacTipDetail(tid).enqueue(new Callback<SsacTip>() {
            @Override
            public void onResponse(Call<SsacTip> call, Response<SsacTip> response) {
                if (response.isSuccessful()) {
                    if (response.body().code == 200) {
                        totalNumber = response.body().tipDetail.size();
                        tvPageNumber.setText(1 + " / " + totalNumber);
                        mAdapter.addItems(response.body().tipDetail);
                    }
                }
            }

            @Override
            public void onFailure(Call<SsacTip> call, Throwable t) {

            }
        });
    }
}
