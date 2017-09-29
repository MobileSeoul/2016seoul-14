package com.seoulapp.ssg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.model.Ssg;
import com.seoulapp.ssg.ui.adapter.basic.BasicRecyclerAdapter;
import com.seoulapp.ssg.ui.adapter.viewholder.BasicViewHolder;
import com.seoulapp.ssg.widget.SquareImageView;

import java.util.List;


public class SsgHistoryRecyclerAdapter extends BasicRecyclerAdapter {

    public SsgHistoryRecyclerAdapter(Context context) {
        super(context);
    }

    public SsgHistoryRecyclerAdapter(Context context, List items) {
        super(context, items);
    }

    public void removeItem(int position) {
        if (getItems() != null) {
            getItems().remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public BasicViewHolder getViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();
        return new ViewHolder(inflater.inflate(R.layout.item_ssg_history, parent, false));
    }

    public class ViewHolder extends BasicViewHolder<Ssg> {
        SquareImageView cv_ssghistory_image;


        public ViewHolder(View itemView) {
            super(itemView);
            cv_ssghistory_image = (SquareImageView) itemView.findViewById(R.id.cv_ssghistory_image);
        }

        @Override
        public void onBindView(Ssg ssg) {
            Glide.with(getContext())
                    .load(ssg.picture)
                    .centerCrop()
                    .into(cv_ssghistory_image);
        }

    }
}

