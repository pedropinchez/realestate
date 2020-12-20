package com.example.realestate.activity;



import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SpinnerAdapter;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.realestate.R;
import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.model.UserAccountCredential;

import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity implements ActionBar.OnNavigationListener {
    protected SpinnerAdapter mActionBarSpinnerAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Update locale to the one chosen by current user
		setCurrentUserLocale();
		
		// Update title after locale is set
		int titleResId = getTitleResource();
		if (titleResId > 0) {
			setTitle(titleResId);
		}
    }
	
	private void setCurrentUserLocale() {
		RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
		UserAccountCredential cred = app.getCurrentUserCredential();
		
		if (cred != null) {
			Locale locale = app.getCurrentUserLocale();

			if (locale != null) {
				Locale.setDefault(locale);

				Configuration config = new Configuration();
				config.locale = locale;
				this.getResources().updateConfiguration(config, null);
			}
		}
	}
	
	protected int getTitleResource() {
		return R.string.add;
	}

	protected int getMenuResource() {
		return 0;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
        int menuRes = getMenuResource();

        if (menuRes > 0) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(getMenuResource(), menu);
        }

		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        return false;
    }

    protected SpinnerAdapter getActionBarSpinnerAdapter() {
        return null;
    }

    protected void updateActionBarSpinner() {
        ActionBar ab = getSupportActionBar();

        if (ab == null) return;

        // Enable action bar drop-down navigation
        mActionBarSpinnerAdapter = getActionBarSpinnerAdapter();

        if (mActionBarSpinnerAdapter != null) {
            // Don't show the title if spinner is available
            ab.setDisplayShowTitleEnabled(false);
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
            ab.setListNavigationCallbacks(mActionBarSpinnerAdapter, this);
        } else {
            ab.setDisplayShowTitleEnabled(true);
            ab.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        }

    }
}
