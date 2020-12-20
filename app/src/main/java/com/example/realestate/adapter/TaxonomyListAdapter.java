package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;


public class TaxonomyListAdapter extends BaseCursorAdapter {

    public TaxonomyListAdapter(Context context) {
        super(context, 0, null, new String[] {}, null, 0);
    }

	public TaxonomyListAdapter(Context context, int layout, Cursor c,
                               String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	protected View createNewItemView(View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_simple_1, null);

		TextView txtName = (TextView)convertView.findViewById(android.R.id.text1);

		ViewHolder viewHolder = new ViewHolder(txtName);
		convertView.setTag(viewHolder);
		
		return convertView;
	}
	
	protected View getItemView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createNewItemView(convertView, parent);
        }

        return convertView;
	}
	
	protected void setupView(Cursor cursor, int position, ViewHolder viewHolder) {
		viewHolder.txtName.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Cursor cursor = getCursor();
		convertView = getItemView(convertView, parent);
		ViewHolder viewHolder = (ViewHolder)convertView.getTag();

        // Move cursor back to current position
        cursor.moveToPosition(position);
        setupView(cursor, position, viewHolder);
        
        return convertView;
	}
	
	protected class ViewHolder {
		public TextView txtName;

		public ViewHolder(TextView txtName) {
			this.txtName = txtName;
		}
	}
}
