package com.example.realestate.fragment;


import com.example.realestate.activity.CurrencyUnitFormFragmentActivity;
import com.example.realestate.common.VocabularyType;

public class CurrencyUnitListFragment extends TaxonomyListFragment {
	@Override
	protected boolean isHierarchyEnabled() {
		return false;
	}

	@Override
	protected String getTaxonomyType() {
		return VocabularyType.CURRENCY_UNIT;
	}

	@Override
	public Class<?> getItemFormActivityClass() {
		return CurrencyUnitFormFragmentActivity.class;
	}

	@Override
	public Class<?> getItemFormFragmentClass() {
		return CurrencyUnitFormFragment.class;
	}
}
