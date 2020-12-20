package com.example.realestate.fragment;

import android.database.Cursor;
import android.os.Bundle;

import android.text.TextUtils;


import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.adapter.BaseCursorAdapter;
import com.example.realestate.adapter.HierarchicalTaxonomyListAdapter;
import com.example.realestate.adapter.TaxonomyListAdapter;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;

import java.util.ArrayList;
import java.util.List;

public abstract class TaxonomyListFragment extends BaseItemListFragment {
	protected boolean isHierarchyEnabled() {
		return true;
	}
	
	protected abstract String getTaxonomyType();

	@Override
	protected BaseCursorAdapter createListViewAdapter() {
	    if (isHierarchyEnabled()) {
	    	return new HierarchicalTaxonomyListAdapter(getActivity());
	    }

        return new TaxonomyListAdapter(getActivity());
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String selection = "C." + TableColumn.TAXONOMY + " = ? ";
		List<String> selectionArgs = new ArrayList<String>();
		selectionArgs.add(getTaxonomyType());

		String query = getSearchQuery();

		if (!TextUtils.isEmpty(query)) {
			selection += " AND C." + TableColumn.TITLE + " LIKE ? ";
			selectionArgs.add("%" + query + "%");
		}

		String[] selectionArgsStr = selectionArgs.toArray(new String[selectionArgs.size()]);

	    CursorLoader cursorLoader = new CursorLoader(getActivity(),
	    	ContentDescriptor.ContentUri.ExtendedUri.VOCABULARY_GROUP_BY_PARENT,
	        null, selection, selectionArgsStr, null);

	    return cursorLoader;
	}
}
