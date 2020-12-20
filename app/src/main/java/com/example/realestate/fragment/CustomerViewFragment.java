package com.example.realestate.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.util.Pair;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.realestate.R;
import com.example.realestate.common.CustomerType;
import com.example.realestate.common.Gender;
import com.example.realestate.database.TableColumn;
import com.example.realestate.fragment.dialog.ActionDialogFragment;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewFragment extends BaseItemViewFragment implements View.OnClickListener {
	private TextView mTxtName;
	private TextView mTxtType;
	private TextView mTxtLocality;
	private TextView mTxtAddress;
	private TextView mTxtEmail;
	private TextView mTxtWorkEmail;
	private TextView mTxtMobilePhone;
	private TextView mTxtWorkPhone;
	private TextView mTxtHomePhone;
	private TextView mTxtGender;
	private TextView mTxtDoB;
	private TextView mTxtCompany;
	private TextView mTxtDepartment;
	private TextView mTxtTitle;
	private TextView mTxtNote;
	private ImageView mImgPic;

    private ImageButton mBtnCall;
    private ImageButton mBtnSendEmail;
    private ImageButton mBtnSendSMS;

    private String mWorkEmail;
    private String mPersonalEmail;

    private String mWorkPhone;
    private String mHomePhone;
    private String mMobilePhone;

	@Override
	protected int getContentView() {
		return R.layout.fragment_customer_view;
	}

	@Override
	protected Uri getItemContentUri() {
		return ContentDescriptor.ContentUri.CUSTOMER;
	}

    @Override
    public void onClick(final View view) {
        final Activity activity = getActivity();
        int dialogTitle = 0;

        if (view == mBtnCall || view == mBtnSendSMS || view == mBtnSendEmail) {
            final List<Pair<String, Integer>> items;

            if (view == mBtnCall || view == mBtnSendSMS) {
                items = getPhoneActionMenuItems();
                dialogTitle = R.string.select_phone_number;
            } else if (view == mBtnSendEmail) {
                items = getEmailActionMenuItems();
                dialogTitle = R.string.select_email_address;
            } else {
                items = null;
            }

            if (items == null) return;

            ActionDialogFragment dialog = new ActionDialogFragment();
            dialog.setActionItems(items).setTitle(dialogTitle);
            dialog.setOnClickListener(new Dialog.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Pair<String, Integer> pair = items.get(i);

                    if (view == mBtnCall || view == mBtnSendSMS) {
                        String phoneNumber = pair.first;

                        if (view == mBtnCall && !GeneralUtils.makePhoneCall(activity, phoneNumber)) {
                            // TODO: Inform users here
                        } else if (view == mBtnSendSMS && !GeneralUtils.sendSMS(activity, phoneNumber)) {
                            // TODO: Inform users here
                        }
                    } else {
                        if (!GeneralUtils.sendEmail(activity, pair.first)) {
                            // TODO: Inform users here
                        }
                    }
                }
            });
            dialog.show(getFragmentManager(), "CustomerViewFragment");
        }
    }

    private List<Pair<String, Integer>> getPhoneActionMenuItems() {
        List<Pair<String, Integer>> items = new ArrayList<Pair<String, Integer>>();

        if (!TextUtils.isEmpty(mMobilePhone)) {
            items.add(new Pair<String, Integer>(mMobilePhone, R.string.mobile_phone));
        }
        if (!TextUtils.isEmpty(mHomePhone)) {
            items.add(new Pair<String, Integer>(mHomePhone, R.string.home_phone));
        }
        if (!TextUtils.isEmpty(mWorkPhone)) {
            items.add(new Pair<String, Integer>(mWorkPhone, R.string.work_phone));
        }

        return items;
    }

    private List<Pair<String, Integer>> getEmailActionMenuItems() {
        List<Pair<String, Integer>> items = new ArrayList<Pair<String, Integer>>();

        if (!TextUtils.isEmpty(mPersonalEmail)) {
            items.add(new Pair<String, Integer>(mPersonalEmail, R.string.email));
        }
        if (!TextUtils.isEmpty(mWorkEmail)) {
            items.add(new Pair<String, Integer>(mWorkEmail, R.string.work_email));
        }

        return items;
    }

    @Override
	protected void initContentView(View view) {
		super.initContentView(view);
		mTxtName = (TextView)view.findViewById(R.id.txtName);
		mTxtType = (TextView)view.findViewById(R.id.txtType);
		mTxtLocality = (TextView)view.findViewById(R.id.txtLocality);
		mTxtAddress = (TextView)view.findViewById(R.id.txtAddress);
		mTxtEmail = (TextView)view.findViewById(R.id.txtEmail);
		mTxtWorkEmail = (TextView)view.findViewById(R.id.txtWorkEmail);
		mTxtMobilePhone = (TextView)view.findViewById(R.id.txtMobilePhone);
		mTxtWorkPhone = (TextView)view.findViewById(R.id.txtWorkPhone);
		mTxtHomePhone = (TextView)view.findViewById(R.id.txtHomePhone);
		mTxtGender = (TextView)view.findViewById(R.id.txtGender);
		mTxtDoB = (TextView)view.findViewById(R.id.txtDoB);
		mTxtCompany = (TextView)view.findViewById(R.id.txtCompany);
		mTxtDepartment = (TextView)view.findViewById(R.id.txtDepartment);
		mTxtTitle = (TextView)view.findViewById(R.id.txtTitle);
		mTxtNote = (TextView)view.findViewById(R.id.txtNote);
		mImgPic = (ImageView)view.findViewById(R.id.imgUserPic);

        mBtnCall = (ImageButton)view.findViewById(R.id.btnCall);
        mBtnSendEmail = (ImageButton)view.findViewById(R.id.btnSendEmail);
        mBtnSendSMS = (ImageButton)view.findViewById(R.id.btnSMS);

        mBtnCall.setOnClickListener(this);
        mBtnSendEmail.setOnClickListener(this);
        mBtnSendSMS.setOnClickListener(this);
	}
	
	private boolean showIfNotEmpty(TextView view, View viewToShow, String str) {
		boolean empty = false;
		
		if (TextUtils.isEmpty(str)) {
			empty = true;
			viewToShow.setVisibility(View.GONE);
		} else {
			view.setText(str);
			viewToShow.setVisibility(View.VISIBLE);
		}
		
		return empty;
	}

	@Override
	protected void populateContentView(Cursor cursor, Bundle savedInstanceState) {
		View view = getView();
		
		String thumbPath = CursorUtils.getRecordStringValue(cursor, TableColumn.THUMBNAIL_PATH);
		if (!TextUtils.isEmpty(thumbPath)) {
			Bitmap thumbnail = BitmapFactory.decodeFile(thumbPath);
			if (thumbnail != null) {
				mImgPic.setImageBitmap(thumbnail);
			}
		}

        int customerType = CursorUtils.getRecordIntValue(cursor, TableColumn.TYPE);
        mTxtType.setText(customerType == CustomerType.PRIVATE ? R.string.customer_type_private : R.string.customer_type_company);

        int gender = CursorUtils.getRecordIntValue(cursor, TableColumn.GENDER);
        mTxtGender.setText(gender == Gender.MALE ? R.string.male : R.string.female);

        boolean localityEmpty = showIfNotEmpty(
                mTxtLocality,
                view.findViewById(R.id.groupLocality),
                CursorUtils.getRecordStringValue(cursor, "locality_name"));

        String address = CursorUtils.getRecordStringValue(cursor, TableColumn.ADDRESS);
        showIfNotEmpty(mTxtAddress, mTxtAddress, address);

        if (localityEmpty && TextUtils.isEmpty(address)) {
            view.findViewById(R.id.groupAddress).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.groupAddress).setVisibility(View.VISIBLE);
        }

		mTxtName.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NAME));

        mPersonalEmail = CursorUtils.getRecordStringValue(cursor, TableColumn.EMAIL);
        mWorkEmail = CursorUtils.getRecordStringValue(cursor, TableColumn.WORK_EMAIL);

        mHomePhone = CursorUtils.getRecordStringValue(cursor, TableColumn.HOME_PHONE);
        mMobilePhone = CursorUtils.getRecordStringValue(cursor, TableColumn.MOBILE_PHONE);
        mWorkPhone = CursorUtils.getRecordStringValue(cursor, TableColumn.WORK_PHONE);

		boolean emailEmpty = showIfNotEmpty(
				mTxtEmail, 
				view.findViewById(R.id.groupEmail), 
				mPersonalEmail);

		boolean workEmailEmpty = showIfNotEmpty(
				mTxtWorkEmail, 
				view.findViewById(R.id.groupWorkEmail), 
				mWorkEmail);
		
		if (emailEmpty && workEmailEmpty) {
			view.findViewById(R.id.groupEmail).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.groupEmail).setVisibility(View.VISIBLE);
		}

		boolean companyEmpty = showIfNotEmpty(mTxtCompany, 
				mTxtCompany, 
				CursorUtils.getRecordStringValue(cursor, TableColumn.COMPANY));
		boolean departmentEmpty = showIfNotEmpty(mTxtDepartment, 
				mTxtDepartment, 
				CursorUtils.getRecordStringValue(cursor, TableColumn.DEPARTMENT));
		boolean titleEmpty = showIfNotEmpty(mTxtTitle, 
				mTxtTitle, 
				CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE));
		
		if (companyEmpty && departmentEmpty && titleEmpty) {
			view.findViewById(R.id.groupCompany).setVisibility(View.GONE);
		} else {
			view.findViewById(R.id.groupCompany).setVisibility(View.VISIBLE);
		}

		boolean mobilePhoneEmpty = showIfNotEmpty(
				mTxtMobilePhone, 
				view.findViewById(R.id.groupMobilePhone), 
				mMobilePhone);
		boolean workPhoneEmpty = showIfNotEmpty(
				mTxtWorkPhone, 
				view.findViewById(R.id.groupWorkPhone), 
				mWorkPhone);
		boolean homePhoneEmpty = showIfNotEmpty(
				mTxtHomePhone, 
				view.findViewById(R.id.groupHomePhone), 
				mHomePhone);
		
		if (mobilePhoneEmpty && workPhoneEmpty && homePhoneEmpty) {
			view.findViewById(R.id.groupPhone).setVisibility(View.GONE);
		}

		showIfNotEmpty(
				mTxtDoB, 
				mTxtDoB, 
				CursorUtils.getRecordStringValue(cursor, TableColumn.DOB));

		showIfNotEmpty(
				mTxtNote,
                mTxtNote,
				CursorUtils.getRecordStringValue(cursor, TableColumn.NOTE));
	}
}
