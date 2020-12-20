package com.example.realestate.fragment;


import com.example.realestate.R;
import com.example.realestate.common.VocabularyType;

public class CurrencyUnitFormFragment extends TaxonomyFormFragment {
	@Override
	protected boolean isHierarchyEnabled() {
		return false;
	}

	@Override
	protected String getVocabularyName() {
		return VocabularyType.CURRENCY_UNIT;
	}

    @Override
    protected int getDescriptionFieldHint() {
        return R.string.currency_symbol;
    }
}
