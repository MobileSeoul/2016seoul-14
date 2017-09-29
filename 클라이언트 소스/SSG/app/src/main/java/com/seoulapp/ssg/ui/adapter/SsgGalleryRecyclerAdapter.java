package com.seoulapp.ssg.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seoulapp.ssg.R;
import com.seoulapp.ssg.api.SsgApiService;
import com.seoulapp.ssg.managers.PropertyManager;
import com.seoulapp.ssg.model.Ssg;
import com.seoulapp.ssg.network.ServiceGenerator;
import com.seoulapp.ssg.ui.adapter.basic.BasicRecyclerAdapter;
import com.seoulapp.ssg.ui.adapter.viewholder.BasicViewHolder;
import com.seoulapp.ssg.ui.dialog.SsgReportDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dongyoon on 2016. 10. 19..
 */

public class SsgGalleryRecyclerAdapter extends BasicRecyclerAdapter {

    public interface OnSsgItemClick {

        void onEraseClick(int position);
    }

    private OnSsgItemClick mCallback;

    public SsgGalleryRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    public BasicViewHolder getViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater();
        parent.setBackgroundResource(R.color.colorPrimary);
        return new GalleryViewHolder(inflater.inflate(R.layout.item_ssg_gallery, parent, false));
    }

    private class GalleryViewHolder extends BasicViewHolder<Ssg> implements View.OnClickListener, SsgReportDialog.OnOkButtonClick {
        ImageView ivSsgPicture;
        TextView tvSpotDetail, tvSsgDate, tvSsgComment, btnReportSsg;
        Button btnEraseSsg;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ivSsgPicture = (ImageView) itemView.findViewById(R.id.iv_picture);
            tvSpotDetail = (TextView) itemView.findViewById(R.id.tv_spot_detail);
            tvSsgDate = (TextView) itemView.findViewById(R.id.tv_ssg_date);
            tvSsgComment = (TextView) itemView.findViewById(R.id.tv_ssg_comment);
            btnEraseSsg = (Button) itemView.findViewById(R.id.btn_remove_ssg);
            btnReportSsg = (TextView) itemView.findViewById(R.id.btn_report_ssg);
            btnEraseSsg.setOnClickListener(this);
            btnReportSsg.setOnClickListener(this);
        }

        @Override
        public void onBindView(Ssg ssg) {
            tvSpotDetail.setText(ssg.spotDetail);
            tvSsgDate.setText(ssg.date);
            tvSsgComment.setText(ssg.comment);
            setLikeBtn(ssg.isLike(), ssg.likeCount);
            setReportBtn(ssg.isDeclare());

            Glide
                    .with(getContext())
                    .load(ssg.picture)
                    .fitCenter()
                    .into(ivSsgPicture);
        }

        private void setLikeBtn(boolean like, int likeCount) {
            btnEraseSsg.setText("지워주세요!\n" + likeCount);
            if (like) {
                btnEraseSsg.setBackgroundColor(getContext().getResources().getColor(R.color.ssg_erase_btn_clicked));
            } else {
                btnEraseSsg.setBackgroundColor(getContext().getResources().getColor(R.color.ssg_erase_btn_non_clicked));
            }
        }

        private void setReportBtn(boolean b) {
            if (b) {
                btnReportSsg.setTextColor(getContext().getResources().getColor(R.color.btnChanged));
                btnReportSsg.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_church_bell_red), null, null, null);
            } else {
                btnReportSsg.setTextColor(getContext().getResources().getColor(android.R.color.black));
                btnReportSsg.setCompoundDrawablesWithIntrinsicBounds(getContext().getResources().getDrawable(R.drawable.ic_church_bell_black), null, null, null);
            }
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            int position = getAdapterPosition();
            Ssg ssg = (Ssg) getItem(position);
            switch (id) {
                case R.id.btn_report_ssg:
                    showReportDialog(ssg.isDeclare());
                    break;
                case R.id.btn_remove_ssg:
                    btnEraseSsg.setClickable(false);
                    requestSsgLike(ssg);
                    break;

            }
        }

        private void requestSsgLike(final Ssg item) {
            SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
            Call<Ssg> call = service.like(item.ssgId, PropertyManager.getInstance().getUserId());
            call.enqueue(new Callback<Ssg>() {
                @Override
                public void onResponse(Call<Ssg> call, Response<Ssg> response) {
                    if (response.isSuccessful()) {
                        if(response.body().isLike()) {
                            item.likeCount += 1;
                            item.isLike = 1;
                        } else {
                            item.likeCount -= 1;
                            item.isLike = 0;
                            item.like = null;
                        }

                        setLikeBtn(response.body().isLike(), item.likeCount);
                        btnEraseSsg.setClickable(true);

                    }
                }

                @Override
                public void onFailure(Call<Ssg> call, Throwable t) {

                }
            });
        }

        private void requestSsgDeclare(final Ssg item) {
            SsgApiService service = ServiceGenerator.getInstance().createService(SsgApiService.class);
            Call<Ssg> call = service.declare(item.ssgId, PropertyManager.getInstance().getUserId());
            call.enqueue(new Callback<Ssg>() {
                @Override
                public void onResponse(Call<Ssg> call, Response<Ssg> response) {
                    if(response.isSuccessful()) {
                        if(response.body().isDeclare()) {
                            item.isDeclare = 1;
                            setReportBtn(true);
                        } else {
                            item.isDeclare = 0;
                            item.declare = null;
                            setReportBtn(false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Ssg> call, Throwable t) {

                }
            });

        }

        private void showReportDialog(boolean isReported) {
            SsgReportDialog dialog = new SsgReportDialog();
            dialog.setOnOkButtonClickListener(this);
            Bundle bundle = new Bundle();
            bundle.putBoolean("isReported", isReported);
            dialog.setArguments(bundle);
            if (getContext() instanceof FragmentActivity) {
                dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), null);
            }
        }

        @Override
        public void onOkClick() {
            Ssg ssg = (Ssg)getItem(getAdapterPosition());
            requestSsgDeclare(ssg);

        }
    }

}
