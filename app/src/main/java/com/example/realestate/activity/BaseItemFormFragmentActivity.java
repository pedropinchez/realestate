package com.example.realestate.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.realestate.R;
import com.example.realestate.fragment.BaseItemFormFragment;


public abstract class BaseItemFormFragmentActivity extends BaseItemFragmentActivity implements BaseItemFormFragment.FormFragmentEventsHandler {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (mItemId > 0) {
			int editTitle = getEditModeTitleResource();
			if (editTitle > 0) {
				setTitle(editTitle);
			}
		}
	}
	
	@Override
	protected int getMenuResource() {
		return R.menu.menu_base_form;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean ret = super.onCreateOptionsMenu(menu);

		MenuItem deleteItem = menu.findItem(R.id.action_delete);
		if (deleteItem != null && mItemId <= 0) {
			deleteItem.setVisible(false);
		}
		
		return ret;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                BaseItemFormFragment fragment = (BaseItemFormFragment) mItemFragment;
                if (fragment.saveItem()) {
                    finish();
                } else {

                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
	
	protected boolean isEditMode() {
		return mItemId > 0;
	}

	protected int getEditModeTitleResource() {
		return R.string.edit;
	}
	
	@Override
	public void onFormCanceled() {
        finish();
	}

	@Override
	public void onFormSaved() {
        finish();
	}
}
