package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;


public class TaxonomyListMultipleAdapter extends BaseCursorMultipleAdapter {

    public TaxonomyListMultipleAdapter(Context context) {
        super(context, 0, null, new String[] {}, null, 0);
    }

	public TaxonomyListMultipleAdapter(Context context, int layout, Cursor c,
                                       String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	protected View createNewItemView(View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_simple_1_with_checkbox, null);

		TextView txtName = (TextView)convertView.findViewById(android.R.id.text1);
        CheckBox chkCheck = (CheckBox)convertView.findViewById(R.id.checkbox);

		ViewHolder viewHolder = new ViewHolder(txtName, chkCheck);
		convertView.setTag(viewHolder);
		
		return convertView;
	}

	protected View getItemView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createNewItemView(convertView, parent);
        }

        return convertView;
	}

    @Override
    public long getItemId(int pos) {
        Cursor cursor = getCursor();
        if (cursor == null) {
            return -1;
        }

        cursor.moveToPosition(pos);
        return CursorUtils.getRecordLongValue(cursor, TableColumn._ID);
    }

    protected void setupView(Cursor cursor, int position, ViewHolder viewHolder) {
		viewHolder.txtName.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));
        viewHolder.chkCheck.setChecked(isChecked(position));
	}

    @Override
    protected ItemData getSelectedItemData(int pos) {
        Cursor cursor = getCursor();
        if (cursor == null) {
            return null;
        }

        cursor.moveToPosition(pos);
        long id = CursorUtils.getRecordLongValue(cursor, TableColumn._ID); // getItemId()
        String title = CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE);

        return new ItemData(id, pos, title);
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
        public CheckBox chkCheck;

		public ViewHolder(TextView txtName, CheckBox chkCheck) {
			this.txtName = txtName;
            this.chkCheck = chkCheck;
		}
	}
}
