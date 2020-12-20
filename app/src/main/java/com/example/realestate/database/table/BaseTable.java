package com.example.realestate.database.table;

import java.util.HashMap;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.realestate.database.TableColumn;


public class BaseTable {
	public static final String BASE_FIELDS_CREATE = TableColumn._ID + " integer primary key autoincrement,"
			+ TableColumn.CREATED_AT + " text,"
			+ TableColumn.UPDATED_AT + " text,"
			+ TableColumn.USER_ID	+ " integer default 0,"
			+ TableColumn.CREATED_BY + " integer default 0,"
			+ TableColumn.UPDATED_BY + " integer default 0,";
	
	public static final String PHONE_FIELDS_CREATE = ""
			+ TableColumn.HOME_PHONE + " text,"
			+ TableColumn.WORK_PHONE + " text,"
			+ TableColumn.MOBILE_PHONE + " text, ";
	
	public static HashMap<String, String> getProjectionMap() {
		HashMap<String, String> map = new HashMap<String, String>();

		map.put(TableColumn._ID, TableColumn._ID);
		map.put(TableColumn.CREATED_AT, TableColumn.CREATED_AT);
		map.put(TableColumn.UPDATED_AT, TableColumn.UPDATED_AT);
		map.put(TableColumn.CREATED_BY, TableColumn.CREATED_BY);
		map.put(TableColumn.UPDATED_BY, TableColumn.UPDATED_BY);
		map.put(TableColumn.USER_ID, TableColumn.USER_ID);
		
		return map;
	}

	public static void onCreate(SQLiteDatabase db) {
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(BaseTable.class.getName(), "Upgrading from version " + oldVersion + " to version " + newVersion + ", which will destroy all the data");
	}
}
