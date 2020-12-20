package com.example.realestate.activity;


import com.example.realestate.fragment.PropertyViewFragment;

public class PropertyViewFragmentActivity extends BaseItemViewFragmentActivity {

	@Override
	protected Class<?> getFragmentClass() {
        return PropertyViewFragment.class;
	}

	@Override
	protected Class<?> getItemFormActivityClass() {
        return PropertyFormActivity.class;
	}
}
