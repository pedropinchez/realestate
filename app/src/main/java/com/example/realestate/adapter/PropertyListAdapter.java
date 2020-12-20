package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.realestate.R;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.GeneralUtils;
import com.example.realestate.view.BaseSectionHeaderListItemView;
import com.squareup.picasso.Picasso;

public class PropertyListAdapter extends BaseSectionHeaderCursorAdapter {
    public PropertyListAdapter(Context context) {
        // from can't be null
        super(context);
    }

	public PropertyListAdapter(Context context, int layout, Cursor c,
                               String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
	}

	@Override
	protected String getSectionHeaderColumn() {
		return "locality_name";
	}
	
	@Override
	protected View createNewItemView(Context context) {
		BaseSectionHeaderListItemView view = new BaseSectionHeaderListItemView(context,
                                                                               null,
                                                                               R.layout.list_item_property);
		view.setTag(new ViewHolder(view));
		
		return view;
	}
	
	@Override
	protected Object[] getHeaderAggregateValues(String currentHeaderVal, int startingPos, Cursor cursor) {
		int counter = 1;
		String tmpVal = null;
		
		// Move the cursor to the starting position of the section
		cursor.moveToPosition(startingPos);
		int totalProperties = CursorUtils.getRecordIntValue(cursor, "_id");

		while (cursor.moveToNext()) {
			tmpVal = getHeaderValue(cursor, -1); 

			if (currentHeaderVal.equals(tmpVal) || (TextUtils.isEmpty(currentHeaderVal) && TextUtils.isEmpty(tmpVal))) {
				counter++;
				totalProperties += CursorUtils.getRecordIntValue(cursor, "_id");
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

		final ViewHolder holder = (ViewHolder)viewHolder;
		
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

		holder.mTxtTitle.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NAME));
        holder.mTxtPrice.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.PRICE));

        String coverPath = CursorUtils.getRecordStringValue(cursor, TableColumn.COVER_PIC_PATH);

        int imgWidth = GeneralUtils.dpToPx(getContext(), 100);
        int imgHeight = imgWidth;

        /*MediaUtils.setImageThumbnailAsync(holder.mCoverPic,
                                          coverPath,
                                          2 * imgWidth,
                                          2 * imgHeight,
                                          ThumbnailType.PROPERTY,
                                          null);*/
        Picasso.get()
               .load("file://" + coverPath)
               .resize(imgWidth, imgHeight)
               .centerCrop()
               .into(holder.mCoverPic);
    }
	
	private class ViewHolder extends BaseSectionHeaderViewHolderWithCounter {
		public TextView mTxtPrice;
		public TextView mTxtTitle;
		public ImageView mCoverPic;
		
		public ViewHolder(View view) {
			super(view);

			mTxtPrice = (TextView)view.findViewById(R.id.txtPrice);
			mTxtTitle = (TextView)view.findViewById(R.id.txtTitle);
			mCoverPic = (ImageView)view.findViewById(R.id.imgCoverPic);
		}
	}
}
