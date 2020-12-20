package com.example.realestate.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.realestate.R;
import com.example.realestate.adapter.CustomerViewFragmentAdapter;
import com.example.realestate.fragment.CustomerViewFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewActivity extends LoggedInRequiredActivity implements ActionBar.TabListener {
	private static final String ITEM_ID = "item_id";
	
    private ViewPager mViewPager;
	private CustomerViewFragmentAdapter mFragmentAdapter;

	private long mItemId;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_view);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(ITEM_ID)) {
				mItemId = bundle.getLong(ITEM_ID);
			}
		}

		List<Bundle> fragmentArgs = new ArrayList<Bundle>();
		Bundle args = new Bundle();
		args.putLong(ITEM_ID, mItemId);
		fragmentArgs.add(args);
		fragmentArgs.add(args);

        mFragmentAdapter = new CustomerViewFragmentAdapter(getSupportFragmentManager(), fragmentArgs);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        	@Override
        	public void onPageSelected(int position) {
        		// When swiping between pages, select the corresponding tab.
        		getSupportActionBar().setSelectedNavigationItem(position);
        	}
        });

		createTabs(actionBar);
	}
	
	private void createTabs(ActionBar actionBar) {
		actionBar.addTab(actionBar.newTab().setText("Profile").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Properties").setTabListener(this));
	}

	@Override
	protected int getMenuResource() {
		return R.menu.menu_base_item_view;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.customer;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		case R.id.action_edit:
			Intent intent = new Intent(this, CustomerFormFragmentActivity.class);
			intent.putExtra(ITEM_ID, mItemId);
			startActivity(intent);
			return true;

		case R.id.action_delete:
			new AlertDialog.Builder(this)
	        .setTitle(R.string.delete)
	        .setMessage(R.string.delete_alert)
	        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	CustomerViewFragment fragment = (CustomerViewFragment)mFragmentAdapter.getItem(0);

	            	if (fragment.deleteItem()) {
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
	
	@Override
	protected void onResume() {
		super.onResume();
		// Reload the view when returning from Edit activity
		// TODO: Check if this is done twice (one here and one in fragment)
		CustomerViewFragment fragment = (CustomerViewFragment)mFragmentAdapter.getItem(0);
		if (fragment != null && fragment.getActivity() != null) {
			fragment.reloadItem();
		}
	}
	
	@Override
	public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		if (mViewPager != null) {
			mViewPager.setCurrentItem(tab.getPosition());
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
	}
}
