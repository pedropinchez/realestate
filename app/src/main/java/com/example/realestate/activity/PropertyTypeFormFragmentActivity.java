package com.example.realestate.activity;

import com.example.realestate.R;
import com.example.realestate.fragment.PropertyTypeFormFragment;

public class PropertyTypeFormFragmentActivity extends TaxonomyFormFragmentActivity {

    @Override
    protected Class<?> getFragmentClass() {
        return PropertyTypeFormFragment.class;
    }

    @Override
	protected int getEditModeTitleResource() {
		return R.string.edit_property_type;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.add_property_type;
	}
}
