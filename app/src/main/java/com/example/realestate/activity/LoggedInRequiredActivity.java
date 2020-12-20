package com.example.realestate.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.model.UserAccountCredential;


public class LoggedInRequiredActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		checkLoginRequired();

		// Enable up navigation for the app icon
	    ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    actionBar.setDisplayShowTitleEnabled(true);
	}
	
	protected void checkLoginRequired() {
		// Check if user is logged in
		RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
		UserAccountCredential currentUser = app.getCurrentUserCredential();

		if (currentUser == null) {
			Intent loginIntent = new Intent(this, UserLoginActivity.class);
			startActivity(loginIntent);
			finish();
		}	
	}
}
