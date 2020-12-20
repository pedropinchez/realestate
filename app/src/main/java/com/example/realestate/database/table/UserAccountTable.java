package com.example.realestate.database.table;


import android.database.sqlite.SQLiteDatabase;

import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class UserAccountTable extends BaseTable {
	public static final String TABLE_NAME = "user_accounts";
	
	public interface UserAccountStatus {
		static final int DISABLED = -2;
		static final int ACTIVE = 0;
		static final int LOGGED_IN = -1;
	}
	
	public static final String TABLE_CREATE = "CREATE TABLE "
			+ TABLE_NAME
			+ "("
			+ BASE_FIELDS_CREATE
			+ TableColumn.NAME + " text not null,"
			+ TableColumn.EMAIL + " text null,"
			+ TableColumn.USER_NAME + " text not null,"
			+ TableColumn.THUMBNAIL_PATH + " text null,"
			+ TableColumn.COVER_PIC_PATH + " text null,"
			+ TableColumn.PASSWORD + " text not null,"
			+ TableColumn.LANGUAGE + " text default 'vi_VN',"
			+ TableColumn.STATUS + " integer default " + UserAccountStatus.DISABLED
			+ ")";
	
	public static HashMap<String, String> getProjectionMap() {
		HashMap<String, String> map = BaseTable.getProjectionMap();
		
		map.put(TableColumn.NAME, TableColumn.NAME);
		map.put(TableColumn.EMAIL, TableColumn.EMAIL);
		map.put(TableColumn.USER_NAME, TableColumn.USER_NAME);
		map.put(TableColumn.THUMBNAIL_PATH, TableColumn.THUMBNAIL_PATH);
		map.put(TableColumn.COVER_PIC_PATH, TableColumn.COVER_PIC_PATH);
		map.put(TableColumn.PASSWORD, TableColumn.PASSWORD);
		map.put(TableColumn.LANGUAGE, TableColumn.LANGUAGE);
		map.put(TableColumn.STATUS, TableColumn.STATUS);

		return map;
	}
	
	public static void onCreate(SQLiteDatabase db) {
		BaseTable.onCreate(db);
		db.execSQL(TABLE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		BaseTable.onUpgrade(db, oldVersion, newVersion);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}
