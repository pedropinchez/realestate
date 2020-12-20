package com.example.realestate.activity;

public abstract class TaxonomyFormFragmentActivity extends BaseItemFormFragmentActivity {
    protected abstract int getTitleResource();
    protected abstract int getEditModeTitleResource();
    protected abstract Class<?> getFragmentClass();
}
