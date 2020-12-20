package com.example.realestate.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;


public abstract class DashboardSectionItemFragment extends BaseFragment {
    private int mPosition;
    private Cursor mCursor;
    private long mItemId;

    protected abstract int getContentView();
    protected abstract Class<?> getItemViewActivityClass();
    protected abstract void setupView(View view, Cursor cursor);

    public void setCursorPosition(int pos) {
        mPosition = pos;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(getContentView(), container, false);

        if (mCursor != null && mPosition >=0) {
            mCursor.moveToPosition(mPosition);
            mItemId = getItemId(mCursor);

            if (mItemId > 0) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), getItemViewActivityClass());
                        intent.putExtra(BaseItemFragment.ITEM_ID, mItemId);
                        startActivity(intent);
                    }
                });
            }
            setupView(v, mCursor);
        }
        return v;
    }

    protected long getItemId(Cursor cursor) {
        return CursorUtils.getRecordLongValue(mCursor, TableColumn._ID);
    }
}
