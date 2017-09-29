package com.seoulapp.ssg.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.Model;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.adapter.SsacHistoryRecyclerAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySsacHistoryActivity extends BaseActivity {
    private SsacHistoryRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ssac_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new SsacHistoryRecyclerAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);


        SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
        service.getMySsacHistory(PropertyManager.getInstance().getUserId()).enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode() == 200) {
                        mAdapter.addItems(response.body().ssacs);
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

            }
        });
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
