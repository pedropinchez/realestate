package com.example.realestate.activity;


import com.example.realestate.R;
import com.example.realestate.fragment.CustomerFormFragment;

public class CustomerFormFragmentActivity extends BaseItemFormFragmentActivity {
	@Override
	protected int getTitleResource() {
		return R.string.add_customer;
	}
	
	@Override
	protected int getEditModeTitleResource() {
		return R.string.edit_customer;
	}
	
	@Override
	public void onFormCanceled() {
	}

	@Override
	public void onFormSaved() {
	}

	@Override
	protected Class<?> getFragmentClass() {
		return CustomerFormFragment.class;
	}
}
