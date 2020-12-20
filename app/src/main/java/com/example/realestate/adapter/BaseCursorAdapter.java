package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;

import androidx.cursoradapter.widget.SimpleCursorAdapter;


public class BaseCursorAdapter extends SimpleCursorAdapter {
	private Context mContext;
	
	public BaseCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
	}

    public BaseCursorAdapter(Context context, Cursor c) {
        super(context, 0, c, new String[]{}, null, 0);
        mContext = context;
    }
	
	public Context getContext() {
		return mContext;
	}
}
