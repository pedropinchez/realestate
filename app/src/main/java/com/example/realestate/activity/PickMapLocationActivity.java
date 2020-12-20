package com.example.realestate.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.realestate.R;
import com.example.realestate.asyntask.AddressToLocationTask;
import com.example.realestate.asyntask.LocationToAddressTask;
import com.example.realestate.util.GeneralUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class PickMapLocationActivity extends LoggedInRequiredActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener, LocationToAddressTask.Callbacks, AddressToLocationTask.Callbacks {
    public static final String MAP_ADDRESS = "map_address";

	private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private Marker mLocationMarker;
    private String mPickAddress;
    private ProgressBar mProgressBar;

    private LocationToAddressTask mAddressTask;
    private AddressToLocationTask mLocationTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_map_location);

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        mMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mMapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(MAP_ADDRESS)) {
            mPickAddress = bundle.getString(MAP_ADDRESS);
        }

        if (mPickAddress != null) {
            mLocationTask = new AddressToLocationTask();
            mLocationTask.setCallbacks(this);
        }
    }

	@Override
	protected int getTitleResource() {
		return R.string.pick_location;
	}

    @Override
    protected int getMenuResource() {
        return R.menu.menu_location_picker;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This activity should always start for result
                finish();
                return true;

            case R.id.action_done:
                Intent result = new Intent();
                result.putExtra(MAP_ADDRESS, mPickAddress);
                setResult(RESULT_OK, result);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);

        if (mPickAddress == null) {
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    // Pick current address if it's not passed as intent argument
                    if (mPickAddress == null) {
                        addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                }
            });
        } else {
            mLocationTask.execute(mPickAddress);
        }
    }

    private void addMarker(LatLng loc) {
        MarkerOptions options = new MarkerOptions().position(loc);

        mLocationMarker = mMap.addMarker(options);

        CameraUpdate currentLoc = CameraUpdateFactory.newLatLngZoom(loc, 15);
        mMap.animateCamera(currentLoc);

        // Cancel current task
        if (mAddressTask != null && mAddressTask.getStatus() == AsyncTask.Status.RUNNING) {
            mAddressTask.cancel(true);
        }

        // Start a new task since one task can only be executed once
        mAddressTask = new LocationToAddressTask();
        mAddressTask.setCallbacks(this);
        mAddressTask.execute(loc);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mLocationMarker != null) {
            mLocationMarker.remove();
            mMap.clear();
        }

        addMarker(latLng);
    }

    private void setMarkerTitle(String title) {
        mLocationMarker.setTitle(title);
        mLocationMarker.hideInfoWindow();
        mLocationMarker.showInfoWindow();
    }

    @Override
    public void onPreResolvingLocation() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAddressResolved(Address address) {
        mProgressBar.setVisibility(View.GONE);

        if (address != null) {
            mPickAddress = GeneralUtils.formatAddress(address);
            setMarkerTitle(mPickAddress);
        }
    }

    @Override
    public void onPreResolvingAddress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLatLngResolved(LatLng latLng) {
        mProgressBar.setVisibility(View.GONE);

        if (latLng != null) {
            addMarker(latLng);
            setMarkerTitle(mPickAddress);
        }
    }
}
