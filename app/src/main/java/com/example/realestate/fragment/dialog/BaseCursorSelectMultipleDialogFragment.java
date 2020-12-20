package com.example.realestate.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;


import com.example.realestate.adapter.BaseCursorMultipleAdapter;

import java.util.List;

public abstract class BaseCursorSelectMultipleDialogFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static int sBaseLoaderId = 300;

    private BaseCursorMultipleAdapter mListAdapter;
    private ListView mListView;
    private Callbacks mCallbacks;
    private int[] mSelectedItemPositions;
    private int mTitleRes;
    protected int mLoaderId;

    protected abstract BaseCursorMultipleAdapter createListAdapter(Context context);

    public BaseCursorSelectMultipleDialogFragment() {
        mLoaderId = sBaseLoaderId++;
    }

    public interface Callbacks {
        void onSelectionConfirmed(List<BaseCursorMultipleAdapter.ItemData> itemData);
        void onSelectionCancelled();
    }

    public void setCallbacksListener(Callbacks listener) {
        mCallbacks = listener;
    }

    public void setSelectedItems(int[] positions) {
        mSelectedItemPositions = positions;
    }

    public void setTitle(int titleRes) {
        mTitleRes = titleRes;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final FragmentActivity activity = getActivity();

        mListView = new ListView(activity);
        mListAdapter = createListAdapter(activity);

        // Do it here since mListAdapter is not available when setSelectedItems() is called
        if (mSelectedItemPositions != null) {
            mListAdapter.setCheckedItems(mSelectedItemPositions);
        }

        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                boolean isChecked = mListAdapter.isChecked(position);
                mListAdapter.setChecked(position, !isChecked);

                // TODO: Check if there is any better ways to invalidate the changed rows.
                // Invalidating the whole data set will make it slow. Check:
                // http://stackoverflow.com/questions/2123083/android-listview-refresh-single-row
                mListAdapter.notifyDataSetChanged();
            }
        });

        activity.getSupportLoaderManager().initLoader(mLoaderId, null, this);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mCallbacks != null) {
                            mCallbacks.onSelectionConfirmed(mListAdapter.getSelectedItemsData());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mCallbacks != null) {
                            mCallbacks.onSelectionCancelled();
                        }
                    }
                })
                .setView(mListView);

        if (mTitleRes > 0) {
            builder.setTitle(mTitleRes);
        }

        return builder.create();
    }

    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == mLoaderId) {
            mListAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == mLoaderId) {
            mListAdapter.swapCursor(null);
        }
    }
}
