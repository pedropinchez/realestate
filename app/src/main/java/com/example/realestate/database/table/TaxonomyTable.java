package com.example.realestate.database.table;


import android.database.sqlite.SQLiteDatabase;

import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class TaxonomyTable extends BaseTable {
    public static final String TABLE_NAME = "vocabularies";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + BASE_FIELDS_CREATE
            + TableColumn.TITLE + " text not null,"
            + TableColumn.DESCRIPTION + " text null,"
            + TableColumn.VOCABULARY + " text not null,"
            + TableColumn.PARENT_ID + " integer null,"
            + TableColumn.DATA + " text null"
            + ")";

    public static HashMap<String, String> getProjectionMap() {
        HashMap<String, String> map = BaseTable.getProjectionMap();

        map.put(TableColumn.TITLE, TableColumn.TITLE);
        map.put(TableColumn.DESCRIPTION, TableColumn.DESCRIPTION);
        map.put(TableColumn.VOCABULARY, TableColumn.VOCABULARY);
        map.put(TableColumn.PARENT_ID, TableColumn.PARENT_ID);
        map.put(TableColumn.DATA, TableColumn.DATA);

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
