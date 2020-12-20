package com.example.realestate.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;


import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.R;
import com.example.realestate.activity.AppointmentFormFragmentActivity;
import com.example.realestate.activity.AppointmentViewFragmentActivity;
import com.example.realestate.adapter.AppointmentListAdapter;
import com.example.realestate.adapter.BaseCursorAdapter;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentListFragment extends BaseItemListFragment {
    // Default to show in coming events
    private int mDueOption = 2;

    @Override
    public Class<?> getItemFormActivityClass() {
        return AppointmentFormFragmentActivity.class;
    }

    @Override
    public Class<?> getItemFormFragmentClass() {
        return AppointmentFormFragment.class;
    }

    @Override
    public Class<?> getItemViewActivityClass() {
        return AppointmentViewFragmentActivity.class;
    }

    @Override
    public Class<?> getItemViewFragmentClass() {
        return AppointmentViewFragment.class;
    }

    @Override
    protected BaseCursorAdapter createListViewAdapter() {
        return new AppointmentListAdapter(getActivity());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = " (1=1) ";
        String[] selectionArgs = null;

        String query = getSearchQuery();

        if (!TextUtils.isEmpty(query)) {
            selection = TableColumn.TITLE + " LIKE ? OR " + TableColumn.LOCATION + " LIKE ? ";

            selectionArgs = new String[] {
                    "%" + query + "%",
                    "%" + query + "%"
            };
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String now = sdf.format(new Date());

        switch (mDueOption) {
            // Show past events only
            case 1:
                selection += " AND from_time < '" + now + "'";
                break;

            // Show upcoming events
            case 2:
                selection += " AND from_time >= '" + now + "'";
                break;

            // Show all
            case 0:
            default:
                break;
        }

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                ContentDescriptor.ContentUri.ExtendedUri.APPOINTMENT_GROUP_BY_WEEK,
                null, selection, selectionArgs, null);

        return cursorLoader;
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        FragmentActivity activity = getActivity();

        if (null != activity) {
            mDueOption = position;
            activity.getSupportLoaderManager().restartLoader(mLoaderID, null, this);
            return true;
        }

        return false;
    }

    @Override
    public View getRightSlidingPaneView(Activity context) {
        return context.getLayoutInflater().inflate(R.layout.navigation_drawer_right, null);
    }
}
