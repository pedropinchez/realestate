package com.example.realestate.activity;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AlertDialog;

import com.example.realestate.R;
import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.database.TableColumn;
import com.example.realestate.model.UserAccountCredential;

public abstract class BaseFormActivity extends LoggedInRequiredActivity implements OnClickListener {
	public static final String ITEM_ID		= "item_id";
	public static final String PICK_RESULT	= "pick_result";

	private long itemId = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		int contentView = getContentView();
		
		if (contentView > 0) {
			setContentView(contentView);
		}
		
		initContentView();
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(ITEM_ID)) {
				itemId = bundle.getLong(ITEM_ID);
				Log.d(BaseFormActivity.class.getName(), "Item ID: " + itemId);
				loadItem(itemId);
			}
		}
		
		if (itemId > 0) {
			int editTitle = getEditModeTitleResource();
			if (editTitle > 0) {
				setTitle(editTitle);
			}
		}
	}
	
	protected boolean isEditMode() {
		return itemId > 0;
	}

	protected int getEditModeTitleResource() {
		return R.string.edit;
	}
	
	protected abstract int getContentView();
	protected abstract Uri getItemContentUri();
	
	protected void initContentView() {
		// Do something here
	}
	
	private void showFormElementError(String message, View element) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		element.requestFocus();
	}
	
	protected void showFormElementError(int messageResource, View element) {
		String message = getResources().getString(messageResource);
		showFormElementError(message, element);
	}

	protected long getItemId() {
		return itemId;
	}
	
	protected void loadItem(long itemId) {
		
	}
	
	protected boolean validateForm() {
		return true;
	}
	
	protected void saveItem() {
		return;
	}
	
	protected void doDeleteItem() {
		new AlertDialog.Builder(this)
        .setTitle(R.string.delete)
        .setMessage(R.string.delete_alert)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	deleteItem();
            }
        })
        .setNegativeButton(android.R.string.no, null)
        .show();
	}
	
	protected void deleteItem() {
		Uri uri = getItemContentUri();
		String where = TableColumn._ID + "=?";
		String[] whereArgs = { String.valueOf(itemId) };
		
		int count = getContentResolver().delete(uri, where, whereArgs);
		
		if (count == 1) {
			finish();
		}
	}
	
	protected void doSaveItem() {
		if (validateForm()) {
			saveItem();
		}
	}
	
	protected Cursor loadItemFromDatabase(long itemId) {
		// Load product item from database
		String selection = TableColumn._ID + "=?";
		String[] selectionArgs = { String.valueOf(itemId) };

		Uri uri = getItemContentUri();
		Cursor cursor = getContentResolver().query(uri, null, selection, selectionArgs, null);
		
		// NOTE: this is important to move to the first record before reading
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
		}
		
		return cursor;
	}

	protected boolean saveItemToDatabase(ContentValues values) {
		long itemId = getItemId();
		boolean success = false;

		Uri uri = getItemContentUri();
		UserAccountCredential currentUser = ((RealEstateBrokerApp)getApplication()).getCurrentUserCredential();

		if (itemId > 0) {
			values.put(TableColumn._ID, itemId);
			
			if (currentUser != null) {
				values.put(TableColumn.UPDATED_BY, currentUser.id);
			}
		}
		
		// Mark who is creating/updating item
		else {
			if (currentUser != null) {
				values.put(TableColumn.CREATED_BY, currentUser.id);
			}
		}

		if (itemId <= 0) {
			Uri itemUri = getContentResolver().insert(uri, values);
			success = itemUri != null;
		}
		else {
			String where = TableColumn._ID + "=?";
			String[] whereArgs = { String.valueOf(itemId) };
			success = getContentResolver().update(uri, values, where, whereArgs) == 1;
		}
		
		if (success) {
			finish();
		}
		
		return success;
	}

	@Override
	public void onClick(View v) {
	}
	
	@Override
	protected int getMenuResource() {
		return R.menu.menu_base_form;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        boolean retval = super.onCreateOptionsMenu(menu);

		// Search widget
		MenuItem deleteItem = menu.findItem(R.id.action_delete);
		if (deleteItem != null && itemId <= 0) {
			deleteItem.setVisible(false);
		}

        return retval;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_save:
			doSaveItem();
			return true;

		case R.id.action_delete:
			doDeleteItem();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
