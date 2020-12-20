package com.example.realestate.database.table;

import android.database.sqlite.SQLiteDatabase;


import com.example.realestate.database.TableColumn;

import java.util.HashMap;

public class AppointmentReminderTable extends BaseTable {
    public static final String TABLE_NAME = "appointment_reminders";

    public static final String TABLE_CREATE = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + TableColumn.APPOINTMENT_ID + " integer not null,"
            + TableColumn.REMINDER_TIME + " text not null"
            + ")";

    public static HashMap<String, String> getProjectionMap() {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(TableColumn.APPOINTMENT_ID, TableColumn.APPOINTMENT_ID);
        map.put(TableColumn.REMINDER_TIME, TableColumn.REMINDER_TIME);

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
