package com.example.realestate.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.view.View;

import com.example.realestate.database.TableColumn;
import com.example.realestate.util.DatabaseUtils;
import com.example.realestate.util.ViewUtils;


public abstract class BaseItemFormFragment extends BaseItemFragment implements
View.OnClickListener {
	protected FormFragmentEventsHandler mEventHandler = null;

	public interface FormFragmentEventsHandler {
		public void onFormCanceled();
		public void onFormSaved();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof FormFragmentEventsHandler) {
			mEventHandler = (FormFragmentEventsHandler)activity;
		}
	}
	
	protected boolean isEditMode() {
		return mItemId > 0;
	}

	protected void showFormElementError(int messageResource, View element) {
		ViewUtils.showFormElementError(getActivity(), messageResource, element);
	}

	protected boolean validateForm() {
		return true;
	}
	
	public boolean saveItem() {
		if (!validateForm()) return false;

        ContentValues values = getSaveFormValues();

        if (values != null) {
            return saveItemToDatabase(values);
        }

        return false;
	}

    protected ContentValues getSaveFormValues() {
        return null;
    }
	
	protected boolean saveItemToDatabase(ContentValues values) {
		if (mItemId > 0) {
			values.put(TableColumn._ID, mItemId);
		}
		long retval = DatabaseUtils.saveItemToDatabase(getActivity(), getItemContentUri(), values);
		
		// Update mItemId with ID of the new item
		if (mItemId <= 0 && retval > 0) {
			mItemId = retval;
		}

		return retval > 0;
	}
}
