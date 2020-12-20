package com.example.realestate.activity;


import com.example.realestate.R;
import com.example.realestate.fragment.CurrencyUnitFormFragment;

public class CurrencyUnitFormFragmentActivity extends TaxonomyFormFragmentActivity {
    @Override
    protected Class<?> getFragmentClass() {
        return CurrencyUnitFormFragment.class;
    }

    @Override
	protected int getEditModeTitleResource() {
		return R.string.edit_currency_unit;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.add_currency_unit;
	}
}
