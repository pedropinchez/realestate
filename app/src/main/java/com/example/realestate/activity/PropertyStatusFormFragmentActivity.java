package com.example.realestate.activity;

import com.example.realestate.R;
import com.example.realestate.fragment.PropertyStatusFormFragment;

public class PropertyStatusFormFragmentActivity extends TaxonomyFormFragmentActivity {
    @Override
    protected Class<?> getFragmentClass() {
        return PropertyStatusFormFragment.class;
    }

    @Override
	protected int getEditModeTitleResource() {
		return R.string.edit_property_status;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.add_property_status;
	}
}
