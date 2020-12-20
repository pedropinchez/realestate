package com.example.realestate.util;

import android.content.Context;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.example.realestate.adapter.TaxonomySpinnerAdapter;


public class AdapterUtils {
	public static SimpleCursorAdapter createTaxonomySpinnerAdapter(Context context) {
		SimpleCursorAdapter adapter = null;
		
		adapter = new TaxonomySpinnerAdapter(context);
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		
		return adapter;
	}
}
