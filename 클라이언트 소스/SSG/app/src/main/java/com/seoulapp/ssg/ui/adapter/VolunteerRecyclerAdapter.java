package com.seoulapp.ssg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.model.Ssac;
import com.seoulapp.ssg.ui.adapter.basic.BasicRecyclerAdapter;
import com.seoulapp.ssg.ui.adapter.viewholder.BasicViewHolder;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Dongyoon on 2016. 10. 3..
 */

public class VolunteerRecyclerAdapter extends BasicRecyclerAdapter<Ssac> {

    public VolunteerRecyclerAdapter(Context context) {
        super(context);
    }

    public VolunteerRecyclerAdapter(Context context, List<Ssac> items) {
        super(context, items);
    }

    @Override
    public BasicViewHolder getViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();

        return new VolunteerViewHolder(inflater.inflate(R.layout.item_ssac_mini, parent, false));
    }

    private class VolunteerViewHolder extends BasicViewHolder<Ssac> {
        TextView tvVolunteerName;
        ImageView ivVolunteerSpot;

        public VolunteerViewHolder(View itemView) {
            super(itemView);
            tvVolunteerName = (TextView) itemView.findViewById(R.id.tv_volunteer_name);
            ivVolunteerSpot = (ImageView) itemView.findViewById(R.id.iv_volunteer_spot);
        }

        @Override
        public void onBindView(Ssac ssac) {
            tvVolunteerName.setText(ssac.getSpot());
            Glide
                    .with(getContext())
                    .load(ssac.getThumbnail())
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(getContext()))
                    .into(ivVolunteerSpot);
        }
    }
}
