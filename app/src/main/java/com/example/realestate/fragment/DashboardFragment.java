package com.example.realestate.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.realestate.R;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends BaseFragment {
    private static final String TAG_APPOINTMENT = "appointment";
    private static final String TAG_PROPERTY = "property";
    private static final String TAG_CUSTOMER = "customer";

    List<DashboardSectionFragment> mSections;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSections = new ArrayList<DashboardSectionFragment>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createSections(savedInstanceState);
    }

    private void createSections(Bundle savedInstanceState) {
        FragmentManager fm = getChildFragmentManager();

        if (savedInstanceState == null) {
            FragmentTransaction ft = fm.beginTransaction();

            DashboardAppointmentFragment appointmentFragment = new DashboardAppointmentFragment();
            ft.replace(R.id.frameAppointment, appointmentFragment, TAG_APPOINTMENT);
            mSections.add(appointmentFragment);

            DashboardPropertyFragment propertyFragment = new DashboardPropertyFragment();
            ft.replace(R.id.frameProperty, propertyFragment, TAG_PROPERTY);
            mSections.add(propertyFragment);

            DashboardCustomerFragment customerFragment = new DashboardCustomerFragment();
            ft.replace(R.id.frameCustomer, customerFragment, TAG_CUSTOMER);
            mSections.add(customerFragment);

            ft.commit();
        } else {
            mSections.add((DashboardSectionFragment)fm.findFragmentByTag(TAG_APPOINTMENT));
            mSections.add((DashboardSectionFragment)fm.findFragmentByTag(TAG_PROPERTY));
            mSections.add((DashboardSectionFragment) fm.findFragmentByTag(TAG_CUSTOMER));
        }
    }

    @Override
    public void onDestroy() {
        mSections = null;
        super.onDestroy();
    }
}
