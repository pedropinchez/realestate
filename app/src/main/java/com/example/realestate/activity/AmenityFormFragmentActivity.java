package com.example.realestate.activity;


import com.example.realestate.R;
import com.example.realestate.fragment.AmenityFormFragment;

public class AmenityFormFragmentActivity extends TaxonomyFormFragmentActivity {

	@Override
	protected int getTitleResource() {
		return R.string.add_amenity;
	}

    @Override
    protected Class<?> getFragmentClass() {
        return AmenityFormFragment.class;
    }

    @Override
	protected int getEditModeTitleResource() {
		return R.string.edit_amenity;
	}
}
