package com.example.realestate.activity;

import android.content.Intent;
import android.view.MenuItem;

import com.example.realestate.R;


public abstract class BaseItemViewFragmentActivity extends BaseItemFragmentActivity {
	@Override
	protected int getMenuResource() {
		return R.menu.menu_base_item_view;
	}
	
	protected abstract Class<?> getItemFormActivityClass();

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, getItemFormActivityClass());
                intent.putExtra(ITEM_ID, mItemId);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
	protected void onResume() {
		super.onResume();
		// Reload the view when returning from Edit activity
		// TODO: Check if this is done twice (one here and one in fragment)
		mItemFragment.reloadItem();
	}
}
