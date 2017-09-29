package com.seoulapp.ssg.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Dongyoon on 16. 6. 10..
 */
public abstract class BasicViewHolder<ITEM> extends RecyclerView.ViewHolder {

    public BasicViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBindView(ITEM item);

}
