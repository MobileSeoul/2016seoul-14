package com.seoulapp.ssg.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.model.Photo;

import java.util.ArrayList;


/**
 * Created by Boram Moon on 2016-10-06.
 */

public class VolunteerPagerAdapter extends PagerAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Photo> pics;

    public VolunteerPagerAdapter(Context c) {
        super();
        mContext = c;
        mInflater = LayoutInflater.from(c);
    }

    public void additems(ArrayList<Photo> items) {
        pics = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pics == null ? 0 : pics.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = null;
        ImageView ivVolunteer;
        v = mInflater.inflate(R.layout.item_volunteer_img, null);
        ivVolunteer = (ImageView) v.findViewById(R.id.iv_volunteer_img);

        String photoUrl = pics.get(position).picture;
        Glide.with(container.getContext()).load(photoUrl).into(ivVolunteer);

        ((ViewPager)container).addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((View) obj);
    }

}

