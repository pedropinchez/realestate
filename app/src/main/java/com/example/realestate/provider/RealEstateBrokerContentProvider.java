package com.example.realestate.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.example.realestate.database.query.RawQuery;
import com.example.realestate.common.AttachmentType;
import com.example.realestate.common.ContentItemType;
import com.example.realestate.common.CustomerType;
import com.example.realestate.database.DatabaseHelper;
import com.example.realestate.database.TableColumn;
import com.example.realestate.database.query.RawQuery;
import com.example.realestate.database.table.AppointmentReminderTable;
import com.example.realestate.database.table.AppointmentTable;
import com.example.realestate.database.table.CustomerTable;
import com.example.realestate.database.table.ItemAttachmentTable;
import com.example.realestate.database.table.ItemAttributeTable;
import com.example.realestate.database.table.ItemVocabularyTable;
import com.example.realestate.database.table.PropertyTable;
import com.example.realestate.database.table.TaxonomyTable;
import com.example.realestate.database.table.UserAccountTable;
import com.example.realestate.provider.ContentDescriptor.ContentPath;
import com.example.realestate.provider.ContentDescriptor.ContentUri;
import com.example.realestate.provider.ContentDescriptor.QueryType;
import com.example.realestate.util.DateTimeUtils;
import com.example.realestate.util.GeneralUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RealEstateBrokerContentProvider extends ContentProvider {
	private DatabaseHelper mDbHelper;
	private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		addURI(ContentPath.CUSTOMER, QueryType.CUSTOMER);
		addURI(ContentPath.USER_ACCOUNT, QueryType.USER_ACCOUNT);
		addURI(ContentPath.PROPERTY, QueryType.PROPERTY);
		addURI(ContentPath.VOCABULARY, QueryType.VOCABULARY);
        addURI(ContentPath.APPOINTMENT, QueryType.APPOINTMENT);
        addURI(ContentPath.LEAD, QueryType.LEAD);
        addURI(ContentPath.ITEM_ATTACHMENT, QueryType.ITEM_ATTACHMENT);
        addURI(ContentPath.ITEM_ATTRIBUTE, QueryType.ITEM_ATTRIBUTE);
        addURI(ContentPath.ITEM_TAXONOMY, QueryType.ITEM_TAXONOMY);

		addURI(ContentPath.CUSTOMER + "/#", QueryType.CUSTOMER_ID);
		addURI(ContentPath.USER_ACCOUNT + "/#", QueryType.USER_ACCOUNT_ID);
		addURI(ContentPath.PROPERTY + "/#", QueryType.PROPERTY_ID);
		addURI(ContentPath.VOCABULARY + "/#", QueryType.VOCABULARY_ID);
        addURI(ContentPath.APPOINTMENT + "/#", QueryType.APPOINTMENT_ID);
        addURI(ContentPath.LEAD + "/#", QueryType.LEAD_ID);

		// Extended paths
		addURI(ContentPath.ExtendedPath.VOCABULARY_GROUP_BY_PARENT, QueryType.ExtendedQueryType.VOCABULARY_GROUP_BY_PARENT);
		addURI(ContentPath.ExtendedPath.CUSTOMER_GROUP_BY_LOCALITY, QueryType.ExtendedQueryType.CUSTOMER_GROUP_BY_LOCALITY);
        addURI(ContentPath.ExtendedPath.APPOINTMENT_GROUP_BY_WEEK, QueryType.ExtendedQueryType.APPOINTMENT_GROUP_BY_WEEK);
        addURI(ContentPath.ExtendedPath.APPOINTMENT_MOST_DUE, QueryType.ExtendedQueryType.APPOINTMENT_MOST_DUE);
        addURI(ContentPath.ExtendedPath.PROPERTY_GROUP_BY_CATEGORY, QueryType.ExtendedQueryType.PROPERTY_GROUP_BY_CATEGORY);
        addURI(ContentPath.ExtendedPath.PROPERTY_GROUP_BY_LOCALITY, QueryType.ExtendedQueryType.PROPERTY_GROUP_BY_LOCALITY);
        addURI(ContentPath.ExtendedPath.PROPERTY_LATEST, QueryType.ExtendedQueryType.PROPERTY_LATEST);
	}
	
	private static void addURI(String path, int code) {
		sUriMatcher.addURI(ContentDescriptor.AUTHORITY, path, code);
	}
	
	private boolean matchSingleTableMultipleItems(int queryType) {
		boolean matched = false;

		switch (queryType) {
            case QueryType.USER_ACCOUNT:
            case QueryType.VOCABULARY:
            case QueryType.PROPERTY:
            case QueryType.CUSTOMER:
            case QueryType.APPOINTMENT:
            case QueryType.ITEM_ATTACHMENT:
            case QueryType.ITEM_ATTRIBUTE:
                matched = true;
                break;

            default:
                break;
        }
		
		return matched;
	}
	
	private boolean matchSingleTableOneItem(int queryType) {
		boolean matched = false;

		switch (queryType) {
            case QueryType.USER_ACCOUNT_ID:
            case QueryType.VOCABULARY_ID:
            case QueryType.PROPERTY_ID:
            case QueryType.CUSTOMER_ID:
            case QueryType.APPOINTMENT_ID:
                matched = true;
                break;

            default:
                break;
        }
		
		return matched;
	}
	
	private String getDatabaseTable(int uriQueryType) {
        switch (uriQueryType) {
            case QueryType.USER_ACCOUNT:
            case QueryType.USER_ACCOUNT_ID:
                return UserAccountTable.TABLE_NAME;

            case QueryType.CUSTOMER:
            case QueryType.CUSTOMER_ID:
                return CustomerTable.TABLE_NAME;

            case QueryType.VOCABULARY:
            case QueryType.VOCABULARY_ID:
                return TaxonomyTable.TABLE_NAME;

            case QueryType.PROPERTY:
            case QueryType.PROPERTY_ID:
                return PropertyTable.TABLE_NAME;

            case QueryType.APPOINTMENT:
            case QueryType.APPOINTMENT_ID:
                return AppointmentTable.TABLE_NAME;

            case QueryType.ITEM_TAXONOMY:
                return ItemVocabularyTable.TABLE_NAME;

            case QueryType.ITEM_ATTACHMENT:
                return ItemAttachmentTable.TABLE_NAME;

            case QueryType.ITEM_ATTRIBUTE:
                return ItemAttributeTable.TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unknown uri query type: " + uriQueryType);
        }
    }

    private HashMap<String, String> getProjectionMap(int uriQueryType) {
        switch (uriQueryType) {
            case QueryType.USER_ACCOUNT:
            case QueryType.USER_ACCOUNT_ID:
                return UserAccountTable.getProjectionMap();

            case QueryType.CUSTOMER:
            case QueryType.CUSTOMER_ID:
                return CustomerTable.getProjectionMap();

            case QueryType.VOCABULARY:
            case QueryType.VOCABULARY_ID:
                return TaxonomyTable.getProjectionMap();

            case QueryType.PROPERTY:
            case QueryType.PROPERTY_ID:
                return PropertyTable.getProjectionMap();

            case QueryType.APPOINTMENT:
            case QueryType.APPOINTMENT_ID:
                return AppointmentTable.getProjectionMap();

            case QueryType.ITEM_ATTRIBUTE:
                return ItemAttributeTable.getProjectionMap();

            case QueryType.ITEM_TAXONOMY:
                return ItemVocabularyTable.getProjectionMap();

            case QueryType.ITEM_ATTACHMENT:
                return ItemAttachmentTable.getProjectionMap();

            default:
                return null;
        }
	}
	
	private void notifyChange(Uri uri) {
		int uriQueryType = sUriMatcher.match(uri);
		ContentResolver cr = getContext().getContentResolver();
		
        cr.notifyChange(uri, null);

        // Notify also observers of custom queries
        switch (uriQueryType) {
            case QueryType.CUSTOMER:
            case QueryType.CUSTOMER_ID:
                cr.notifyChange(ContentUri.ExtendedUri.CUSTOMER_GROUP_BY_LOCALITY, null);
                break;

            case QueryType.PROPERTY:
            case QueryType.PROPERTY_ID:
                cr.notifyChange(ContentUri.ExtendedUri.PROPERTY_GROUP_BY_CATEGORY, null);
                cr.notifyChange(ContentUri.ExtendedUri.PROPERTY_GROUP_BY_LOCALITY, null);
                break;

            case QueryType.VOCABULARY:
            case QueryType.VOCABULARY_ID:
                cr.notifyChange(ContentUri.ExtendedUri.VOCABULARY_GROUP_BY_PARENT, null);
                break;

            case QueryType.APPOINTMENT:
            case QueryType.APPOINTMENT_ID:
                cr.notifyChange(ContentUri.ExtendedUri.APPOINTMENT_GROUP_BY_WEEK, null);
                break;

            default:
                break;
        }
    }

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int uriQueryType = sUriMatcher.match(uri);
		String dbTable = getDatabaseTable(uriQueryType);

        switch (uriQueryType) {
            case QueryType.APPOINTMENT_ID:
                return deleteAppointment(dbTable, uri, where, whereArgs);
        }
		
		if (matchSingleTableMultipleItems(uriQueryType)) {
			return deleteMultipleItems(dbTable, uriQueryType, uri, where, whereArgs);
		}
		else if (matchSingleTableOneItem(uriQueryType)) {
			return deleteSingleItem(dbTable, uri, where, whereArgs);
		}
		
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}


    private int deleteAppointment(String table, Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int affected = 0;
        String itemId = uri.getLastPathSegment();

        db.beginTransaction();
        try {
            affected = deleteSingleItem(table, uri, where, whereArgs);

            if (affected == 1) {
                db.execSQL("DELETE FROM appointment_reminders WHERE appointment_id = ?",
                        new String[]{itemId});
            }

            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        return affected;
    }
	
	private int deleteMultipleItems(String table, int queryType, Uri uri, String where, String[] whereArgs) {
		int count = mDbHelper.getWritableDatabase().delete(table, where, whereArgs);

		// Notify interested parties
		if (count > 0)
			notifyChange(uri);
		
		return count;
	}
	
	private int deleteSingleItem(String table, Uri uri, String where, String[] whereArgs) {
		String itemId = uri.getLastPathSegment();
		String whereClause = BaseColumns._ID + "=" + itemId + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : "");
		int count = mDbHelper.getWritableDatabase().delete(table, whereClause, whereArgs);

		// Notify interested parties
		if (count == 1)
			notifyChange(uri);

		return count;
	}

	@Override
	public String getType(Uri uri) {
		int queryType = sUriMatcher.match(uri);

		if (matchSingleTableMultipleItems(queryType)) {
			return ContentDescriptor.CONTENT_TYPE;
		}
		else if (matchSingleTableOneItem(queryType)) {
			return ContentDescriptor.CONTENT_ITEM_TYPE;
		}

		throw new IllegalArgumentException("Unknown URI: " + uri);
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// Set the current timestamp for created_at field
		if (!values.containsKey(TableColumn.CREATED_AT)) {
			values.put(TableColumn.CREATED_AT, DateTimeUtils.getCurrentSqlTimestamp());
		}

        switch (sUriMatcher.match(uri)) {
            case QueryType.USER_ACCOUNT:
                return insertUserAccount(values, uri);

            case QueryType.CUSTOMER:
                return insertCustomer(values, uri);

            case QueryType.VOCABULARY:
                return insertVocabulary(values, uri);

            case QueryType.PROPERTY:
                return insertProperty(values, uri);

            case QueryType.APPOINTMENT:
                return insertAppointment(values, uri);

            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
    }
	
	private void checkRequiredFields(String[] requiredFields, ContentValues values) {
		// Check if all required columns are provided
		for(String column: requiredFields) {
			if (!values.containsKey(column)) {
				throw new IllegalArgumentException("Column " + column + " is required");
			}
		}
	}
	
	private Uri insertItem(String table, Uri contentUri, ContentValues values) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long id = db.insert(table, null, values);

		if (id > 0 && contentUri != null) {
			notifyChange(contentUri);

			Uri itemUri = Uri.withAppendedPath(contentUri, String.valueOf(id));
			return itemUri;
		}

		return null;
	}

    private Uri insertAppointment(ContentValues values, Uri uri) {
        String[] requiredFields = new String[] {
                TableColumn.TITLE,
        };

        checkRequiredFields(requiredFields, values);

        if (!values.containsKey(TableColumn.ExtendedColumn.REMINDERS)) {
            return insertItem(AppointmentTable.TABLE_NAME, uri, values);
        }

        String reminderTimes = values.getAsString(TableColumn.ExtendedColumn.REMINDERS);
        values.remove(TableColumn.ExtendedColumn.REMINDERS);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri itemUri = null;

        db.beginTransaction();
        try {
            itemUri = insertItem(AppointmentTable.TABLE_NAME, uri, values);
            if (itemUri != null) {
                String itemId = itemUri.getLastPathSegment();
                String[] times = reminderTimes.split(";");
                ContentValues subValues = new ContentValues();

                for(String time : times) {
                    subValues.clear();
                    subValues.put(TableColumn.ITEM_ID, itemId);
                    subValues.put(TableColumn.REMINDER_TIME, time);
                    insertItem(AppointmentReminderTable.TABLE_NAME, null, subValues);
                }

                // NOTE: This is important (like commit the transaction)
                db.setTransactionSuccessful();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            itemUri = null;
        }
        finally {
            db.endTransaction();
        }

        return itemUri;
    }

	private Uri insertVocabulary(ContentValues values, Uri uri) {
		String[] requiredFields = new String[] {
				TableColumn.TITLE,
				TableColumn.TAXONOMY,
		};

		checkRequiredFields(requiredFields, values);

		return insertItem(TaxonomyTable.TABLE_NAME, uri, values);
	}

    private String getAndRemoveContentItem(ContentValues values, String key) {
        String ret = null;
        if (values.containsKey(key)) {
            ret = values.getAsString(key);
            values.remove(key);
        }

        return ret;
    }
	
	private Uri insertProperty(ContentValues values, Uri uri) {
		String[] requiredFields = new String[] {
				TableColumn.NAME,
		};
		
		checkRequiredFields(requiredFields, values);

        String statuses   = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.STATUSES);
        String types      = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.TYPES);
        String amenities  = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.AMENITIES);
        String facilities = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.FACILITIES);

        String kvPairs    = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.NAME_VALUE_PAIRS);
        String pictures   = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.PICTURES);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri itemUri = null;

        db.beginTransaction();

        try {
            itemUri = insertItem(PropertyTable.TABLE_NAME, uri, values);

            if (itemUri != null) {
                String itemId = itemUri.getLastPathSegment();

                insertPropertyTaxonomies(db, itemId, statuses, types, amenities, facilities);

                if (kvPairs != null) {
                    insertPropertyNameValuePairs(db, kvPairs, itemId);
                }

                if (pictures != null) {
                    insertPropertyPictures(db, pictures, itemId);
                }

                db.setTransactionSuccessful();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            itemUri = null;
        }
        finally {
            db.endTransaction();
        }

        return itemUri;
	}

    private void insertPropertyTaxonomies(SQLiteDatabase db, String itemId, String ... taxonomies) {
        ContentValues values = new ContentValues();

        for (String tids : taxonomies) {
            if (tids == null) continue;

            String[] taxonomyIds = tids.split(";");

            for(String id : taxonomyIds) {
                values.clear();
                values.put(TableColumn.ITEM_ID, itemId);
                values.put(TableColumn.TAXONOMY_ID, id);
                values.put(TableColumn.ITEM_TYPE, ContentItemType.PROPERTY);

                insertItem(ItemVocabularyTable.TABLE_NAME, null, values);
            }
        }
    }

    private void insertPropertyPictures(SQLiteDatabase db, String pictures, String itemId) {
        String[] paths = pictures.split(";");
        ContentValues values = new ContentValues();

        for (String path : paths) {
            values.clear();

            values.put(TableColumn.ITEM_ID, itemId);
            values.put(TableColumn.PATH, path);
            values.put(TableColumn.MIME, "");
            // TODO: remove this once ITEM_TYPE is added to ItemAttachmentTable
            //values.put(TableColumn.ITEM_TYPE, ContentItemType.PROPERTY);
            values.put(TableColumn.ATTACHMENT_TYPE, AttachmentType.IMAGE);

            insertItem(ItemAttachmentTable.TABLE_NAME, null, values);
        }
    }

    private void insertPropertyNameValuePairs(SQLiteDatabase db, String kvPairs, String itemId) {
        String[] pairs = kvPairs.split("\n");
        String[] nameVal = null;
        ContentValues values = new ContentValues();

        for (String pair : pairs) {
            nameVal = pair.split("\t");

            values.clear();
            values.put(TableColumn.ITEM_ID, itemId);
            values.put(TableColumn.NAME, nameVal[0]);
            values.put(TableColumn.VALUE, nameVal[1]);
            values.put(TableColumn.ITEM_TYPE, ContentItemType.PROPERTY);

            insertItem(ItemAttributeTable.TABLE_NAME, null, values);
        }
    }

	private Uri insertUserAccount(ContentValues values, Uri uri) {
		String[] requiredFields = new String[] {
				TableColumn.USER_NAME,
				TableColumn.NAME,
				TableColumn.PASSWORD,
		};
		
		checkRequiredFields(requiredFields, values);
		
		// Hash the password
		String password = GeneralUtils.md5(values.getAsString(TableColumn.PASSWORD));
		if (password != null) {
			values.put(TableColumn.PASSWORD, password);
		}
		
		return insertItem(UserAccountTable.TABLE_NAME, uri, values);
	}

	private Uri insertCustomer(ContentValues values, Uri uri) {
		List<String> requiredFields = new ArrayList<String>();
		
		requiredFields.add(TableColumn.NAME);
		requiredFields.add(TableColumn.ADDRESS);
		requiredFields.add(TableColumn.MOBILE_PHONE);
		
		if (values.containsKey(TableColumn.TYPE)) {
			int customerType = values.getAsInteger(TableColumn.TYPE);
			
			if (customerType == CustomerType.COMPANY) {
				requiredFields.add(TableColumn.WORK_PHONE);
			}
		}
		
		String[] fields = requiredFields.toArray(new String[0]);
		checkRequiredFields(fields, values);

		return insertItem(CustomerTable.TABLE_NAME, uri, values);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DatabaseHelper(getContext());
		return false;
	}
	
	private String getDefaultSortOrder(int queryType) {
		String sortOrder = null;
		
		switch (queryType) {
		case QueryType.USER_ACCOUNT:
			sortOrder = TableColumn.NAME + " ASC ";
			break;

		case QueryType.CUSTOMER:
			sortOrder = TableColumn.NAME + " ASC ";
			break;
		
		default:
			break;
		}
		
		return sortOrder;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        int queryType = sUriMatcher.match(uri);
		HashMap<String, String> projectionMap = getProjectionMap(queryType);
        
        qb.setProjectionMap(projectionMap);
        String rawQuery = null;
        Cursor cursor = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Match complex/special query types first
        switch (queryType) {
            case QueryType.CUSTOMER_ID:
                rawQuery = RawQuery.CUSTOMER_DETAIL;
                break;

            case QueryType.APPOINTMENT_ID:
                rawQuery = RawQuery.APPOINTMENT_DETAIL;
                break;

            case QueryType.PROPERTY_ID:
                rawQuery = RawQuery.PROPERTY_DETAIL;
                break;

            case QueryType.ITEM_TAXONOMY:
                rawQuery = RawQuery.ITEM_TAXONOMY;
                break;

            case QueryType.ExtendedQueryType.PROPERTY_GROUP_BY_LOCALITY:
                rawQuery = RawQuery.PROPERTY_GROUP_BY_LOCALITY.toString(selection);
                break;

            case QueryType.ExtendedQueryType.PROPERTY_LATEST:
                rawQuery = RawQuery.PROPERTY_LATEST;
                break;

            case QueryType.ExtendedQueryType.CUSTOMER_GROUP_BY_LOCALITY:
                rawQuery = RawQuery.CUSTOMER_GROUP_BY_LOCALITY.toString(selection);
                break;

            case QueryType.ExtendedQueryType.VOCABULARY_GROUP_BY_PARENT:
                rawQuery = RawQuery.TAXONOMY_GROUP_BY_PARENT.toString(selection);
                break;

            case QueryType.ExtendedQueryType.APPOINTMENT_GROUP_BY_WEEK:
                rawQuery = RawQuery.APPOINTMENT_GROUP_BY_WEEK.toString(selection);
                break;

            case QueryType.ExtendedQueryType.APPOINTMENT_MOST_DUE:
                rawQuery = RawQuery.APPOINTMENTS_MOST_DUE;
                break;
        }

        if (rawQuery == null) {
            if (matchSingleTableMultipleItems(queryType)) {
                qb.setTables(getDatabaseTable(queryType));

                if (sortOrder == null) {
                    sortOrder = getDefaultSortOrder(queryType);
                }
            }
            else if (matchSingleTableOneItem(queryType)) {
                qb.setTables(getDatabaseTable(queryType));
                String itemId = uri.getLastPathSegment();
                qb.appendWhere(BaseColumns._ID + "=" + itemId);
            } else {
                throw new IllegalArgumentException("Unknown URI: " + uri);
            }

            cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        } else {
        	Log.d(RealEstateBrokerContentProvider.class.getName(), "Running custom query: " + rawQuery);
        	cursor = db.rawQuery(rawQuery, selectionArgs);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        
        return cursor;
	}
	
	
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// Mark the update timestamp
		if (!values.containsKey(TableColumn.UPDATED_AT)) {
			values.put(TableColumn.UPDATED_AT, DateTimeUtils.getCurrentSqlTimestamp());
		}
		
		int uriQueryType = sUriMatcher.match(uri);
		String dbTable = getDatabaseTable(uriQueryType);

        switch (uriQueryType) {
            case QueryType.APPOINTMENT_ID:
                return updateAppointment(dbTable, uri, values, selection, selectionArgs);

            case QueryType.PROPERTY_ID:
                return updateProperty(dbTable, uri, values, selection, selectionArgs);
        }
		
		if (matchSingleTableMultipleItems(uriQueryType)) {
			return updateMultipleItems(dbTable, uri, values, selection, selectionArgs);
		}
		else if (matchSingleTableOneItem(uriQueryType)) {
			return updateSingleItem(dbTable, uri, values, selection, selectionArgs);
		}
		
		throw new IllegalArgumentException("Unknown URI: " + uri);
	}


    private int updateProperty(String table, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int ret = -1;
        String itemId = values.getAsString(TableColumn._ID);

        db.beginTransaction();

        try {
            String statuses   = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.STATUSES);
            String types      = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.TYPES);
            String amenities  = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.AMENITIES);
            String facilities = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.FACILITIES);

            String kvPairs    = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.NAME_VALUE_PAIRS);
            String pictures   = getAndRemoveContentItem(values, TableColumn.ExtendedColumn.PICTURES);

            ret = updateSingleItem(table, uri, values, selection, selectionArgs);

            if (ret > 0) {
                // Delete all item's taxonomies
                db.execSQL("DELETE FROM " + ItemVocabularyTable.TABLE_NAME + " WHERE item_id = ? AND item_type = ?",
                        new String[] {
                                itemId,
                                ContentItemType.PROPERTY
                        });

                // All item attributes
                db.execSQL("DELETE FROM " + ItemAttributeTable.TABLE_NAME + " WHERE item_id = ? AND item_type = ?",
                           new String[] {
                                   itemId,
                                   ContentItemType.PROPERTY
                           });

                // And attachments
                db.execSQL("DELETE FROM " + ItemAttachmentTable.TABLE_NAME + " WHERE item_id = ?",
                           new String[] {
                                   itemId,
                           });

                // Then insert new values
                insertPropertyTaxonomies(db, itemId, statuses, types, amenities, facilities);

                if (kvPairs != null) {
                    insertPropertyNameValuePairs(db, kvPairs, itemId);
                }

                if (pictures != null) {
                    insertPropertyPictures(db, pictures, itemId);
                }

                db.setTransactionSuccessful();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        return ret;
    }

    private int updateAppointment(String table, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int ret = -1;
        String itemId = values.getAsString(TableColumn._ID);

        db.beginTransaction();
        try {
            ret = updateSingleItem(table, uri, values, selection, selectionArgs);

            if (ret > 0) {
                db.execSQL("DELETE FROM appointment_reminders WHERE appointment_id = ?", new String[] {itemId});

                if (values.containsKey("reminders")) {
                    String[] times = values.getAsString("reminders").split(";");
                    values.remove("reminders");

                    for(String time : times) {
                        db.execSQL("INSERT INTO " + AppointmentReminderTable.TABLE_NAME + " VALUES(?,?)",
                                new String[] {itemId, time});
                    }
                }

                db.setTransactionSuccessful();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        return ret;
    }
	
	private int updateMultipleItems(String table, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = mDbHelper.getWritableDatabase().update(table, values, selection, selectionArgs);
		// Let observers know about the update
		if (count > 0)
			notifyChange(uri);
		return count;
	}
	
	private int updateSingleItem(String table, Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String itemId = uri.getLastPathSegment();
		String selectionClause = BaseColumns._ID + "=" + itemId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
		int count = mDbHelper.getWritableDatabase().update(table, values, selectionClause, selectionArgs);
		
		// Let observers know about the update
		if (count == 1)
			notifyChange(uri);
        
        return count;
	}
}