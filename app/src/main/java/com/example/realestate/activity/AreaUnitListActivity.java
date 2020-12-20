package com.example.realestate.activity;


import com.example.realestate.fragment.AreaUnitListFragment;

public class AreaUnitListActivity extends TaxonomyListActivity {
    @Override
    protected Class<?> getFragmentClass() {
        return AreaUnitListFragment.class;
    }
}
