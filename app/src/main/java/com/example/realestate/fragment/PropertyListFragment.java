package com.example.realestate.fragment;

import android.database.Cursor;
import android.os.Bundle;

import android.text.TextUtils;

import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.activity.PropertyFormActivity;
import com.example.realestate.activity.PropertyViewFragmentActivity;
import com.example.realestate.adapter.BaseCursorAdapter;
import com.example.realestate.adapter.PropertyListAdapter;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;


public class PropertyListFragment extends BaseItemListFragment {

	@Override
	public Class<?> getItemFormActivityClass() {
		return PropertyFormActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return PropertyFormFragment.class;
	}

    @Override
    public Class<?> getItemViewFragmentClass() {
        return PropertyViewFragment.class;
    }

    @Override
    public Class<?> getItemViewActivityClass() {
        return PropertyViewFragmentActivity.class;
    }

    @Override
    protected BaseCursorAdapter createListViewAdapter() {
        return new PropertyListAdapter(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = null;
        String[] selectionArgs = null;

        String query = getSearchQuery();

        if (!TextUtils.isEmpty(query)) {
            selection = "P." + TableColumn.NAME + " LIKE ? OR locality_name LIKE ? ";

            selectionArgs = new String[] {
                    "%" + query + "%",
                    "%" + query + "%"
            };
        }

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                                                     ContentDescriptor.ContentUri.ExtendedUri.PROPERTY_GROUP_BY_LOCALITY,
                                                     null, selection, selectionArgs, null);

        return cursorLoader;
    }

}
