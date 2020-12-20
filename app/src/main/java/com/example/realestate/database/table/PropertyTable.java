package com.example.realestate.database.table;


import android.database.sqlite.SQLiteDatabase;

import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class PropertyTable extends BaseTable {
	public static final String TABLE_NAME = "properties";

	public static final String TABLE_CREATE = "CREATE TABLE "
			+ TABLE_NAME
			+ "("
			+ BASE_FIELDS_CREATE
			+ TableColumn.NAME + " text not null,"
			+ TableColumn.DESCRIPTION + " text null,"
			+ TableColumn.CODE + " text null,"
			+ TableColumn.BROKERAGE + " real null,"
			+ TableColumn.BROKERAGE_TYPE + " integer default 0,"
			+ TableColumn.PRICE + " real null,"
			+ TableColumn.PRICE_UNIT_ID + " integer null,"
			+ TableColumn.PRICE_POSTFIX + " text null,"
			+ TableColumn.ADDRESS + " text null,"
			+ TableColumn.COVER_PIC_PATH + " text null,"
			+ TableColumn.LOCALITY_ID + " integer null,"
			+ TableColumn.BEDROOMS + " integer null,"
			+ TableColumn.BATHROOMS + " integer null,"
			+ TableColumn.BALCONIES + " integer null,"
			+ TableColumn.GARAGES + " integer null,"
            + TableColumn.AREA_UNIT_ID + " integer null,"
			+ TableColumn.COVERED_AREA + " real null,"
            + TableColumn.CARPET_AREA + " real nul,"
            + TableColumn.BUILTUP_AREA + " real null,"
            + TableColumn.SUPER_BUILTUP_AREA + " real null,"
			+ TableColumn.CUSTOMER_ID + " integer null,"
			+ TableColumn.FLOOR_NUMBER + " integer null,"
			+ TableColumn.TOTAL_FLOORS + " integer null,"
			+ TableColumn.CONSTRUCTION_YEAR + " integer null,"
			+ TableColumn.FURNISHING + " integer null,"
			+ TableColumn.FACING + " integer null,"
			+ TableColumn.ALLOTTED_PARKING + " integer null"
			+ ")";
	
	public static HashMap<String, String> getProjectionMap() {
		HashMap<String, String> map = BaseTable.getProjectionMap();

		map.put(TableColumn.NAME, TableColumn.NAME);
		map.put(TableColumn.DESCRIPTION, TableColumn.DESCRIPTION);
		map.put(TableColumn.CODE, TableColumn.CODE);
		map.put(TableColumn.COVER_PIC_PATH, TableColumn.COVER_PIC_PATH);
		map.put(TableColumn.BROKERAGE, TableColumn.BROKERAGE);
		map.put(TableColumn.BROKERAGE_TYPE, TableColumn.BROKERAGE_TYPE);
		map.put(TableColumn.PRICE, TableColumn.PRICE);
		map.put(TableColumn.PRICE_UNIT_ID, TableColumn.PRICE_UNIT_ID);
		map.put(TableColumn.PRICE_POSTFIX, TableColumn.PRICE_POSTFIX);
		map.put(TableColumn.ADDRESS, TableColumn.ADDRESS);
		map.put(TableColumn.LOCALITY_ID, TableColumn.LOCALITY_ID);
		map.put(TableColumn.BEDROOMS, TableColumn.BEDROOMS);
		map.put(TableColumn.BATHROOMS, TableColumn.BATHROOMS);
		map.put(TableColumn.BALCONIES, TableColumn.BALCONIES);
		map.put(TableColumn.GARAGES, TableColumn.GARAGES);
		map.put(TableColumn.AREA_UNIT_ID, TableColumn.AREA_UNIT_ID);
        map.put(TableColumn.COVERED_AREA, TableColumn.COVERED_AREA);
        map.put(TableColumn.CARPET_AREA, TableColumn.CARPET_AREA);
        map.put(TableColumn.BUILTUP_AREA, TableColumn.BUILTUP_AREA);
        map.put(TableColumn.SUPER_BUILTUP_AREA, TableColumn.SUPER_BUILTUP_AREA);
		map.put(TableColumn.CUSTOMER_ID, TableColumn.CUSTOMER_ID);
		map.put(TableColumn.FLOOR_NUMBER, TableColumn.FLOOR_NUMBER);
		map.put(TableColumn.TOTAL_FLOORS, TableColumn.TOTAL_FLOORS);
		map.put(TableColumn.CONSTRUCTION_YEAR, TableColumn.CONSTRUCTION_YEAR);
		map.put(TableColumn.FURNISHING, TableColumn.FURNISHING);
		map.put(TableColumn.FACING, TableColumn.FACING);
		map.put(TableColumn.ALLOTTED_PARKING, TableColumn.ALLOTTED_PARKING);

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
