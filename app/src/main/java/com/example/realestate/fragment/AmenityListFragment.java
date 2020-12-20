package com.example.realestate.fragment;

import com.example.realestate.activity.AmenityFormFragmentActivity;
import com.example.realestate.common.VocabularyType;

public class AmenityListFragment extends TaxonomyListFragment {
	@Override
	protected boolean isHierarchyEnabled() {
		return false;
	}

	@Override
	protected String getTaxonomyType() {
		return VocabularyType.AMENITY;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
        return AmenityFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
        return AmenityFormFragment.class;
	}
}
