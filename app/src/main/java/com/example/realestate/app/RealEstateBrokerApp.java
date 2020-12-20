package com.example.realestate.app;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;


import com.example.realestate.activity.UserLoginActivity;
import com.example.realestate.database.TableColumn;
import com.example.realestate.database.table.UserAccountTable;
import com.example.realestate.model.UserAccountCredential;
import com.example.realestate.provider.ContentDescriptor;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Locale;

public class RealEstateBrokerApp extends Application {
	// Default user locale when app starts without user's selected preference
	public static final String defaultUserLocale = "vi_VN";
    private static Context sAppContext = null;

	private UserAccountCredential mCurrentUser;
	private Locale mCurrentUserLocale;
	
	public static final String[] supportedLocales = {
		"en_US",
		"vi_VN"
	};

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();

        // Init ImageLoader config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    public static Context getContext() {
        return sAppContext;
    }
	
	public void setCurrentUserCredential(UserAccountCredential user) {
		mCurrentUser = user;
	}
	
	public UserAccountCredential getCurrentUserCredential() {
		return mCurrentUser;
	}
	
	public Locale getCurrentUserLocale() {
		return mCurrentUserLocale;
	}
	
	public void setCurrentUserLocale() {
		if (mCurrentUser != null) {
			changeLocale(mCurrentUser.locale);
		}
	}
	
	public void setDefaultLocale() {
		changeLocale(defaultUserLocale);
	}
	
	public void changeLocale(String code) {
		boolean valid = false;
		
		for(String lolCode : supportedLocales) {
			if (lolCode.equals(code)) {
				valid = true;
				break;
			}
		}
		
		if (!valid) {
			code = "vi_VN";
		}
		 
		mCurrentUserLocale = new Locale(code); 
		Locale.setDefault(mCurrentUserLocale);
		Configuration config = new Configuration();
		config.locale = mCurrentUserLocale;
		getApplicationContext().getResources().updateConfiguration(config, null);	
	}

    public void logout(Activity context) {
        ContentValues values = new ContentValues();
        values.put(TableColumn.STATUS, UserAccountTable.UserAccountStatus.ACTIVE);

        // Set all users as not logged-in
        getContentResolver().update(ContentDescriptor.ContentUri.USER_ACCOUNT, values, null, null);

        // Reset the cache in application
        setCurrentUserCredential(null);

        // Show login form
        Intent loginIntent = new Intent(this, UserLoginActivity.class);
        startActivity(loginIntent);

        context.finish();
    }
}
