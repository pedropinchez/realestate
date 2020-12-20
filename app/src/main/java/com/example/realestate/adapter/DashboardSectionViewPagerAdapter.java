package com.example.realestate.adapter;

import android.database.Cursor;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.realestate.fragment.DashboardSectionItemFragment;


public abstract class DashboardSectionViewPagerAdapter extends FragmentPagerAdapter {
    private Cursor mCursor;

    protected abstract DashboardSectionItemFragment createItem();

    public DashboardSectionViewPagerAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);
        mCursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        DashboardSectionItemFragment fragment = createItem();
        fragment.setCursorPosition(position);
        fragment.setCursor(mCursor);

        return fragment;
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }
}
