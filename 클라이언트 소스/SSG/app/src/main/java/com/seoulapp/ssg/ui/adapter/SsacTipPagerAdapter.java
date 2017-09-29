package com.seoulapp.ssg.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.model.SsacTip;

import java.util.ArrayList;

/**
 * Created by Dongyoon on 2016. 10. 1..
 */

public class SsacTipPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<SsacTip> items;

    public interface OnItemClickListener {
        void onItemClick(SsacTip item);
    }

    private OnItemClickListener mCallback;

    public void setOnItemClickListener(OnItemClickListener callback) {
        mCallback = callback;
    }

    public SsacTipPagerAdapter(Context context) {
        this.context = context;
    }

    public void addItems(ArrayList<SsacTip> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_ssac_tip, container, false);
        final SsacTip tip = items.get(position);
        TextView tv_tip_subject = (TextView) itemView.findViewById(R.id.tv_tip_subject);
        TextView tv_tip_title = (TextView) itemView.findViewById(R.id.tv_tip_title);
        TextView tv_tip_subtitle = (TextView) itemView.findViewById(R.id.tv_tip_subtitle);
        ImageView iv_tip_thumbnail = (ImageView) itemView.findViewById(R.id.iv_tip_thumbnail);
        tv_tip_subject.setText(tip.category);
        tv_tip_title.setText(tip.title);
        tv_tip_subtitle.setText(tip.subTitle);
        Glide.with(context).load(tip.thumbnail).into(iv_tip_thumbnail);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.onItemClick(tip);
                }
            }
        });


        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
