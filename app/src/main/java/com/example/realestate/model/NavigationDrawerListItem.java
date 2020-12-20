package com.example.realestate.model;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerListItem {
	public int mTitleResId;
	public int mIconResId;
	public Class<?> mIntentClass;
	public List<NavigationDrawerListItem> mChildren;
	public int mItemId;

	public NavigationDrawerListItem(int titleRestId, int iconResId, int itemId, Class<?> intentClass) {
		mItemId = itemId;
		mTitleResId = titleRestId;
		mIconResId = iconResId;
		mIntentClass = intentClass;
		mChildren = new ArrayList<NavigationDrawerListItem>();
	}

	public boolean isExpandable() {
		return mChildren.size() > 0;
	}
	
	public void addChild(NavigationDrawerListItem child) {
		mChildren.add(child);
	}
}
