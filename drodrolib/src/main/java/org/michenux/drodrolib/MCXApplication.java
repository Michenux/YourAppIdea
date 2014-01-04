package org.michenux.drodrolib;

import android.app.Activity;
import android.app.Application;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProvider;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

public class MCXApplication extends Application {

	public static final String LOG_TAG = "MCX";

	private ObjectGraph objectGraph;
	
	public void inject(Activity activity) {
		getObjectGraph().inject(activity);
	}
	
	public void inject(ContentProvider contentProvider) {
		getObjectGraph().inject(contentProvider) ;
	}

    public void inject(Fragment fragment) {
        getObjectGraph().inject(fragment);
    }

    public void inject(AbstractThreadedSyncAdapter fragment) {
        getObjectGraph().inject(fragment);
    }

    public void injectSelf() {
        getObjectGraph().inject(this);
    }

	public synchronized ObjectGraph getObjectGraph() {
		if (objectGraph == null) {
			List<Object> modules = new ArrayList<>();
			buildDaggerModules(modules);
			this.objectGraph = ObjectGraph.create(modules.toArray());
			onObjectGraphCreated(this.objectGraph);
		}
		return objectGraph;
	}
	
	public void buildDaggerModules( List<Object> modules) {
		modules.add(new MCXModule());
	}
	
	public void onObjectGraphCreated( ObjectGraph objectGraph) {
		
	}
}
