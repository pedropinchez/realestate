package com.example.realestate.activity;


import com.example.realestate.fragment.NearbyFacilityListFragment;

public class NearbyFacilityListActivity extends TaxonomyListActivity {
    @Override
    protected Class<?> getFragmentClass() {
        return NearbyFacilityListFragment.class;
    }
}
