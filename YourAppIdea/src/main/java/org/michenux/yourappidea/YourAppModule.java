package org.michenux.yourappidea;

import org.michenux.drodrolib.MCXModule;
import org.michenux.yourappidea.aroundme.AroundMeFragment;
import org.michenux.yourappidea.aroundme.CityContentProvider;
import org.michenux.yourappidea.aroundme.PlaceContentProvider;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.facebook.FbLoginFragment;
import org.michenux.yourappidea.friends.FriendContentProvider;
import org.michenux.yourappidea.home.LoginActivity;
import org.michenux.yourappidea.home.MainFragment;
import org.michenux.yourappidea.home.YourAppMainActivity;
import org.michenux.yourappidea.home.YourAppNavigationFragment;
import org.michenux.yourappidea.settings.SettingsFragment;
import org.michenux.yourappidea.tutorial.TutorialListFragment;
import org.michenux.yourappidea.tutorial.sync.TutorialContentProvider;
import org.michenux.yourappidea.tutorial.sync.TutorialSyncAdapter;

import dagger.Module;

@Module(
	    injects = {
            YourApplication.class,

            YourAppMainActivity.class,
            LoginActivity.class,

    		FriendContentProvider.class,
            TutorialContentProvider.class,
            PlaceContentProvider.class,
            CityContentProvider.class,
            YourAppNavigationFragment.class,

            MainFragment.class,
            DonateFragment.class,
            SettingsFragment.class,
            TutorialListFragment.class,
            AroundMeFragment.class,
            FbLoginFragment.class,

            TutorialSyncAdapter.class
	    },
	    includes = {
	    	MCXModule.class
	    },
	    overrides=true
	)
public class YourAppModule {

}
