package com.example.realestate.activity;


import com.example.realestate.R;
import com.example.realestate.fragment.LocalityFormFragment;

public class LocalityFormFragmentActivity extends TaxonomyFormFragmentActivity {

    @Override
    protected Class<?> getFragmentClass() {
        return LocalityFormFragment.class;
    }

    @Override
	protected int getEditModeTitleResource() {
		return R.string.edit_locality;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.add_locality;
	}
}
