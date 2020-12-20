package com.example.realestate.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.FragmentActivity;

import com.example.realestate.R;
import com.example.realestate.adapter.BaseCursorMultipleAdapter;
import com.example.realestate.database.TableColumn;
import com.example.realestate.fragment.dialog.BaseCursorSelectMultipleDialogFragment;
import com.example.realestate.fragment.dialog.TaxonomySelectMultipleFragment;
import com.example.realestate.util.CursorUtils;

import java.util.List;


public abstract class BasePropertySectionFormFragment extends BaseFragment {
	public abstract ContentValues getFormValues();
	protected abstract int getContentView();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Fragment can work in non-visible state where no UI is needed
		if (container == null) {
			return null;
		}

		View view = inflater.inflate(getContentView(), container, false);

		initContentView(view);
        populateContentView(null, savedInstanceState);

		return view;
	}

    protected void initContentView(View view) {
		
	}
	
	public void populateContentView(Cursor cursor, Bundle savedInstanceState) {
		
	}

    public void updateTaxonomies(Cursor cursor) {

    }

    public void updateAttributes(Cursor cursor) {

    }

    public void updateAttachments(Cursor cursor) {

    }

	public boolean validateForm(FragmentActivity context) {
		return true;
	}

    protected String packTaxonomyIds(List<BaseCursorMultipleAdapter.ItemData> items) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0, c = items.size(); i < c; i++) {
            builder.append(items.get(i).mItemId);
            if (i < c - 1) {
                builder.append(";");
            }
        }

        return builder.toString();
    }

    protected void selectSingleTaxonomy(final EditText view, final SimpleCursorAdapter adapter,
                                        final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.select);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                Cursor cursor = adapter.getCursor();
                cursor.moveToPosition(position);
                String title = CursorUtils.getRecordStringValue(cursor, TableColumn.TITLE);
                view.setText(title);

                if (listener != null) {
                    listener.onClick(dialogInterface, position);
                }
            }
        });
        builder.show();
    }

    protected void selectMultipleTaxonomies(final EditText view, String vocabulary,
                                            final List<BaseCursorMultipleAdapter.ItemData> itemsData,
                                            BaseCursorSelectMultipleDialogFragment.Callbacks callbacks) {
        TaxonomySelectMultipleFragment dialog = new TaxonomySelectMultipleFragment();
        dialog.setVocabularyType(vocabulary);

        // Set currently selected items
        if (itemsData != null) {
            int[] itemPositions = new int[itemsData.size()];
            for(int i = 0, c = itemsData.size(); i < c; i++) {
                itemPositions[i] = itemsData.get(i).mItemPosition;
            }

            dialog.setSelectedItems(itemPositions);
        }

        dialog.setTitle(R.string.select);
        dialog.setCallbacksListener(callbacks);
        dialog.show(getFragmentManager(), "PropertyFormFragment");
    }

    protected void updateSelectedTaxonomies(final EditText view, final List<BaseCursorMultipleAdapter.ItemData> itemsData) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0, c = itemsData.size(); i < c; i++) {
            builder.append(itemsData.get(i).mItemTitle);
            if (i < c - 1) {
                builder.append(", ");
            }
        }

        view.setText(builder.toString());
    }
}