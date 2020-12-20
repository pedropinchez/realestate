package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;


public class TaxonomySpinnerAdapter extends HierarchicalTaxonomyListAdapter {
	public TaxonomySpinnerAdapter(Context context) {
        super(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		Cursor cursor = getCursor();
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, null); 
			TextView txtName = (TextView)convertView.findViewById(android.R.id.text1);
			viewHolder = new ViewHolder(txtName);
			convertView.setTag(viewHolder);
		}
		
		cursor.moveToPosition(position);

		viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.txtName.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));
		
		return convertView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}
}
