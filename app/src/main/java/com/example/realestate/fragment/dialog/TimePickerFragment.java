package com.example.realestate.fragment.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;


import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
	private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;

	private int mHour = 0;
	private int mMinute = 0;

	public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener listener) {
		mOnTimeSetListener = listener;
	}
	
	public void setInitTime(int hour, int minute) {
        mHour = hour;
        mMinute = minute;
	}

    public void setInitTime(final Calendar calendar) {
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);
    }

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = mHour > 0 ? mHour : c.get(Calendar.HOUR_OF_DAY);
        int minute = mMinute > 0 ? mMinute : c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), mOnTimeSetListener, hour, minute, true);
    }
}
