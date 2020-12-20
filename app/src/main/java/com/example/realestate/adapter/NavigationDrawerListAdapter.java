package com.example.realestate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.realestate.R;
import com.example.realestate.model.NavigationDrawerListItem;

import java.util.List;

public class NavigationDrawerListAdapter extends BaseExpandableListAdapter {
	private Context mContext;
	private List<NavigationDrawerListItem> mItems;
    private long mCurrentItemId = 0;
	
	public NavigationDrawerListAdapter(Context context, List<NavigationDrawerListItem> items) {
		mContext = context;
		mItems = items;
	}

    public void setCurrentItemId(long id) {
        mCurrentItemId = id;
        notifyDataSetChanged();
    }

    private void markCurrentItem(NavigationDrawerListItem itemModel, ViewHolder view) {
        if (view == null || view.mMarker == null) return;

        if (itemModel.mItemId == mCurrentItemId) {
            view.mMarker.setVisibility(View.VISIBLE);
        } else {
            view.mMarker.setVisibility(View.INVISIBLE);
        }
    }

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		NavigationDrawerListItem groupItem = mItems.get(groupPosition);
        return groupItem.isExpandable() ? groupItem.mChildren.get(childPosition) : null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		NavigationDrawerListItem item = (NavigationDrawerListItem)getChild(groupPosition, childPosition);
        return item != null ? item.mItemId : -1;
	}
	
	private NavigationDrawerListItem getItem(int groupPos, int childPos) {
		NavigationDrawerListItem item = mItems.get(groupPos);
		if (!item.isExpandable() || childPos < 0) return item;
		return item.mChildren.get(childPos);
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		NavigationDrawerListItem item = (NavigationDrawerListItem)getChild(groupPosition, childPosition);
		ViewHolder viewHolder = null;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_nav_drawer, null);

            TextView text = (TextView)convertView.findViewById(R.id.txtTitle);
            ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
            View marker = (View)convertView.findViewById(R.id.marker);
            viewHolder = new ViewHolder(text, imgIcon, null, marker);
            convertView.setTag(viewHolder);
		}

		viewHolder = (ViewHolder)convertView.getTag();
        markCurrentItem(item, viewHolder);

		viewHolder.mTxtTitle.setText(item.mTitleResId);
		viewHolder.mImgIcon.setImageResource(item.mIconResId);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		NavigationDrawerListItem groupItem = mItems.get(groupPosition);
		return groupItem.isExpandable() ? groupItem.mChildren.size() : 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mItems.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mItems.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
        NavigationDrawerListItem groupItem = mItems.get(groupPosition);
		return groupItem.mItemId;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		final NavigationDrawerListItem group = mItems.get(groupPosition);
		ViewHolder viewHolder = null;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_nav_drawer_group, null);

            TextView text = (TextView)convertView.findViewById(R.id.txtTitle);
            ImageView imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
            ImageView imgIndicator = (ImageView)convertView.findViewById(R.id.imgIndicator);
            View marker = (View)convertView.findViewById(R.id.marker);

            viewHolder = new ViewHolder(text, imgIcon, imgIndicator, marker);
            convertView.setTag(viewHolder);
		}

		viewHolder = (ViewHolder)convertView.getTag();
        markCurrentItem(group, viewHolder);

        viewHolder.mTxtTitle.setText(group.mTitleResId);

        if (viewHolder.mImgIndicator != null) {
            viewHolder.mImgIndicator.setImageResource(isExpanded ? R.drawable.ic_action_collapse : R.drawable.ic_action_expand);
            if (!group.isExpandable()) {
                viewHolder.mImgIndicator.setVisibility(View.GONE);
            } else {
                viewHolder.mImgIndicator.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.mImgIcon.setImageResource(group.mIconResId);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}
	
	private class ViewHolder {
		public ImageView mImgIcon;
		public ImageView mImgIndicator;
		public TextView mTxtTitle;
        public View mMarker;
		
		public ViewHolder(TextView txtTitle, ImageView imgIcon, ImageView imgIndicator, View marker) {
			mImgIcon = imgIcon;
			mImgIndicator = imgIndicator;
			mTxtTitle = txtTitle;
            mMarker = marker;
		}
	}
}
