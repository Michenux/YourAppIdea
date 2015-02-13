package org.michenux.yourappidea.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.michenux.drodrolib.gms.gplus.GoogleApiClientDelegate;
import org.michenux.drodrolib.security.UserSessionCallback;
import org.michenux.drodrolib.ui.navdrawer.NavigationDrawerFragment;
import org.michenux.yourappidea.facebook.FbLoginDelegate;
import org.michenux.drodrolib.info.AppUsageUtils;
import org.michenux.drodrolib.security.SecurityUtils;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.NavigationController;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import javax.inject.Inject;

public class YourAppMainActivity extends ActionBarActivity implements UserSessionCallback {

    @Inject
    NavigationController navController;

    @Inject
    UserHelper mUserHelper;

    private AdView mAdView ;

    private FbLoginDelegate mFbLoginDelegate;

    private GoogleApiClientDelegate mGoogleApiClientDlg;

    private Toolbar mToolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getApplication()).inject(this);
        setContentView(R.layout.main);

        // toolbar
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // For debug
        if ( BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "HashKey: " + SecurityUtils.logHashKey(this));
        }

        // app usage
        AppUsageUtils.updateLastUsedTimestamp(this);

        // init fragment
        if (savedInstanceState == null) {
            this.navController.goHomeFragment(this);
            this.navController.showWhatsNew(this);
        }

        // ads
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1174B15820BDCDE357023377AAF1D72D")
                .addTestDevice("FB73634EFAFEF29BE7973A97B5543A4D")
                .addTestDevice("3C4438D5DE2E7086B63C92FC5846F662") //LG Nexus 5
                .build();
        mAdView.loadAd(adRequest);

        // social networks
        mFbLoginDelegate = new FbLoginDelegate(mUserHelper, this, savedInstanceState);
        mFbLoginDelegate.setUserSessionCallback(this);

        mGoogleApiClientDlg = new GoogleApiClientDelegate(this, mUserHelper, savedInstanceState);
        mGoogleApiClientDlg.setUserSessionCallback(this);
    }

    @Override
    public void onBackPressed() {

        NavigationDrawerFragment navigationDrawerFragment = findNavDrawerFragment();
        if ( navigationDrawerFragment == null || !navigationDrawerFragment.onBackPressed()) {
            // See bug: http://stackoverflow.com/questions/13418436/android-4-2-back-stack-behaviour-with-nested-fragments/14030872#14030872
            // If the fragment exists and has some back-stack entry
            FragmentManager fm = getSupportFragmentManager();
            Fragment currentFragment = fm.findFragmentById(R.id.content_frame);
            if (currentFragment != null && currentFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                // Get the fragment fragment manager - and pop the backstack
                currentFragment.getChildFragmentManager().popBackStack();
            }
            // Else, nothing in the direct fragment back stack
            else {
                if ( currentFragment != null && !NavigationController.HOME_FRAGMENT_TAG.equals(currentFragment.getTag())) {
                    this.navController.goHomeFragment(this);
                }
                else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFbLoginDelegate.onActivityResult(requestCode, resultCode, data);
        mGoogleApiClientDlg.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClientDlg.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClientDlg.onStop();
        mFbLoginDelegate.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdView.pause();
        mFbLoginDelegate.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdView.resume();
        mFbLoginDelegate.onResume();
    }

    @Override
    public void onDestroy() {
        mAdView.destroy();
        super.onDestroy();
        mFbLoginDelegate.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mFbLoginDelegate.onSaveInstanceState(outState);
        mGoogleApiClientDlg.onSaveInstanceState(outState);
    }

    @Override
    public void onLogin() {
        NavigationDrawerFragment navigationDrawerFragment = findNavDrawerFragment();
        navigationDrawerFragment.refreshMenu();
    }

    public YourAppNavigationFragment findNavDrawerFragment() {
        return (YourAppNavigationFragment) getSupportFragmentManager().findFragmentByTag("navdrawer_fragment");
    }

    @Override
    public void onLogout() {
        YourAppNavigationFragment navigationDrawerFragment = findNavDrawerFragment();
        navigationDrawerFragment.showPrimaryMenu();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        NavigationDrawerFragment navigationDrawerFragment = findNavDrawerFragment();
        if ( navigationDrawerFragment.onKeyDown(keyCode, event)) {
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public FbLoginDelegate getFbLoginDelegate() {
        return mFbLoginDelegate;
    }

    public GoogleApiClientDelegate getGoogleApiClientDlg() {
        return mGoogleApiClientDlg;
    }
}
