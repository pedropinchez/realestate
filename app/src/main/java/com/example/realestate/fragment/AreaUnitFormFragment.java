package com.example.realestate.fragment;


import com.example.realestate.common.VocabularyType;

public class AreaUnitFormFragment extends TaxonomyFormFragment {
	@Override
	protected boolean isHierarchyEnabled() {
		return false;
	}

	@Override
	protected String getVocabularyName() {
		return VocabularyType.AREA_UNIT;
	}
}
