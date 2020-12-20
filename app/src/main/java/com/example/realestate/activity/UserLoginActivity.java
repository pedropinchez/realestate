package com.example.realestate.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.database.TableColumn;
import com.example.realestate.database.table.UserAccountTable;
import com.example.realestate.model.UserAccountCredential;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.GeneralUtils;


public class UserLoginActivity extends BaseActivity implements OnClickListener {
	private Button btnLogin;
	private Button btnSignup;
	private EditText txtUsername;
	private EditText txtPassword;
	private CheckBox chkRemember;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		
		txtUsername = (EditText)findViewById(R.id.txtUsername);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		
		chkRemember = (CheckBox)findViewById(R.id.chkRememberMe);
		
		btnLogin = (Button)findViewById(R.id.btnLogin);
		btnSignup = (Button)findViewById(R.id.btnSignUp);
		
		btnLogin.setOnClickListener(this);
		btnSignup.setOnClickListener(this);
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.login;
	}

	@Override
	public void onClick(View view) {
		if (view == btnLogin) {
			doUserLogin();
		}
		else if (view == btnSignup) {
			Intent signupIntent = new Intent(this, UserSignupActivity.class);
			startActivity(signupIntent);
		}
	}
	
	private void doUserLogin() {		
		String username = txtUsername.getText().toString();
		String passd = txtPassword.getText().toString();
		
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passd)) {
			Toast.makeText(this, getResources().getString(R.string.user_login_empty_fields), Toast.LENGTH_LONG).show();
			return;
		}
		
		// Get the hash of the password
		passd = GeneralUtils.md5(passd);
		
		Uri uri = ContentDescriptor.ContentUri.USER_ACCOUNT;

		String[] projection = {
				TableColumn._ID,
				TableColumn.NAME,
				TableColumn.STATUS,
				TableColumn.PASSWORD,
				TableColumn.EMAIL,
				TableColumn.LANGUAGE,
                TableColumn.COVER_PIC_PATH
		};

		String selection = TableColumn.USER_NAME + "=? AND " + TableColumn.PASSWORD + "=?";
		String[] selectionArgs = { username, passd };

		Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			UserAccountCredential currentUser = UserAccountCredential.readFromCursor(cursor);
			
			RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
			app.setCurrentUserCredential(currentUser);

			// Mark user status
			if (chkRemember.isChecked()) {
				// Set all users as not logged-in
				ContentValues values = new ContentValues();
				values.put(TableColumn.STATUS, UserAccountTable.UserAccountStatus.ACTIVE);
				getContentResolver().update(uri, values, null, null);
				
				// Mark this user as logged in. Only one user can be remembered
				values.put(TableColumn.STATUS, UserAccountTable.UserAccountStatus.LOGGED_IN);
				String where = TableColumn._ID + "=?";
				String[] whereArgs = { String.valueOf(currentUser.id) };

				getContentResolver().update(uri, values, where, whereArgs);
			}
			
			Intent mainIntent = new Intent(this, MainActivity.class); 
			startActivity(mainIntent);

			// We're done with this activity
			finish();
		} else {
			Toast.makeText(this, getResources().getString(R.string.user_login_fail), Toast.LENGTH_LONG).show();
		}
	}
}
