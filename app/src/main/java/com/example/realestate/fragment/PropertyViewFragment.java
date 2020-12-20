package com.example.realestate.fragment;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;


import com.example.realestate.R;
import com.example.realestate.adapter.PhotoThumbnailSliderViewPagerAdapter;
import com.example.realestate.common.AttachmentType;
import com.example.realestate.common.ContentItemType;
import com.example.realestate.common.VocabularyType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.GeneralUtils;
import com.example.realestate.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class PropertyViewFragment extends BaseItemViewFragment {
    private TextView mTxtTitle;
    private TextView mTxtNote;
    private TextView mTxtCode;
    private TextView mTxtStatus;
    private TextView mTxtType;
    private TextView mTxtAddress;
    private TextView mTxtPrice;
    private TextView mTxtPricePostfix;
    private TextView mTxtCoveredArea;
    private TextView mCarpetArea;
    private TextView mBuiltupArea;
    private TextView mTxtBedrooms;
    private TextView mTxtBathrooms;
    private TextView mTxtBalconies;
    private TextView mTxtGarages;
    private TextView mTxtParkings;
    private TextView mTxtFloorNumber;
    private TextView mTxtTotalFloor;
    private TextView mTxtConstructionYear;
    private TextView mTxtFurnishing;
    private TextView mTxtFacing;
    private TextView mTxtFacilities;
    private TextView mTxtAmenities;
    private TextView mTxtBrokerage;
    private TextView mTxtPager;

    private TableLayout mTable;
    private ViewPager mPhotoViewPager;
    private PhotoThumbnailSliderViewPagerAdapter mPhotoAdapter;

    private String mCurrencyUnit;
    private String mAreaUnit;
    private String mCoverImagePath;

    private boolean mIsAttachmentLoaded = false;
    private boolean mIsItemLoaded = false;
    private List<String> mPhotoPaths = new ArrayList<String>();

	@Override
	protected int getContentView() {
        return R.layout.fragment_property_view;
	}

	@Override
	protected Uri getItemContentUri() {
        return ContentDescriptor.ContentUri.PROPERTY;
	}

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mTxtTitle = (TextView)view.findViewById(R.id.txtTitle);
        mTxtNote = (TextView)view.findViewById(R.id.txtNote);
        mTxtCode = (TextView)view.findViewById(R.id.txtCode);
        mTxtStatus = (TextView)view.findViewById(R.id.txtStatus);
        mTxtType = (TextView)view.findViewById(R.id.txtType);
        mTxtAddress = (TextView)view.findViewById(R.id.txtAddress);
        mTxtPrice = (TextView)view.findViewById(R.id.txtPrice);
        mTxtPricePostfix = (TextView)view.findViewById(R.id.txtPricePostfix);
        mTxtCoveredArea = (TextView)view.findViewById(R.id.txtCoveredArea);
        mCarpetArea = (TextView)view.findViewById(R.id.txtCarpetArea);
        mBuiltupArea = (TextView)view.findViewById(R.id.txtBuiltupArea);
        mTxtBedrooms = (TextView)view.findViewById(R.id.txtBedrooms);
        mTxtBathrooms = (TextView)view.findViewById(R.id.txtBathrooms);
        mTxtBalconies = (TextView)view.findViewById(R.id.txtBalconies);
        mTxtGarages = (TextView)view.findViewById(R.id.txtGarages);
        mTxtParkings = (TextView)view.findViewById(R.id.txtNumParkingLots);
        mTxtFloorNumber = (TextView)view.findViewById(R.id.txtFloorNumber);
        mTxtTotalFloor = (TextView)view.findViewById(R.id.txtTotalFloor);
        mTxtConstructionYear = (TextView)view.findViewById(R.id.txtConstructionYear);
        mTxtFurnishing = (TextView)view.findViewById(R.id.txtFurnishing);
        mTxtFacing = (TextView)view.findViewById(R.id.txtFacing);
        mTxtFacilities = (TextView)view.findViewById(R.id.txtFacility);
        mTxtAmenities = (TextView)view.findViewById(R.id.txtAmenity);
        mTxtBrokerage = (TextView)view.findViewById(R.id.txtBrokerageValue);
        mTxtPager = (TextView)view.findViewById(R.id.txtPager);

        mTable = (TableLayout)view.findViewById(R.id.tblContent);
        mPhotoViewPager = (ViewPager)view.findViewById(R.id.photoPager);
        mPhotoViewPager.setOffscreenPageLimit(3);
        mPhotoViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTxtPager.setText(String.format("%d/%d", (position + 1), mPhotoPaths.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int screenWidth = ViewUtils.getScreenWidth(getActivity());
        mPhotoViewPager.getLayoutParams().width = screenWidth;
        mPhotoViewPager.getLayoutParams().height = screenWidth * 9/16;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadAttachments(new String[]{AttachmentType.IMAGE});
        loadAttributes(new String[]{ContentItemType.PROPERTY});
        loadTaxonomies(null);
    }

    @Override
    protected void onTaxonomiesLoaded(Cursor cursor) {
        super.onTaxonomiesLoaded(cursor);

        String vocabulary = null;
        String title = null;

        List<String> statuses = new ArrayList<String>();
        List<String> facilities = new ArrayList<String>();
        List<String> amenities = new ArrayList<String>();
        List<String> types = new ArrayList<String>();

        while (cursor.moveToNext()) {
            vocabulary = CursorUtils.getRecordStringValue(cursor, TableColumn.VOCABULARY);
            title = CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE);

            if (vocabulary.equals(VocabularyType.CURRENCY_UNIT)) {
                mCurrencyUnit = CursorUtils.getRecordStringValue(cursor, TableColumn.DESCRIPTION);
                if (TextUtils.isEmpty(mCurrencyUnit)) {
                    mCurrencyUnit = title;
                }
            }
            else if (vocabulary.equals(VocabularyType.AREA_UNIT)) {
                mAreaUnit = title;
            }
            else if (vocabulary.equals(VocabularyType.PROPERTY_STATUS)) {
                statuses.add(title);
            }
            else if (vocabulary.equals(VocabularyType.PROPERTY_TYPE)) {
                types.add(title);
            }
            else if (vocabulary.equals(VocabularyType.FACILITY)) {
                facilities.add(title);
            }
            else if (vocabulary.equals(VocabularyType.AMENITY)) {
                amenities.add(title);
            }
        }

        if (statuses.size() > 0) {
            mTxtStatus.setText(TextUtils.join("\n", statuses));
        } else {
            mTxtStatus.setText(R.string.not_set);
        }

        if (facilities.size() > 0) {
            mTxtFacilities.setText(TextUtils.join("\n", facilities));
        } else {
            mTxtFacilities.setText(R.string.not_set);
        }

        if (amenities.size() > 0) {
            mTxtAmenities.setText(TextUtils.join("\n", amenities));
        } else {
            mTxtAmenities.setText(R.string.not_set);
        }
    }

    @Override
    protected void onAttachmentsLoaded(Cursor cursor) {
        super.onAttachmentsLoaded(cursor);
        mIsAttachmentLoaded = true;

        String path = null;

        while (cursor.moveToNext()) {
            path = CursorUtils.getRecordStringValue(cursor, TableColumn.PATH);
            mPhotoPaths.add(path);
        }

        updatePhotoViewPager();
    }

    private void updatePhotoViewPager() {
        if (mIsAttachmentLoaded && mIsItemLoaded && mPhotoPaths.size() > 0) {
            mPhotoAdapter = new PhotoThumbnailSliderViewPagerAdapter(getActivity(), mPhotoPaths);
            mPhotoViewPager.setAdapter(mPhotoAdapter);
            mTxtPager.setText(String.format("1/%d", mPhotoPaths.size()));
        }
    }

    @Override
    protected void onAttributesLoaded(Cursor cursor) {
        super.onAttributesLoaded(cursor);

        String name = null;
        String value = null;
        TableRow row = null;
        LayoutInflater lif = LayoutInflater.from(getActivity());
        TextView txtName = null;
        TextView txtValue = null;

        while (cursor.moveToNext()) {
            name = CursorUtils.getRecordStringValue(cursor, TableColumn.NAME);
            value = CursorUtils.getRecordStringValue(cursor, TableColumn.VALUE);

            row = (TableRow)lif.inflate(R.layout.widget_name_value_pair_item_view, null);
            if (row != null) {
                txtName = (TextView)row.findViewById(R.id.txtName);
                txtValue = (TextView)row.findViewById(R.id.txtValue);

                txtName.setText(name);
                txtValue.setText(value);

                mTable.addView(row);
            }
        }
    }

    @Override
	protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
        if (cursor != null) {
            mIsItemLoaded = true;
            String coverPath = CursorUtils.getRecordStringValue(cursor, TableColumn.COVER_PIC_PATH);

            // Make this the first photo
            if (!mPhotoPaths.contains(coverPath)) {
                mPhotoPaths.add(0, coverPath);
            }
            updatePhotoViewPager();

            Resources res = getResources();
            String notSet = res.getString(R.string.na);

            ViewUtils.setCursorText(mTxtTitle, cursor, TableColumn.NAME);

            String note = CursorUtils.getRecordStringValue(cursor, TableColumn.DESCRIPTION);
            if (TextUtils.isEmpty(note)) {
                mTxtNote.setVisibility(View.GONE);
            } else {
                mTxtNote.setText(note);
            }

            mTxtCode.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.CODE, notSet));
            mTxtType.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.CODE, notSet));
            mTxtAddress.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.ADDRESS, notSet));

            String price = CursorUtils.getRecordStringValue(cursor, TableColumn.PRICE);
            if (TextUtils.isEmpty(price)) {
                mTxtPrice.setText(notSet);
            } else {
                double priceVal = Double.parseDouble(price);
                mTxtPrice.setText(GeneralUtils.formatCurrency(priceVal, "$"));
            }

            mTxtPrice.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.PRICE, notSet));
            mTxtPricePostfix.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.PRICE_POSTFIX, notSet));

            mTxtCoveredArea.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.COVERED_AREA, notSet));
            mCarpetArea.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.CARPET_AREA, notSet));
            mBuiltupArea.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.BUILTUP_AREA, notSet));

            mTxtBedrooms.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.BEDROOMS, notSet));
            mTxtBathrooms.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.BATHROOMS, notSet));
            mTxtBalconies.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.BALCONIES, notSet));
            mTxtGarages.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.GARAGES, notSet));
            mTxtParkings.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.ALLOTTED_PARKING, notSet));

            mTxtFloorNumber.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.FLOOR_NUMBER, notSet));
            mTxtTotalFloor.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.TOTAL_FLOORS, notSet));
            mTxtConstructionYear.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.CONSTRUCTION_YEAR, notSet));

            String[] furnishingArr = res.getStringArray(R.array.furnishing);
            int furnishing = CursorUtils.getRecordIntValue(cursor, TableColumn.FURNISHING);

            if (furnishing >= 0 && furnishing <= furnishingArr.length - 1) {
                mTxtFurnishing.setText(furnishingArr[furnishing]);
            } else {
                mTxtFurnishing.setText(notSet);
            }

            String[] facingArr = res.getStringArray(R.array.facing);
            int facing = CursorUtils.getRecordIntValue(cursor, TableColumn.FACING);
            if (facing >= 0 && facing < facingArr.length) {
                mTxtFacing.setText(facingArr[facing]);
            } else {
                mTxtFacing.setText(notSet);
            }
        }
	}
}
