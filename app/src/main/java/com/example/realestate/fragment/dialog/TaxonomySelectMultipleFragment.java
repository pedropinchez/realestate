package com.example.realestate.fragment.dialog;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;


import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.realestate.adapter.BaseCursorMultipleAdapter;
import com.example.realestate.adapter.TaxonomyListMultipleAdapter;
import com.example.realestate.common.VocabularyType;
import com.example.realestate.database.TableColumn;
import com.example.realestate.provider.ContentDescriptor;

import java.util.ArrayList;
import java.util.List;

public class TaxonomySelectMultipleFragment extends BaseCursorSelectMultipleDialogFragment {
    private String mVocabularyType = VocabularyType.PROPERTY_STATUS;

    @Override
    protected BaseCursorMultipleAdapter createListAdapter(Context context) {
        return new TaxonomyListMultipleAdapter(context);
    }

    public void setVocabularyType(String type) {
        mVocabularyType = type;
        // TODO: need to restart the loader here or it will use the same loader for different
        // taxonomies
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == mLoaderId) {
            String selection = "C." + TableColumn.TAXONOMY + " = ? ";
            List<String> selectionArgs = new ArrayList<String>();
            selectionArgs.add(mVocabularyType);

            String[] selectionArgsStr = selectionArgs.toArray(new String[selectionArgs.size()]);

            CursorLoader cursorLoader = new CursorLoader(getActivity(),
                    ContentDescriptor.ContentUri.ExtendedUri.VOCABULARY_GROUP_BY_PARENT,
                    null, selection, selectionArgsStr, null);

            return cursorLoader;
        }

        return null;
    }
}
