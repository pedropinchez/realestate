package com.example.realestate.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.R;
import com.example.realestate.activity.AppointmentFormFragmentActivity;
import com.example.realestate.adapter.DashboardSectionViewPagerAdapter;
import com.example.realestate.provider.ContentDescriptor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardAppointmentFragment extends DashboardSectionFragment {
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        CursorLoader loader = new CursorLoader(getActivity(),
                ContentDescriptor.ContentUri.ExtendedUri.APPOINTMENT_MOST_DUE,
                null,
                null,
                new String[] { now, "2" },
                null);

        return loader;
    }

    @Override
    protected Class<?> getItemFormActivityClass() {
        return AppointmentFormFragmentActivity.class;
    }

    @Override
    protected int getEmptyMessage() {
        return R.string.no_appointments;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_dashboard_section_appointment;
    }

    @Override
    protected DashboardSectionViewPagerAdapter createViewPagerAdapter(FragmentManager fm, Cursor cursor) {
        return new DashboardSectionViewPagerAdapter(fm, cursor) {
            @Override
            protected DashboardSectionItemFragment createItem() {
                return new DashboardAppointmentItemFragment();
            }
        };
    }

    @Override
    protected void initNonViewPager(View view, Cursor cursor) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        DashboardAppointmentItemFragment fragment = null;

        for (int i = 0, c = cursor.getCount(); i < c; i++) {
            fragment = DashboardAppointmentItemFragment.newInstance(i, cursor);
            ft.add(R.id.container, fragment);
        }

        ft.commit();
    }
}