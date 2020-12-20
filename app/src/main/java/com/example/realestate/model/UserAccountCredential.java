package com.example.realestate.model;

import android.database.Cursor;

import com.example.realestate.database.TableColumn;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;

public class UserAccountCredential {
	public long id;
	public String email;
	public String password;
	public String fullName;
	public String locale;
	public String coverPicture;
	
	public static UserAccountCredential readFromCursor(Cursor cursor) {
		UserAccountCredential cred = new UserAccountCredential();

		cred.id = CursorUtils.getRecordIntValue(cursor, TableColumn._ID);
		cred.email = CursorUtils.getRecordStringValue(cursor, TableColumn.EMAIL);
		cred.password = CursorUtils.getRecordStringValue(cursor, TableColumn.PASSWORD);
		cred.fullName = CursorUtils.getRecordStringValue(cursor, TableColumn.NAME);
		cred.locale = CursorUtils.getRecordStringValue(cursor, TableColumn.LANGUAGE);
		cred.coverPicture = CursorUtils.getRecordStringValue(cursor, TableColumn.COVER_PIC_PATH);
		
		return cred;
	}
}
