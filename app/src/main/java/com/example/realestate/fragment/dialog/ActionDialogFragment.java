package com.example.realestate.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.realestate.R;

import java.util.List;

public class ActionDialogFragment extends DialogFragment {
    List<Pair<String, Integer>> mActionItems;
    Dialog.OnClickListener mClickListener;
    int mTitleRes = 0;

    public ActionDialogFragment() {
        super();
    }

    public ActionDialogFragment setActionItems(List<Pair<String, Integer>> items) {
        mActionItems = items;
        return this;
    }

    public ActionDialogFragment setOnClickListener(Dialog.OnClickListener listener) {
        mClickListener = listener;
        return this;
    }

    public ActionDialogFragment setTitle(int titleRes) {
        mTitleRes = titleRes;
        return this;
    }

    private ArrayAdapter createAdapter(Context context) {
        ArrayAdapter adapter = new ArrayAdapter(context, R.layout.list_item_customer_action, android.R.id.text1, mActionItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView text1 = (TextView)view.findViewById(android.R.id.text1);
                TextView text2 = (TextView)view.findViewById(android.R.id.text2);

                Pair<String, Integer> item = mActionItems.get(position);
                text1.setText(item.first);
                text2.setText(item.second);

                return view;
            }
        };

        return adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final FragmentActivity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setAdapter(createAdapter(activity), mClickListener);

        if (mTitleRes > 0) {
            builder.setTitle(mTitleRes);
        }

        return builder.create();
    }
}
