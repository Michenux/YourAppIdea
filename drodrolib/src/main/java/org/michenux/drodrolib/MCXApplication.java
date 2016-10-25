package org.michenux.drodrolib;

import android.app.Activity;
import android.app.Application;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProvider;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

public class MCXApplication extends MultiDexApplication {
    public static final String LOG_TAG = "MCX";

    private ObjectGraph objectGraph;

    public void inject(Activity activity) {
        getObjectGraph().inject(activity);
    }

    public void inject(ContentProvider contentProvider) {
        getObjectGraph().inject(contentProvider);
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

    public void buildDaggerModules(List<Object> modules) {
        modules.add(new MCXModule());
    }

    public void onObjectGraphCreated(ObjectGraph objectGraph) {
    }

    public static MCXApplication getRealApplication(Context applicationContext) {
        MCXApplication application = null;

        if (applicationContext instanceof MCXApplication) {
            application = (MCXApplication) applicationContext;
        } else {
            Application realApplication = null;
            Field magicField = null;
            try {
                magicField = applicationContext.getClass().getDeclaredField("realApplication");
                magicField.setAccessible(true);
                realApplication = (Application) magicField.get(applicationContext);
            } catch (NoSuchFieldException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } catch (IllegalAccessException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            application = (MCXApplication) realApplication;
        }

        return application;
    }
}
