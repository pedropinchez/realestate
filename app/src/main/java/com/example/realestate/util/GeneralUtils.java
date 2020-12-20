package com.example.realestate.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import androidx.core.app.ShareCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class GeneralUtils {
	public static String md5(String in) {
	    MessageDigest digest;

	    try {
	        digest = MessageDigest.getInstance("MD5");
	        digest.reset();
	        digest.update(in.getBytes());
	        byte[] a = digest.digest();
	        int len = a.length;
	        StringBuilder sb = new StringBuilder(len << 1);
	        for (int i = 0; i < len; i++) {
	            sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
	            sb.append(Character.forDigit(a[i] & 0x0f, 16));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException e) { 
	    	e.printStackTrace(); 
	    }
	    return null;
	}
	

	public static String formatCurrency(double amount, String currencyUnit) {
		NumberFormat nf = NumberFormat.getCurrencyInstance();

		DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
		decimalFormatSymbols.setCurrencySymbol(currencyUnit);
		((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
		return nf.format(amount);
	}

    public static boolean sendEmail(Activity context, String mailTo) {
        try {
            ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(context);
            builder.setType("message/rfc822");
            builder.addEmailTo(mailTo);
            builder.startChooser();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean sendSMS(Activity context, String toNumber) {
        try {
            Uri smsUri = Uri.parse("tel:" + toNumber);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsUri);
            sendIntent.setType("vnd.android-dir/mms-sms");
            context.startActivity(sendIntent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean makePhoneCall(Activity context, String number) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            context.startActivity(callIntent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatAddress(Address address) {
        String addressLine = address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "";
        String locality = address.getLocality();
        String subLocality = address.getSubLocality();

        if (locality != null) {
            addressLine = addressLine + ", " + locality;
        }
        else if (subLocality != null) {
            addressLine = addressLine + ", " + subLocality;
        }

        return addressLine;
    }

    public static int dpToPx(Context context, int dp) {
        float density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}
