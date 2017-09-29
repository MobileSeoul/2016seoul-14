package com.seoulapp.ssg.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.seoulapp.ssg.R;

/**
 * Created by Dongyoon on 2016. 10. 21..
 */

public class UploadPictureDialog extends DialogFragment {
    private AlertDialog.Builder builder;
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;

    public UploadPictureDialog() {
    }

    public interface OnChoiceClickListener {
        public void onCameraClick();

        public void onGalleryClick();
    }

    OnChoiceClickListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setOnChoiceClickListener(OnChoiceClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        builder.setItems(R.array.upload_picture_methods, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // The 'which' argument contains the index position
                // of the selected item
                if (which == CAMERA) {
                    mListener.onCameraClick();
                    dialog.dismiss();
                } else {
                    mListener.onGalleryClick();
                    dialog.dismiss();
                }
            }
        });
        return builder.create();

    }
}
