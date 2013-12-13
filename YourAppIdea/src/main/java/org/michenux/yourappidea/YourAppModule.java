package org.michenux.yourappidea;

import org.michenux.android.MCXModule;
import org.michenux.yourappidea.aroundme.AroundMeFragment;
import org.michenux.yourappidea.aroundme.PlaceContentProvider;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.friends.FriendContentProvider;
import org.michenux.yourappidea.home.MainFragment;
import org.michenux.yourappidea.home.YourAppMainActivity;
import org.michenux.yourappidea.settings.SettingsFragment;
import org.michenux.yourappidea.tutorial.TutorialListFragment;
import org.michenux.yourappidea.tutorial.contentprovider.TutorialContentProvider;
import org.michenux.yourappidea.tutorial.sync.TutorialSyncAdapter;

import dagger.Module;

@Module(
	    injects = {
            YourApplication.class,

            YourAppMainActivity.class,

    		FriendContentProvider.class,
            TutorialContentProvider.class,
            PlaceContentProvider.class,

            MainFragment.class,
            DonateFragment.class,
            SettingsFragment.class,
            TutorialListFragment.class,
            AroundMeFragment.class,

            TutorialSyncAdapter.class
	    },
	    includes = {
	    	MCXModule.class
	    },
	    overrides=true
	)
public class YourAppModule {

}
