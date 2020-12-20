package com.example.realestate.fragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.realestate.R;
import com.example.realestate.activity.PropertyViewFragmentActivity;
import com.example.realestate.database.TableColumn;
import com.example.realestate.util.CursorUtils;
import com.example.realestate.util.ViewUtils;
import com.squareup.picasso.Picasso;

public class DashboardPropertyItemFragment extends DashboardSectionItemFragment {
    private int mImageWidth;
    private ImageView mImgView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mImageWidth = ViewUtils.getContentAreaWidth(getActivity(), 8);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.list_item_property_dashboard;
    }

    @Override
    protected Class<?> getItemViewActivityClass() {
        return PropertyViewFragmentActivity.class;
    }

    public static DashboardPropertyItemFragment newInstance(int position, Cursor cursor) {
        DashboardPropertyItemFragment item = new DashboardPropertyItemFragment();
        item.setCursor(cursor);
        item.setCursorPosition(position);

        return item;
    }

    @Override
    protected void setupView(View view, Cursor cursor) {
        mImgView = (ImageView)view.findViewById(R.id.imgCoverPic);
        TextView txtTitle = (TextView)view.findViewById(R.id.txtTitle);
        txtTitle.setText(CursorUtils.getRecordStringValue(cursor, TableColumn.NAME));

        mImgView.getLayoutParams().width = mImageWidth;
        mImgView.getLayoutParams().height = mImageWidth * 9/16;

        String coverPath = CursorUtils.getRecordStringValue(cursor, TableColumn.COVER_PIC_PATH);
        if (!TextUtils.isEmpty(coverPath)) {
            /*MediaUtils.setImageThumbnailAsync(mImgView,
                                              coverPath,
                                              mImageWidth,
                                              mImageWidth * 9/16,
                                              ThumbnailType.PROPERTY,
                                              null);*/
            Picasso.get()
                    .load("file://" + coverPath)
                    .resize(mImageWidth, mImageWidth * 9/16)
                    .into(mImgView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //MediaUtils.recycleImageView(mImgView);
    }
}
