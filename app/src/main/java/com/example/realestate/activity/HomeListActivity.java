package com.example.realestate.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.example.realestate.R;
import com.example.realestate.adapter.HomeMenuListAdapter;
import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.database.TableColumn;
import com.example.realestate.database.table.UserAccountTable;
import com.example.realestate.model.ListMenuItem;
import com.example.realestate.model.UserAccountCredential;
import com.example.realestate.provider.ContentDescriptor;


public class HomeListActivity extends LoggedInRequiredActivity implements OnItemClickListener {
	public static HomeListActivity home;
	
	ListView mListView;
	TextView lblCurrentUser;
	
	List<ListMenuItem> mItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(false);

		setContentView(R.layout.activity_home_list);
		
		lblCurrentUser = (TextView)findViewById(R.id.lblCurrentUser);
		mListView = (ListView)findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);
		
		welcomeUser();
		initAdapter();
		
		home = this;
	}

	private void doLogout() {
		RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();

		ContentValues values = new ContentValues();
		values.put(TableColumn.STATUS, UserAccountTable.UserAccountStatus.ACTIVE);

		// Set all users as not logged-in
		getContentResolver().update(ContentDescriptor.ContentUri.USER_ACCOUNT, values, null, null);

		// Reset the cache in application
		app.setCurrentUserCredential(null);
		
		// Show login form
		Intent loginIntent = new Intent(this, UserLoginActivity.class);
		startActivity(loginIntent);

		finish();
	}

	public void welcomeUser() {
		RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
		UserAccountCredential cred = app.getCurrentUserCredential();

		if (cred != null) {
			lblCurrentUser.setText(cred.fullName);
		}
	}

	private void initAdapter() {
		mItems = new ArrayList<ListMenuItem>();
		
		mItems.add(new ListMenuItem(R.string.settings_tools, 
				R.drawable.saoviet_ic_settings,
				SettingsToolsActivity.class));

		mItems.add(new ListMenuItem(R.string.about_help, 
				R.drawable.saoviet_ic_help,
				AboutHelpActivity.class));

		mItems.add(new ListMenuItem(R.string.logout, 
				R.drawable.saoviet_ic_signout,
				PropertyFormActivity.class));

		mItems.add(new ListMenuItem(R.string.logout, 
				R.drawable.saoviet_ic_signout,
				null));
		
		mListView.setAdapter(new HomeMenuListAdapter(this, 0, mItems));
	}

	@Override
	public void onItemClick(AdapterView<?> group, View view, int position, long id) {
		ListMenuItem item = mItems.get(position);
		int length = mItems.size();
		
		if (position == length - 1) {
			doLogout();
		}
		else {
			Class<?> intentClass = item.intentClass;
			if (intentClass != null) {
				Intent intent = new Intent(this, intentClass);
				startActivity(intent);
			}
		}
	}
}
