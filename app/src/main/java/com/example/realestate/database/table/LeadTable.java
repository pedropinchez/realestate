package com.example.realestate.database.table;


import android.database.sqlite.SQLiteDatabase;

import com.example.realestate.common.Furnishing;
import com.example.realestate.common.LeadStatus;
import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class LeadTable extends BaseTable {
    public static final String TABLE_NAME = "leads";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + BASE_FIELDS_CREATE
            + TableColumn.CUSTOMER_ID + " integer not null,"
            + TableColumn.MIN_AREA + " real null,"
            + TableColumn.MAX_AREA + " real null,"
            + TableColumn.MIN_PRICE + " real null,"
            + TableColumn.MAX_PRICE + " real null,"
            + TableColumn.NOTE + " text null,"
            + TableColumn.FURNISHING + " integer not null default " + Furnishing.FURNISHED + ","
            + TableColumn.STATUS + " integer not null default " + LeadStatus.OPEN
            + ")";

    public static HashMap<String, String> getProjectionMap() {
        HashMap<String, String> map = BaseTable.getProjectionMap();

        map.put(TableColumn.CUSTOMER_ID, TableColumn.CUSTOMER_ID);
        map.put(TableColumn.MIN_AREA, TableColumn.MIN_AREA);
        map.put(TableColumn.MAX_AREA, TableColumn.MAX_AREA);
        map.put(TableColumn.MIN_PRICE, TableColumn.MIN_PRICE);
        map.put(TableColumn.MAX_PRICE, TableColumn.MAX_PRICE);
        map.put(TableColumn.NOTE, TableColumn.NOTE);
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
