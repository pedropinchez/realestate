package com.example.realestate.fragment;


import com.example.realestate.activity.PropertyTypeFormFragmentActivity;
import com.example.realestate.common.VocabularyType;

public class PropertyTypeListFragment extends TaxonomyListFragment {
	@Override
	protected boolean isHierarchyEnabled() {
		return false;
	}
	
	@Override
	protected String getTaxonomyType() {
		return VocabularyType.PROPERTY_TYPE;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
		return PropertyTypeFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return PropertyTypeFormFragment.class;
	}
}
