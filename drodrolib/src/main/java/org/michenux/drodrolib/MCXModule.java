package org.michenux.drodrolib;

import org.michenux.drodrolib.db.sqlite.SQLiteDatabaseFactory;
import org.michenux.drodrolib.gms.gplus.GooglePlusLoginFragment;
import org.michenux.drodrolib.network.volley.BitmapCacheHolder;
import org.michenux.drodrolib.security.UserHelper;

import dagger.Module;

@Module(injects = {
        SQLiteDatabaseFactory.class,
        BitmapCacheHolder.class,
        UserHelper.class,

        GooglePlusLoginFragment.class
    })
public class MCXModule {

}
