package org.michenux.drodrolib;

import org.michenux.drodrolib.db.sqlite.SQLiteDatabaseFactory;
import org.michenux.drodrolib.gms.auth.GoogleAuthFragment;
import org.michenux.drodrolib.security.UserHelper;

import dagger.Module;

@Module(injects = {
        SQLiteDatabaseFactory.class,
        UserHelper.class,
        GoogleAuthFragment.class
    })
public class MCXModule {

}
