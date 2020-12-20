package com.example.realestate.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.activity.CustomerFormFragmentActivity;
import com.example.realestate.activity.CustomerViewActivity;
import com.example.realestate.adapter.BaseCursorAdapter;
import com.example.realestate.adapter.CustomerListAdapter;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;


public class CustomerListFragment extends BaseItemListFragment {

	@Override
	protected BaseCursorAdapter createListViewAdapter() {
	    return new CustomerListAdapter(getActivity());
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String selection = null;
		String[] selectionArgs = null;

		String query = getSearchQuery();

		if (!TextUtils.isEmpty(query)) {
			selection = "C." + TableColumn.NAME + " LIKE ? OR locality_name LIKE ? ";

			selectionArgs = new String[] { 
					"%" + query + "%", 
					"%" + query + "%" 
			};
		}

	    CursorLoader cursorLoader = new CursorLoader(getActivity(),
	    	ContentDescriptor.ContentUri.ExtendedUri.CUSTOMER_GROUP_BY_LOCALITY,
	        null, selection, selectionArgs, null);

	    return cursorLoader;
	}
	
	@Override
	public Class<?> getItemViewActivityClass() {
		return CustomerViewActivity.class;
	}
	
	@Override
	public Class<?> getItemViewFragmentClass() {
		return CustomerViewFragment.class;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
		return CustomerFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return CustomerFormFragment.class;
	}
}
