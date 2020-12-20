package com.example.realestate.database.table;

import android.database.sqlite.SQLiteDatabase;


import com.example.realestate.common.CustomerType;
import com.example.realestate.common.Gender;
import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class CustomerTable extends BaseTable {
	public static final String TABLE_NAME = "customers";
	
	private static final String TABLE_CREATE = "CREATE TABLE "
			+ TABLE_NAME
			+ "("
			+ BASE_FIELDS_CREATE
			+ PHONE_FIELDS_CREATE
			+ TableColumn.NAME + " text,"
			+ TableColumn.ADDRESS + " text not null,"
			+ TableColumn.EMAIL + " text null,"
			+ TableColumn.WORK_EMAIL + " text null,"
			+ TableColumn.LOCALITY_ID + " integer,"
			+ TableColumn.COMPANY + " text,"
			+ TableColumn.DEPARTMENT + " text,"
			+ TableColumn.TITLE + " text,"
			+ TableColumn.NOTE + " text,"
			+ TableColumn.THUMBNAIL_PATH + " text null,"
			+ TableColumn.GENDER + " integer not null default " + Gender.MALE + ","
			+ TableColumn.DOB + " text,"
			+ TableColumn.TYPE + " int default " + CustomerType.PRIVATE
			+ ")";
	
	public static HashMap<String, String> getProjectionMap() {
		HashMap<String, String> map = BaseTable.getProjectionMap();

		map.put(TableColumn.NAME, TableColumn.NAME);
		map.put(TableColumn.ADDRESS, TableColumn.ADDRESS);
		map.put(TableColumn.EMAIL, TableColumn.EMAIL);
		map.put(TableColumn.WORK_EMAIL, TableColumn.WORK_EMAIL);
		map.put(TableColumn.HOME_PHONE, TableColumn.HOME_PHONE);
		map.put(TableColumn.WORK_PHONE, TableColumn.WORK_PHONE);
		map.put(TableColumn.MOBILE_PHONE, TableColumn.MOBILE_PHONE);
		map.put(TableColumn.LOCALITY_ID, TableColumn.LOCALITY_ID);
		map.put(TableColumn.NOTE, TableColumn.NOTE);
		map.put(TableColumn.GENDER, TableColumn.GENDER);
		map.put(TableColumn.THUMBNAIL_PATH, TableColumn.THUMBNAIL_PATH);
		map.put(TableColumn.DOB, TableColumn.DOB);
		map.put(TableColumn.COMPANY, TableColumn.COMPANY);
		map.put(TableColumn.DEPARTMENT, TableColumn.DEPARTMENT);
		map.put(TableColumn.TITLE, TableColumn.TITLE);
		map.put(TableColumn.TYPE, TableColumn.TYPE);

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
