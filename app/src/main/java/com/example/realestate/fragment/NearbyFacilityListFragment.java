package com.example.realestate.fragment;


import com.example.realestate.activity.NearbyFacilityFormFragmentActivity;
import com.example.realestate.common.VocabularyType;

public class NearbyFacilityListFragment extends TaxonomyListFragment {
	@Override
	protected String getTaxonomyType() {
		return VocabularyType.FACILITY;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
		return NearbyFacilityFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return NearbyFacilityFormFragment.class;
	}
}
