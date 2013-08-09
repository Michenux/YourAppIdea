package org.michenux.yourappidea;

import org.michenux.android.MCXModule;
import org.michenux.yourappidea.donations.DonateFragment;
import org.michenux.yourappidea.home.YourAppMainActivity;
import org.michenux.yourappidea.home.MainFragment;
import org.michenux.yourappidea.friends.FriendContentProvider;

import dagger.Module;

@Module(
	    injects = {
	    	YourAppMainActivity.class,
    		FriendContentProvider.class,
            MainFragment.class,
            DonateFragment.class
	    },
	    includes = {
	    	MCXModule.class
	    },
	    overrides=true
	)
public class YourAppModule {

}
