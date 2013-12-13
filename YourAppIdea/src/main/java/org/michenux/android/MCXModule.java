package org.michenux.android;

import org.michenux.android.db.sqlite.SQLiteDatabaseFactory;
import org.michenux.android.network.volley.BitmapCacheHolder;

import dagger.Module;

@Module(injects = { SQLiteDatabaseFactory.class, BitmapCacheHolder.class })
public class MCXModule {

}
