package com.example.realestate.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.realestate.adapter.ColorPickerListAdapter;
import com.example.realestate.model.ColorPickerItem;

import java.util.List;

public class ColorPickerFragment extends DialogFragment {
    Dialog.OnClickListener mClickListener;
    private List<ColorPickerItem> mItems;
    int mTitleRes = 0;

    public void setOnClickListener(DialogInterface.OnClickListener listener) {
        mClickListener = listener;
    }

    public void setItems(List<ColorPickerItem> items) {
        mItems = items;
    }

    public void setTitle(int titleRes) {
        mTitleRes = titleRes;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final FragmentActivity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ColorPickerListAdapter adapter = new ColorPickerListAdapter(activity, 0, mItems);
        builder.setAdapter(adapter, mClickListener);

        if (mTitleRes > 0) {
            builder.setTitle(mTitleRes);
        }

        return builder.create();
    }
}
