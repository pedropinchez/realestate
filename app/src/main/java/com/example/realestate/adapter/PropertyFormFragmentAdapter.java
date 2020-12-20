package com.example.realestate.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.realestate.fragment.BasePropertySectionFormFragment;

import java.util.List;

public class PropertyFormFragmentAdapter extends FragmentStatePagerAdapter {
	List<BasePropertySectionFormFragment> mFragments;

	public PropertyFormFragmentAdapter(FragmentManager fm, List<BasePropertySectionFormFragment> fragments) {
		super(fm);
		mFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

}
