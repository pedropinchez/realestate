package com.example.realestate.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.common.ThumbnailType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.DateTimeUtils;
import com.example.realestate.util.GeneralUtils;
import com.example.realestate.util.MediaUtils;
import com.example.realestate.view.BaseSectionHeaderListItemView;


public class AppointmentListAdapter extends BaseSectionHeaderCursorAdapter {
    public AppointmentListAdapter(Context context) {
        super(context);
    }

    @Override
    protected String getSectionHeaderColumn() {
        return "week_number";
    }

    @Override
    protected View createNewItemView(Context context) {
        BaseSectionHeaderListItemView view = new BaseSectionHeaderListItemView(context,
                                                                               null,
                                                                               R.layout.list_item_appointment);
        view.setTag(new ViewHolder(view));
        return view;
    }

    @Override
    protected void setUpViewHolder(BaseSectionHeaderViewHolder viewHolder, Cursor cursor,
                                   boolean headerValueChanged, Object[] headerVals) {
        super.setUpViewHolder(viewHolder, cursor, headerValueChanged, headerVals);
        ViewHolder holder = (ViewHolder)viewHolder;

        // Header values are only available if header value is changed
        if (headerValueChanged) {
            int counter = (Integer)headerVals[0];
            int weekNumber = CursorUtils.getRecordIntValue(cursor, "week_number");

            holder.sectionHeaderView1.setText("w" + weekNumber);
            holder.sectionHeaderView2.setText(String.valueOf(counter));
        }

        holder.mTxtTitle.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));
        String location = CursorUtils.getRecordStringValue(cursor, TableColumn.LOCATION);
        View parent = (View)holder.mTxtLocation.getParent();

        if (TextUtils.isEmpty(location)) {
            holder.mTxtLocation.setText(R.string.not_set);
        } else {
            holder.mTxtLocation.setText(location);
        }

        int imgWidth = GeneralUtils.dpToPx(getContext(), 64);
        int imgHeight = imgWidth;

        String customerThumbnail = CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH);
        MediaUtils.setImageThumbnailAsync(holder.mImgCustomerPic,
                                          customerThumbnail,
                                          2 * imgWidth,
                                          2 * imgHeight,
                                          ThumbnailType.CUSTOMER,
                                          null);

        int remindersCount = CursorUtils.getRecordIntValue(cursor, "reminder_count");
        holder.mImgReminder.setVisibility(remindersCount > 0 ? View.VISIBLE : View.GONE);

        String fromTime = CursorUtils.getRecordStringValue(cursor, TableColumn.FROM_TIME);
        holder.mTxtTime.setText(DateTimeUtils.formatSqlTimestamp(fromTime));

        int indicatorColor = DateTimeUtils.getDueIndicatorColor(fromTime);
        if (indicatorColor > 0) {
            holder.mDueIndicator.setBackgroundColor(getContext().getResources().getColor(indicatorColor));
        }
    }

    private class ViewHolder extends BaseSectionHeaderViewHolderWithCounter {
        public TextView mTxtTitle;
        public TextView mTxtLocation;
        public TextView mTxtTime;
        public ImageView mImgReminder;
        public ImageView mImgCustomerPic;
        public View mDueIndicator;

        public ViewHolder(View view) {
            super(view);

            mTxtTitle = (TextView)view.findViewById(R.id.txtTitle);
            mTxtLocation = (TextView)view.findViewById(R.id.txtLocation);
            mTxtTime = (TextView)view.findViewById(R.id.txtTime);
            mImgReminder = (ImageView)view.findViewById(R.id.imgReminder);
            mImgCustomerPic = (ImageView)view.findViewById(R.id.imgCustomerPic);
            mDueIndicator = view.findViewById(R.id.dueIndicator);
        }
    }
}
