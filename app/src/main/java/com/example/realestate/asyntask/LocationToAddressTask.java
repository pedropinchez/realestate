package com.example.realestate.asyntask;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.example.realestate.app.RealEstateBrokerApp;
import com.google.android.gms.maps.model.LatLng;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

public class LocationToAddressTask extends AsyncTask<LatLng, Integer, Address> {
    private WeakReference<Callbacks> mCallbacks;

    public interface Callbacks {
        void onPreResolvingLocation();
        void onAddressResolved(Address address);
    }

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = new WeakReference<Callbacks>(callbacks);
    }

    @Override
    protected void onPreExecute() {
        if (mCallbacks != null) {
            Callbacks callback = mCallbacks.get();
            if (callback != null) {
                callback.onPreResolvingLocation();
            }
        }
    }

    @Override
    protected Address doInBackground(LatLng... locations) {
        LatLng loc = locations[0];
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(RealEstateBrokerApp.getContext());

        try {
            addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Address address) {
        if (mCallbacks != null) {
            Callbacks callback = mCallbacks.get();
            if (callback != null) {
                callback.onAddressResolved(address);
            }

            mCallbacks = null;
        }
    }
}
