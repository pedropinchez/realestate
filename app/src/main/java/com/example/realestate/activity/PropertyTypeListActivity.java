package com.example.realestate.activity;


import com.example.realestate.fragment.PropertyTypeListFragment;

public class PropertyTypeListActivity extends TaxonomyListActivity {

    @Override
    protected Class<?> getFragmentClass() {
        return PropertyTypeListFragment.class;
    }
}
