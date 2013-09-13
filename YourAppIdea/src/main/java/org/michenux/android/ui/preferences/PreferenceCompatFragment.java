package org.michenux.android.ui.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

import org.michenux.yourappidea.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class PreferenceCompatFragment extends ListFragment {

    private static final int FIRST_REQUEST_CODE = 100;

    private static final int MSG_BIND_PREFERENCES = 0;

    private PreferenceManager mPreferenceManager;

    private ListView mListView;

    private int xmlId;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case MSG_BIND_PREFERENCES:
                    bindPreferences();
                    break;
            }
        }
    };

    public static PreferenceCompatFragment newInstance(int preferences) {
        PreferenceCompatFragment prefFragment = new PreferenceCompatFragment();
        Bundle args = new Bundle();
        args.putInt("xml", preferences);
        prefFragment.setArguments(args);
        return prefFragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        xmlId = getArguments().getInt("xml");
        this.mPreferenceManager = createPreferenceManager();
        this.mListView = (ListView) LayoutInflater.from(getActivity()).inflate(R.layout.preference_list_content, null);
        this.mListView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        addPreferencesFromResource(this.xmlId);
        postBindPreferences();
        //((OnPreferenceAttachedListener) getActivity()).onPreferenceAttached(getPreferenceScreen(), xmlId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        postBindPreferences();
        return mListView;
    }

    public Preference findPreference(CharSequence key) {
        return this.mPreferenceManager.findPreference(key);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.dispatchActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("xml", this.xmlId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ViewParent parentView = this.mListView.getParent();
        if (parentView != null) {
            ((ViewGroup) parentView).removeView(this.mListView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dispatchActivityStop();
    }

    @Override
    public void onDestroy() {
        mListView = null;
        this.dispatchActivityDestroy();
        super.onDestroy();
    }

    public PreferenceManager getPreferenceManager() {
        return mPreferenceManager;
    }

    private PreferenceManager createPreferenceManager() {
        try {
            Constructor<PreferenceManager> c = PreferenceManager.class.getDeclaredConstructor(Activity.class, int.class);
            c.setAccessible(true);
            return c.newInstance(this.getActivity(), FIRST_REQUEST_CODE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void addPreferencesFromResource(int preferencesResId) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("inflateFromResource", Context.class, int.class, PreferenceScreen.class);
            m.setAccessible(true);
            PreferenceScreen prefScreen = (PreferenceScreen) m.invoke(mPreferenceManager, getActivity(), preferencesResId, getPreferenceScreen());
            setPreferenceScreen(prefScreen);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private PreferenceScreen getPreferenceScreen() {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("getPreferenceScreen");
            m.setAccessible(true);
            return (PreferenceScreen) m.invoke(mPreferenceManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("setPreferences", PreferenceScreen.class);
            m.setAccessible(true);
            boolean result = (Boolean) m.invoke(mPreferenceManager, preferenceScreen);
            if (result && preferenceScreen != null) {
                postBindPreferences();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void bindPreferences() {
        try {
            final PreferenceScreen preferenceScreen = getPreferenceScreen();
            if (preferenceScreen != null) {
                preferenceScreen.bind(this.mListView);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dispatchActivityDestroy() {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityDestroy");
            m.setAccessible(true);
            m.invoke(mPreferenceManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dispatchActivityStop() {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityStop");
            m.setAccessible(true);
            m.invoke(mPreferenceManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void dispatchActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Method m = PreferenceManager.class.getDeclaredMethod("dispatchActivityResult", int.class, int.class, Intent.class);
            m.setAccessible(true);
            m.invoke(mPreferenceManager, requestCode, resultCode, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void postBindPreferences() {
        if (!mHandler.hasMessages(MSG_BIND_PREFERENCES)) {
            mHandler.obtainMessage(MSG_BIND_PREFERENCES).sendToTarget();
        }
    }

    public interface OnPreferenceAttachedListener{
        public void onPreferenceAttached(PreferenceScreen root, int xmlId);
    }
}
