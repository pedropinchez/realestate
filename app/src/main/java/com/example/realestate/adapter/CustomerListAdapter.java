package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.common.CustomerType;
import com.example.realestate.common.ThumbnailType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.GeneralUtils;
import com.example.realestate.util.MediaUtils;
import com.example.realestate.view.BaseSectionHeaderListItemView;


public class CustomerListAdapter extends BaseSectionHeaderCursorAdapter {
    public CustomerListAdapter(Context context) {
        // from can't be null
        super(context);
    }

	public CustomerListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	protected String getSectionHeaderColumn() {
		return "locality_name";
	}
	
	@Override
	protected View createNewItemView(Context context) {
		BaseSectionHeaderListItemView view = new BaseSectionHeaderListItemView(context, null, R.layout.list_item_customer);
		view.setTag(new ViewHolder(view));
		
		return view;
	}
	
	@Override
	protected Object[] getHeaderAggregateValues(String currentHeaderVal, int startingPos, Cursor cursor) {
		int counter = 1;
		String tmpVal = null;
		
		// Move the cursor to the starting position of the section
		cursor.moveToPosition(startingPos);
		int totalProperties = CursorUtils.getRecordIntValue(cursor, "property_count");

		while (cursor.moveToNext()) {
			tmpVal = getHeaderValue(cursor, -1); 

			if (currentHeaderVal.equals(tmpVal) || (TextUtils.isEmpty(currentHeaderVal) && TextUtils.isEmpty(tmpVal))) {
				counter++;
				totalProperties += CursorUtils.getRecordIntValue(cursor, "property_count");
			}
			else {
				break;
			}
		}
		
		// Move back to the current position after calculation
		cursor.moveToPosition(startingPos);
		
		return new Object[] {
				counter,
				totalProperties
		};
	}
	
	@Override
	protected void setUpViewHolder(BaseSectionHeaderViewHolder viewHolder, Cursor cursor,
			boolean headerValueChanged, Object[] headerVals) {
		super.setUpViewHolder(viewHolder, cursor, headerValueChanged, headerVals);
		
		ViewHolder holder = (ViewHolder)viewHolder;
		
		// Header values are only available if header value is changed
		if (headerValueChanged) {
			int counter = (Integer)headerVals[0];
			String localityName = CursorUtils.getRecordStringValue(cursor, "locality_name");
			int totalProperties = (Integer)headerVals[1];

			if (localityName == null) {
				localityName = getContext().getString(R.string.unlined);
			}

			holder.sectionHeaderView1.setText(localityName);
			holder.sectionHeaderView2.setText(String.valueOf(counter));
		}

		holder.mainTextView.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NAME));

        int type = CursorUtils.getRecordIntValue(cursor, TableColumn.TYPE);
        holder.subTextView.setText(type == CustomerType.PRIVATE ? R.string.customer_type_private : R.string.customer_type_company);

        int imgWidth = GeneralUtils.dpToPx(getContext(), 64);
        int imgHeight = imgWidth;

        String profilePicPath = CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH);
        MediaUtils.setImageThumbnailAsync(holder.profilePicView,
                                          profilePicPath,
                                          2 * imgWidth,
                                          2 * imgHeight,
                                          ThumbnailType.CUSTOMER,
                                          null);
    }
	
	private class ViewHolder extends BaseSectionHeaderViewHolderWithCounter {
		public RatingBar ratingView;
		public TextView mainTextView;
		public TextView subTextView;
		public ImageView profilePicView;
		
		public ViewHolder(View view) {
			super(view);

			mainTextView = (TextView)view.findViewById(android.R.id.text1);
			subTextView = (TextView)view.findViewById(android.R.id.text2);
			profilePicView = (ImageView)view.findViewById(R.id.imgProfilePic);
		}
	}
}
