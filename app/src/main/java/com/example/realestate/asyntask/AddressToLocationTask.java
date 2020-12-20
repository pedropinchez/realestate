package com.example.realestate.asyntask;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.example.realestate.app.RealEstateBrokerApp;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class AddressToLocationTask extends AsyncTask<String, Integer, LatLng> {
    // We need weak reference here since the callback can be an activity
    // which may finish while the task is still running.
    private WeakReference<Callbacks> mCallbacks;

    public interface Callbacks {
        void onPreResolvingAddress();
        void onLatLngResolved(LatLng latLng);
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = new WeakReference<Callbacks>(callbacks);
    }

    @Override
    protected void onPreExecute() {
        if (mCallbacks != null) {
            Callbacks callback = mCallbacks.get();
            if (callback != null) {
                callback.onPreResolvingAddress();
            }
        }
    }

    @Override
    protected LatLng doInBackground(String... places) {
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(RealEstateBrokerApp.getContext());

        try {
            addresses = geocoder.getFromLocationName(places[0], 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            return new LatLng(address.getLatitude(), address.getLongitude());
        }

        return null;
    }

    @Override
    protected void onPostExecute(LatLng latLng) {
        if (mCallbacks != null) {
            Callbacks callback = mCallbacks.get();
            if (callback != null) {
                callback.onLatLngResolved(latLng);
            }
        }
    }
}
