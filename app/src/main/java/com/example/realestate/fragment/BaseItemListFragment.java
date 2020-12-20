package com.example.realestate.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.realestate.R;
import com.example.realestate.activity.ActionbarEventAwareInterface;
import com.example.realestate.adapter.BaseCursorAdapter;


public abstract class BaseItemListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        OnItemClickListener, ActionbarEventAwareInterface {
	private static int sLoaderIDCounter = 0;
	protected int mLoaderID;

	private BaseCursorAdapter mAdapter;
	private ListView mListView;
	private ListFragmentEventsHandler mCallbacks;
	protected String mSearchQuery;
	
	public abstract Class<?> getItemFormActivityClass();
	public abstract Class<?> getItemFormFragmentClass();
    protected abstract BaseCursorAdapter createListViewAdapter();

	public interface ListFragmentEventsHandler {
		public void onListItemClick(int position, long id);
        public void onAddButtonClick();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(getContentView(), container, false);

		mListView = (ListView)view.findViewById(R.id.listview);

		if (mListView == null) {
			throw new IllegalArgumentException("List view not found");
		}
		mListView.setEmptyView(view.findViewById(R.id.list_empty));
		mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallbacks != null) {
                    mCallbacks.onAddButtonClick();
                }
            }
        });
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if ((activity instanceof ListFragmentEventsHandler)) {
            mCallbacks = (ListFragmentEventsHandler)activity;
		}
	}

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null; // Release the reference
    }

    @Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initAdapter();
	}

    protected void setEmptyListText(int textRes) {
        TextView view = (TextView)getView().findViewById(R.id.list_empty_view_text);
        if (view != null) {
            view.setText(textRes);
        }
    }
	
	public Class<?> getItemViewActivityClass() {
		return null;
	}

	public Class<?> getItemViewFragmentClass() {
		return null;
	}
	
	protected int getContentView() {
		return R.layout.activity_base_list;
	}

    public View getRightSlidingPaneView(Activity context) {
        return null;
    }

	public BaseCursorAdapter getListViewAdapter() {
		return mAdapter;
	}
	
	protected void initAdapter() {
		mLoaderID = sLoaderIDCounter++;
		
		getActivity().getSupportLoaderManager().initLoader(mLoaderID, null, this);
		
		mAdapter = createListViewAdapter();

		if (mListView != null) {
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(this);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO: Create CursorLoader with search query here
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (mAdapter != null) {
			mAdapter.swapCursor(data);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (mAdapter != null) {
			mAdapter.swapCursor(null);
		}
	}
	
	protected String getSearchQuery() {
		return mSearchQuery;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCallbacks != null) {
            mCallbacks.onListItemClick(position, id);
        }
	}
	
	@Override
	public boolean onSearchQueryChanged(String query) {
		mSearchQuery = !TextUtils.isEmpty(query) ? query : null;

		// Reset loader if search text is changed. NOTE: only do this if the adapter
		// has been created. Otherwise, we'll get NullPointerException error.
		if (mAdapter != null) {
			getActivity().getSupportLoaderManager().restartLoader(mLoaderID, null, this);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}
	
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		return false;
	}
}
