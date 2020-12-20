package com.example.realestate.asyntask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;


import com.example.realestate.util.MediaUtils;

import java.lang.ref.WeakReference;

public class ThumbnailGenerationTask extends AsyncTask<String, Void, Bitmap> {
    private WeakReference<ImageView> mImageViewRef;

    private int mImgWidth;
    private int mImgHeight;
    private String mSavedFolder;

    // NOTE: If the callback is Activity or Fragment, use WeakReference instead of strong reference
    private WeakReference<AsyncTaskCallback<String, Void, Bitmap>> mTaskCallback;

    public ThumbnailGenerationTask(ImageView imgView, int thumbnailWidth, int thumbnailHeight, String folder) {
        mImgWidth = thumbnailWidth;
        mImgHeight = thumbnailHeight;
        mSavedFolder = folder;

        mImageViewRef = new WeakReference<ImageView>(imgView);
    }

    public void setCallback(AsyncTaskCallback<String, Void, Bitmap> callback) {
        mTaskCallback = new WeakReference<AsyncTaskCallback<String, Void, Bitmap>>(callback);
    }

    @Override
    protected Bitmap doInBackground(String... paths) {
        String imagePath = paths[0];
        String thumbnailPath = MediaUtils.saveThumbnail(imagePath, mSavedFolder, mImgWidth, mImgHeight);

        if (thumbnailPath != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = null;

            try {
                bitmap = BitmapFactory.decodeFile(thumbnailPath, options);
            }
            catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (bitmap != null && mImageViewRef != null) {
            final ImageView imageView = mImageViewRef.get();
            if (imageView != null) {
                final ThumbnailGenerationTask task = getThumbGeneratorTask(imageView);
                if (this == task) {
                    // Recycle the image view first
                    //MediaUtils.recycleImageView(imageView);
                    imageView.setImageBitmap(bitmap);

                    if (mTaskCallback != null) {
                        AsyncTaskCallback<String, Void, Bitmap> cb = mTaskCallback.get();
                        if (cb != null) {
                            cb.onPostExecute(bitmap);
                        }
                    }
                }
            }
        }
    }

    private static ThumbnailGenerationTask getThumbGeneratorTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof PlaceholderBitmapDrawable) {
                final PlaceholderBitmapDrawable holderDrawable = (PlaceholderBitmapDrawable) drawable;
                return holderDrawable.getThumbGeneratorTask();
            }
        }

        return null;
    }

    public static class PlaceholderBitmapDrawable extends ColorDrawable {
        private final WeakReference<ThumbnailGenerationTask> mThumbGeneratorTask;

        public PlaceholderBitmapDrawable(ThumbnailGenerationTask task) {
            super(Color.LTGRAY);
            mThumbGeneratorTask = new WeakReference<ThumbnailGenerationTask>(task);
        }

        public ThumbnailGenerationTask getThumbGeneratorTask() {
            return mThumbGeneratorTask.get();
        }
    }
}
