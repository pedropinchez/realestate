package com.example.realestate.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;

import com.example.realestate.R;
import com.example.realestate.fragment.BaseItemFragment;


public abstract class BaseItemFragmentActivity extends LoggedInRequiredActivity {
    protected static final String ITEM_ID = "item_id";
    private static final String TAG_ITEM_FRAGMENT = "item_fragment";

    protected long mItemId;
    protected BaseItemFragment mItemFragment;

    protected abstract Class<?> getFragmentClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(ITEM_ID)) {
                mItemId = bundle.getLong(ITEM_ID);
            }
        }

        // Only create the fragment when activity it's first created
        if (savedInstanceState == null) {
            createFragment();
        }
        // Otherwise, get the fragment from fragment manager. Note that
        // fragment still gets destroyed (onDestroy called) when configuration
        // changes but fragment manager automatically recreates it for us.
        else {
            retrieveFragment();
        }
	}
	
	protected int getContentView() {
		return R.layout.activity_item_fragment;
	}

    protected void retrieveFragment() {
        FragmentManager fm = getSupportFragmentManager();
        mItemFragment = (BaseItemFragment)fm.findFragmentByTag(TAG_ITEM_FRAGMENT);
        // No need to set fragment arguments here since it's still active
    }
	
	protected void createFragment() {
		Class<?> cls = getFragmentClass();
		
		if (cls == null) {
			throw new NullPointerException("Fragment class can not be null");
		}
		
		try {
			mItemFragment = (BaseItemFragment)cls.newInstance();
		    FragmentManager fragmentManager = getSupportFragmentManager();
		    Bundle args = getFragmentArguments();
		    
		    if (args != null) {
		    	mItemFragment.setArguments(args);
		    }

		    if (mItemFragment != null) {
                // TODO: check if we really need this
		    	fragmentManager.beginTransaction()
		    	.replace(R.id.fragmentFrame, mItemFragment, TAG_ITEM_FRAGMENT)
		    	.commit();
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Bundle getFragmentArguments() {
		if (mItemId <= 0) return null;

		Bundle bundle = new Bundle();
		bundle.putLong(ITEM_ID, mItemId);

		return bundle;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.delete_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mItemFragment.deleteItem()) {
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
