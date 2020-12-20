package com.example.realestate.fragment;


import com.example.realestate.activity.AreaUnitFormFragmentActivity;
import com.example.realestate.common.VocabularyType;

public class AreaUnitListFragment extends TaxonomyListFragment {
	@Override
	protected boolean isHierarchyEnabled() {
		return false;
	}

	@Override
	protected String getTaxonomyType() {
		return VocabularyType.AREA_UNIT;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
		return AreaUnitFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return AreaUnitFormFragment.class;
	}
}
