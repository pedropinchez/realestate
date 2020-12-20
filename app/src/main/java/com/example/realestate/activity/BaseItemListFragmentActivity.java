package com.example.realestate.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;

import com.example.realestate.R;
import com.example.realestate.fragment.BaseItemFragment;
import com.example.realestate.fragment.BaseItemListFragment;


public abstract class BaseItemListFragmentActivity extends LoggedInRequiredActivity implements  BaseItemListFragment.ListFragmentEventsHandler {
    protected abstract Class<?> getFragmentClass();
    protected BaseItemListFragment mListFragment;
    protected boolean mIsResultIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        // Whether the activity is shown for picking up an item
        mIsResultIntent = Intent.ACTION_PICK.equals(getIntent().getAction());

        createFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                // Return to caller activity if it's for a result
                if (mIsResultIntent) {
                    finish();
                    return true;
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get the result for the calling activity when this is started for picking up items
     * @param cursor Adapter cursor
     * @return A bundle
     */
    protected Bundle getIntentPickResult(Cursor cursor) {
        return new Bundle();
    }

    @Override
    public void onListItemClick(int position, long id) {
        // Return the intent result
        if (mIsResultIntent) {
            Cursor cursor = mListFragment.getListViewAdapter().getCursor();
            cursor.moveToPosition(position);

            Intent result = new Intent();
            result.putExtra(BaseItemFragment.ITEM_ID, id);
            result.putExtra(BaseItemFragment.PICK_RESULT, getIntentPickResult(cursor));

            setResult(RESULT_OK, result);
            finish();
            return;
        }

        Class<?> itemActivityCls = mListFragment.getItemViewActivityClass();

        if (itemActivityCls == null) {
            itemActivityCls = mListFragment.getItemFormActivityClass();
        }

        if (itemActivityCls != null) {
            Intent intent = new Intent(this, itemActivityCls);

            if (BaseItemFragmentActivity.class.isAssignableFrom(itemActivityCls)) {
                intent.putExtra(BaseItemFragmentActivity.ITEM_ID, id);
            }

            startActivity(intent);
        }
    }

    /**
     * When the Add button in the empty view is clicked
     */
    @Override
    public void onAddButtonClick() {

    }

    /**
     * Get the layout resource for the content view
     * @return
     */
    protected int getContentView() {
        return R.layout.activity_item_fragment;
    }

    protected void createFragment() {
        Class<?> cls = getFragmentClass();

        if (cls == null) {
            throw new NullPointerException("Fragment class can not be null");
        }

        try {
            mListFragment = (BaseItemListFragment)cls.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle args = getFragmentArguments();

            if (args != null) {
                mListFragment.setArguments(args);
            }

            if (mListFragment != null) {
                fragmentManager.beginTransaction()
                        .add(R.id.fragmentFrame, mListFragment)
                        .commit();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get any arguments that need to be passed to fragment
     * @return A bundle
     */
    protected Bundle getFragmentArguments() {
        return null;
    }
}
