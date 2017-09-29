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
import com.seoulapp.ssg.model.SsacTipContents;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by Dongyoon on 2016. 10. 31..
 */

public class SsacTipDetailPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<SsacTipContents> items;

    public SsacTipDetailPagerAdapter(Context context) {
        mContext = context;
    }

    public void addItems(ArrayList<SsacTipContents> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.item_ssac_tip_detail, container, false);
        SsacTipContents tip = items.get(position);
        TextView tv_tip_contents = (TextView) itemView.findViewById(R.id.tv_tip_contents);
        ImageView iv_background = (ImageView) itemView.findViewById(R.id.ivBackgroud);
        ImageView iv_tip = (ImageView) itemView.findViewById(R.id.iv_tip);
        tv_tip_contents.setText(tip.contents);
        Glide.with(mContext).load(tip.contents_img).fitCenter().into(iv_tip);
        Glide.with(mContext).load(tip.contents_img).bitmapTransform(new BlurTransformation(mContext, 25)).into(iv_background);


        ((ViewPager) container).addView(itemView);

        return itemView;
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
