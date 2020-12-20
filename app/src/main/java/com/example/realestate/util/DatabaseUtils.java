package com.example.realestate.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;



import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.database.TableColumn;
import com.example.realestate.model.UserAccountCredential;

public class DatabaseUtils {
	public static Cursor loadItemFromDatabase(Context context, Uri contentUri, long itemId) {
        contentUri = contentUri.withAppendedPath(contentUri, String.valueOf(itemId));
		String selection = TableColumn._ID + "=?";
		String[] selectionArgs = { String.valueOf(itemId) };

		Cursor cursor = context.getContentResolver().query(contentUri, 
				null, 
				selection,
				selectionArgs,
				null);
		
		// NOTE: this is important to move to the first record before reading
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
		}
		
		return cursor;
	}
	
	public static boolean deleteItem(Context context, Uri contentUri, long itemId) {
        contentUri = contentUri.withAppendedPath(contentUri, String.valueOf(itemId));
		int count = context.getContentResolver().delete(contentUri, null, null);
		return count == 1;
	}
	
	public static long saveItemToDatabase(Activity activity, Uri contentUri, ContentValues values) {
		boolean update = values.containsKey(TableColumn._ID);

        if (update) {
            contentUri = contentUri.withAppendedPath(contentUri, String.valueOf(values.getAsLong(TableColumn._ID)));
        }

		UserAccountCredential currentUser = ((RealEstateBrokerApp)activity.getApplication()).getCurrentUserCredential();

		if (currentUser != null) {
			values.put(update ? TableColumn.UPDATED_BY : TableColumn.CREATED_BY, currentUser.id);
		}
		
		if (update) {
			String where = TableColumn._ID + "=?";
			String[] whereArgs = { String.valueOf(values.getAsLong(TableColumn._ID)) };
			return activity.getContentResolver().update(contentUri, values, where, whereArgs);
		} else {
			Uri itemUri = activity.getContentResolver().insert(contentUri, values);
			if (itemUri != null) {
				return Long.parseLong(itemUri.getLastPathSegment());
			}
		}
		
		return 0;
	}
}
