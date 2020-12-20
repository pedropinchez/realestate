package com.example.realestate.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.realestate.R;
import com.example.realestate.adapter.HomeMenuListAdapter;
import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.model.ListMenuItem;
import com.example.realestate.model.UserAccountCredential;


public class SettingsToolsActivity extends LoggedInRequiredActivity implements OnItemClickListener {
	ListView mListView;
	Button btnSettings;
	
	List<ListMenuItem> mItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.activity_settings_tools);
		
		btnSettings = (Button)findViewById(R.id.btnSettings);
		btnSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				launchSettings();
			}
		});
		
		mListView = (ListView)findViewById(R.id.listview);
		mListView.setOnItemClickListener(this);

		initAdapter();
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.settings_tools;
	}

	private void launchSettings() {
		Intent settingsIntent = new Intent(this, UserSignupActivity.class);

		RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
		UserAccountCredential currentUser = app.getCurrentUserCredential();
		settingsIntent.putExtra(BaseFormActivity.ITEM_ID, currentUser.id);

		startActivity(settingsIntent);
	}
	
	private void initAdapter() {
		mItems = new ArrayList<ListMenuItem>();
		
		mItems.add(new ListMenuItem(R.string.import_customers, 
				R.drawable.saoviet_ic_import,
				CustomerImportFormActivity.class));

		mItems.add(new ListMenuItem(R.string.facilities, 
				R.drawable.saoviet_ic_import,
				NearbyFacilityListActivity.class));

		mItems.add(new ListMenuItem(R.string.localities, 
				R.drawable.saoviet_ic_import,
				LocalityListActivity.class));

		mItems.add(new ListMenuItem(R.string.property_statuses, 
				R.drawable.saoviet_ic_import,
				PropertyStatusListActivity.class));

		mItems.add(new ListMenuItem(R.string.purposes, 
				R.drawable.saoviet_ic_import,
				PropertyTypeListActivity.class));

		mItems.add(new ListMenuItem(R.string.area_units, 
				R.drawable.saoviet_ic_import,
				AreaUnitListActivity.class));

		mItems.add(new ListMenuItem(R.string.currency_units, 
				R.drawable.saoviet_ic_import,
				CurrencyUnitListActivity.class));

		mListView.setAdapter(new HomeMenuListAdapter(this, 0, mItems));
	}

	@Override
	public void onItemClick(AdapterView<?> group, View view, int position, long id) {
		ListMenuItem item = mItems.get(position);
		
		Class<?> intentClass = item.intentClass;
		if (intentClass != null) {
			Intent intent = new Intent(this, intentClass);
			startActivity(intent);
		}
	}
}
