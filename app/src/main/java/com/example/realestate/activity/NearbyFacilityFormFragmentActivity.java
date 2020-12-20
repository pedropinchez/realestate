package com.example.realestate.activity;


import com.example.realestate.R;
import com.example.realestate.fragment.NearbyFacilityFormFragment;

public class NearbyFacilityFormFragmentActivity extends TaxonomyFormFragmentActivity {

    @Override
    protected Class<?> getFragmentClass() {
        return NearbyFacilityFormFragment.class;
    }

    @Override
	protected int getEditModeTitleResource() {
		return R.string.edit_facility;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.add_facility;
	}

}
