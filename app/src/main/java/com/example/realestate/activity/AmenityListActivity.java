package com.example.realestate.activity;


import com.example.realestate.fragment.AmenityListFragment;

public class AmenityListActivity extends TaxonomyListActivity {
    @Override
    protected Class<?> getFragmentClass() {
        return AmenityListFragment.class;
    }
}
