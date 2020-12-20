package com.example.realestate.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.realestate.asyntask.AsyncTaskCallback;
import com.example.realestate.asyntask.ThumbnailGenerationTask;
import com.ipaulpro.afilechooser.utils.FileUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaUtils {
	private static final String BASE_FOLDER = "com.ngocketit.realestatebroker";
	
	public static String saveThumbnail(String imagePath, String targetFolder, int width, int height) {
        // Return original image if both sizes are invalid
        if (width <= 0 && height <= 0) {
            return imagePath;
        }

		File storage = Environment.getExternalStorageDirectory();

		String thumbnailFolder = storage.getAbsolutePath() + "/" + BASE_FOLDER + "/" + targetFolder +
                "/" + String.valueOf(width) + "x" + String.valueOf(height);
		String thumbnailPath = thumbnailFolder + "/" + GeneralUtils.md5(imagePath);

		File thumbnailFile = new File(thumbnailPath);
		if (thumbnailFile.exists()) {
			return thumbnailPath;
		}

        File dir = new File (thumbnailFolder);
        if (!dir.exists() && !dir.mkdirs()) {
        	return null;
        }

        // If only either size is valid, calculate the other size automatically
        if (width <= 0 || height <= 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            int fullWidth = options.outWidth;
            int fullHeight = options.outHeight;

            if (width <= 0) width = fullWidth * height/fullHeight;
            if (height <= 0) height = fullHeight * width/fullWidth;
        }

        Bitmap thumbnail = null;
        try {
            thumbnail = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath),
                                                        width,
                                                        height);
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
        }

		String ret= null;
		
		if (thumbnail != null) {
			FileOutputStream outStream = null;

			try {
				outStream = new FileOutputStream(thumbnailPath);
				thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
				ret = thumbnailPath;
                thumbnail.recycle();
                thumbnail = null;
			}
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (outStream != null) {
						outStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ret;
	}

    public static void setImageThumbAsync(Context context, ImageView view, String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        int fullWidth = options.outWidth;
        int fullHeight = options.outHeight;
    }
	
	public static boolean setImageFromFile(ImageView view, String path) {
		Bitmap thumbnail = BitmapFactory.decodeFile(path);
		if (thumbnail != null) {
			view.setImageBitmap(thumbnail);
            return true;
		}
		
		return false;
	}

    public static void setImageThumbnailAsync(ImageView imageView, String imgPath,
                                              int width, int height, String folder,
                                              AsyncTaskCallback<String, Void, Bitmap> callback) {
        ThumbnailGenerationTask task = new ThumbnailGenerationTask(imageView, width, height, folder);
        task.setCallback(callback);

        ThumbnailGenerationTask.PlaceholderBitmapDrawable drawable = new ThumbnailGenerationTask.PlaceholderBitmapDrawable(task);
        imageView.setImageDrawable(drawable);
        task.execute(imgPath);
    }

    public static void selectFiles(Fragment context, String fileMime, int resTitle, int requestCode) {
        Intent getContentIntent = FileUtils.createGetContentIntent();
        getContentIntent.setType(fileMime);

        Intent intent = Intent.createChooser(getContentIntent, context.getResources().getString(resTitle));
        context.startActivityForResult(intent, requestCode);
    }

    public static boolean hasCamera(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private static File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    public static String takeCameraPhoto(Fragment fragment, int requestCode) {
        Activity context = fragment.getActivity();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                           Uri.fromFile(photoFile));
                fragment.startActivityForResult(takePictureIntent, requestCode);
                return photoFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void recycleImageView1(ImageView imgView) {
        if (imgView != null) {
            Drawable img = imgView.getDrawable();
            if (img != null && img instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) img).getBitmap();
                bitmap.recycle();
                bitmap = null;
            }
        }
    }
}
