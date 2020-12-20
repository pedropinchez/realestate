package com.example.realestate.fragment;

import android.database.Cursor;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.example.realestate.R;
import com.example.realestate.activity.CustomerFormFragmentActivity;
import com.example.realestate.adapter.DashboardSectionViewPagerAdapter;


public class DashboardCustomerFragment extends DashboardSectionFragment {
    @Override
    protected Class<?> getItemFormActivityClass() {
        return CustomerFormFragmentActivity.class;
    }

    @Override
    protected DashboardSectionViewPagerAdapter createViewPagerAdapter(FragmentManager fm, Cursor cursor) {
        return null;
    }

    @Override
    protected View createContentView(Cursor cursor) {
        return null;
    }

    @Override
    protected int getEmptyMessage() {
        return R.string.no_customers;
    }
}
