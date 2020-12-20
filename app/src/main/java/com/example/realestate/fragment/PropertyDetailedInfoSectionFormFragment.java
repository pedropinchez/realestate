package com.example.realestate.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.realestate.R;
import com.example.realestate.adapter.BaseCursorMultipleAdapter;
import com.example.realestate.adapter.TaxonomyListAdapter;
import com.example.realestate.common.VocabularyType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.fragment.dialog.BaseCursorSelectMultipleDialogFragment;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;


public class PropertyDetailedInfoSectionFormFragment extends BasePropertySectionFormFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener  {
    private static final int LOADER_PRICE_UNIT = 100;
    private static final int LOADER_AREA_UNIT  = 101;
    private static final int LOADER_AMENITY    = 102;
    private static final int LOADER_FACILITY   = 103;

	private EditText mTxtBedrooms;
	private EditText mTxtBathrooms;
	private EditText mTxtBalconies;
	private EditText mTxtGarages;
	private EditText mTxtParking;
	private EditText mTxtFloorNumber;
	private EditText mTxtTotalFloor;
	private EditText mTxtConstructionYear;

    private EditText mTxtPrice;
    private EditText mTxtPricePostfix;
    private EditText mTxtCoveredArea;
    private EditText mTxtCarpetArea;
    private EditText mTxtBuiltupArea;

    private EditText mTxtPriceUnit;
    private EditText mTxtAreaUnit;
    private EditText mTxtAmenity;
    private EditText mTxtFacility;

	private Spinner mSpnFacing;
	private Spinner mSpnFurnishing;

    private SimpleCursorAdapter mPriceUnitAdapter;
    private SimpleCursorAdapter mAreaUnitAdapter;

    private List<BaseCursorMultipleAdapter.ItemData> mAmenityData;
    private List<BaseCursorMultipleAdapter.ItemData> mFacilityData;

    private long mPriceUnitId;
    private long mAreaUnitId;

	@Override
	protected int getContentView() {
		return R.layout.fragment_property_form_detailed_info;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAmenityData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
        mFacilityData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapters();
    }

    private void initAdapters() {
        Activity activity = getActivity();

        mPriceUnitAdapter = new TaxonomyListAdapter(activity);
        mAreaUnitAdapter = new TaxonomyListAdapter(activity);

        getActivity().getSupportLoaderManager().initLoader(LOADER_PRICE_UNIT, null, this);
        getActivity().getSupportLoaderManager().initLoader(LOADER_AREA_UNIT, null, this);
    }

    @Override
	protected void initContentView(View view) {
		super.initContentView(view);
        mTxtBedrooms = (EditText) view.findViewById(R.id.txtBedrooms);
        mTxtBalconies = (EditText) view.findViewById(R.id.txtBalconies);
        mTxtBathrooms = (EditText) view.findViewById(R.id.txtBathrooms);
        mTxtGarages = (EditText) view.findViewById(R.id.txtGarages);
        mTxtParking = (EditText) view.findViewById(R.id.txtNumParkingLots);
        mTxtFloorNumber = (EditText) view.findViewById(R.id.txtFloorNumber);
        mTxtTotalFloor = (EditText) view.findViewById(R.id.txtTotalFloor);
        mTxtConstructionYear = (EditText) view.findViewById(R.id.txtConstructionYear);
        mTxtPrice = (EditText) view.findViewById(R.id.txtPrice);
        mTxtPricePostfix = (EditText) view.findViewById(R.id.txtPricePostfix);
        mTxtCoveredArea = (EditText) view.findViewById(R.id.txtCoveredArea);
        mTxtCarpetArea = (EditText) view.findViewById(R.id.txtCarpetArea);
        mTxtBuiltupArea = (EditText) view.findViewById(R.id.txtBuiltupArea);

        mSpnFacing = (Spinner) view.findViewById(R.id.spnFacing);
        mSpnFurnishing = (Spinner) view.findViewById(R.id.spnFurnishing);

        mTxtPriceUnit = (EditText) view.findViewById(R.id.txtPriceUnit);
        mTxtPriceUnit.setOnClickListener(this);

        mTxtAmenity = (EditText) view.findViewById(R.id.txtAmenity);
        mTxtAmenity.setOnClickListener(this);

        mTxtFacility = (EditText) view.findViewById(R.id.txtFacility);
        mTxtFacility.setOnClickListener(this);

        mTxtAreaUnit = (EditText) view.findViewById(R.id.txtAreaUnit);
        mTxtAreaUnit.setOnClickListener(this);
    }

	@Override
	public void populateContentView(Cursor cursor, Bundle savedInstanceState) {
		if (cursor != null) {
			ViewUtils.setCursorText(mTxtBedrooms, cursor, TableColumn.BEDROOMS);
			ViewUtils.setCursorText(mTxtBathrooms, cursor, TableColumn.BATHROOMS);
			ViewUtils.setCursorText(mTxtBalconies, cursor, TableColumn.BALCONIES);
			ViewUtils.setCursorText(mTxtGarages, cursor, TableColumn.GARAGES);
			ViewUtils.setCursorText(mTxtParking, cursor, TableColumn.ALLOTTED_PARKING);
			ViewUtils.setCursorText(mTxtFloorNumber, cursor, TableColumn.FLOOR_NUMBER);
			ViewUtils.setCursorText(mTxtTotalFloor, cursor, TableColumn.TOTAL_FLOORS);
			ViewUtils.setCursorText(mTxtConstructionYear, cursor, TableColumn.CONSTRUCTION_YEAR);
            ViewUtils.setCursorText(mTxtPrice, cursor, TableColumn.PRICE);
            ViewUtils.setCursorText(mTxtPricePostfix, cursor, TableColumn.PRICE_POSTFIX);
            ViewUtils.setCursorText(mTxtCoveredArea, cursor, TableColumn.COVERED_AREA);
            ViewUtils.setCursorText(mTxtCarpetArea, cursor, TableColumn.CARPET_AREA);
            ViewUtils.setCursorText(mTxtBuiltupArea, cursor, TableColumn.BUILTUP_AREA);

            int furnishing = CursorUtils.getRecordIntValue(cursor, TableColumn.FURNISHING);
            if (furnishing >= 0) {
                mSpnFurnishing.setSelection(furnishing);
            }

            int facing = CursorUtils.getRecordIntValue(cursor, TableColumn.FACING);
            if (facing >= 0) {
                mSpnFacing.setSelection(facing);
            }

            mPriceUnitId = CursorUtils.getRecordIntValue(cursor, TableColumn.PRICE_UNIT_ID);
            updateUnitText(mPriceUnitId, mPriceUnitAdapter, mTxtPriceUnit);

            mAreaUnitId = CursorUtils.getRecordIntValue(cursor, TableColumn.AREA_UNIT_ID);
            updateUnitText(mAreaUnitId, mAreaUnitAdapter, mTxtAreaUnit);
		}
	}

    @Override
    public boolean validateForm(FragmentActivity context) {
        if (ViewUtils.showFormElementErrorIfEmpty(context, R.string.enter_field_value, mTxtPrice )) {
            return false;
        }

        return super.validateForm(context);
    }

    @Override
	public ContentValues getFormValues() {
		ContentValues values = new ContentValues();

		values.put(TableColumn.BEDROOMS, mTxtBedrooms.getText().toString());
		values.put(TableColumn.BALCONIES, mTxtBalconies.getText().toString());
		values.put(TableColumn.BATHROOMS, mTxtBathrooms.getText().toString());
		values.put(TableColumn.GARAGES, mTxtGarages.getText().toString());
		values.put(TableColumn.ALLOTTED_PARKING, mTxtParking.getText().toString());
		values.put(TableColumn.FLOOR_NUMBER, mTxtFloorNumber.getText().toString());
		values.put(TableColumn.TOTAL_FLOORS, mTxtTotalFloor.getText().toString());
		values.put(TableColumn.CONSTRUCTION_YEAR, mTxtConstructionYear.getText().toString());

        values.put(TableColumn.PRICE_UNIT_ID, mPriceUnitId);
        values.put(TableColumn.AREA_UNIT_ID, mAreaUnitId);

        values.put(TableColumn.PRICE, mTxtPrice.getText().toString());
        values.put(TableColumn.PRICE_POSTFIX, mTxtPricePostfix.getText().toString());
        values.put(TableColumn.COVERED_AREA, mTxtCoveredArea.getText().toString());
        values.put(TableColumn.CARPET_AREA, mTxtCarpetArea.getText().toString());
        values.put(TableColumn.BUILTUP_AREA, mTxtBuiltupArea.getText().toString());

        values.put(TableColumn.FACING, mSpnFacing.getSelectedItemPosition());
        values.put(TableColumn.FURNISHING, mSpnFurnishing.getSelectedItemPosition());

        if (mAmenityData.size() > 0) {
            values.put(TableColumn.ExtendedColumn.AMENITIES, packTaxonomyIds(mAmenityData));
        }

        if (mFacilityData.size() > 0) {
            values.put(TableColumn.ExtendedColumn.FACILITIES, packTaxonomyIds(mFacilityData));
        }

		return values;
	}

    private void updateUnitText(long unitId, SimpleCursorAdapter adapter, EditText view) {
        Cursor cursor = adapter.getCursor();

        if (unitId > 0 && cursor != null) {
            int pos = CursorUtils.getItemPositionFromId(cursor, unitId);
            if (pos >= 0) {
                cursor.moveToPosition(pos);
                ViewUtils.setCursorText(view, cursor, TableColumn.TITLE);
            }
        }
    }

    @Override
    public void onClick(final View v) {
        if (v == mTxtPriceUnit) {
            selectSingleTaxonomy((EditText)v, mPriceUnitAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Cursor cursor = mPriceUnitAdapter.getCursor();
                    cursor.moveToPosition(i);
                    mPriceUnitId = CursorUtils.getRecordLongValue(cursor, TableColumn._ID);
                }
            });
        }
        else if (v == mTxtAreaUnit) {
            selectSingleTaxonomy((EditText)v, mAreaUnitAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Cursor cursor = mAreaUnitAdapter.getCursor();
                    cursor.moveToPosition(i);
                    mAreaUnitId = CursorUtils.getRecordLongValue(cursor, TableColumn._ID);
                }
            });
        }
        else if (v == mTxtAmenity) {
            selectMultipleTaxonomies((EditText)v, VocabularyType.AMENITY, mAmenityData,
                    new BaseCursorSelectMultipleDialogFragment.Callbacks() {
                @Override
                public void onSelectionConfirmed(List<BaseCursorMultipleAdapter.ItemData> itemData) {
                    mAmenityData.clear();
                    mAmenityData = itemData;
                    updateSelectedTaxonomies((EditText)v, mAmenityData);
                }

                @Override
                public void onSelectionCancelled() {

                }
            });
        }
        else if (v == mTxtFacility) {
            selectMultipleTaxonomies((EditText)v, VocabularyType.FACILITY, mFacilityData,
                    new BaseCursorSelectMultipleDialogFragment.Callbacks() {
                @Override
                public void onSelectionConfirmed(List<BaseCursorMultipleAdapter.ItemData> itemData) {
                    mFacilityData.clear();
                    mFacilityData = itemData;
                    updateSelectedTaxonomies((EditText)v, mFacilityData);
                }

                @Override
                public void onSelectionCancelled() {

                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle opts) {
        switch (id) {
            case LOADER_PRICE_UNIT:
                return CursorUtils.createTaxonomyCursorLoader(getActivity(), VocabularyType.CURRENCY_UNIT);

            case LOADER_AREA_UNIT:
                return CursorUtils.createTaxonomyCursorLoader(getActivity(), VocabularyType.AREA_UNIT);
        }

        return null;
    }

    private void swapCursor(int loaderId, Cursor data) {
        switch (loaderId) {
            case LOADER_PRICE_UNIT:
                mPriceUnitAdapter.swapCursor(data);
                updateUnitText(mPriceUnitId, mPriceUnitAdapter, mTxtPriceUnit);
                break;

            case LOADER_AREA_UNIT:
                mAreaUnitAdapter.swapCursor(data);
                updateUnitText(mAreaUnitId, mAreaUnitAdapter, mTxtAreaUnit);
                break;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        swapCursor(loader.getId(), data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        swapCursor(loader.getId(), null);
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

            if (vocabulary.equals(VocabularyType.AMENITY)) {
                if (mAmenityData == null) {
                    mAmenityData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
                }

                mAmenityData.add(new BaseCursorMultipleAdapter.ItemData(id, mAmenityData.size(), title));
            }
            else if (vocabulary.equals(VocabularyType.FACILITY)) {
                if (mFacilityData == null) {
                    mFacilityData = new ArrayList<BaseCursorMultipleAdapter.ItemData>();
                }

                mFacilityData.add(new BaseCursorMultipleAdapter.ItemData(id, mFacilityData.size(), title));
            }
        }

        updateSelectedTaxonomies(mTxtAmenity, mAmenityData);
        updateSelectedTaxonomies(mTxtFacility, mFacilityData);
    }
}
