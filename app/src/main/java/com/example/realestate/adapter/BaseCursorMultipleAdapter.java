package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseCursorMultipleAdapter extends BaseCursorAdapter {
    protected HashMap<Integer, Boolean> mItemsChecked;

    public abstract long getItemId(int pos);

    public BaseCursorMultipleAdapter(Context context) {
        this(context, 0, null, new String[] {}, null, 0);
    }

    public BaseCursorMultipleAdapter(Context context, int layout, Cursor c,
                                     String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public void setCheckedItems(int[] itemPositions) {
        if (mItemsChecked == null) {
            mItemsChecked = new HashMap<Integer, Boolean>();
        }

        for (int pos : itemPositions) {
            mItemsChecked.put(pos, true);
        }
    }

    public void setChecked(int pos, boolean checked) {
        if (mItemsChecked == null) {
            return;
        }

        mItemsChecked.put(pos, checked);
    }

    public boolean isChecked(int pos) {
        if (mItemsChecked != null && mItemsChecked.containsKey(pos)) {
            return mItemsChecked.get(pos);
        }

        return false;
    }

    protected ItemData getSelectedItemData(int pos) {
        return null;
    }

    public List<ItemData> getSelectedItemsData() {
        List<ItemData> data = new ArrayList<ItemData>();

        for (int pos : mItemsChecked.keySet()) {
            if (isChecked(pos)) {
                data.add(getSelectedItemData(pos));
            }
        }

        return data;
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        Cursor oldCursor = super.swapCursor(c);

        if (mItemsChecked == null) {
            mItemsChecked = new HashMap<Integer, Boolean>();
        }

        return oldCursor;
    }

    static public class ItemData {
        public int mItemPosition;
        public long mItemId;
        public String mItemTitle;

        public ItemData(long id, int pos, String title) {
            mItemPosition = pos;
            mItemId = id;
            mItemTitle = title;
        }
    }
}
