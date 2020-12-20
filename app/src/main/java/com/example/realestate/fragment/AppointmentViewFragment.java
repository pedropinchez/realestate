package com.example.realestate.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.asyntask.AddressToLocationTask;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.DateTimeUtils;
import com.example.realestate.util.MediaUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AppointmentViewFragment extends BaseItemViewFragment implements AddressToLocationTask.Callbacks,
        OnMapReadyCallback {
    private TextView mTxtTitle;
    private TextView mTxtNote;
    private TextView mTxtFromTime;
    private TextView mTxtToTime;
    private TextView mTxtLocation;

    private TextView mTxtCustomerName;
    private TextView mTxtCustomerType;
    private ImageView mImgCustomerPic;

    private View mGroupCustomer;
    private ViewGroup mGroupReminders;
    private ViewGroup mGroupReminderTimes;
    private ViewGroup mGroupLocation;

    private String mLocation;
    private AddressToLocationTask mAddressTask;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    @Override
    protected int getContentView() {
        return R.layout.fragment_appointment_view;
    }

    @Override
    protected Uri getItemContentUri() {
        return ContentDescriptor.ContentUri.APPOINTMENT;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mTxtTitle = (TextView)view.findViewById(R.id.txtTitle);
        mTxtNote = (TextView)view.findViewById(R.id.txtNote);
        mTxtFromTime = (TextView)view.findViewById(R.id.txtFromTime);
        mTxtToTime = (TextView)view.findViewById(R.id.txtToTime);
        mTxtLocation = (TextView)view.findViewById(R.id.txtLocation);

        mTxtCustomerName = (TextView)view.findViewById(R.id.txtCustomerName);
        mTxtCustomerType = (TextView)view.findViewById(R.id.txtCustomerType);

        mGroupCustomer = view.findViewById(R.id.groupCustomer);
        mGroupReminders = (ViewGroup)view.findViewById(R.id.groupReminders);
        mGroupReminderTimes = (ViewGroup)view.findViewById(R.id.groupReminderTimes);
        mGroupLocation = (ViewGroup)view.findViewById(R.id.groupLocation);

        mImgCustomerPic = (ImageView)view.findViewById(R.id.imgCustomerPic);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mMapFragment.getMapAsync(this);
    }

    @Override
    protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
        if (cursor != null) {
            mTxtTitle.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));

            String note = CursorUtils.getRecordStringValue(cursor, TableColumn.NOTE);
            if (TextUtils.isEmpty(note)) {
                mTxtNote.setVisibility(View.GONE);
            } else {
                mTxtNote.setText(note);
                mTxtNote.setVisibility(View.VISIBLE);
            }

            mLocation = CursorUtils.getRecordStringValue(cursor, TableColumn.LOCATION);
            if (TextUtils.isEmpty(mLocation)) {
                mGroupLocation.setVisibility(View.GONE);
            } else {
                mGroupLocation.setVisibility(View.VISIBLE);
                mTxtLocation.setText(mLocation);
            }

            String fromTime = CursorUtils.getRecordStringValue(cursor, TableColumn.FROM_TIME);
            String toTime = CursorUtils.getRecordStringValue(cursor, TableColumn.TO_TIME);

            mTxtFromTime.setText(DateTimeUtils.formatSqlTimestamp(fromTime));
            mTxtToTime.setText(DateTimeUtils.formatSqlTimestamp(toTime));

            String customerName = CursorUtils.getRecordStringValue(cursor, "customer_name");
            if (TextUtils.isEmpty(customerName)) {
                mGroupCustomer.setVisibility(View.GONE);
            } else {
                mGroupCustomer.setVisibility(View.VISIBLE);
                mTxtCustomerName.setText(customerName);

                MediaUtils.setImageFromFile(mImgCustomerPic, CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH));
            }


            showReminders(CursorUtils.getRecordStringValue(cursor, "reminders"), fromTime);
        }
    }

    private void showReminders(String reminders, String fromTime) {
        if (TextUtils.isEmpty(reminders)) {
            mGroupReminders.setVisibility(View.GONE);
            return;
        }

        mGroupReminders.setVisibility(View.VISIBLE);
        mGroupReminderTimes.removeAllViews();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            calendar.setTime(sdf.parse(fromTime));

            String[] reminderTimes = reminders.split(";");
            TextView reminderView = null;
            int minutes = 0;
            LayoutInflater lif = getActivity().getLayoutInflater();

            for(String time : reminderTimes) {
                minutes = Integer.parseInt(time);
                calendar.add(Calendar.MINUTE, -minutes);

                reminderView = (TextView)lif.inflate(R.layout.widget_content_textview, null);
                reminderView.setText(DateTimeUtils.formatDatetime(calendar.getTime()));
                mGroupReminderTimes.addView(reminderView);

                calendar.add(Calendar.MINUTE, minutes);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreResolvingAddress() {

    }

    @Override
    public void onLatLngResolved(LatLng latLng) {
        if (latLng == null) {
            // TODO: show error message here
            return;
        }

        MarkerOptions options = new MarkerOptions().position(latLng).title(mLocation);
        mMap.addMarker(options).showInfoWindow();
        CameraUpdate currentLoc = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(currentLoc);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        if (!TextUtils.isEmpty(mLocation)) {
            mAddressTask = new AddressToLocationTask();
            mAddressTask.setCallbacks(this);
            mAddressTask.execute(mLocation);
        }
    }
}
