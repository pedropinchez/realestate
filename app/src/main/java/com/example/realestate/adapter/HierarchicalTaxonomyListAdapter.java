package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;

import com.example.realestate.R;
import com.example.realestate.util.CursorUtils;


public class HierarchicalTaxonomyListAdapter extends
        TaxonomyListAdapter {

    public HierarchicalTaxonomyListAdapter(Context context) {
        super(context, 0, null, new String[] {}, null, 0);
    }

	public HierarchicalTaxonomyListAdapter(Context context, int layout,
                                           Cursor c, String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}
	
	@Override
	protected void setupView(Cursor cursor, int position, ViewHolder viewHolder) {
		super.setupView(cursor, position, viewHolder);
		int parentId = 0;
        int childCount = 0;

        if (cursor.getColumnIndex("parent_id") >= 0) {
            parentId = CursorUtils.getRecordIntValue(cursor, "parent_id");

            // If it's the parent item, get number of child items
            if (parentId <= 0) {
                while (cursor.moveToNext()) {
                    parentId = CursorUtils.getRecordIntValue(cursor, "parent_id");

                    if (parentId > 0) {
                        childCount++;
                    }
                    else {
                        break;
                    }
                }
            }

            cursor.moveToPosition(position);
        }

        if (childCount > 0) {
            viewHolder.txtName.setBackgroundResource(R.drawable.list_item_bg_header);
        } else {
            viewHolder.txtName.setBackgroundResource(R.drawable.list_item_bg);
        }
    }
}
