package com.example.realestate.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.R;
import com.example.realestate.common.VocabularyType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.AdapterUtils;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.MediaUtils;
import com.ipaulpro.afilechooser.utils.FileUtils;


public class CustomerFormFragment extends BaseItemFormFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	private static final int LOADER_LOCALITY = 0x1;
	private static final int FILE_CHOOSER = 0x1;
	private static final int THUMBNAIL_SIZE = 64;

	private EditText mTxtFullName;
	private EditText mTxtAddress;
	private EditText mTxtMobilePhone;
	private EditText mTxtHomePhone;
	private EditText mTxtWorkPhone;
	private EditText mTxtNote;
	private EditText mTxtCompany;
	private EditText mTxtDepartment;
	private EditText mTxtTitle;
	private EditText mTxtDoB;
	private EditText mTxtEmail;
	private EditText mTxtWorkEmail;

	private Spinner mSpnType;
	private Spinner mSpnLocality;
	private Spinner mSpnGender;
	
	private ImageButton mBtnCallMobile;
	private ImageButton mBtnCallHome;
	private ImageButton mBtnCallWork;
	private ImageButton mBtnSendEmail;
	private ImageButton mBtnSendWorkEmail;
	
	private ImageView mPicture;
	private String mThumbnailPath = null;

	private long mLocalityId;
	private SimpleCursorAdapter mLocalityAdapter;

	@Override
	protected int getContentView() {
		return R.layout.fragment_customer_form;
	}

	@Override
	protected Uri getItemContentUri() {
		return ContentDescriptor.ContentUri.CUSTOMER;
	}
	
	@Override
	protected void initContentView(View view) {
		super.initContentView(view);

		mTxtFullName = (EditText)view.findViewById(R.id.txtFullName);
		mTxtAddress = (EditText)view.findViewById(R.id.txtAddress);
		mTxtMobilePhone = (EditText)view.findViewById(R.id.txtMobilePhone);
		mTxtHomePhone = (EditText)view.findViewById(R.id.txtHomePhone);
		mTxtWorkPhone = (EditText)view.findViewById(R.id.txtWorkPhone);
		mTxtNote = (EditText)view.findViewById(R.id.txtNote);
		mTxtCompany = (EditText)view.findViewById(R.id.txtCompany);
		mTxtDepartment = (EditText)view.findViewById(R.id.txtDepartment);
		mTxtTitle = (EditText)view.findViewById(R.id.txtTitle);
		mTxtDoB = (EditText)view.findViewById(R.id.txtDob);
		mTxtEmail = (EditText)view.findViewById(R.id.txtEmail);
		mTxtWorkEmail = (EditText)view.findViewById(R.id.txtWorkEmail);

		mSpnType = (Spinner)view.findViewById(R.id.spnCustomerType);
		mSpnLocality = (Spinner)view.findViewById(R.id.spnLocality);
		mSpnGender = (Spinner)view.findViewById(R.id.spnGender);
		
		mBtnCallHome = (ImageButton)view.findViewById(R.id.btnCallHomePhone);
		mBtnCallMobile = (ImageButton)view.findViewById(R.id.btnCallMobile);
		mBtnCallWork = (ImageButton)view.findViewById(R.id.btnCallWorkPhone);
		mBtnSendEmail = (ImageButton)view.findViewById(R.id.btnSendEmail);
		mBtnSendWorkEmail = (ImageButton)view.findViewById(R.id.btnSendWorkEmail);
		
		mPicture = (ImageView)view.findViewById(R.id.imgUserPic);
		mPicture.setOnClickListener(this);
		
		mBtnCallHome.setOnClickListener(this);
		mBtnCallMobile.setOnClickListener(this);
		mBtnCallWork.setOnClickListener(this);
		mBtnSendEmail.setOnClickListener(this);
		mBtnSendWorkEmail.setOnClickListener(this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initAdapters();
	}
	
	private void initAdapters() {
		mLocalityAdapter = AdapterUtils.createTaxonomySpinnerAdapter(getActivity());
		mSpnLocality.setAdapter(mLocalityAdapter);

		getActivity().getSupportLoaderManager().initLoader(LOADER_LOCALITY, null, this);
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TableColumn.NAME, mTxtFullName.getText());
        outState.putCharSequence(TableColumn.ADDRESS, mTxtAddress.getText());
        outState.putCharSequence(TableColumn.EMAIL, mTxtEmail.getText());
        outState.putCharSequence(TableColumn.WORK_EMAIL, mTxtWorkEmail.getText());
        outState.putCharSequence(TableColumn.MOBILE_PHONE, mTxtMobilePhone.getText());
        outState.putCharSequence(TableColumn.HOME_PHONE, mTxtHomePhone.getText());
        outState.putCharSequence(TableColumn.WORK_PHONE, mTxtWorkPhone.getText());
        outState.putCharSequence(TableColumn.DOB, mTxtDoB.getText());
        outState.putCharSequence(TableColumn.COMPANY, mTxtCompany.getText());
        outState.putCharSequence(TableColumn.DEPARTMENT, mTxtDepartment.getText());
        outState.putCharSequence(TableColumn.TITLE, mTxtTitle.getText());
        outState.putCharSequence(TableColumn.NOTE, mTxtNote.getText());

        outState.putInt(TableColumn.LOCALITY_ID, mSpnLocality.getSelectedItemPosition());
        outState.putInt(TableColumn.GENDER, mSpnGender.getSelectedItemPosition());
        outState.putInt(TableColumn.TYPE, mSpnType.getSelectedItemPosition());
    }

    @Override
	protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
        // Restore the state
        if (savedInstanceState != null) {
            mLocalityId = savedInstanceState.getInt(TableColumn.LOCALITY_ID);
            mSpnType.setSelection(savedInstanceState.getInt(TableColumn.TYPE));
            mSpnGender.setSelection(savedInstanceState.getInt(TableColumn.GENDER));

            mTxtFullName.setText(savedInstanceState.getCharSequence(TableColumn.NAME));
            mTxtAddress.setText(savedInstanceState.getCharSequence(TableColumn.ADDRESS));
            mTxtEmail.setText(savedInstanceState.getCharSequence(TableColumn.EMAIL));
            mTxtWorkEmail.setText(savedInstanceState.getCharSequence(TableColumn.WORK_EMAIL));
            mTxtHomePhone.setText(savedInstanceState.getCharSequence(TableColumn.HOME_PHONE));
            mTxtWorkPhone.setText(savedInstanceState.getCharSequence(TableColumn.WORK_PHONE));
            mTxtMobilePhone.setText(savedInstanceState.getCharSequence(TableColumn.MOBILE_PHONE));
            mTxtDoB.setText(savedInstanceState.getCharSequence(TableColumn.DOB));
            mTxtCompany.setText(savedInstanceState.getCharSequence(TableColumn.COMPANY));
            mTxtDepartment.setText(savedInstanceState.getCharSequence(TableColumn.DEPARTMENT));
            mTxtTitle.setText(savedInstanceState.getCharSequence(TableColumn.TITLE));
            mTxtNote.setText(savedInstanceState.getCharSequence(TableColumn.NOTE));
        }
        // Otherwise, load fresh data
        else {
            if (cursor != null && cursor.getCount() ==  1) {
                cursor.moveToFirst();

                mLocalityId = CursorUtils.getRecordIntValue(cursor, TableColumn.LOCALITY_ID);
                mSpnType.setSelection(CursorUtils.getRecordIntValue(cursor, TableColumn.TYPE));
                mSpnGender.setSelection(CursorUtils.getRecordIntValue(cursor, TableColumn.GENDER));

                mTxtFullName.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NAME));
                mTxtAddress.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.ADDRESS));

                mTxtEmail.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.EMAIL));
                mTxtWorkEmail.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.WORK_EMAIL));
                mTxtDoB.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.DOB));
                mTxtCompany.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.COMPANY));
                mTxtDepartment.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.DEPARTMENT));
                mTxtTitle.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));

                mTxtMobilePhone.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.MOBILE_PHONE));
                mTxtHomePhone.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.HOME_PHONE));
                mTxtWorkPhone.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.WORK_PHONE));

                mTxtNote.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NOTE));
                mThumbnailPath = CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH);

                if (!TextUtils.isEmpty(mThumbnailPath)) {
                    MediaUtils.setImageFromFile(mPicture, mThumbnailPath);
                }
            }
        }
	}
	
	@Override
	protected boolean validateForm() {
		if (mSpnType.getSelectedItemPosition() == 0) {
			showFormElementError(R.string.customer_alert_type, mSpnType);
			return false;
		}
		
		if (TextUtils.isEmpty(mTxtFullName.getText())) {
			showFormElementError(R.string.customer_alert_shopname, mTxtFullName);
			return false;
		}
		
		return true;
	}
	
	@Override
	protected ContentValues getSaveFormValues() {
		ContentValues values = new ContentValues();
		
		values.put(TableColumn.TYPE, mSpnType.getSelectedItemPosition());
        values.put(TableColumn.LOCALITY_ID, mSpnLocality.getSelectedItemId());

		values.put(TableColumn.NAME, mTxtFullName.getText().toString());
		values.put(TableColumn.ADDRESS, mTxtAddress.getText().toString());
		values.put(TableColumn.MOBILE_PHONE, mTxtMobilePhone.getText().toString());
		values.put(TableColumn.WORK_PHONE, mTxtWorkPhone.getText().toString());
		values.put(TableColumn.HOME_PHONE, mTxtHomePhone.getText().toString());
		values.put(TableColumn.EMAIL, mTxtEmail.getText().toString());
		values.put(TableColumn.WORK_EMAIL, mTxtWorkEmail.getText().toString());
		values.put(TableColumn.MOBILE_PHONE, mTxtMobilePhone.getText().toString());
		values.put(TableColumn.WORK_PHONE, mTxtWorkPhone.getText().toString());
		values.put(TableColumn.HOME_PHONE, mTxtHomePhone.getText().toString());
		values.put(TableColumn.DOB, mTxtDoB.getText().toString());
		values.put(TableColumn.COMPANY, mTxtCompany.getText().toString());
		values.put(TableColumn.DEPARTMENT, mTxtDepartment.getText().toString());
		values.put(TableColumn.TITLE, mTxtTitle.getText().toString());
		values.put(TableColumn.NOTE, mTxtNote.getText().toString());

		if (!TextUtils.isEmpty(mThumbnailPath)) {
			values.put(TableColumn.THUMBNAIL_PATH, mThumbnailPath);
		}

        return values;
	}
	
	private void makePhoneCall(String number) {
		try {
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + number)); 
			startActivity(callIntent);
		} catch (Exception e) {
			Toast.makeText(getActivity(), R.string.call_fail_alert, Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v == mBtnCallHome) {
			String home = mTxtHomePhone.getText().toString();
			
			if (!TextUtils.isEmpty(home)) {
				makePhoneCall(home);
			}
		}
		else if (v == mBtnCallMobile) {
			String mobile = mTxtMobilePhone.getText().toString();

			if (!TextUtils.isEmpty(mobile)) {
				makePhoneCall(mobile);
			}
		}
		else if (v == mBtnCallWork) {
			String work = mTxtWorkPhone.getText().toString();

			if (!TextUtils.isEmpty(work)) {
				makePhoneCall(work);
			}
		}
		else if (v == mPicture) {
			selectPicture();
		}
	}
	
	private void selectPicture() {
		// Create the ACTION_GET_CONTENT Intent
		Intent getContentIntent = FileUtils.createGetContentIntent();
		getContentIntent.setType(FileUtils.MIME_TYPE_IMAGE);

		Intent intent = Intent.createChooser(getContentIntent, getResources().getString(R.string.select_file));
		startActivityForResult(intent, FILE_CHOOSER);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FILE_CHOOSER && resultCode == Activity.RESULT_OK) {
			final Uri uri = data.getData();

			// Get the File path from the Uri
			String path = FileUtils.getPath(getActivity(), uri);

			// Alternatively, use FileUtils.getFile(Context, Uri)
			if (path != null && FileUtils.isLocal(path)) {
				String thumbnailPath = MediaUtils.saveThumbnail(path, "customer_thumbs", THUMBNAIL_SIZE, THUMBNAIL_SIZE);

				if (thumbnailPath != null) {
					if (MediaUtils.setImageFromFile(mPicture, thumbnailPath)) {
						mThumbnailPath = thumbnailPath;
					}
				}
			}	
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle opts) {
        switch (id) {
            case LOADER_LOCALITY:
                CursorLoader loader = new CursorLoader(getActivity(),
                        ContentDescriptor.ContentUri.ExtendedUri.VOCABULARY_GROUP_BY_PARENT,
                        null,
                        "C." + TableColumn.TAXONOMY + " = ? ",
                        new String[] { VocabularyType.LOCALITY},
                        null);
                return loader;
        }

		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (getActivity() == null) {
			return;
		}

        switch (loader.getId()) {
            case LOADER_LOCALITY:
                // Insert extra item to top of the list
                MatrixCursor extras = new MatrixCursor(new String[] {
                        TableColumn._ID,
                        TableColumn.TITLE
                });

                extras.addRow(new String[] { "0", getString(R.string.select_locality) });
                Cursor[] cursors = { extras, data };
                Cursor extendedCursor = new MergeCursor(cursors);

                mLocalityAdapter.swapCursor(extendedCursor);

                int currentLinePos = -1;
                extendedCursor.moveToPosition(-1);

                while (extendedCursor.moveToNext()) {
                    currentLinePos++;

                    int lid = CursorUtils.getRecordIntValue(extendedCursor, TableColumn._ID);
                    if (lid == mLocalityId) {
                        break;
                    }
                }

                mSpnLocality.setSelection(currentLinePos > 0 ? currentLinePos : 0);
                extendedCursor.moveToFirst();
                break;
        }
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_LOCALITY:
                mLocalityAdapter.swapCursor(null);
                break;
        }
	}
}
