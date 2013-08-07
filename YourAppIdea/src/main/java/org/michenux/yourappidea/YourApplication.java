package org.michenux.yourappidea;

import java.util.List;

import org.michenux.android.MCXApplication;
import org.michenux.android.db.sqlite.SQLiteDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.pm.PackageManager.NameNotFoundException;
import dagger.ObjectGraph;

public class YourApplication extends MCXApplication {
	
	/**
	 * 
	 */
	private static final Logger log = LoggerFactory.getLogger(YourApplication.class);	
	
	@Override
	public void buildDaggerModules(List<Object> modules) {
		modules.add(new YourAppModule());
	}
	
	@Override
	public void onObjectGraphCreated(ObjectGraph objectGraph) {
		super.onObjectGraphCreated(objectGraph);
		log.debug("onObjectGraphCreated");
		SQLiteDatabaseFactory sqliteDbFactory = objectGraph.get(SQLiteDatabaseFactory.class);
		try {
			sqliteDbFactory.init(this, true, true);
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
