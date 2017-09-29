package com.seoulapp.ssg.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seoulapp.ssg.R;
import com.seoulapp.ssg.model.Ssac;
import com.seoulapp.ssg.ui.adapter.basic.BasicRecyclerAdapter;
import com.seoulapp.ssg.ui.adapter.viewholder.BasicViewHolder;

/**
 * Created by Dongyoon on 2016. 10. 27..
 */
public class SsacHistoryRecyclerAdapter extends BasicRecyclerAdapter {
    private Context mContext;
    public SsacHistoryRecyclerAdapter(Context context) {
        super(context);
        mContext = context;

    }

    @Override
    public BasicViewHolder getViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();

        return new SsacHistoryViewHolder(inflater.inflate(R.layout.item_ssac_list, parent, false));
    }

    private class SsacHistoryViewHolder extends BasicViewHolder<Ssac> {
        private TextView tvDate, tvLocation, tvTime, tvDday;

        public SsacHistoryViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvDday = (TextView) itemView.findViewById(R.id.tv_dday);

        }

        @Override
        public void onBindView(Ssac ssac) {
            tvDate.setText(ssac.getSchedule());
            tvLocation.setText(ssac.getSpot());
            tvTime.setText(ssac.getTime());
            int dDay = ssac.Dday;

            if(dDay > 0) {
                tvDday.setText("참여 D - " + dDay);
                tvDday.setBackground(mContext.getResources().getDrawable(R.drawable.bg_button_skyblue));
            } else if(ssac.apply == 1) { // 참여완료
                tvDday.setText("참여완료");
                tvDday.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_blue));

            } else if(ssac.apply == 0){
                tvDday.setText("불참");
                tvDday.setBackground(mContext.getResources().getDrawable(R.drawable.bg_btn_red));
            }


        }
    }

}
