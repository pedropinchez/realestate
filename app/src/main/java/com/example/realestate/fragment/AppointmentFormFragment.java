package com.example.realestate.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;


import com.example.realestate.R;
import com.example.realestate.activity.CustomerListActivity;
import com.example.realestate.activity.PickMapLocationActivity;
import com.example.realestate.database.TableColumn;
import com.example.realestate.fragment.dialog.DatePickerFragment;
import com.example.realestate.fragment.dialog.TimePickerFragment;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.receiver.AppointmentAlarmReceiver;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.MediaUtils;
import com.example.realestate.util.ViewUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppointmentFormFragment extends BaseItemFormFragment {
    public static final String ITEM_TITLE = "item_title";
    public static final String ITEM_TIME = "item_time";
    public static final String ITEM_LOCATION = "item_location";

    private static final int INTENT_PICK_LOCATION = 1;
    private static final int INTENT_PICK_CUSTOMER = 2;

    private static final String TAG_REMOVE_REMINDER = "remove_reminder";
    private static final String REMINDER_TIME_POSITIONS = "reminder_time_positions";

    private EditText mTxtFromDate;
    private EditText mTxtFromTime;
    private EditText mTxtToDate;
    private EditText mTxtToTime;
    private EditText mTxtTitle;
    private EditText mTxtNote;
    private EditText mTxtLocation;
    private EditText mTxtCustomerName;

    private ImageView mImgCustomerPic;
    private ImageButton mBtnPickLocation;
    private ImageButton mBtnRemoveCustomer;

    private View mBtnAddReminder;
    private ViewGroup mReminderContainer;

    private Calendar mFromTime;
    private Calendar mToTime;
    private int[] mReminderTimeValues;

    private int mDefaultTimeDiffHour;
    private long mFromTimeMillis;
    private long mToTimeMillis;

    private long mCustomerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDefaultTimeDiffHour = 1;
        mFromTime = Calendar.getInstance();
        mToTime = Calendar.getInstance();
        mReminderTimeValues = getResources().getIntArray(R.array.reminder_time_values);

        if (savedInstanceState != null) {
            mCustomerId = savedInstanceState.getLong(TableColumn.CUSTOMER_ID);
        }

        adjustToTime();
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_appointment_form;
    }

    @Override
    protected Uri getItemContentUri() {
        return ContentDescriptor.ContentUri.APPOINTMENT;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mTxtFromDate = ViewUtils.findEditText(view, R.id.txtFromDate);
        mTxtFromDate.setOnClickListener(this);

        mTxtFromTime = ViewUtils.findEditText(view, R.id.txtFromTime);
        mTxtFromTime.setOnClickListener(this);

        mTxtToDate = ViewUtils.findEditText(view, R.id.txtToDate);
        mTxtToDate.setOnClickListener(this);

        mTxtToTime = ViewUtils.findEditText(view, R.id.txtToTime);
        mTxtToTime.setOnClickListener(this);

        mTxtTitle = ViewUtils.findEditText(view, R.id.txtTitle);
        mTxtNote = ViewUtils.findEditText(view, R.id.txtNote);
        mTxtLocation = ViewUtils.findEditText(view, R.id.txtLocation);
        mTxtCustomerName = ViewUtils.findEditText(view, R.id.txtCustomerName);
        mTxtCustomerName.setOnClickListener(this);

        mImgCustomerPic = (ImageView)view.findViewById(R.id.imgCustomerPic);
        mBtnPickLocation = (ImageButton)view.findViewById(R.id.btnPickLocation);
        mBtnPickLocation.setOnClickListener(this);

        mBtnRemoveCustomer = (ImageButton)view.findViewById(R.id.btnRemoveCustomer);
        mBtnRemoveCustomer.setOnClickListener(this);

        mBtnAddReminder = view.findViewById(R.id.btnAddReminder);
        mBtnAddReminder.setOnClickListener(this);

        mReminderContainer = (ViewGroup)view.findViewById(R.id.reminderContainer);

        updateTimes();
    }

    private void updateTimes() {
        DateFormat df = DateFormat.getDateInstance();
        Date fromTime = mFromTime.getTime();
        Date toTime = mToTime.getTime();

        mTxtFromDate.setText(df.format(fromTime));
        mTxtToDate.setText(df.format(toTime));

        DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
        mTxtFromTime.setText(tf.format(fromTime));
        mTxtToTime.setText(tf.format(toTime));

        mFromTimeMillis = mFromTime.getTimeInMillis();
        mToTimeMillis = mToTime.getTimeInMillis();
    }

    private void adjustToTime() {
        mToTime.set(mFromTime.get(Calendar.YEAR),
                    mFromTime.get(Calendar.MONTH),
                    mFromTime.get(Calendar.DAY_OF_MONTH),
                    mFromTime.get(Calendar.HOUR_OF_DAY) + mDefaultTimeDiffHour,
                    mFromTime.get(Calendar.MINUTE));
    }

    private void restoreReminders(int[] indices) {
        for (int i = 0, c = indices.length; i < c; i++) {
            addReminder(indices[i]);
        }
    }

    @Override
    protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFromTime.setTimeInMillis(savedInstanceState.getLong(TableColumn.FROM_TIME));
            mToTime.setTimeInMillis(savedInstanceState.getLong(TableColumn.TO_TIME));
            int[] reminderTimeIndices = savedInstanceState.getIntArray(REMINDER_TIME_POSITIONS);
            restoreReminders(reminderTimeIndices);
        } else {
            // NOTE: have to check for null cursor here since this is called even for add form
            if (cursor != null) {
                mTxtTitle.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));
                mTxtLocation.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.LOCATION));
                mTxtNote.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NOTE));
                mTxtCustomerName.setText(CursorUtils.getRecordStringValue(cursor, "customer_name"));

                MediaUtils.setImageFromFile(mImgCustomerPic, CursorUtils.getRecordStringValue(cursor, "thumbnail_path"));
                mCustomerId = CursorUtils.getRecordLongValue(cursor, TableColumn.CUSTOMER_ID);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date fromTime = sdf.parse(CursorUtils.getRecordStringValue(cursor, TableColumn.FROM_TIME));
                    mFromTime.setTime(fromTime);

                    Date toTime = sdf.parse(CursorUtils.getRecordStringValue(cursor, TableColumn.TO_TIME));
                    mToTime.setTime(toTime);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }

                populateReminders(cursor);

                updateTimes();
            }
        }
    }

    private void populateReminders(Cursor cursor) {
        String reminders = CursorUtils.getRecordStringValue(cursor, "reminders");
        if (TextUtils.isEmpty(reminders)) {
            return;
        }

        String[] times = reminders.split(";");
        int timeIndex = -1;

        for (String time : times) {
            timeIndex = getReminderIndex(Integer.parseInt(time));
            if (timeIndex >= 0) {
                addReminder(timeIndex);
            }
        }
    }

    private int getReminderIndex(int value) {
        int index = -1;

        for (int i = 0, c = mReminderTimeValues.length; i < c; i++) {
            if (mReminderTimeValues[i] == value) {
                index = i;
                break;
            }
        }

        return index;
    }

    private void pickDate(final View view) {
        DatePickerFragment dialog = new DatePickerFragment();
        dialog.setInitDate(view == mTxtFromDate ? mFromTime : mToTime);

        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if (view == mTxtFromDate) {
                    mFromTime.set(year, month, day);

                    Calendar now = Calendar.getInstance();
                    if (mFromTime.before(now)) {
                        mFromTime = now;
                    }

                    adjustToTime();
                }
                else {
                    mToTime.set(year, month, day);

                    if (mToTime.before(mFromTime)) {
                        adjustToTime();
                    }
                }
                updateTimes();
            }
        });

        dialog.show(getActivity().getSupportFragmentManager(), "AppointmentFormFragment");
    }

    private void pickTime(final View view) {
        TimePickerFragment dialog = new TimePickerFragment();
        dialog.setInitTime(view == mTxtFromTime ? mFromTime : mToTime);
        dialog.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                if (view == mTxtFromTime) {
                    mFromTime.set(Calendar.HOUR_OF_DAY, hour);
                    mFromTime.set(Calendar.MINUTE, minute);

                    Calendar now = Calendar.getInstance();
                    if (mFromTime.before(now)) {
                        mFromTime = now;
                    }

                    adjustToTime();
                }
                else {
                    mToTime.set(Calendar.HOUR_OF_DAY, hour);
                    mToTime.set(Calendar.MINUTE, minute);

                    if (mToTime.before(mFromTime)) {
                        adjustToTime();
                    }
                }
                updateTimes();
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(), "AppointmentFormFragment");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Don't care if it's not ok
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case INTENT_PICK_LOCATION:
                String address = data.getStringExtra(PickMapLocationActivity.MAP_ADDRESS);
                if (!TextUtils.isEmpty(address)) {
                    mTxtLocation.setText(address);
                }
                break;

            case INTENT_PICK_CUSTOMER:
                updateCustomer(data);
                break;
        }
    }

    private void updateCustomer(Intent data) {
        Bundle result = data.getBundleExtra(BaseItemFragment.PICK_RESULT);
        mCustomerId = data.getLongExtra(BaseItemFragment.ITEM_ID, -1);
        mTxtCustomerName.setText(result.getString(TableColumn.NAME));

        MediaUtils.setImageFromFile(mImgCustomerPic, result.getString(TableColumn.THUMBNAIL_PATH));
    }

    protected boolean validateForm() {
        if (TextUtils.isEmpty(mTxtTitle.getText())) {
            showFormElementError(R.string.alert_title_empty, mTxtTitle);
            return false;
        }

        return true;
    }

    @Override
    protected ContentValues getSaveFormValues() {
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(TableColumn.TITLE, mTxtTitle.getText().toString());
        values.put(TableColumn.LOCATION, mTxtLocation.getText().toString());
        values.put(TableColumn.NOTE, mTxtNote.getText().toString());
        values.put(TableColumn.FROM_TIME, sdf.format(mFromTime.getTime()));
        values.put(TableColumn.TO_TIME, sdf.format(mToTime.getTime()));
        values.put(TableColumn.CUSTOMER_ID, mCustomerId);

        // Save reminder time indices
        ViewGroup layout = null;
        Spinner spnReminderTime = null;
        StringBuilder builder = new StringBuilder();
        long reminderTime = 0;

        for (int i = 0, c = mReminderContainer.getChildCount(); i < c; i++) {
            layout = (ViewGroup)mReminderContainer.getChildAt(i);
            spnReminderTime = (Spinner)layout.findViewById(R.id.spnTime);
            reminderTime = mReminderTimeValues[spnReminderTime.getSelectedItemPosition()];
            builder.append(reminderTime);
            if (i < c - 1) {
                builder.append(";");
            }
        }

        // Put reminders timestamp (in seconds) to saved values
        if (builder.length() > 0) {
            values.put(TableColumn.ExtendedColumn.REMINDERS, builder.toString());
        }

        return values;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(TableColumn.FROM_TIME, mFromTimeMillis);
        outState.putLong(TableColumn.TO_TIME, mToTimeMillis);
        outState.putLong(TableColumn.CUSTOMER_ID, mCustomerId);

        // Save reminder time indices
        ViewGroup layout = null;
        Spinner spnReminderTime = null;
        int numReminders = mReminderContainer.getChildCount();
        int[] reminderTimeIndices = new int[numReminders];

        for (int i = 0; i < numReminders; i++) {
            layout = (ViewGroup)mReminderContainer.getChildAt(i);
            spnReminderTime = (Spinner) layout.findViewById(R.id.spnTime);
            reminderTimeIndices[i] = spnReminderTime.getSelectedItemPosition();
        }

        outState.putIntArray(REMINDER_TIME_POSITIONS, reminderTimeIndices);
    }

    private void addReminder(int reminderIndex) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.widget_reminder_item, null);

        ImageButton btnRemove = (ImageButton)view.findViewById(R.id.btnRemove);
        btnRemove.setTag(TAG_REMOVE_REMINDER);
        btnRemove.setOnClickListener(this);

        Spinner spn = (Spinner)view.findViewById(R.id.spnTime);
        spn.setSelection(reminderIndex);

        mReminderContainer.addView(view);
    }

    @Override
    public void onClick(View view) {
        if (view == mTxtFromDate || view == mTxtToDate) {
            pickDate(view);
        }
        else if (view == mTxtFromTime || view == mTxtToTime) {
            pickTime(view);
        }
        else if (view == mBtnPickLocation) {
            Intent mapIntent = new Intent(getActivity(), PickMapLocationActivity.class);
            mapIntent.setAction(Intent.ACTION_PICK);

            String currentAddress = mTxtLocation.getText().toString();

            if (!TextUtils.isEmpty(currentAddress)) {
                mapIntent.putExtra(PickMapLocationActivity.MAP_ADDRESS, currentAddress);
            }
            startActivityForResult(mapIntent, INTENT_PICK_LOCATION);
        }
        else if (view == mBtnAddReminder) {
            addReminder(0);
        }
        else if (view == mTxtCustomerName) {
            Intent intent = new Intent(getActivity(), CustomerListActivity.class);
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(PickMapLocationActivity.MAP_ADDRESS, mTxtLocation.getText());
            startActivityForResult(intent, INTENT_PICK_CUSTOMER);
        }
        else if (view == mBtnRemoveCustomer) {
            mTxtCustomerName.setText("");
            mImgCustomerPic.setImageBitmap(null);
            mCustomerId = -1;
        }
        else {
            String tag = (String)view.getTag();
            if (tag == TAG_REMOVE_REMINDER) {
                ViewGroup group = (ViewGroup)view.getParent();
                if (group != null) {
                    mReminderContainer.removeView(group);
                }
            }
        }
    }

    @Override
    public boolean saveItem() {
        boolean saved = super.saveItem();

        if (saved) {
            createAlarms();
        }

        return saved;
    }

    private void createAlarms() {
        ViewGroup layout = null;
        Spinner spnReminderTime = null;

        Activity activity = getActivity();
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(activity.ALARM_SERVICE);

        int numReminders = mReminderContainer.getChildCount();
        long reminderTime = 0;
        Intent contentIntent = null;
        PendingIntent alarmIntent = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String title = mTxtTitle.getText().toString();
        String location = mTxtLocation.getText().toString();
        String time = sdf.format(mFromTime.getTime());
        long itemId = getItemId();
        long now = System.currentTimeMillis();

        for (int i = 0; i < numReminders; i++) {
            layout = (ViewGroup)mReminderContainer.getChildAt(i);
            spnReminderTime = (Spinner)layout.findViewById(R.id.spnTime);
            reminderTime = mFromTimeMillis - 60 * 1000 * mReminderTimeValues[spnReminderTime.getSelectedItemPosition()];

            // There is no point setting an alarm in the past
            if (reminderTime < now) {
                continue;
            }

            contentIntent = new Intent(activity, AppointmentAlarmReceiver.class);

            // Save item ID to intent so that we can retrieve later
            contentIntent.putExtra(ITEM_ID, itemId);
            contentIntent.putExtra(ITEM_TITLE, title);
            contentIntent.putExtra(ITEM_LOCATION, location);
            contentIntent.putExtra(ITEM_TIME, time);

            alarmIntent = PendingIntent.getBroadcast(activity, (int)itemId + i, contentIntent, 0);

            // NOTE: Beginning with API 19 (KITKAT) alarm delivery is inexact: the OS will shift alarms
            // in order to minimize wakeups and battery use. There are new APIs to support applications
            // which need strict delivery guarantees; see setWindow(int, long, long, PendingIntent) and
            // setExact(int, long, PendingIntent). Applications whose targetSdkVersion is earlier than API
            // 19 will continue to see the previous behavior in which all alarms are delivered exactly
            // when requested.
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTime, alarmIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, alarmIntent);
            }
        }
    }
}
