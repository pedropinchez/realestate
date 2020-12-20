package com.example.realestate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.realestate.R;
import com.example.realestate.activity.PhotoFullscreenSliderActivity;
import com.example.realestate.fragment.PhotoFullscreenSliderFragment;
import com.example.realestate.util.ViewUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoThumbnailSliderViewPagerAdapter extends PagerAdapter {
    private List<String> mPhotos;
    private Context mContext;

    private int mImageWidth = 0;

    public PhotoThumbnailSliderViewPagerAdapter(Context context, List<String> photos) {
        mPhotos = photos;
        mContext = context;
        mImageWidth = ViewUtils.getContentAreaWidth((Activity)context, 0);
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater lif = LayoutInflater.from(mContext);

        ImageView view = (ImageView)lif.inflate(R.layout.widget_full_width_photo_thumbnail,
                                container,
                                false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PhotoFullscreenSliderActivity.class);
                intent.putExtra(PhotoFullscreenSliderFragment.PHOTO_POSITION, position);
                intent.putStringArrayListExtra(PhotoFullscreenSliderFragment.PHOTO_PATHS, (ArrayList <String>)mPhotos);
                mContext.startActivity(intent);
            }
        });

        view.getLayoutParams().width = mImageWidth;
        view.getLayoutParams().height = mImageWidth * 9/16;

        Picasso.get()
               .load("file://" + mPhotos.get(position))
               .resize(mImageWidth, 9 * mImageWidth/16)
               .centerInside()
               .into(view);

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (ImageView)object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView imgView = (ImageView)object;
        //MediaUtils.recycleImageView(imgView);
        container.removeView(imgView);
    }
}
