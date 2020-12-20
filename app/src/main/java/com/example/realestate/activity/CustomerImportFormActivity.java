package com.example.realestate.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.widget.Spinner;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.R;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;


public class CustomerImportFormActivity extends BaseImportFormActivity implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final int LINE_LOADER_ID = 0x1;

	Spinner spnLines;
	Spinner spnTypes;
	SimpleCursorAdapter saleLinesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.import_customers);
		
		spnLines = (Spinner)findViewById(R.id.spnSaleLine);
		spnTypes = (Spinner)findViewById(R.id.spnCustomerType);

		getSupportLoaderManager().initLoader(LINE_LOADER_ID, null, this);
		String[] from = {
				TableColumn.NAME
		};

		int[] to = {
				android.R.id.text1
		};

		saleLinesAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null, from, to, 0);
		saleLinesAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		spnLines.setAdapter(saleLinesAdapter);
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.import_customers;
	}
	
	@Override
	protected void emptyOldItems() {
		super.emptyOldItems();
		getContentResolver().delete(ContentDescriptor.ContentUri.CUSTOMER, null, null);
	}

	@Override
	protected boolean importSingleItem(String[] items) {
		if (items.length == 8) {
			ContentValues values = new ContentValues();

			values.put(TableColumn.ADDRESS, items[2]);
			values.put(TableColumn.NOTE, items[5]);

			values.put(TableColumn.TYPE, spnTypes.getSelectedItemPosition());
			
			return getContentResolver().insert(ContentDescriptor.ContentUri.CUSTOMER, values) != null;
		}
		
		return false;
	}
	
	@Override
	protected int getViewExtraLayout() {
		return R.layout.form_customer_import_extra_view;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle options) {
		String[] projection = {
				TableColumn._ID,
				TableColumn.NAME
		};

		CursorLoader loader = new CursorLoader(this,
				ContentDescriptor.ContentUri.VOCABULARY,
				projection, 
				null, 
				null, 
				null);

		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Insert extra item to top of the list
		MatrixCursor extras = new MatrixCursor(new String[] { 
				TableColumn._ID,
				TableColumn.NAME
		});

		extras.addRow(new String[] { "0", getString(R.string.unlined) });
		Cursor[] cursors = { extras, data };
		Cursor extendedCursor = new MergeCursor(cursors);

		saleLinesAdapter.swapCursor(extendedCursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		saleLinesAdapter.swapCursor(null);
	}
}	
