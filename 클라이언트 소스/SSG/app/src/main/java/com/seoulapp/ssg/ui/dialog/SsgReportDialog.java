package com.seoulapp.ssg.ui.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.seoulapp.ssg.R;

import java.util.ArrayList;

/**
 * Created by Dongyoon on 2016. 10. 26..
 */

public class SsgReportDialog extends DialogFragment {

    ArrayList<Integer> mSelectedItems;
    boolean isReported;

    public interface OnOkButtonClick {
        void onOkClick();
    }

    private OnOkButtonClick mListener;

    public void setOnOkButtonClickListener(OnOkButtonClick listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        isReported = bundle.getBoolean("isReported");

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialogTheme);
        if (isReported) {
            builder.setTitle(R.string.report_cancel)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (mListener != null) {
                                mListener.onOkClick();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
        } else {
            mSelectedItems = new ArrayList();  // Where we track the selected items

            builder.setTitle(R.string.report_title)
//                    .setMultiChoiceItems(R.array.toppings, null,
//                            new DialogInterface.OnMultiChoiceClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which,
//                                                    boolean isChecked) {
//                                    if (isChecked) {
//                                        // If the user checked the item, add it to the selected items
//                                        mSelectedItems.add(which);
//                                    } else if (mSelectedItems.contains(which)) {
//                                        // Else, if the item is already in the array, remove it
//                                        mSelectedItems.remove(Integer.valueOf(which));
//                                    }
//                                }
//                            })
                    .setSingleChoiceItems(R.array.toppings, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })

                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (mListener != null) {
                                mListener.onOkClick();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
        }


        return builder.create();
    }
}
