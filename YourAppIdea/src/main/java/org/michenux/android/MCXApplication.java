package org.michenux.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;
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

	public synchronized ObjectGraph getObjectGraph() {
		if (objectGraph == null) {
			List<Object> modules = new ArrayList<Object>();
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
