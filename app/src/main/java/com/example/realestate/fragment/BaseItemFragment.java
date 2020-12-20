package com.example.realestate.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import com.example.realestate.activity.ActionbarEventAwareInterface;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;
import com.example.realestate.util.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseItemFragment extends BaseFragment implements ActionbarEventAwareInterface,
        LoaderManager.LoaderCallbacks<Cursor> {
    private static int sBaseItemLoaderId = 1000;
	public static final String ITEM_ID		= "item_id";
	public static final String PICK_RESULT	= "pick_result";

    protected int mItemLoaderId = sBaseItemLoaderId++;
    protected int mTaxonomyLoaderId = sBaseItemLoaderId++;
    protected int mAttachmentLoaderid = sBaseItemLoaderId++;
    protected int mAttributeLoaderId = sBaseItemLoaderId++;

	protected long mItemId = 0;
    private Bundle mSavedInstanceState;
    private String[] mVocabularyTypes;
    private String[] mAttachmentTypes;
    private String[] mItemTypes;

	protected abstract int getContentView();
	protected abstract Uri getItemContentUri();
	protected abstract void populateContentView(Cursor cursor, Bundle savedInstanceState);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getArguments();

		if (bundle != null && bundle.containsKey(ITEM_ID)) {
			mItemId = bundle.getLong(ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Fragment can work in non-visible state where no UI is needed
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(getContentView(), container, false);
		initContentView(view);

        if (savedInstanceState != null) {
            mSavedInstanceState = savedInstanceState;
        }

		return view;
	}

    protected void initAdapter() {
        getActivity().getSupportLoaderManager().initLoader(mItemLoaderId, null, this);
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // Start loading the item using the loader
        if (mItemId > 0) {
            initAdapter();
        }
    }
	
	protected long getItemId() {
		return mItemId;
	}

    public void reloadItem() {
        FragmentActivity activity = getActivity();

        // Too early
        if (activity == null) {
            return;
        }

        activity.getSupportLoaderManager().restartLoader(mItemLoaderId, null, this);
    }

    public void loadItem(long itemId) {
        if (itemId != mItemId) {
            mItemId = itemId;
            reloadItem();
        }
    }

	public void resetContentView() {
		mItemId = -1;
	}
	
	protected void initContentView(View view) {
		
	}
	
	public boolean deleteItem() {
		return DatabaseUtils.deleteItem(getActivity(), getItemContentUri(), mItemId);
	}
	
	protected Cursor loadItemFromDatabase(long itemId) {
		// If this is called before activity is attached
		if (getActivity() == null) return null;
		return DatabaseUtils.loadItemFromDatabase(getActivity(), getItemContentUri(), itemId);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		return false;
	}
	
	@Override
	public boolean onSearchQueryChanged(String query) {
		return false;
	}

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri = null;
        String selection = "";
        List<String> selectionArgs = new ArrayList<String>();

        if (id == mItemLoaderId) {
            contentUri = getItemContentUri();
            contentUri = contentUri.withAppendedPath(contentUri, String.valueOf(mItemId));
            selection += TableColumn._ID + "=?";
            selectionArgs.add(String.valueOf(mItemId));
        }
        else if (id == mTaxonomyLoaderId) {
            contentUri = ContentDescriptor.ContentUri.ITEM_TAXONOMY;
            selectionArgs.add(String.valueOf(mItemId));

            if (mVocabularyTypes != null && mVocabularyTypes.length > 0) {
                selection += " AND (";

                for (int i = 0, c = mVocabularyTypes.length; i < c; i++) {
                    selection += TableColumn.VOCABULARY + "=? OR ";
                    if (i == c - 1) {
                        selection += " 1=0)";
                    }
                    selectionArgs.add(mVocabularyTypes[i]);
                }
            }
        }
        else if (id == mAttachmentLoaderid) {
            contentUri = ContentDescriptor.ContentUri.ITEM_ATTACHMENT;

            selection += TableColumn.ITEM_ID + "=?";
            selectionArgs.add(String.valueOf(mItemId));

            if (mAttachmentTypes != null && mAttachmentTypes.length > 0) {
                selection += " AND (";

                for (int i = 0, c = mAttachmentTypes.length; i < c; i++) {
                    selection += TableColumn.ATTACHMENT_TYPE + "=? OR ";
                    if (i == c - 1) {
                        selection += " 1=0)";
                    }
                    selectionArgs.add(mAttachmentTypes[i]);
                }
            }
        }
        else if (id == mAttributeLoaderId) {
            contentUri = ContentDescriptor.ContentUri.ITEM_ATTRIBUTE;

            selection += TableColumn.ITEM_ID + "=?";
            selectionArgs.add(String.valueOf(mItemId));

            if (mItemTypes != null && mItemTypes.length > 0) {
                selection += " AND (";

                for (int i = 0, c = mItemTypes.length; i < c; i++) {
                    selection += TableColumn.ITEM_TYPE + "=? OR ";
                    if (i == c - 1) {
                        selection += " 1=0)";
                    }
                    selectionArgs.add(mItemTypes[i]);
                }
            }
        }

        if (contentUri != null) {
            String[] whereArgs = new String[selectionArgs.size()];
            selectionArgs.toArray(whereArgs);

            return new CursorLoader(getActivity(),
                                    contentUri,
                                    null,
                                    selection,
                                    whereArgs,
                                    null);
        }

        return null;
    }

    protected void onAttributesLoaded(Cursor cursor) {
        cursor.moveToPosition(-1);
    }

    protected void onTaxonomiesLoaded(Cursor cursor) {
        cursor.moveToPosition(-1);
    }

    protected void onAttachmentsLoaded(Cursor cursor) {
        cursor.moveToPosition(-1);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();
        data.moveToFirst();

        if (id == mItemLoaderId) {
            populateContentView(data, mSavedInstanceState);
        }
        else if (id == mAttributeLoaderId) {
            onAttributesLoaded(data);
        }
        else if (id == mTaxonomyLoaderId) {
            onTaxonomiesLoaded(data);
        }
        else if (id == mAttachmentLoaderid) {
            onAttachmentsLoaded(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == mItemLoaderId) {
            // TODO: reset the view here
        }
    }

    protected void loadTaxonomies(String[] vocabularies) {
        FragmentActivity activity = getActivity();

        if (activity != null && mItemId > 0) {
            mVocabularyTypes = vocabularies;
            activity.getSupportLoaderManager().initLoader(mTaxonomyLoaderId, null, this);
        }
    }

    protected void loadAttachments(String[] attachmentTypes) {
        FragmentActivity activity = getActivity();

        if (activity != null && mItemId > 0) {
            mAttachmentTypes = attachmentTypes;
            activity.getSupportLoaderManager().initLoader(mAttachmentLoaderid, null, this);
        }
    }

    protected void loadAttributes(String[] itemTypes) {
        FragmentActivity activity = getActivity();

        if (activity != null && mItemId > 0) {
            mItemTypes = itemTypes;
            activity.getSupportLoaderManager().initLoader(mAttributeLoaderId, null, this);
        }
    }
}
