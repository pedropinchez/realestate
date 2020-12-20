package com.example.realestate.activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.viewpager.widget.ViewPager;


import com.example.realestate.R;
import com.example.realestate.adapter.PropertyFormFragmentAdapter;
import com.example.realestate.common.AttachmentType;
import com.example.realestate.common.ContentItemType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.fragment.BasePropertySectionFormFragment;
import com.example.realestate.fragment.PropertyBasicInfoSectionFormFragment;
import com.example.realestate.fragment.PropertyDetailedInfoSectionFormFragment;
import com.example.realestate.fragment.PropertyOtherInfoSectionFormFragment;
import com.example.realestate.fragment.PropertyPicturesSectionFormFragment;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public class PropertyFormActivity extends LoggedInRequiredActivity implements ActionBar.TabListener,
        LoaderManager.LoaderCallbacks<Cursor> {
	public static final String ITEM_ID = "item_id";
    private static final int LOADER_ITEM = 0;

    // NOTE: These IDs need to be unique for Activity won't load new cursors
    private static final int LOADER_TAXONOMY = 1000;
    private static final int LOADER_ATTRIBUTE = 1001;
    private static final int LOADER_ATTACHMENT = 1002;

	private PropertyFormFragmentAdapter mFragmentAdapter;
    private ViewPager mViewPager;
    private List<BasePropertySectionFormFragment> mFragments;
    private long mItemId = 0;
    private Cursor mItemCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_form);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		createFragments(savedInstanceState);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(ITEM_ID)) {
				mItemId = bundle.getLong(ITEM_ID);
			}
		}

        // If we're recreating the activity, load the saved item id
        if (savedInstanceState != null) {
            mItemId = savedInstanceState.getLong(ITEM_ID);
        }
		
		if (mItemId > 0) {
			setTitle(R.string.edit_property);
            loadItem(mItemId, savedInstanceState);
		}

        // ViewPager and its adapters use support library fragments, so use getSupportFragmentManager.
        mFragmentAdapter = new PropertyFormFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        	@Override
        	public void onPageSelected(int position) {
        		// When swiping between pages, select the corresponding tab.
        		getSupportActionBar().setSelectedNavigationItem(position);
        	}
        });

		createTabs(actionBar);
	}
	
	private void createFragments(Bundle savedInstanceState) {
        mFragments = new ArrayList<BasePropertySectionFormFragment>();

        mFragments.add(new PropertyBasicInfoSectionFormFragment());
        mFragments.add(new PropertyDetailedInfoSectionFormFragment());
        mFragments.add(new PropertyOtherInfoSectionFormFragment());
        mFragments.add(new PropertyPicturesSectionFormFragment());
    }
	
	private void createTabs(ActionBar actionBar) {
		actionBar.addTab(actionBar.newTab().setText("Basic info").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Detailed info").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Other info").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Pictures").setTabListener(this));
	}
	
	protected boolean validateForm() {
        for (int i = 0, c = mFragments.size(); i < c; i++) {
            if (!mFragments.get(i).validateForm(this)) {
                mViewPager.setCurrentItem(i, true);
                return false;
            }
        }

		return true;
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(ITEM_ID, mItemId);
        super.onSaveInstanceState(outState);
    }

    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mItemId <= 0) {
			menu.findItem(R.id.action_delete).setVisible(false);
		}
	    return true;
	}
	
	@Override
	protected int getTitleResource() {
		return R.string.add_product;
	}
	
	@Override
	protected int getMenuResource() {
		return R.menu.menu_base_form;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		case R.id.action_save:
			saveItem();
			return true;
			
		case R.id.action_delete:
			deleteItem();
			break;
		}

        return super.onOptionsItemSelected(item);
    }
	
	private void loadItem(long itemId, Bundle savedInstanceState) {
        getSupportLoaderManager().initLoader(LOADER_ITEM, null, this);
        getSupportLoaderManager().initLoader(LOADER_ATTACHMENT, null, this);
        getSupportLoaderManager().initLoader(LOADER_TAXONOMY, null, this);
        getSupportLoaderManager().initLoader(LOADER_ATTRIBUTE, null, this);
	}
	
	private void saveItem() {
		if (!validateForm()) return;

		ContentValues values = new ContentValues();
		ContentValues subValues = null;
		
		for (BasePropertySectionFormFragment fragment : mFragments) {
			subValues = fragment.getFormValues();
			if (subValues != null) {
				values.putAll(fragment.getFormValues());
			}
		}
		
		if (mItemId > 0) {
			values.put(TableColumn._ID, mItemId);
		}
		
		if (DatabaseUtils.saveItemToDatabase(this, ContentDescriptor.ContentUri.PROPERTY, values) > 0) {
			finish();
		} else {
			Toast.makeText(this, R.string.save_fail_alert, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void deleteItem() {
		new AlertDialog.Builder(this)
        .setTitle(R.string.logout)
        .setMessage(R.string.delete_alert)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	DatabaseUtils.deleteItem(PropertyFormActivity.this, ContentDescriptor.ContentUri.PROPERTY, mItemId);
            }
        })
        .setNegativeButton(android.R.string.no, null)
        .show();
	}

	@Override
	public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		if (mViewPager != null) {
			mViewPager.setCurrentItem(tab.getPosition());
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
		
	}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = null;
        String selection = "";
        List<String> selectionArgs = new ArrayList<String>();

        if (id == LOADER_ITEM) {
            contentUri = ContentDescriptor.ContentUri.PROPERTY;
            contentUri = contentUri.withAppendedPath(contentUri, String.valueOf(mItemId));
            selection += TableColumn._ID + "=?";
            selectionArgs.add(String.valueOf(mItemId));
        }
        else if (id == LOADER_TAXONOMY) {
            contentUri = ContentDescriptor.ContentUri.ITEM_TAXONOMY;
            selectionArgs.add(String.valueOf(mItemId));
        }
        else if (id == LOADER_ATTACHMENT) {
            contentUri = ContentDescriptor.ContentUri.ITEM_ATTACHMENT;

            selection += TableColumn.ITEM_ID + "=?";
            selection += " AND " + TableColumn.ATTACHMENT_TYPE + "=?";

            selectionArgs.add(String.valueOf(mItemId));
            selectionArgs.add(AttachmentType.IMAGE);
        }
        else if (id == LOADER_ATTRIBUTE) {
            contentUri = ContentDescriptor.ContentUri.ITEM_ATTRIBUTE;

            selection += TableColumn.ITEM_ID + "=?";
            selection += " AND " + TableColumn.ITEM_TYPE + "=?";

            selectionArgs.add(String.valueOf(mItemId));
            selectionArgs.add(ContentItemType.PROPERTY);
        }

        if (contentUri != null) {
            String[] whereArgs = new String[selectionArgs.size()];
            selectionArgs.toArray(whereArgs);

            return new CursorLoader(this,
                                    contentUri,
                                    null,
                                    selection,
                                    whereArgs,
                                    null);
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int loaderId = loader.getId();

        if (loaderId == LOADER_ITEM) {
            mItemCursor = data;
            mItemCursor.moveToFirst();

            for (BasePropertySectionFormFragment fragment : mFragments) {
                fragment.populateContentView(mItemCursor, null);
            }
        }
        else if (loaderId == LOADER_TAXONOMY) {
            for (BasePropertySectionFormFragment fragment : mFragments) {
                fragment.updateTaxonomies(data);
            }
        }
        else if (loaderId == LOADER_ATTRIBUTE) {
            for (BasePropertySectionFormFragment fragment : mFragments) {
                fragment.updateAttributes(data);
            }
        }
        else if (loaderId == LOADER_ATTACHMENT) {
            for (BasePropertySectionFormFragment fragment : mFragments) {
                fragment.updateAttachments(data);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        int loaderId = loader.getId();

        if (loaderId == LOADER_ITEM) {
            mItemCursor = null;
        }
    }
}