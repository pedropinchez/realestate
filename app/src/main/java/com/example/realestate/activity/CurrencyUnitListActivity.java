package com.example.realestate.activity;


import com.example.realestate.fragment.CurrencyUnitListFragment;

public class CurrencyUnitListActivity extends TaxonomyListActivity {

    @Override
    protected Class<?> getFragmentClass() {
        return CurrencyUnitListFragment.class;
    }
}
