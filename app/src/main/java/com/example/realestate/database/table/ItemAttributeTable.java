package com.example.realestate.database.table;


import android.database.sqlite.SQLiteDatabase;

import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class ItemAttributeTable extends BaseTable {
    public static final String TABLE_NAME = "items_attributes";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + BASE_FIELDS_CREATE
            + TableColumn.ITEM_ID + " integer not null,"
            + TableColumn.NAME + " text not null,"
            + TableColumn.VALUE + " text not null,"
            + TableColumn.ITEM_TYPE + " text not null"
            + ")";

    public static HashMap<String, String> getProjectionMap() {
        HashMap<String, String> map = BaseTable.getProjectionMap();

        map.put(TableColumn.ITEM_ID, TableColumn.ITEM_ID);
        map.put(TableColumn.NAME, TableColumn.NAME);
        map.put(TableColumn.VALUE, TableColumn.VALUE);
        map.put(TableColumn.ITEM_TYPE, TableColumn.ITEM_TYPE);

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
