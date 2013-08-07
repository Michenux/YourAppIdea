package org.michenux.yourappidea;

import org.michenux.android.MCXModule;
import org.michenux.yourappidea.activity.YourAppMainActivity;
import org.michenux.yourappidea.friends.FriendContentProvider;

import dagger.Module;

@Module(
	    injects = {
	    	YourAppMainActivity.class,
    		FriendContentProvider.class
	    },
	    includes = {
	    	MCXModule.class
	    },
	    overrides=true
	)
public class YourAppModule {

}
