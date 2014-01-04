package org.michenux.drodrolib;

import org.michenux.drodrolib.db.sqlite.SQLiteDatabaseFactory;
import org.michenux.drodrolib.network.volley.BitmapCacheHolder;

import dagger.Module;

@Module(injects = { SQLiteDatabaseFactory.class, BitmapCacheHolder.class })
public class MCXModule {

}
