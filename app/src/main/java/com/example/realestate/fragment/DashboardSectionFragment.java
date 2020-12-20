package com.example.realestate.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;

import com.example.realestate.R;
import com.example.realestate.adapter.DashboardSectionViewPagerAdapter;
import com.example.realestate.util.ViewUtils;


public abstract class DashboardSectionFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // NOTE: Use a big enough number for this to avoid loading/restarting the same loaders
    private static int sBaseLoaderId = 200;
    private FrameLayout mFrameContent;
    protected int mLoaderId;
    private boolean mIsLoaderInit = false;

    protected ViewPager mPager;
    private LinearLayout mPagerIndicator;
    private FragmentPagerAdapter mPagerAdapter;

    protected abstract int getEmptyMessage();
    protected abstract Class<?> getItemFormActivityClass();
    protected abstract DashboardSectionViewPagerAdapter createViewPagerAdapter(FragmentManager fm, Cursor cursor);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoaderId = sBaseLoaderId++;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_section, container, false);
        mFrameContent = (FrameLayout) view.findViewById(R.id.frameContent);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Show empty view initially
        setupEmptyView();
    }

    public void reloadContent() {
        // If this is by any chances called before activity is attached
        if (getActivity() == null) {
            return;
        }

        LoaderManager lm = getActivity().getSupportLoaderManager();

        if (mIsLoaderInit) {
            // Make sure that we have a valid loader or we'll get an exception
            if (lm.getLoader(mLoaderId) != null) {
                lm.restartLoader(mLoaderId, null, this);
            }
        }
        else {
            // Start loading content
            lm.initLoader(mLoaderId, null, this);
            mIsLoaderInit = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadContent();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == mLoaderId) {
            setupView(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == mLoaderId) {
            setupView(null);
        }
    }

    protected void setupView(Cursor cursor) {
        if (cursor == null || cursor.getCount() <= 0) {
            setupEmptyView();
        } else {
            cursor.moveToFirst();
            setupContentView(cursor);
        }
    }

    protected int getContentView() {
        return R.layout.fragment_dashboard_section_content;
    }

    protected View createContentView(Cursor cursor) {
        FragmentActivity activity = getActivity();

        if (activity == null) {
            return null;
        }

        LayoutInflater lif = activity.getLayoutInflater();
        ViewGroup view = (ViewGroup)lif.inflate(getContentView(), null);

        return view;
    }

    protected int getOffscreenPageLimit() {
        return 1;
    }

    protected void setupContentView(Cursor cursor) {
        View view = createContentView(cursor);

        if (view != null) {
            mFrameContent.removeAllViews();
            mFrameContent.addView(view);

            mPager = (ViewPager)view.findViewById(R.id.pager);
            // View pager may not be available in certain screen orientation
            if (mPager != null) {
                mPager.setOffscreenPageLimit(getOffscreenPageLimit());
                initViewPager(view, cursor);
            } else {
                initNonViewPager(view, cursor);
            }
        }
    }

    protected void initNonViewPager(View view, Cursor cursor) {

    }

    protected void initViewPager(View view, Cursor cursor) {
        mPager.removeAllViews();
        mPagerAdapter = createViewPagerAdapter(getChildFragmentManager(), cursor);
        mPager.setAdapter(mPagerAdapter);

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (mPagerIndicator == null) return;

                int count = mPagerIndicator.getChildCount();
                int bgRes = 0;

                for (int i = 0; i < count; i++) {
                    bgRes = i == position ?
                            R.drawable.brokermate_circle_pager_indicator :
                            R.drawable.brokermate_circle_pager_indicator_inactive;
                    mPagerIndicator.getChildAt(i).setBackgroundResource(bgRes);
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        createIndicators(view, cursor.getCount());
    }

    private void createIndicators(View view, int count) {
        mPagerIndicator = (LinearLayout)view.findViewById(R.id.pager_indicator);

        if (mPagerIndicator == null) {
            return;
        }

        if (count <= 1) {
            mPagerIndicator.setVisibility(View.GONE);
            return;
        }

        View indicator = null;
        LayoutInflater lf = getActivity().getLayoutInflater();
        int bgRes = 0;

        for(int i = 0; i < count; i++) {
            // NOTE: It's important that ViewGroup is specified in inflate() or view
            // won't be added to the layout.
            indicator = lf.inflate(R.layout.circle_pager_indicator, mPagerIndicator, false);
            bgRes = i == 0 ?
                    R.drawable.brokermate_circle_pager_indicator :
                    R.drawable.brokermate_circle_pager_indicator_inactive;
            indicator.setBackgroundResource(bgRes);
            mPagerIndicator.addView(indicator);
        }
    }

    protected void addNewItem() {
        Intent itemIntent = new Intent(getActivity(), getItemFormActivityClass());
        startActivity(itemIntent);
    }

    protected void setupEmptyView() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        mFrameContent.removeAllViews();

        View view = activity.getLayoutInflater().inflate(R.layout.dashboard_section_empty, null);
        TextView txtMessage = (TextView)view.findViewById(R.id.txtMessage);
        txtMessage.setText(getEmptyMessage());

        View btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });

        mFrameContent.addView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPager != null) {
            ViewUtils.unbindDrawables(mPager);
        }
    }
}
