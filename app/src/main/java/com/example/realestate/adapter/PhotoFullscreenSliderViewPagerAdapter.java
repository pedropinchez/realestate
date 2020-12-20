package com.example.realestate.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;


import androidx.viewpager.widget.PagerAdapter;

import com.example.realestate.R;
import com.example.realestate.asyntask.AsyncTaskCallback;
import com.example.realestate.util.ViewUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoFullscreenSliderViewPagerAdapter extends PagerAdapter {
    private List<String> mPhotos;
    // Context is usually the containing Fragment or Activity so we don't
    // worry about memory leak here.
    private Context mContext;
    private int mScreenWidth;

    public PhotoFullscreenSliderViewPagerAdapter(Context context, List<String> photos) {
        mPhotos = photos;
        mContext = context;
        mScreenWidth = ViewUtils.getScreenWidth((Activity)context);
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater lif = LayoutInflater.from(mContext);
        View view = lif.inflate(R.layout.widget_photo_fullscreen_slider_item,
                                container,
                                false);

        final ImageView imgView = (ImageView) view.findViewById(R.id.imgPhoto);
        final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progressBar);

        AsyncTaskCallback<String, Void, Bitmap> callback = new AsyncTaskCallback<String, Void, Bitmap>() {
            @Override
            public void onPreExecute() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onProgressUpdate(Void... values) {

            }

            @Override
            public void onPostExecute(Bitmap Bitmap) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };

        Picasso.get()
               .load("file://" + mPhotos.get(position))
               .resize(mScreenWidth, mScreenWidth)
               .centerInside()
               .into(imgView, new  Callback() {
                   @Override
                   public void onSuccess() {
                       if (progressBar != null) {
                           progressBar.setVisibility(View.GONE);
                       }
                   }

                   @Override
                   public void onError(Exception e) {
                       if (progressBar != null) {
                           progressBar.setVisibility(View.GONE);
                       }
                   }


               });

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;

        if (view != null) {
            ImageView imgView = (ImageView) view.findViewById(R.id.imgPhoto);

            // Not sure if this is necessary
            //MediaUtils.recycleImageView(imgView);

            ViewUtils.unbindDrawables((View) object);
            container.removeView(view);
            view = null;
        }
    }
}
