package com.example.realestate.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.realestate.R;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;


public class PropertyOtherInfoSectionFormFragment extends BasePropertySectionFormFragment implements View.OnClickListener {
    private View mBtnAddKeyVal;
    private ViewGroup mGroupKeyVal;
    private Spinner mSpnBrokerageType;
    private EditText mTxtBrokerageAmount;

	@Override
	protected int getContentView() {
		return R.layout.fragment_property_form_other_info;
	}

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mGroupKeyVal = (ViewGroup)view.findViewById(R.id.groupKeyValPairs);
        mBtnAddKeyVal = view.findViewById(R.id.btnAddKeyVal);
        mBtnAddKeyVal.setOnClickListener(this);

        mSpnBrokerageType = (Spinner)view.findViewById(R.id.spnBrokerageType);
        mTxtBrokerageAmount = (EditText)view.findViewById(R.id.txtBrokerageValue);
    }

    @Override
	public void populateContentView(Cursor cursor, Bundle savedInstanceState) {
        if (cursor != null) {
            mTxtBrokerageAmount.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.BROKERAGE));

            int brokerageType = CursorUtils.getRecordIntValue(cursor, TableColumn.BROKERAGE_TYPE);
            if (brokerageType >= 0) {
                mSpnBrokerageType.setSelection(brokerageType);
            }
        }
	}

	@Override
	public ContentValues getFormValues() {
        ContentValues values = new ContentValues();

        values.put(TableColumn.BROKERAGE_TYPE, mSpnBrokerageType.getSelectedItemPosition());
        values.put(TableColumn.BROKERAGE, mTxtBrokerageAmount.getText().toString());

        View item = null;
        EditText txtKey = null;
        EditText txtVal = null;
        StringBuilder builder = new StringBuilder();

        for (int i = 0, c = mGroupKeyVal.getChildCount(); i < c; i++) {
            item = mGroupKeyVal.getChildAt(i);
            txtKey = (EditText)item.findViewById(R.id.txtName);
            txtVal = (EditText)item.findViewById(R.id.txtValue);

            builder.append(txtKey.getText());
            builder.append("\t");
            builder.append(txtVal.getText());

            if (i < c - 1) {
                builder.append("\n");
            }
        }

        if (builder.length() > 0) {
            values.put(TableColumn.ExtendedColumn.NAME_VALUE_PAIRS, builder.toString());
        }

        return values;
	}

    @Override
    public void onClick(View view) {
        if (view == mBtnAddKeyVal) {
            addKeyValPair(null, null);
        }
    }

    private void addKeyValPair(String name, String value) {
        LayoutInflater lif = getActivity().getLayoutInflater();
        View item = lif.inflate(R.layout.widget_name_value_pair_item_form, mGroupKeyVal, false);

        if (!TextUtils.isEmpty(name)) {
            EditText txtName = (EditText)item.findViewById(R.id.txtName);
            txtName.setText(name);
        }

        if (!TextUtils.isEmpty(value)) {
            EditText txtValue = (EditText)item.findViewById(R.id.txtValue);
            txtValue.setText(value);
        }

        ImageButton btnRemove = (ImageButton)item.findViewById(R.id.btnRemove);
        btnRemove.setTag(item);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View root = (View)view.getTag();
                if (root != null) {
                    mGroupKeyVal.removeView(root);
                }
            }
        });

        mGroupKeyVal.addView(item);
    }

    @Override
    public void updateAttributes(Cursor cursor) {
        super.updateAttributes(cursor);
        cursor.moveToPosition(-1);

        String vocabulary = null;
        String name = null;
        String value = null;

        while (cursor.moveToNext()) {
            name = CursorUtils.getRecordStringValue(cursor, TableColumn.NAME);
            value = CursorUtils.getRecordStringValue(cursor, TableColumn.VALUE);

            addKeyValPair(name, value);
        }
    }
}
