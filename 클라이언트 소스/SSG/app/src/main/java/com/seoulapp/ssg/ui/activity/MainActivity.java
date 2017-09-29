package com.seoulapp.ssg.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.listener.RecyclerItemClickListener;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.Model;
import com.seoulapp.ssg.model.Ssac;
import com.seoulapp.ssg.model.SsacTip;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.adapter.SsacTipPagerAdapter;
import com.seoulapp.ssg.ui.adapter.VolunteerRecyclerAdapter;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, SsacTipPagerAdapter.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager ssacPager;
    private CircleIndicator circleIndicator;
    private RecyclerView rcv_volunteer;

    private SsacTipPagerAdapter tipPagerAdapter;
    private VolunteerRecyclerAdapter volunteerRecyclerAdapter;
    String mName, mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ssacPager = (ViewPager) findViewById(R.id.vp_ssac_tip);
        circleIndicator = (CircleIndicator) findViewById(R.id.indicator);
        rcv_volunteer = (RecyclerView) findViewById(R.id.rcv_volunteer);
        rcv_volunteer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        volunteerRecyclerAdapter = new VolunteerRecyclerAdapter(this);
        rcv_volunteer.setAdapter(volunteerRecyclerAdapter);

        tipPagerAdapter = new SsacTipPagerAdapter(this);
        ssacPager.setAdapter(tipPagerAdapter);

        tipPagerAdapter.setOnItemClickListener(this);


        Button btnSsgReport = (Button) findViewById(R.id.btn_ssg_report);
        btnSsgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(i);
            }
        });
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };

            drawerLayout.addDrawerListener(actionBarDrawerToggle);

            actionBarDrawerToggle.syncState();
        }

        rcv_volunteer.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Ssac ssac = volunteerRecyclerAdapter.getItem(position);
                                Intent intent = new Intent(MainActivity.this, VolunteerActivity.class);
                                intent.putExtra("volunteerParcel", ssac);
                                startActivity(intent);
                            }
                        }
                )
        );

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);


        View nav_header_view = navigationView.getHeaderView(0);

        ImageView ivProfile = (ImageView) nav_header_view.findViewById(R.id.iv_profile_picture);
        Glide
                .with(this)
                .load(PropertyManager.getInstance().getUserPhoto())
                .placeholder(R.drawable.ic_profile_none)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivProfile);
        TextView tvProfile = (TextView) nav_header_view.findViewById(R.id.tv_user_name);
        tvProfile.setText(PropertyManager.getInstance().getUserNickname());

        getMainViewData();

    }

    private void getMainViewData() {
        SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
        Call<Model> call = service.getMainViewData();

        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {

                if (response.isSuccessful()) {
                    volunteerRecyclerAdapter.addItems(response.body().ssacs);
                    tipPagerAdapter.addItems(response.body().ssacTips);
                    circleIndicator.setViewPager(ssacPager);
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_ssg_gallery:
                intent = new Intent(MainActivity.this, SsgGalleryActivity.class);
                startActivity(intent);

                break;

            case R.id.nav_ssg_history:
                intent = new Intent(MainActivity.this, MySsgHistoryActivity.class);
                startActivity(intent);

                break;

            case R.id.nav_sak_history:
                intent = new Intent(MainActivity.this, MySsacHistoryActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_setting:
                intent = new Intent(MainActivity.this, SsgSettingActivity.class);
                intent.putExtra("profile_picture", mProfile);
                intent.putExtra("profile_name", mName);
                startActivity(intent);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onItemClick(SsacTip item) {
        Intent i = new Intent(this, SsacTipActivity.class);
        i.putExtra("tid", item.tid);
        startActivity(i);
    }
}