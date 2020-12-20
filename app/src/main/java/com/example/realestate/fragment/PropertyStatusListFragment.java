package com.example.realestate.fragment;

import com.example.realestate.activity.PropertyStatusFormFragmentActivity;
import com.example.realestate.common.VocabularyType;

public class PropertyStatusListFragment extends TaxonomyListFragment {
	@Override
	protected boolean isHierarchyEnabled() {
		return false;
	}
	
	@Override
	protected String getTaxonomyType() {
		return VocabularyType.PROPERTY_STATUS;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
		return PropertyStatusFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return PropertyFormFragment.class;
	}
}
