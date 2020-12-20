package com.example.realestate.activity;

import android.os.Bundle;
import android.view.Window;

import com.example.realestate.R;
import com.example.realestate.fragment.PhotoFullscreenSliderFragment;


public class PhotoFullscreenSliderActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't show action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fullscreen_photo_slider);
        createFragments();
    }

    private void createFragments() {
        Bundle args = getIntent().getExtras();
        PhotoFullscreenSliderFragment fragment = new PhotoFullscreenSliderFragment();
        fragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.frameContent, fragment)
                                   .commit();
    }
}