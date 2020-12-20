package com.example.realestate.fragment;

import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.activity.AppointmentViewFragmentActivity;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.DateTimeUtils;
import com.example.realestate.util.MediaUtils;


public class DashboardAppointmentItemFragment extends DashboardSectionItemFragment {
    @Override
    protected int getContentView() {
        return R.layout.list_item_appointment_dashboard;
    }

    @Override
    protected Class<?> getItemViewActivityClass() {
        return AppointmentViewFragmentActivity.class;
    }

    public static DashboardAppointmentItemFragment newInstance(int position, Cursor cursor) {
        DashboardAppointmentItemFragment item = new DashboardAppointmentItemFragment();
        item.setCursor(cursor);
        item.setCursorPosition(position);

        return item;
    }

    @Override
    protected void setupView(View view, Cursor cursor) {
        TextView mTxtTitle = (TextView)view.findViewById(R.id.txtTitle);
        TextView mTxtTime = (TextView)view.findViewById(R.id.txtTime);
        TextView mTxtLocation = (TextView)view.findViewById(R.id.txtLocation);

        mTxtTitle.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));
        String fromTime = CursorUtils.getRecordStringValue(cursor, TableColumn.FROM_TIME);

        mTxtTime.setText(DateTimeUtils.formatSqlTimestamp(fromTime));

        String location = CursorUtils.getRecordStringValue(cursor, TableColumn.LOCATION);
        if (TextUtils.isEmpty(location)) {
            mTxtLocation.setText(R.string.not_set);
        } else {
            mTxtLocation.setText(location);
        }

        ImageView mImgReminder = (ImageView)view.findViewById(R.id.imgReminder);
        int reminderCount = CursorUtils.getRecordIntValue(cursor, "reminder_count");
        mImgReminder.setVisibility(reminderCount > 0 ? View.VISIBLE : View.GONE);

        View mDueIndicator = view.findViewById(R.id.dueIndicator);
        int indicatorColor = DateTimeUtils.getDueIndicatorColor(fromTime);
        if (indicatorColor > 0) {
            mDueIndicator.setBackgroundColor(getResources().getColor(indicatorColor));
        }

        ImageView mImgCustomerPic = (ImageView)view.findViewById(R.id.imgCustomerPic);
        String thumbPath = CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH);

        if (TextUtils.isEmpty(thumbPath)) {
            mImgCustomerPic.setImageBitmap(null);
        } else {
            MediaUtils.setImageFromFile(mImgCustomerPic, thumbPath);
        }
    }
}
