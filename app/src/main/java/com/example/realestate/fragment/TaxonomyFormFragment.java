package com.example.realestate.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import com.example.realestate.R;
import com.example.realestate.database.DatabaseHelper;
import com.example.realestate.database.TableColumn;
import com.example.realestate.database.query.RawQuery;
import com.example.realestate.database.table.TaxonomyTable;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.CursorUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class TaxonomyFormFragment extends BaseItemFormFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	protected static int sBaseParentsLoaderId = 500;

	private EditText mTxtName;
	private EditText mTxtDesc;
	private Button mBtnCancel;
	private Button mBtnSave;
	private Spinner mSpnParent;

	private SimpleCursorAdapter mParentAdapter;
	private int mParentID = 0;
	private DatabaseHelper mDbHelper;
	protected int mParentsLoaderId;
	
	protected abstract String getVocabularyName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// For now, we only need mDbHelper in edit mode
		if (getItemId() > 0) {
			mDbHelper = new DatabaseHelper(getActivity());
		}
	}
	
	protected boolean isHierarchyEnabled() {
		return false;
	}

    protected int getDescriptionFieldHint() {
        return R.string.description;
    }
	
	@Override
	protected void initContentView(View view) {
		super.initContentView(view);
		
		mTxtName = (EditText)view.findViewById(R.id.txtName);
		mTxtDesc = (EditText)view.findViewById(R.id.txtDesc);
        if (mTxtDesc != null) {
            mTxtDesc.setHint(getDescriptionFieldHint());
        }
		
		mBtnCancel = (Button)view.findViewById(R.id.btnCancel);
		mBtnSave = (Button)view.findViewById(R.id.btnSave);
		
		mBtnCancel.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
		
		mSpnParent = (Spinner)view.findViewById(R.id.spnParent);
		
		if (!isHierarchyEnabled() && mSpnParent != null) {
			mSpnParent.setVisibility(View.GONE);
		}
	}
	
	private void initParentAdapter() {
		if (!isHierarchyEnabled() || mSpnParent == null) {
			return;
		}
		
		// Don't show parent box if the item already has children
		if (hasChildren()) {
			mSpnParent.setVisibility(View.GONE);
			return;
		}

		mSpnParent.setVisibility(View.VISIBLE);

		String[] from = {
				TableColumn.TITLE
		};

		int[] to = {
				android.R.id.text1
		};
		
		mParentAdapter = new SimpleCursorAdapter(
				getActivity(), 
				android.R.layout.simple_spinner_item, 
				null, 
				from, 
				to, 
				0);
		mParentAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		mSpnParent.setAdapter(mParentAdapter);

		// NOTE: If at the point of call the caller is in its started state, and the requested loader 
		// already exists and has generated its data, then callback onLoadFinished(Loader, D) will be 
		// called immediately (inside of this function), so you must be prepared for this to happen.
		getActivity().getSupportLoaderManager().initLoader(mParentsLoaderId, null, this);
	}
	
	protected boolean hasChildren() {
		long itemId = getItemId();
		boolean hasChildren = false;
		
		if (itemId > 0) {
			Cursor cursor = mDbHelper.getReadableDatabase().rawQuery(
					RawQuery.TAXONOMY_CHILD_COUNT,
					new String[] { String.valueOf(itemId) }
					);

			if (cursor != null) {
				// NOTE: next() or moveToFirst() has to be called after query is made
				cursor.moveToFirst();
				hasChildren = CursorUtils.getRecordIntValue(cursor, TableColumn._COUNT) > 0;
				cursor.close();
			}
		}
		
		return hasChildren;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle opts) {
        if (id == mParentsLoaderId) {
            String[] projection = {
                    TableColumn._ID,
                    TableColumn.TITLE,
                    TableColumn.PARENT_ID
            };

            String selection = TableColumn.TAXONOMY + " = ? ";
            List<String> selectionArgs = new ArrayList<String>();
            selectionArgs.add(getVocabularyName());

            selection += " AND " + TableColumn._ID + " IN (SELECT " + TableColumn._ID + " FROM " + TaxonomyTable.TABLE_NAME + " WHERE " + TableColumn.PARENT_ID + " IS NULL OR " + TableColumn.PARENT_ID + " = 0 ) ";

            if (isEditMode()) {
                // Don't let it to be parent of itself
                selection += " AND " + TableColumn._ID + " != ? ";
                selectionArgs.add(String.valueOf(getItemId()));

                // Don't let it to be child of anything if it has children, otherwise
                // we will end up with more than 2 levels of hierarchy
                if (hasChildren()) {
                    selection += " AND _id IN (SELECT DISTINCT P._id FROM vocabularies P JOIN vocabularies C ON C.parent_id=P._id WHERE C.parent_id > 0) ";
                }
            }

            String[] selectionArgsStr = selectionArgs.toArray(new String[selectionArgs.size()]);

            CursorLoader loader = new CursorLoader(getActivity(),
                                                   getItemContentUri(),
                                                   projection,
                                                   selection,
                                                   selectionArgsStr,
                                                   null);

            return loader;
        }

        return super.onCreateLoader(id, opts);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (loader.getId() == mParentsLoaderId) {
            if (getActivity() == null) {
                return;
            }

            if (data == null) {
                mSpnParent.setVisibility(View.GONE);
                return;
            }

            // Insert extra item to top of the list
            MatrixCursor extras = new MatrixCursor(new String[]{
                    TableColumn._ID,
                    TableColumn.TITLE
            });

            extras.addRow(new String[]{"0", getString(R.string.parent)});
            Cursor[] cursors = {extras, data};
            Cursor extendedCursor = new MergeCursor(cursors);

            int currentParentPos = -1;
            extendedCursor.moveToPosition(-1);

            while (extendedCursor.moveToNext()) {
                currentParentPos++;

                int itemId = CursorUtils.getRecordIntValue(extendedCursor, TableColumn._ID);
                if (itemId == mParentID) {
                    break;
                }
            }

            mSpnParent.setSelection(currentParentPos > 0 ? currentParentPos : 0);
            extendedCursor.moveToFirst();

            if (mParentAdapter != null) {
                mParentAdapter.swapCursor(extendedCursor);
            }

            return;
        }

        // Let parent loads the item itself
        super.onLoadFinished(loader, data);

        // If we finish loading the item, start loading the parent
        if (loader.getId() == mItemLoaderId) {
            initParentAdapter();
        }
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == mParentsLoaderId) {
            mParentAdapter.swapCursor(null);
            return;
        }

        super.onLoaderReset(loader);
	}
	
	@Override
	protected Uri getItemContentUri() {
		return ContentDescriptor.ContentUri.VOCABULARY;
	}
	
	@Override
	public void onClick(View v) {
		if (v == mBtnCancel) {
			if (mEventHandler != null)
				mEventHandler.onFormCanceled();
		}
		else if (v == mBtnSave) {
			saveItem();

			if (mEventHandler != null)
				mEventHandler.onFormSaved();
		}
	}
	
	@Override
	protected int getContentView() {
		return R.layout.fragment_taxonomy_form;
	}
	
	@Override
	protected boolean validateForm() {
		if (TextUtils.isEmpty(mTxtName.getText())) {
			showFormElementError(R.string.line_alert_name, mTxtName);
			return false;
		}

		return true;
	}
	
	@Override
	protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
		if (cursor != null) {
			cursor.moveToFirst();

			mTxtName.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));

            if (mTxtDesc != null) {
                mTxtDesc.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.DESCRIPTION));
            }
			
			if (isHierarchyEnabled()) {
                mParentID = CursorUtils.getRecordIntValue(cursor, TableColumn.PARENT_ID);
			}
		}
	}
	
	@Override
	public void resetContentView() {
		super.resetContentView();

		mTxtName.setText("");
        if (mTxtDesc != null) {
            mTxtDesc.setText("");
        }
	}
	
	@Override
	protected ContentValues getSaveFormValues() {
		ContentValues values = new ContentValues();
		
		values.put(TableColumn.TITLE, mTxtName.getText().toString());

        if (mTxtDesc != null) {
            values.put(TableColumn.DESCRIPTION, mTxtDesc.getText().toString());
        }
		values.put(TableColumn.TAXONOMY, getVocabularyName());
		
		if (isHierarchyEnabled()) {
			long parentID = mSpnParent.getSelectedItemId();
			if (parentID >= 0)
				values.put(TableColumn.PARENT_ID, parentID);
		}

        return values;
	}
}