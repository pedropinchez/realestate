package com.example.realestate.util;


import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.text.TextUtils;

import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;

public class CursorUtils {
	public static String getRecordStringValue(Cursor cursor, String colName) {
		int colIndex = cursor.getColumnIndexOrThrow(colName);
		return cursor.getString(colIndex);
	}

    public static String getRecordStringValue(Cursor cursor, String colName, String defValue) {
        String value = getRecordStringValue(cursor, colName);
        return TextUtils.isEmpty(value) ? defValue : value;
    }

	public static int getRecordIntValue(Cursor cursor, String colName) {
		int colIndex = cursor.getColumnIndexOrThrow(colName);
		return cursor.getInt(colIndex);
	}

    public static long getRecordLongValue(Cursor cursor, String colName) {
        int colIndex = cursor.getColumnIndexOrThrow(colName);
        return cursor.getLong(colIndex);
    }

	public static float getRecordFloatValue(Cursor cursor, String colName) {
		int colIndex = cursor.getColumnIndexOrThrow(colName);
		return cursor.getFloat(colIndex);
	}
	
	public static int getItemPositionFromId(Cursor cursor, long id) {
		int pos = -1;
		cursor.moveToPosition(-1);

		while (cursor.moveToNext()) {
			pos++;

			long itemId = getRecordIntValue(cursor, TableColumn._ID);
			if (itemId == id) {
				break;
			}
		}

		cursor.moveToFirst();
		return pos;
	}
	
	public static Cursor makeMergedVocabularySpinnerCursor(Context context, Cursor data, int selectStr) {
		// Insert extra item to top of the list
		MatrixCursor extras = new MatrixCursor(new String[] {
				TableColumn._ID,
				TableColumn.TITLE
		});

		extras.addRow(new String[] { "0", context.getString(selectStr) });
		Cursor[] cursors = { extras, data };
		return new MergeCursor(cursors);
	}

	public static Loader<Cursor> createTaxonomyCursorLoader(Context context, String type) {
		String[] projection = {
				TableColumn._ID,
				TableColumn.TITLE
		};

		CursorLoader loader = new CursorLoader(context,
				ContentDescriptor.ContentUri.ExtendedUri.VOCABULARY_GROUP_BY_PARENT,
				projection, 
				"C." + TableColumn.TAXONOMY + " = ? ",
				new String[] { type },
				null);

		return loader;
	}
}
