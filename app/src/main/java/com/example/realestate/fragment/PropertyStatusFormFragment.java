package com.example.realestate.fragment;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;


import com.example.realestate.R;
import com.example.realestate.common.VocabularyType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.fragment.dialog.ColorPickerFragment;
import com.example.realestate.model.ColorPickerItem;
import com.example.realestate.util.CursorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PropertyStatusFormFragment extends TaxonomyFormFragment {
    private Button mBtnColor;
    private Spinner mSpnActive;
    private int mIndicatorColor = 0;

    @Override
    protected boolean isHierarchyEnabled() {
        return false;
    }

    @Override
    protected String getVocabularyName() {
        return VocabularyType.PROPERTY_STATUS;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mBtnColor = (Button) view.findViewById(R.id.btnColor);
        mBtnColor.setOnClickListener(this);

        mSpnActive = (Spinner)view.findViewById(R.id.spnActiveType);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_property_status_form;
    }

    @Override
    protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
        super.populateContentView(cursor, savedInstanceState);

        if (cursor != null) {
            int indicatorColor = CursorUtils.getRecordIntValue(cursor, TableColumn.DESCRIPTION);
            if (indicatorColor > 0) {
                mBtnColor.setBackgroundColor(indicatorColor);
            }

            mSpnActive.setSelection(CursorUtils.getRecordIntValue(cursor, TableColumn.DESCRIPTION));

            try {
                String dataStr = CursorUtils.getRecordStringValue(cursor, TableColumn.DATA);
                if (!TextUtils.isEmpty(dataStr)) {
                    JSONObject json = new JSONObject(dataStr);
                    int color = json.getInt("indicator_color");

                    if (color != 0) {
                        mBtnColor.setBackgroundColor(color);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected ContentValues getSaveFormValues() {
        ContentValues values = super.getSaveFormValues();

        values.put(TableColumn.DESCRIPTION, mSpnActive.getSelectedItemPosition());

        if (mIndicatorColor != 0) {
            JSONObject json = new JSONObject();

            try {
                json.put("indicator_color", mIndicatorColor);
                values.put(TableColumn.DATA, json.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return values;
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnColor) {
            final List<ColorPickerItem> items = new ArrayList<ColorPickerItem>();
            items.add(new ColorPickerItem(R.color.branding_red, 0));
            items.add(new ColorPickerItem(R.color.branding_blue, 0));
            items.add(new ColorPickerItem(R.color.branding_red_light, 0));
            items.add(new ColorPickerItem(R.color.branding_yellow_dark, 0));
            items.add(new ColorPickerItem(R.color.branding_yellow_light, 0));
            items.add(new ColorPickerItem(0, R.string.custom));

            ColorPickerFragment dialog = new ColorPickerFragment();
            dialog.setTitle(R.string.select);
            dialog.setItems(items);
            dialog.setOnClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    ColorPickerItem item = items.get(i);

                    if (item.mColor > 0) {
                        mIndicatorColor = getResources().getColor(item.mColor);
                        mBtnColor.setBackgroundColor(mIndicatorColor);
                    }
                }
            });
            dialog.show(getFragmentManager(), "PropertysStatusFormFragment");
            return;
        }

        super.onClick(v);
    }
}