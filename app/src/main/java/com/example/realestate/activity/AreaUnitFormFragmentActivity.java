package com.example.realestate.activity;

import com.example.realestate.R;
import com.example.realestate.fragment.AreaUnitFormFragment;

public class AreaUnitFormFragmentActivity extends TaxonomyFormFragmentActivity {

	@Override
	protected int getTitleResource() {
		return R.string.add_area_unit;
	}

    @Override
    protected Class<?> getFragmentClass() {
        return AreaUnitFormFragment.class;
    }

    @Override
	protected int getEditModeTitleResource() {
		return R.string.edit_area_unit;
	}
}
