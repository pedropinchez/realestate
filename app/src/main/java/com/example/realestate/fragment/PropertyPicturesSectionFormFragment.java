package com.example.realestate.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.realestate.R;
import com.example.realestate.activity.PhotoFullscreenSliderActivity;
import com.example.realestate.adapter.PropertyPictureGridAdapter;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.MediaUtils;
import com.ipaulpro.afilechooser.utils.FileUtils;


import java.util.ArrayList;

public class PropertyPicturesSectionFormFragment extends BasePropertySectionFormFragment implements View.OnClickListener {
    private static final int FILE_CHOOSER = 0x1;
    private static int sPictureThumbSize = 0;

    private PropertyPictureGridAdapter mPicturesAdapter;
    private View mBtnAdd;
    private GridView mGridPictures;
    private ArrayList<String> mPicturePaths;

	@Override
	protected int getContentView() {
		return R.layout.fragment_property_form_pictures;
	}

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mBtnAdd = view.findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);

        mGridPictures = (GridView)view.findViewById(R.id.gridPictures);
        mPicturesAdapter = new PropertyPictureGridAdapter(getActivity());
        mGridPictures.setAdapter(mPicturesAdapter);
        mGridPictures.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PhotoFullscreenSliderActivity.class);
                intent.putExtra(PhotoFullscreenSliderFragment.PHOTO_POSITION, i);
                intent.putStringArrayListExtra(PhotoFullscreenSliderFragment.PHOTO_PATHS, (ArrayList <String>)mPicturePaths);
                startActivity(intent);
            }
        });

        mPicturePaths = new ArrayList<String>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("picture_paths", mPicturePaths);
    }

    @Override
	public void populateContentView(Cursor cursor, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ArrayList<String> paths = savedInstanceState.getStringArrayList("picture_paths");
            if (paths != null) {
                for(String path : paths) {
                    addGridPicture(path);
                }
            }
        }
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        sPictureThumbSize = 0;
    }

    @Override
    public void updateAttachments(Cursor cursor) {
        super.updateAttachments(cursor);

        String path = null;

        while (cursor.moveToNext()) {
            path = CursorUtils.getRecordStringValue(cursor, TableColumn.PATH);
            mPicturePaths.add(path);
            addGridPicture(path);
        }
    }

    @Override
	public ContentValues getFormValues() {
        ContentValues values = new ContentValues();
        if (mPicturePaths != null) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0, c = mPicturePaths.size(); i < c; i++) {
                builder.append(mPicturePaths.get(i));
                if (i < c - 1) {
                    builder.append(";");
                }
            }

            if (builder.length() > 0) {
                values.put(TableColumn.ExtendedColumn.PICTURES, builder.toString());
            }
        }
        return values;
	}

    @Override
    public void onClick(View view) {
        if (view == mBtnAdd) {
            Intent getContentIntent = FileUtils.createGetContentIntent();
            getContentIntent.setType(FileUtils.MIME_TYPE_IMAGE);

            Intent intent = Intent.createChooser(getContentIntent, getResources().getString(R.string.select_file));
            startActivityForResult(intent, FILE_CHOOSER);
        }
    }

    private void addGridPicture(String path) {
        // Alternatively, use FileUtils.getFile(Context, Uri)
        if (path != null && FileUtils.isLocal(path)) {
            mPicturePaths.add(path);

            int size = sPictureThumbSize > 0 ? sPictureThumbSize : mGridPictures.getColumnWidth();
            if (sPictureThumbSize <= 0) {
                sPictureThumbSize = size;
            }

            if (size <= 0) size = 128;

            String thumbnailPath = MediaUtils.saveThumbnail(path, "property_pic_thumb", size, size);

            if (thumbnailPath != null) {
                mPicturesAdapter.addPicture(thumbnailPath);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case FILE_CHOOSER:
                    final Uri uri = data.getData();
                    addGridPicture(FileUtils.getPath(getActivity(), uri));
                    return;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
