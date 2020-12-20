package com.example.realestate.fragment.dialog;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;


import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {
	private OnDateSetListener mDateSetListener;
	private int mInitYear = 0;
	private int mInitMonth = 0;
	private int mInitDay = 0;
	
	public void setOnDateSetListener(OnDateSetListener listener) {
		mDateSetListener = listener;
	}
	
	public void setInitDate(int year, int month, int day) {
		mInitYear = year;
		mInitMonth = month;
		mInitDay = day;
	}

    public void setInitDate(final Calendar calendar) {
        mInitYear = calendar.get(Calendar.YEAR);
        mInitMonth = calendar.get(Calendar.MONTH);
        mInitDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = mInitYear != 0 ? mInitYear : c.get(Calendar.YEAR);
        int month = mInitMonth != 0 ? mInitMonth : c.get(Calendar.MONTH);
        int day = mInitDay != 0 ? mInitDay : c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), mDateSetListener, year, month, day);
    }
}
