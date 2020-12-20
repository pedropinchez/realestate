package com.example.realestate.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUtils {
	public static void setCursorText(TextView view, Cursor cursor, String column) {
		view.setText(CursorUtils.getRecordStringValue(cursor, column));
	}
	
	public static void showFormElementError(Context context, String message, View element) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		element.requestFocus();
	}
	
	public static void showFormElementError(Context context, int messageResource, View element) {
		String message = context.getResources().getString(messageResource);
		showFormElementError(context, message, element);
	}

    public static boolean showFormElementErrorIfEmpty(Context context, int msg, View view) {
        boolean isEmpty = false;

        if (view instanceof EditText) {
            isEmpty = TextUtils.isEmpty(((EditText)view).getText());
        }
        else if (view instanceof Spinner) {
            isEmpty = ((Spinner)view).getSelectedItemId() <= 0;
        }

        if (isEmpty) {
            showFormElementError(context, msg, view);
        }

        return isEmpty;
    }
	
	public static boolean isTextViewEmpty(TextView view) {
		return TextUtils.isEmpty(view.getText());
	}

    public static EditText findEditText(View view, int id) {
        return (EditText)view.findViewById(id);
    }

    public static Spinner findSpinner(View view, int id) {
        return (Spinner)view.findViewById(id);
    }

    public static TextView findTextView(View view, int id) {
        return (TextView)view.findViewById(id);
    }

    public static int getContentAreaWidth(Activity context, int paddingInDp) {
        int paddingInPx = GeneralUtils.dpToPx(context, paddingInDp);
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels - 2 * paddingInPx;
    }

    public static int getContentAreaWidth(Activity context) {
        return getContentAreaWidth(context, 16);
    }

    public static int getScreenWidth(Activity context) {
        return getContentAreaWidth(context, 0);
    }

    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}
