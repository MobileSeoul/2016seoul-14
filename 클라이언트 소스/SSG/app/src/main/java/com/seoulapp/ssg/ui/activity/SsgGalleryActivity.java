package com.seoulapp.ssg.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.SsgModel;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.adapter.SsgGalleryRecyclerAdapter;
import com.seoulapp.ssg.util.DividerItemDecoration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SsgGalleryActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = SsgGalleryActivity.class.getSimpleName();
    boolean isLast = false;
    private SsgGalleryRecyclerAdapter mAdapter;
    private int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssg_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new SsgGalleryRecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);


        SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);

        Call<SsgModel> call = service.getSsgGallery(pageNumber, PropertyManager.getInstance().getUserId());

        call.enqueue(new Callback<SsgModel>() {
            @Override
            public void onResponse(Call<SsgModel> call, Response<SsgModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().code == 200) {
                        mAdapter.addItems(response.body().ssgs);
                        if (response.body().last) {
                            isLast = response.body().last;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SsgModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
