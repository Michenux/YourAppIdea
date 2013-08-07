package org.michenux.yourappidea.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.Toast;

import org.michenux.yourappidea.R;

import org.michenux.android.eula.Eula;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.airport.AirportFragment;
import org.michenux.yourappidea.controller.NavigationController;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.fragment.MainFragment;
import org.michenux.yourappidea.friends.FriendMainFragment;
import org.michenux.yourappidea.navdrawer.AbstractNavDrawerActivity;
import org.michenux.yourappidea.navdrawer.NavDrawerActivityConfiguration;
import org.michenux.yourappidea.navdrawer.NavDrawerAdapter;
import org.michenux.yourappidea.navdrawer.NavDrawerItem;
import org.michenux.yourappidea.navdrawer.NavMenuItem;
import org.michenux.yourappidea.navdrawer.NavMenuSection;

import javax.inject.Inject;

public class YourAppMainActivity extends AbstractNavDrawerActivity {

    private static final String HOME_FRAGMENT_TAG = "home";

    @Inject
    NavigationController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getApplication()).inject(this);

        if (savedInstanceState == null) {
            goHomeFragment();
            Eula.show(this, R.string.eula_title, R.string.eula_accept,
                    R.string.eula_refuse);
        }
    }

    private void goHomeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new MainFragment(), HOME_FRAGMENT_TAG).commit();
        setTitleWithDrawerTitle();
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create(100, "Demos"),
                NavMenuItem.create(101, "List/Detail", "navdrawer_friends",
                        true, this),
                NavMenuItem.create(102, "Airport", "navdrawer_airport", true,
                        this),
                NavMenuSection.create(200, "General"),
                NavMenuItem.create(202, "Rate this app", "navdrawer_rating",
                        false, this),
                NavMenuItem.create(203, "Donate", "navdrawer_donations", true, this),
                NavMenuItem.create(204, "Eula", "navdrawer_eula", false, this),
                NavMenuItem.create(205, "Quit", "navdrawer_quit", false, this) };

        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration
                .setDrawerShadow(R.drawable.drawer_shadow);
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration
                .setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(new NavDrawerAdapter(
                this, R.layout.navdrawer_item, menu));
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
                        .replace(R.id.content_frame, new AirportFragment())
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
                this.navController.showEula(this);
                break;
            case 205:
                this.navController.showExitDialog(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCodes.PREFERENCES_RESULTCODE) {
            Toast.makeText(this, "Back from preferences", Toast.LENGTH_SHORT)
                    .show();
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
            if ( !HOME_FRAGMENT_TAG.equals(currentFragment.getTag())) {
                goHomeFragment();
            }
            else {
                // Let super handle the back press
                super.onBackPressed();
            }
        }
    }
}
