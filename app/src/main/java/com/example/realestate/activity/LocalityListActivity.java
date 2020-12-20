package com.example.realestate.activity;


import com.example.realestate.fragment.LocalityListFragment;

public class LocalityListActivity extends TaxonomyListActivity {

    @Override
    protected Class<?> getFragmentClass() {
        return LocalityListFragment.class;
    }
}
