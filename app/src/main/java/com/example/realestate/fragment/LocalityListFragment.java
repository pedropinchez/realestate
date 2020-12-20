package com.example.realestate.fragment;

import com.example.realestate.activity.LocalityFormFragmentActivity;
import com.example.realestate.common.VocabularyType;

public class LocalityListFragment extends TaxonomyListFragment {

	@Override
	protected String getTaxonomyType() {
		return VocabularyType.LOCALITY;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
		return LocalityFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return LocalityFormFragment.class;
	}

}
