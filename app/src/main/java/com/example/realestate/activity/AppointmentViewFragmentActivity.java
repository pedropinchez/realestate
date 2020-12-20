package com.example.realestate.activity;


import android.view.MenuItem;

import com.example.realestate.R;
import com.example.realestate.fragment.AppointmentViewFragment;

public class AppointmentViewFragmentActivity  extends BaseItemViewFragmentActivity {
    @Override
    protected Class<?> getItemFormActivityClass() {
        return AppointmentFormFragmentActivity.class;
    }

    @Override
    protected Class<?> getFragmentClass() {
        return AppointmentViewFragment.class;
    }

    @Override
    protected int getTitleResource() {
        return R.string.view_appointment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               // finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
