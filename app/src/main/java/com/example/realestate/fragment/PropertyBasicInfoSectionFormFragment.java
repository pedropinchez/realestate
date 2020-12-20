package com.example.realestate.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.realestate.R;
import com.example.realestate.activity.CustomerListActivity;
import com.example.realestate.activity.PickMapLocationActivity;
import com.example.realestate.adapter.BaseCursorMultipleAdapter;
import com.example.realestate.common.VocabularyType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.fragment.dialog.BaseCursorSelectMultipleDialogFragment;
import com.example.realestate.util.AdapterUtils;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.MediaUtils;
import com.example.realestate.util.ViewUtils;
import com.ipaulpro.afilechooser.utils.FileUtils;


import java.util.ArrayList;
import java.util.List;

public class PropertyBasicInfoSectionFormFragment extends BasePropertySectionFormFragment implements LoaderManager.LoaderCallbacks<Cursor>,
View.OnClickListener {
	private static final int LOADER_LOCALITY = 1;
	private static final int LOADER_PROPERTY_TYPE = 2;

    private static final int INTENT_PICK_LOCATION = 1;
    private static final int INTENT_PICK_CUSTOMER = 2;
    private static final int INTENT_PICK_PICTURE  = 3;
    private static final int INTENT_TAKE_PICTURE  = 4;

	private EditText mTxtName;
	private EditText mTxtCode;
	private EditText mTxtLocation;
	private EditText mTxtNote;

    private EditText mTxtCustomerName;
    private EditText mTxtStatus;
    private EditText mTxtType;

	private ImageButton mBtnPickLocation;
    private ImageButton mBtnRemoveCustomer;

    private ImageView mImgCoverPic;
    private ImageView mImgCustomerPic;

	private Spinner mSpnLocality;

	private SimpleCursorAdapter mLocalityAdapter;
	private SimpleCursorAdapter mPropertyTypeAdapter;

	private long mLocalityId;
    private long mCustomerId;
    private String mCoverPicturePath;
    private boolean mHasCamera;

    private List<BaseCursorMultipleAdapter.ItemData> mStatusesData;
    private List<BaseCursorMultipleAdapter.ItemData> mTypesData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStatusesData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
        mTypesData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
	protected void initContentView(View view) {
		super.initContentView(view);
		mImgCoverPic = (ImageView)view.findViewById(R.id.imgCoverPic);
        mImgCoverPic.setOnClickListener(this);
        registerForContextMenu(mImgCoverPic);

		mTxtName = (EditText)view.findViewById(R.id.txtName);
		mTxtCode = (EditText)view.findViewById(R.id.txtCode);
		mTxtLocation = (EditText)view.findViewById(R.id.txtLocation);
		mTxtNote = (EditText)view.findViewById(R.id.txtNote);

        mTxtStatus = (EditText)view.findViewById(R.id.txtStatus);
        mTxtStatus.setOnClickListener(this);

        mTxtType = (EditText)view.findViewById(R.id.txtType);
        mTxtType.setOnClickListener(this);

		mBtnPickLocation = (ImageButton)view.findViewById(R.id.btnPickCoordinate);
		mBtnPickLocation.setOnClickListener(this);
		
		mSpnLocality = (Spinner)view.findViewById(R.id.spnLocality);
        mImgCustomerPic = (ImageView)view.findViewById(R.id.imgCustomerPic);

        mTxtCustomerName = ViewUtils.findEditText(view, R.id.txtCustomerName);
        mTxtCustomerName.setOnClickListener(this);

        mBtnRemoveCustomer = (ImageButton)view.findViewById(R.id.btnRemoveCustomer);
        mBtnRemoveCustomer.setOnClickListener(this);
	}
	
	private void initAdapters() {
		Activity activity = getActivity();
		
		mLocalityAdapter = AdapterUtils.createTaxonomySpinnerAdapter(activity);
		mSpnLocality.setAdapter(mLocalityAdapter);

		mPropertyTypeAdapter = AdapterUtils.createTaxonomySpinnerAdapter(activity);

		getActivity().getSupportLoaderManager().initLoader(LOADER_LOCALITY, null, this);
		getActivity().getSupportLoaderManager().initLoader(LOADER_PROPERTY_TYPE, null, this);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initAdapters();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle opts) {
		switch (id) {
            case LOADER_LOCALITY:
                return CursorUtils.createTaxonomyCursorLoader(getActivity(), VocabularyType.LOCALITY);

            case LOADER_PROPERTY_TYPE:
                return CursorUtils.createTaxonomyCursorLoader(getActivity(), VocabularyType.PROPERTY_TYPE);
        }

        return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (getActivity() == null) {
			return;
		}
		
		Cursor cursor = null;
		
		switch (loader.getId()) {
            case LOADER_LOCALITY:
                cursor = CursorUtils.makeMergedVocabularySpinnerCursor(getActivity(), data, R.string.locality);
                mLocalityAdapter.swapCursor(cursor);
                mSpnLocality.setSelection(CursorUtils.getItemPositionFromId(cursor, mLocalityId));
                break;
        }
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_LOCALITY:
                mLocalityAdapter.swapCursor(null);
                break;

            case LOADER_PROPERTY_TYPE:
                mPropertyTypeAdapter.swapCursor(null);
                break;
        }
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_property_form_basic_info;
	}

	@Override
	public void populateContentView(Cursor cursor, Bundle savedInstanceState) {
		if (cursor != null) {
            ViewUtils.setCursorText(mTxtName, cursor, TableColumn.NAME);
            ViewUtils.setCursorText(mTxtCode, cursor, TableColumn.CODE);
            ViewUtils.setCursorText(mTxtLocation, cursor, TableColumn.ADDRESS);
            ViewUtils.setCursorText(mTxtNote, cursor, TableColumn.DESCRIPTION);

			mLocalityId = CursorUtils.getRecordIntValue(cursor, TableColumn.LOCALITY_ID);
            mCustomerId = CursorUtils.getRecordIntValue(cursor, TableColumn.CUSTOMER_ID);

			String coverPath = CursorUtils.getRecordStringValue(cursor, TableColumn.COVER_PIC_PATH);
			if (!TextUtils.isEmpty(coverPath)) {
                setCoverPicture(coverPath);
			}

            ViewUtils.setCursorText(mTxtCustomerName, cursor, "customer_name");

            String customerThumbnail = CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH);
            if (!TextUtils.isEmpty(customerThumbnail)) {
                MediaUtils.setImageFromFile(mImgCustomerPic, customerThumbnail);
            }
		}
	}

	@Override
	public ContentValues getFormValues() {
		ContentValues values = new ContentValues();
		values.put(TableColumn.NAME, mTxtName.getText().toString());
		values.put(TableColumn.CODE, mTxtCode.getText().toString());
		values.put(TableColumn.ADDRESS, mTxtLocation.getText().toString());
		values.put(TableColumn.DESCRIPTION, mTxtNote.getText().toString());

		values.put(TableColumn.LOCALITY_ID, mSpnLocality.getSelectedItemId());
        values.put(TableColumn.CUSTOMER_ID, mCustomerId);

        if (mStatusesData.size() > 0) {
            values.put(TableColumn.ExtendedColumn.STATUSES, packTaxonomyIds(mStatusesData));
        }

        if (mTypesData.size() > 0) {
            values.put(TableColumn.ExtendedColumn.TYPES, packTaxonomyIds(mTypesData));
        }

        if (mCoverPicturePath != null) {
            values.put(TableColumn.COVER_PIC_PATH, mCoverPicturePath);
        }

		return values;
	}
	
	@Override
	public boolean validateForm(FragmentActivity context) {
        if (ViewUtils.showFormElementErrorIfEmpty(context, R.string.enter_field_value, mTxtName )) {
            return false;
        }

		return super.validateForm(context);
	}

	@Override
	public void onClick(final View v) {
		if (v == mBtnPickLocation) {
            Intent mapIntent = new Intent(getActivity(), PickMapLocationActivity.class);
            mapIntent.setAction(Intent.ACTION_PICK);

            String currentAddress = mTxtLocation.getText().toString();

            if (!TextUtils.isEmpty(currentAddress)) {
                mapIntent.putExtra(PickMapLocationActivity.MAP_ADDRESS, currentAddress);
            }

            startActivityForResult(mapIntent, INTENT_PICK_LOCATION);
		}
        else if (v == mTxtStatus) {
            selectMultipleTaxonomies((EditText)v, VocabularyType.PROPERTY_STATUS, mStatusesData,
                    new BaseCursorSelectMultipleDialogFragment.Callbacks() {
                @Override
                public void onSelectionConfirmed(List<BaseCursorMultipleAdapter.ItemData> itemData) {
                    mStatusesData.clear();
                    mStatusesData = itemData;
                    updateSelectedTaxonomies((EditText)v, mStatusesData);
                }

                @Override
                public void onSelectionCancelled() {

                }
            });
        }
        else if (v == mTxtType) {
            selectMultipleTaxonomies((EditText)v, VocabularyType.PROPERTY_TYPE, mTypesData,
                    new BaseCursorSelectMultipleDialogFragment.Callbacks() {
                @Override
                public void onSelectionConfirmed(List<BaseCursorMultipleAdapter.ItemData> itemData) {
                    mTypesData.clear();
                    mTypesData = itemData;
                    updateSelectedTaxonomies((EditText)v, mTypesData);
                }

                @Override
                public void onSelectionCancelled() {

                }
            });
        }
        else if (v == mTxtCustomerName) {
            Intent intent = new Intent(getActivity(), CustomerListActivity.class);
            intent.setAction(Intent.ACTION_PICK);
            intent.putExtra(PickMapLocationActivity.MAP_ADDRESS, mTxtLocation.getText());
            startActivityForResult(intent, INTENT_PICK_CUSTOMER);
        }
        else if (v == mBtnRemoveCustomer) {
            mTxtCustomerName.setText("");
            mImgCustomerPic.setImageBitmap(null);
            mCustomerId = -1;
        }
        else if (v == mImgCoverPic) {
            mImgCoverPic.showContextMenu();
        }
	}

    private void setCoverPicture(String path) {
        if (path != null && FileUtils.isLocal(path)) {
            int width = mImgCoverPic.getWidth();
            int height = 9 * width/16;
            String thumbnailPath = MediaUtils.saveThumbnail(path, "property_covers", 2 * width, 2 * height);

            if (thumbnailPath != null) {
                MediaUtils.setImageFromFile(mImgCoverPic, thumbnailPath);
                mCoverPicturePath = path;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case INTENT_PICK_LOCATION:
                    mTxtLocation.setText(data.getStringExtra(PickMapLocationActivity.MAP_ADDRESS));
                    return;

                case INTENT_PICK_CUSTOMER:
                    updateCustomer(data);
                    return;

                case INTENT_PICK_PICTURE:
                    final Uri uri = data.getData();
                    setCoverPicture(FileUtils.getPath(getActivity(), uri));
                    return;

                case INTENT_TAKE_PICTURE:
                    setCoverPicture(mCoverPicturePath);
                    return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateCustomer(Intent data) {
        Bundle result = data.getBundleExtra(BaseItemFragment.PICK_RESULT);
        mCustomerId = data.getLongExtra(BaseItemFragment.ITEM_ID, -1);
        mTxtCustomerName.setText(result.getString(TableColumn.NAME));

        MediaUtils.setImageFromFile(mImgCustomerPic, result.getString(TableColumn.THUMBNAIL_PATH));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v == mImgCoverPic) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_cover_profile_picture, menu);

            menu.findItem(R.id.action_remove_photo).setVisible(mCoverPicturePath != null);
            menu.findItem(R.id.action_take_photo).setVisible(MediaUtils.hasCamera(getActivity()));
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_photo:
                MediaUtils.selectFiles(this,
                                       FileUtils.MIME_TYPE_IMAGE,
                                       R.string.select_file,
                                       INTENT_PICK_PICTURE);
                return true;

            case R.id.action_remove_photo:
                mCoverPicturePath = null;
                mImgCoverPic.setImageBitmap(null);
                return true;

            case R.id.action_take_photo:
                String picturePath = MediaUtils.takeCameraPhoto(this, INTENT_TAKE_PICTURE);
                if (picturePath != null) {
                    mCoverPicturePath = picturePath;
                } else {
                    // TODO: alert for failure here
                }
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void updateTaxonomies(Cursor cursor) {
        super.updateTaxonomies(cursor);
        cursor.moveToPosition(-1);

        String vocabulary = null;
        String title = null;
        long id = 0;

        while (cursor.moveToNext()) {
            vocabulary = CursorUtils.getRecordStringValue(cursor, TableColumn.VOCABULARY);
            title = CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE);
            id = CursorUtils.getRecordLongValue(cursor, TableColumn._ID);

            if (vocabulary.equals(VocabularyType.PROPERTY_STATUS)) {
                if (mStatusesData == null) {
                    mStatusesData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
                }

                mStatusesData.add(new BaseCursorMultipleAdapter.ItemData(id, mStatusesData.size(), title));
            }
            else if (vocabulary.equals(VocabularyType.PROPERTY_TYPE)) {
                if (mTypesData == null) {
                    mTypesData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
                }

                mTypesData.add(new BaseCursorMultipleAdapter.ItemData(id, mTypesData.size(), title));
            }
        }

        updateSelectedTaxonomies(mTxtStatus, mStatusesData);
        updateSelectedTaxonomies(mTxtType, mTypesData);
    }
}