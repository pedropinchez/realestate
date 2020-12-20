package com.example.realestate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.model.ColorPickerItem;

import java.util.List;


public class ColorPickerListAdapter extends ArrayAdapter<ColorPickerItem> {
    private List<ColorPickerItem> mItems;

    public ColorPickerListAdapter(Context context, int resource, List<ColorPickerItem> items) {
        super(context, resource, items);
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColorPickerItem item = mItems.get(position);

        // We don't have recycled view, just create a fresh one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_simple_1, null);
        }

        TextView view = (TextView)convertView.findViewById(android.R.id.text1);

        if (item.mColor == 0) {
            view.setText(item.mText);
            view.setBackgroundColor(getContext().getResources().getColor(R.color.white));

        } else {
            view.setText(null);
            view.setBackgroundColor(getContext().getResources().getColor(item.mColor));
        }

        return convertView;
    }
}
