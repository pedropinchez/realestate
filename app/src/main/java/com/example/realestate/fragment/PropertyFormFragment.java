package com.example.realestate.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.realestate.R;
import com.example.realestate.provider.ContentDescriptor;


public class PropertyFormFragment extends BaseItemFormFragment {

	@Override
	public void onClick(View v) {
	}

	@Override
	protected int getContentView() {
        return R.layout.activity_property_form;
	}

	@Override
	protected Uri getItemContentUri() {
        return ContentDescriptor.ContentUri.PROPERTY;
	}

	@Override
	protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
	}

}
