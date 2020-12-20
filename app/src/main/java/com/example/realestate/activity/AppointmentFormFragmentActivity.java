package com.example.realestate.activity;


import com.example.realestate.R;
import com.example.realestate.fragment.AppointmentFormFragment;

public class AppointmentFormFragmentActivity extends BaseItemFormFragmentActivity {
    @Override
    protected Class<?> getFragmentClass() {
        return AppointmentFormFragment.class;
    }

    @Override
    protected int getTitleResource() {
        return R.string.add_appointment;
    }

    @Override
    protected int getEditModeTitleResource() {
        return R.string.edit_appointment;
    }
}
