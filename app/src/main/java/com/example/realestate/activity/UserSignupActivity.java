package com.example.realestate.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.database.TableColumn;
import com.example.realestate.model.UserAccountCredential;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.MediaUtils;
import com.ipaulpro.afilechooser.utils.FileUtils;


public class UserSignupActivity extends BaseFormActivity { 
	private final int FILE_CHOOSER = 0x1;
	private final int THUMBNAIL_SIZE = 64;
	private final int COVER_WIDTH = 280;
	private final int COVER_HEIGHT = 100;

	private Button btnSignup;
	private EditText txtFullName;
	private EditText txtUsername;
	private EditText txtPassword;
	private EditText txtPasswordConfirm;
	private ImageView mImgProfilePic;
	
	private String mThumbnailPath;
	private String mCoverPath;

	private Spinner spnLanguage;
	
	private String[] languages = {
			"en_US",
			"vi_VN"
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getItemId() > 0) {
			btnSignup.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected int getEditModeTitleResource() {
		return R.string.settings;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.signup;
	}
	
	@Override
	protected void checkLoginRequired() {
		// Login is not required for this
		return;
	}
	
	@Override
	protected void initContentView() {
		super.initContentView();

		btnSignup = (Button)findViewById(R.id.btnSignUp);
		
		txtFullName = (EditText)findViewById(R.id.txtFullName);
		txtUsername = (EditText)findViewById(R.id.txtUsername);
		txtPassword = (EditText)findViewById(R.id.txtPassword);
		txtPasswordConfirm = (EditText)findViewById(R.id.txtPasswordConfirm);
		
		mImgProfilePic = (ImageView)findViewById(R.id.imgProfilePic);
		mImgProfilePic.setOnClickListener(this);

		spnLanguage = (Spinner)findViewById(R.id.spnLanguage);
		spnLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long id) {
				if (position > 0) {
					Toast.makeText(UserSignupActivity.this, R.string.change_lang_alert, Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapter) {
			}
		});
		
		btnSignup.setOnClickListener(this);
	}
	
	@Override
	protected int getContentView() {
		return R.layout.activity_user_signup;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
	
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected Uri getItemContentUri() {
		return ContentDescriptor.ContentUri.USER_ACCOUNT;
	}
	
	@Override
	protected boolean validateForm() {		
		String fullName = txtFullName.getText().toString();
		String password = txtPassword.getText().toString();
		String passwordConfirm = txtPasswordConfirm.getText().toString();
		
		// Check form validity
		if (TextUtils.isEmpty(fullName)) {
			showFormElementError(R.string.user_alert_name, txtFullName);
			return false;
		}

		if (getItemId() <= 0 && TextUtils.isEmpty(password)) {
			showFormElementError(R.string.user_alert_password, txtPassword);
			return false;
		}
		
		if (!TextUtils.isEmpty(password) && password.compareTo(passwordConfirm) != 0) {
			showFormElementError(R.string.user_alert_password_confirm, txtPassword);
			return false;
		}
		
		return true;
	}

	@Override
	protected void loadItem(long itemId) {
		Cursor cursor = loadItemFromDatabase(itemId);

		if (cursor != null && cursor.getCount() == 1) {
			txtFullName.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NAME));
			String langCode = CursorUtils.getRecordStringValue(cursor, TableColumn.LANGUAGE);
			MediaUtils.setImageFromFile(mImgProfilePic, CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH));

			int langIdx = 0;
			for(int i = 0; i < languages.length; i++) {
				if (languages[i].equals(langCode)) {
					langIdx = i + 1;
					break;
				}
			}
			spnLanguage.setSelection(langIdx);
			
			cursor.close();
		}
	}
	
	@Override
	protected void saveItem() {
		super.saveItem();
		String username = txtUsername.getText().toString();
		
		if (getItemId() <= 0 && isUserExisting(username)) {
			Toast.makeText(this, R.string.user_username_exist_alert, Toast.LENGTH_LONG).show();
			txtUsername.requestFocus();
			return;
		}
		
		String fullName = txtFullName.getText().toString();
		String password = txtPassword.getText().toString();
		
		ContentValues values = new ContentValues();
		values.put(TableColumn.USER_NAME, username);
		values.put(TableColumn.NAME, fullName);
		values.put(TableColumn.PASSWORD, password);
		values.put(TableColumn.THUMBNAIL_PATH, mThumbnailPath);
		values.put(TableColumn.COVER_PIC_PATH, mCoverPath);
		
		int langIdx = spnLanguage.getSelectedItemPosition();
		if (langIdx == 0) {
			langIdx = 1;
		} else {
			langIdx--;
		}
		
		values.put(TableColumn.LANGUAGE, languages[langIdx]);
		
		if (!saveItemToDatabase(values)) {
			Toast.makeText(this, R.string.user_signup_fail, Toast.LENGTH_LONG).show();
		} else {
			// Update user name if it's changed
			if (getItemId() > 0) {
				RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
				UserAccountCredential cred = app.getCurrentUserCredential();

				if (cred != null) {
					cred.fullName = fullName;
					HomeListActivity home = HomeListActivity.home;

					if (home != null) {
						home.welcomeUser();
					}
				}
			}
		}
	}
	
	private boolean isUserExisting(String username) {
		String[] projection = {
				TableColumn._ID,
				TableColumn.USER_NAME
		};
		String selection = TableColumn.USER_NAME + "=?";
		String[] selectionArgs = {
				username
		};

		Cursor cursor = getContentResolver().query(ContentDescriptor.ContentUri.USER_ACCOUNT,
				projection, 
				selection, 
				selectionArgs, 
				null);
		
		if (cursor != null) {
			boolean existing = cursor.getCount() > 0;
			cursor.close();
			return existing;
		}

		return false;
	}
	
	@Override
	protected void deleteItem() {
		super.deleteItem();
		doLogout();
	}

	private void doLogout() {
		RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();

		// Reset the cache in application
		app.setCurrentUserCredential(null);

		// Show login form
		Intent loginIntent = new Intent(this, UserLoginActivity.class);
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(loginIntent);

		HomeListActivity.home.finish();
	}
	
	private void selectPicture() {
		// Create the ACTION_GET_CONTENT Intent
		Intent getContentIntent = FileUtils.createGetContentIntent();
		getContentIntent.setType(FileUtils.MIME_TYPE_IMAGE);

		Intent intent = Intent.createChooser(getContentIntent, getResources().getString(R.string.select_file));
		startActivityForResult(intent, FILE_CHOOSER);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILE_CHOOSER && resultCode == Activity.RESULT_OK) {
			final Uri uri = data.getData();

			// Get the File path from the Uri
			String path = FileUtils.getPath(this, uri);

			// Alternatively, use FileUtils.getFile(Context, Uri)
			if (path != null && FileUtils.isLocal(path)) {
				String thumbnailPath = MediaUtils.saveThumbnail(path, "user_thumbs", THUMBNAIL_SIZE, THUMBNAIL_SIZE);
				String coverPath = MediaUtils.saveThumbnail(path, "user_covers", COVER_WIDTH, COVER_HEIGHT);

				if (thumbnailPath != null) {
					if (MediaUtils.setImageFromFile(mImgProfilePic, thumbnailPath)) {
						mThumbnailPath = thumbnailPath;
					}
				}

				if (coverPath != null) {
					mCoverPath = coverPath;
				}
			}	
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		if (v == btnSignup) {
			doSaveItem();
		}
		else if (v == mImgProfilePic) {
			selectPicture();
		}
	}
}
