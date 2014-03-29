package org.michenux.yourappidea.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.michenux.drodrolib.gms.gplus.GoogleApiClientDelegate;
import org.michenux.drodrolib.security.NavDrawerUserLoginItem;
import org.michenux.drodrolib.security.UserSessionCallback;
import org.michenux.drodrolib.ui.navdrawer.NavMenuItem;
import org.michenux.yourappidea.facebook.FbLoginDelegate;
import org.michenux.drodrolib.info.AppUsageUtils;
import org.michenux.drodrolib.security.SecurityUtils;
import org.michenux.drodrolib.security.UserHelper;
import org.michenux.drodrolib.ui.navdrawer.AbstractNavDrawerActivity;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerActivityConfiguration;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerAdapter;
import org.michenux.drodrolib.ui.navdrawer.NavDrawerItem;
import org.michenux.drodrolib.ui.navdrawer.NavMenuBuilder;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.NavigationController;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.airport.AirportListFragment;
import org.michenux.yourappidea.aroundme.AroundMeFragment;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.friends.FriendMainFragment;
import org.michenux.yourappidea.map.SimpleMapFragment;
import org.michenux.yourappidea.tutorial.TutorialListFragment;

import javax.inject.Inject;

public class YourAppMainActivity extends AbstractNavDrawerActivity implements UserSessionCallback {

    @Inject
    NavigationController navController;

    @Inject
    UserHelper mUserHelper;

    private AdView mAdView ;

    private FbLoginDelegate mFbLoginDelegate;

    private GoogleApiClientDelegate mGoogleApiClientDlg;

    private NavDrawerItem logoutDrawerItem = NavMenuItem.createMenuItem(270, R.string.navdrawer_logout, R.drawable.navdrawer_logout, false, false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For debug
        if ( BuildConfig.DEBUG) {
            Log.d(YourApplication.LOG_TAG, "HashKey: " + SecurityUtils.logHashKey(this));
        }

        AppUsageUtils.updateLastUsedTimestamp(this);

        if (savedInstanceState == null) {
            this.navController.goHomeFragment(this);
            this.navController.showWhatsNew(this);
        }

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("1174B15820BDCDE357023377AAF1D72D")
                .addTestDevice("FB73634EFAFEF29BE7973A97B5543A4D")
                .addTestDevice("3C4438D5DE2E7086B63C92FC5846F662") //LG Nexus 5
                .build();
        mAdView.loadAd(adRequest);

        mFbLoginDelegate = new FbLoginDelegate(mUserHelper, this, savedInstanceState);
        mFbLoginDelegate.setUserSessionCallback(this);

        mGoogleApiClientDlg = new GoogleApiClientDelegate(this, mUserHelper, savedInstanceState);
        mGoogleApiClientDlg.setUserSessionCallback(this);
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

        NavMenuBuilder navBuilder = new NavMenuBuilder()
                .addSection(100, R.string.navdrawer_demos)
                .addSectionItem(104, R.string.navdrawer_tutorial, R.drawable.navdrawer_tutorial, true, true)
                .addSectionItem(101, R.string.navdrawer_listdetail, R.drawable.navdrawer_friends, true, true)
                .addSectionItem(102, R.string.navdrawer_airport, R.drawable.navdrawer_airport, true, true)
                .addSectionItem(103, R.string.navdrawer_simplemap, R.drawable.navdrawer_map, true, true)
                .addSectionItem(105, R.string.navdrawer_aroundme, R.drawable.navdrawer_aroundme, true, true)
                .addSection(250, R.string.navdrawer_profile)
                .addDrawerItem(NavDrawerUserLoginItem.createMenuItem(260, R.drawable.navdrawer_user, mUserHelper))
                .addSection(200, R.string.navdrawer_general)
                .addSectionItem(201, R.string.navdrawer_settings, R.drawable.navdrawer_settings, true, true)
                .addSectionItem(202, R.string.navdrawer_rating, R.drawable.navdrawer_rating, false, false)
                .addSectionItem(203, R.string.navdrawer_donations, R.drawable.navdrawer_donations, true, true)
                .addSectionItem(204, R.string.navdrawer_changelog, R.drawable.navdrawer_changelog, false, false)
                .addSectionItem(205, R.string.navdrawer_eula, R.drawable.navdrawer_eula, false, false);

        if ( this.mUserHelper.getCurrentUser() != null ) {
            navBuilder.addDrawerItemAtIndex(logoutDrawerItem, 8);
        }

        NavDrawerItem[] menu = navBuilder.build();

        NavDrawerAdapter adapter = new NavDrawerAdapter(this, R.layout.navdrawer_item);
        adapter.setData(menu);

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration.Builder()
                .mainLayout(R.layout.main)
                .drawerLayoutId(R.id.drawer_layout)
                .leftDrawerId(R.id.left_drawer)
                .adapter(adapter)
                .drawerIcon(R.drawable.ic_drawer)
                .build();

        return navDrawerActivityConfiguration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        switch (id) {
            case 101:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FriendMainFragment())
                        .commit();
                break;
            case 102:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AirportListFragment())
                        .commit();
                break;
            case 103:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new SimpleMapFragment())
                        .commit();
                break;
            case 104:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new TutorialListFragment())
                        .commit();
                break;
            case 105:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new AroundMeFragment())
                        .commit();
                break;
            case 201:
                this.navController.showSettings(this);
                break;
            case 202:
                this.navController.startAppRating(this);
                break;
            case 203:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new DonateFragment())
                        .commit();
                break;
            case 204:
                this.navController.showChangeLog(this);
                break;
            case 205:
                this.navController.showEula(this);
                break;
            case 206:
                this.navController.showExitDialog(this);
                break;

            case 260:
                if ( mUserHelper.getCurrentUser() == null ) {
                    this.navController.showLogin(this);
                }
                break;
            case 270:
                if ( mUserHelper.getCurrentUser() != null ) {
                    switch (mUserHelper.getCurrentUser().getProvider()) {
                        case FbLoginDelegate.PROVIDER_NAME:
                            mFbLoginDelegate.logout();
                            break;
                        case GoogleApiClientDelegate.PROVIDER_NAME:
                            this.mGoogleApiClientDlg.signOut();
                            break;
                    }
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {

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
            if ( !NavigationController.HOME_FRAGMENT_TAG.equals(currentFragment.getTag())) {
                this.navController.goHomeFragment(this);
            }
            else {
                super.onBackPressed();
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
        if ( !getNavConf().hasMenuItemWithId(logoutDrawerItem.getId())) {
            getNavConf().addMenuItemAtIndex(logoutDrawerItem, 8);
            getNavConf().getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onLogout() {
        getNavConf().removeMenuItemWithId(logoutDrawerItem.getId());
        getNavConf().getAdapter().notifyDataSetChanged();
    }
}
