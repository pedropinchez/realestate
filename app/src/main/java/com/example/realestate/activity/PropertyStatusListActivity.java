package com.example.realestate.activity;


import com.example.realestate.fragment.PropertyStatusListFragment;

public class PropertyStatusListActivity extends TaxonomyListActivity {
    @Override
    protected Class<?> getFragmentClass() {
        return PropertyStatusListFragment.class;
    }
}
