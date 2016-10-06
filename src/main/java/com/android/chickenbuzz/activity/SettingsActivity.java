package com.android.chickenbuzz.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.android.chickenbuzz.R;
import com.android.chickenbuzz.fragments.SettingsFragment;
import com.android.chickenbuzz.utils.Constant;

public class SettingsActivity extends BaseActivity  {

    private SettingsActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        setTitle(getString(R.string.title_settings));
        if(Constant.IS_HOME_SCREEN_DISPLAYED)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            SettingsFragment firstFragment = new SettingsFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
//            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, firstFragment).commit();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            if(Constant.IS_HOME_SCREEN_DISPLAYED)
                finish();

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(Constant.IS_HOME_SCREEN_DISPLAYED)
            finish();
    }
}
