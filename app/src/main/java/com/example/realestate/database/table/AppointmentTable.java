package com.example.realestate.database.table;

import android.database.sqlite.SQLiteDatabase;


import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class AppointmentTable extends BaseTable {
    public static final String TABLE_NAME = "appointments";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + BASE_FIELDS_CREATE
            + TableColumn.CUSTOMER_ID + " integer null,"
            + TableColumn.FROM_TIME + " text not null,"
            + TableColumn.TO_TIME + " text not null,"
            + TableColumn.TITLE + " text not null,"
            + TableColumn.LOCATION + " text null,"
            + TableColumn.NOTE + " text null"
            + ")";

    public static HashMap<String, String> getProjectionMap() {
        HashMap<String, String> map = BaseTable.getProjectionMap();

        map.put(TableColumn.CUSTOMER_ID, TableColumn.CUSTOMER_ID);
        map.put(TableColumn.FROM_TIME, TableColumn.FROM_TIME);
        map.put(TableColumn.TO_TIME, TableColumn.TO_TIME);
        map.put(TableColumn.NOTE, TableColumn.NOTE);
        map.put(TableColumn.TITLE, TableColumn.TITLE);
        map.put(TableColumn.LOCATION, TableColumn.LOCATION);

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
