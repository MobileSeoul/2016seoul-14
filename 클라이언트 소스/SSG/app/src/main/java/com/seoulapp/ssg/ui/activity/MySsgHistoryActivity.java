package com.seoulapp.ssg.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.listener.RecyclerItemClickListener;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.Ssg;
import com.seoulapp.ssg.model.SsgModel;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.adapter.SsgHistoryRecyclerAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySsgHistoryActivity extends AppCompatActivity {
    private static final String TAG = MySsgHistoryActivity.class.getSimpleName();
    public static final int REQUEST_CODE_MANAGE_ITEM = 2;

    private SsgHistoryRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssg_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcv_ssghistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        mAdapter = new SsgHistoryRecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);
        SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
        Call<SsgModel> call = service.getMySsgHistory(PropertyManager.getInstance().getUserId());
        call.enqueue(new Callback<SsgModel>() {
            @Override
            public void onResponse(Call<SsgModel> call, Response<SsgModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().code == 200) {
                        mAdapter.addItems(response.body().ssgs);
                    }
                }
            }

            @Override
            public void onFailure(Call<SsgModel> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MySsgHistoryActivity.this, MySsgDetailActivity.class);
                Ssg ssg = (Ssg) mAdapter.getItem(position);
                intent.putExtra("ssg", ssg);
                startActivityForResult(intent, REQUEST_CODE_MANAGE_ITEM);
            }
        }));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MANAGE_ITEM
                && resultCode == Activity.RESULT_OK
                && data != null) {

            Ssg item = data.getParcelableExtra("item");
            handleSsgChanged(item);
        }
    }

    private void handleSsgChanged(Ssg item) {
        int max = mAdapter.getItems().size();
        for (int i = 0; i < max; i++) {
            Ssg ssg = (Ssg) mAdapter.getItem(i);
            if (ssg.ssgId == item.ssgId) {

                if (item.isDelete()) {

                    mAdapter.removeItem(i);
                    return;
                }
                break;
            }
        }
    }
}
