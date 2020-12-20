package com.example.realestate.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.legacy.app.ActionBarDrawerToggle;

import com.example.realestate.R;
import com.example.realestate.activity.BaseActionbarAwareActivity;
import com.example.realestate.activity.SettingsToolsActivity;
import com.example.realestate.adapter.NavigationDrawerListAdapter;
import com.example.realestate.app.RealEstateBrokerApp;
import com.example.realestate.fragment.AmenityListFragment;
import com.example.realestate.fragment.AppointmentListFragment;
import com.example.realestate.fragment.AreaUnitListFragment;
import com.example.realestate.fragment.BaseFragment;
import com.example.realestate.fragment.BaseItemFormFragment;
import com.example.realestate.fragment.BaseItemListFragment;
import com.example.realestate.fragment.CurrencyUnitListFragment;
import com.example.realestate.fragment.CustomerListFragment;
import com.example.realestate.fragment.DashboardFragment;
import com.example.realestate.fragment.LeadListFragment;
import com.example.realestate.fragment.LocalityListFragment;
import com.example.realestate.fragment.NearbyFacilityListFragment;
import com.example.realestate.fragment.PropertyListFragment;
import com.example.realestate.fragment.PropertyStatusListFragment;
import com.example.realestate.fragment.PropertyTypeListFragment;
import com.example.realestate.model.NavigationDrawerListItem;
import com.example.realestate.model.UserAccountCredential;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActionbarAwareActivity implements BaseItemListFragment.ListFragmentEventsHandler,
        BaseItemFormFragment.FormFragmentEventsHandler {
    private static final String CURRENT_LIST_ITEM_ID = "current_item_id";

    private static final int ITEM_DASHBOARD = 1;
    private static final int ITEM_AREA_UNIT = 2;
    private static final int ITEM_CURRENCY_UNIT = 3;
    private static final int ITEM_FACILITY = 4;
    private static final int ITEM_LOCALITY = 5;

    private static final int ITEM_PROP_TYPE = 6;
    private static final int ITEM_PROP_STATUS = 7;
    private static final int ITEM_CUSTOMER = 8;
    private static final int ITEM_LOGOUT = 9;
    private static final int ITEM_ACCOUNT = 10;
    private static final int ITEM_PROPERTY = 11;
    private static final int ITEM_AMENITY = 12;
    private static final int ITEM_LEAD = 13;
    private static final int ITEM_APPOINTMENT = 14;

    private ExpandableListView mNavDrawerList;
    private NavigationDrawerListAdapter mNavDrawerListAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<NavigationDrawerListItem> mListItems = new ArrayList<NavigationDrawerListItem>();

    private boolean mIsTwoPaneLayout;
    private boolean mIsListFragment;

    private FrameLayout mLeftPane;
    private FrameLayout mRightPane;
    private LinearLayout mRightSlidingPane;

    private BaseFragment mLeftPaneFragment;
    private BaseFragment mRightPaneFragment;

    private ImageView mUserCover;
    private long mCurrentListItemId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLeftPane = (FrameLayout)findViewById(R.id.leftPane);
        mRightPane = (FrameLayout)findViewById(R.id.rightPane);
        mRightSlidingPane = (LinearLayout)findViewById(R.id.rightSlidingPane);

        mIsTwoPaneLayout = (mRightPane != null);
        mUserCover = (ImageView)findViewById(R.id.imgUserCover);

        mNavDrawerList = (ExpandableListView)findViewById(R.id.listview);
        mNavDrawerList.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return handleItemClick(groupPosition, childPosition, id);
            }
        });
        mNavDrawerList.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return handleItemClick(groupPosition, -1, id);
            }
        });

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, 0, 0 ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Initially disable this pane
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mRightSlidingPane);

        initAdapters();

        // Enable action bar drop-down navigation. Note that this must be called
        // after the content view has been set or an exception will be thrown.
        updateActionBarSpinner();

        // Always do this so that the left fragment will be replaced even when configuration changes
        initView(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current item's id
        outState.putLong(CURRENT_LIST_ITEM_ID, mCurrentListItemId);
    }

    private void initView(Bundle savedInstanceState) {
        RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
        UserAccountCredential cred = app.getCurrentUserCredential();
        //MediaUtils.setImageFromFile(mUserCover, cred.coverPicture);

        long itemId = ITEM_DASHBOARD;

        // Retrieve the current item ID saved when configuration changes
        if (savedInstanceState != null) {
            itemId = savedInstanceState.getLong(CURRENT_LIST_ITEM_ID);
        }

        // Show current view
        handleItemClick(-1, -1, itemId);
    }

    private void switchFragment(BaseFragment fragment, boolean left) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragment != null) {
            // Remove current fragment
            if (left) {
                if (mLeftPaneFragment != null) {
                    fragmentManager.beginTransaction()
                            .remove(mLeftPaneFragment)
                            .commit();
                    mLeftPaneFragment = null;
                }

                mLeftPaneFragment = fragment;
                mIsListFragment = (mLeftPaneFragment instanceof BaseItemListFragment);
            }

            fragmentManager.beginTransaction()
                    .replace(left ? R.id.leftPane : R.id.rightPane, fragment)
                    .commit();
        }
    }

    private NavigationDrawerListItem getListItemFromId(List<NavigationDrawerListItem> items, long id) {
        NavigationDrawerListItem foundItem = null;

        for (NavigationDrawerListItem item: items) {
            if (item.mItemId == id) {
                foundItem = item;
                break;
            }
            if (item.isExpandable()) {
                foundItem = getListItemFromId(item.mChildren, id);
            }
        }

        return foundItem;
    }

    private boolean handleItemClick(int groupPos, int childPos, long id) {

        // Do nothing if user selects the same item
        if (id == mCurrentListItemId) {
            mDrawerLayout.closeDrawers();
            return true;
        }

        NavigationDrawerListItem selectedItem = getListItemFromId(mListItems, id);
        boolean eventHandled = false;

        if (selectedItem == null) {
            throw new IllegalArgumentException("Invalid list item");
        }

        // Logout menu
        if (id == ITEM_LOGOUT) {
            doLogout();
            return true;
        }

        // Only mark as current for child items
        if (id != ITEM_ACCOUNT && !selectedItem.isExpandable()) {
            mNavDrawerListAdapter.setCurrentItemId(selectedItem.mItemId);
            mCurrentListItemId = id;
        }

        Class<?> itemClass = selectedItem.mIntentClass;

        // Update new fragment in the content pane if it's a fragment
        if (itemClass != null && BaseFragment.class.isAssignableFrom(itemClass)) {
            try {
                switchFragment((BaseFragment)itemClass.newInstance(), true);

                // Invalidate the options menu when the fragment changes
                supportInvalidateOptionsMenu();

                // Reset the right pane if needed
                hideRightPaneFragment();

                prepareRightSlidingPane();

                // Update actionbar spinner
                updateActionBarSpinner();

                eventHandled = true;
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        // Otherwise, start the new activity with given class
        else {
            if (itemClass != null) {
                Intent intent = new Intent(this, itemClass);
                startActivity(intent);
            }
        }

        if (eventHandled) {
            mDrawerLayout.closeDrawers();
            setTitle(selectedItem.mTitleResId);
        }

        return eventHandled;
    }

    private void prepareRightSlidingPane() {
        // Dashboard is special as it's not a list item fragment
        if (shouldDisableSlidingRightPane()) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mRightSlidingPane);
        } else {
            if (mIsListFragment) {
                // Remove previously set pane content view
                if (mRightSlidingPane.getChildCount() > 0) {
                    mRightSlidingPane.removeAllViews();
                }

                View view = ((BaseItemListFragment)mLeftPaneFragment).getRightSlidingPaneView(this);

                if (view != null) {
                    mRightSlidingPane.addView(view);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mRightSlidingPane);
                }
                else {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mRightSlidingPane);
                }
            }
        }

        // So that we can hide the filter icon
        supportInvalidateOptionsMenu();
    }

    private void doLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.logout_confirm)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RealEstateBrokerApp app = (RealEstateBrokerApp)getApplication();
                        app.logout(MainActivity.this);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    protected int getMenuResource() {
        return R.menu.menu_base_list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);

        // Search widget
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
            searchView.setQueryHint(getString(R.string.search));
        }

        // Only show the delete action on two-pane layout
        menu.findItem(R.id.action_delete).setVisible(false);
        menu.findItem(R.id.action_filter).setVisible(false);

        return ret;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Hide some menu items for non-list fragments
        if (!mIsListFragment) {
            menu.findItem(R.id.action_add).setVisible(false);
        }

        // If we're in view/edit mode
        if (mRightPaneFragment != null) {
            menu.findItem(R.id.action_delete).setVisible(true);
        }

        // If there is no right sliding content, hide filter icon
        if (mDrawerLayout.getDrawerLockMode(mRightSlidingPane) == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.findItem(R.id.action_filter).setVisible(false);
        } else {
            menu.findItem(R.id.action_filter).setVisible(true);
        }

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            // The hamburger icon is clicked, hide the right sliding pane
            mDrawerLayout.closeDrawer(mRightSlidingPane);
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_search:
                return true;

            case R.id.action_add:
                showItemView(0);
                return true;

            case R.id.action_delete:
                deleteItem();
                break;

            case R.id.action_filter:
                toggleRightSlidingPane();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean shouldDisableSlidingRightPane() {
        return mCurrentListItemId == ITEM_DASHBOARD;
    }

    private void toggleRightSlidingPane() {
        // If we have no content for the sliding pane, do nothing
        if (shouldDisableSlidingRightPane() || mRightSlidingPane.getChildCount() <= 0) return;

        if (mDrawerLayout.isDrawerOpen(mRightSlidingPane)) {
            mDrawerLayout.closeDrawer(mRightSlidingPane);
        } else {
            mDrawerLayout.openDrawer(mRightSlidingPane);
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    private void deleteItem() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.delete_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (((BaseItemFormFragment)mRightPaneFragment).deleteItem()) {
                            hideRightPaneFragment();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void initAdapters() {
        NavigationDrawerListItem group = null;

        mListItems.add(new NavigationDrawerListItem(
                R.string.dashboard,
                R.drawable.brokermate_ic_dashboard_red,
                ITEM_DASHBOARD,
                DashboardFragment.class));
        mListItems.add(new NavigationDrawerListItem(
                R.string.properties,
                R.drawable.brokermate_ic_property_red,
                ITEM_PROPERTY,
                PropertyListFragment.class));
        mListItems.add(new NavigationDrawerListItem(
                R.string.customers,
                R.drawable.brokermate_ic_customer_red,
                ITEM_CUSTOMER,
                CustomerListFragment.class));
        mListItems.add(new NavigationDrawerListItem(
                R.string.leads,
                R.drawable.brokermate_ic_lead_red,
                ITEM_LEAD,
                LeadListFragment.class));
        mListItems.add(new NavigationDrawerListItem(
                R.string.appointments,
                R.drawable.brokermate_ic_appointment_red,
                ITEM_APPOINTMENT,
                AppointmentListFragment.class));

        group = new NavigationDrawerListItem(
                R.string.configurations,
                R.drawable.brokermate_ic_configuration_red,
                -1,
                null);
        group.addChild(new NavigationDrawerListItem(
                R.string.area_units,
                0,
                ITEM_AREA_UNIT,
                AreaUnitListFragment.class));
        group.addChild(new NavigationDrawerListItem(
                R.string.currency_units,
                0,
                ITEM_CURRENCY_UNIT,
                CurrencyUnitListFragment.class));
        group.addChild(new NavigationDrawerListItem(
                R.string.facilities,
                0,
                ITEM_FACILITY,
                NearbyFacilityListFragment.class));
        group.addChild(new NavigationDrawerListItem(
                R.string.localities,
                0,
                ITEM_LOCALITY,
                LocalityListFragment.class));
        group.addChild(new NavigationDrawerListItem(
                R.string.purposes,
                0,
                ITEM_PROP_TYPE,
                PropertyTypeListFragment.class));
        group.addChild(new NavigationDrawerListItem(
                R.string.property_statuses,
                0,
                ITEM_PROP_STATUS,
                PropertyStatusListFragment.class));
        group.addChild(new NavigationDrawerListItem(
                R.string.amenities,
                0,
                ITEM_AMENITY,
                AmenityListFragment.class));
        mListItems.add(group);

        // Log out
        mListItems.add(new NavigationDrawerListItem(
                R.string.user_account,
                R.drawable.brokermate_ic_account_red,
                ITEM_ACCOUNT,
                SettingsToolsActivity.class));
        mListItems.add(new NavigationDrawerListItem(
                R.string.logout,
                R.drawable.brokermate_ic_logout_red,
                ITEM_LOGOUT,
                null));

        mNavDrawerListAdapter = new NavigationDrawerListAdapter(this, mListItems);
        mNavDrawerList.setAdapter(mNavDrawerListAdapter);

        mNavDrawerListAdapter.setCurrentItemId(mCurrentListItemId);
    }

    private void showItemView(long itemId) {
        if (!(mLeftPaneFragment instanceof BaseItemListFragment)) {
            throw new IllegalStateException("Content pane should be an instance of BaseContentListFragment");
        }

        BaseItemListFragment listFragment = (BaseItemListFragment)mLeftPaneFragment;
        Bundle args = new Bundle();

        // We're in edit mode
        if (itemId > 0) {
            args.putLong(BaseItemFormFragment.ITEM_ID, itemId);
        }

        if (!mIsTwoPaneLayout) {
            Class<?> itemActivityCls = listFragment.getItemViewActivityClass();

            // If we're in Add mode or if view class is not found
            if (itemId <= 0 || itemActivityCls == null) {
                itemActivityCls = listFragment.getItemFormActivityClass();
            }

            if (itemActivityCls != null) {
                Intent intent = new Intent(this, itemActivityCls);
                intent.putExtras(args);
                startActivity(intent);
            }

            return;
        }

        if (mRightPaneFragment != null) {
            if (itemId > 0) {
                ((BaseItemFormFragment)mRightPaneFragment).loadItem(itemId);
            } else {
                ((BaseItemFormFragment)mRightPaneFragment).resetContentView();
            }

            return;
        }

        try {
            Class<?> formFragmentCls = listFragment.getItemFormFragmentClass();
            BaseItemFormFragment fragment = (BaseItemFormFragment)formFragmentCls.newInstance();

            fragment.setArguments(args);
            switchFragment(fragment, false);
            supportInvalidateOptionsMenu();
            mRightPaneFragment = fragment;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(int position, long id) {
        showItemView(id);
    }

    @Override
    public void onAddButtonClick() {
        showItemView(0);
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        if (mLeftPaneFragment != null && mLeftPaneFragment instanceof BaseItemListFragment) {
            return ((BaseItemListFragment)mLeftPaneFragment).onNavigationItemSelected(position, id);
        }
        return false;
    }

    @Override
    protected SpinnerAdapter getActionBarSpinnerAdapter() {
        int currentItem = (int)mCurrentListItemId;
        int dropdownItemsRes = 0;

        switch (currentItem) {
            case ITEM_APPOINTMENT:
                dropdownItemsRes = R.array.appointments_due_filter;
                break;
        }

        if (dropdownItemsRes > 0) {
            String[] items = getResources().getStringArray(dropdownItemsRes);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    String itemText = getItem(position);

                    if (convertView == null) {
                        convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.spinner_dropdown_actionbar, null);
                    }

                    ((TextView)convertView.findViewById(android.R.id.text1)).setText(itemText);

                    return convertView;
                }
            };

            return adapter;
        }

        return null;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        // Notify fragment about the search text change.
        if (mLeftPaneFragment != null && mLeftPaneFragment instanceof BaseItemListFragment) {
            return ((BaseItemListFragment)mLeftPaneFragment).onSearchQueryChanged(newText);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // We're not using this one and let user do instant search instead
        return false;
    }

    private void hideRightPaneFragment() {
        if (mIsTwoPaneLayout && mRightPaneFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mRightPaneFragment).commit();
            mRightPaneFragment = null;
            supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onFormCanceled() {
        hideRightPaneFragment();
    }

    @Override
    public void onFormSaved() {
        hideRightPaneFragment();
    }
}
