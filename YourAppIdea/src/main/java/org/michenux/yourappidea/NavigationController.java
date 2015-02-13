package org.michenux.yourappidea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.transition.TransitionInflater;

import org.michenux.drodrolib.ui.changelog.ChangeLogHelper;
import org.michenux.drodrolib.ui.changelog.EulaChangeLogChainHelper;
import org.michenux.drodrolib.ui.eula.EulaHelper;
import org.michenux.drodrolib.ui.navdrawer.NavigationDrawerFragment;
import org.michenux.yourappidea.home.LoginActivity;
import org.michenux.yourappidea.home.MainFragment;
import org.michenux.yourappidea.home.YourAppMainActivity;
import org.michenux.yourappidea.settings.SettingsFragment;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class NavigationController {

    public static final String HOME_FRAGMENT_TAG = "home";

	@Inject public NavigationController() {
		
	}
	
	public void startAppRating(Context context) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				.parse("market://details?id=" + context.getPackageName())));
	}

    public void goHomeFragment( YourAppMainActivity activity) {
        MainFragment fg = new MainFragment();
        addFragmentTransition(activity, fg);
        NavigationDrawerFragment fragment = activity.findNavDrawerFragment();
        fragment.setTitleWithDrawerTitle();
        fragment.resetSelection();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fg, HOME_FRAGMENT_TAG).commit();
    }

    public void addFragmentTransition(Activity activity, Fragment fg) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fg.setEnterTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.slide_top));
            fg.setExitTransition(TransitionInflater.from(activity).inflateTransition(android.R.transition.slide_right));
        }
    }

    public void confirmEulaAndShowChangeLog(FragmentActivity activity) {
        EulaChangeLogChainHelper chain = new EulaChangeLogChainHelper(R.string.eula_title, R.string.eula_accept,
                R.string.eula_refuse, R.string.changelog_whatsnew_title, R.string.changelog_close, R.xml.changelog, activity);
        chain.show();
    }

	public void confirmEula(FragmentActivity activity) {
        EulaHelper eulaHelper = new EulaHelper(activity);
        eulaHelper.showAcceptRefuse(R.string.eula_title, R.string.eula_accept,
                R.string.eula_refuse);
	}

	public void showEula(FragmentActivity activity) {
        EulaHelper eulaHelper = new EulaHelper(activity);
        eulaHelper.show(R.string.eula_title, R.string.eula_close);
	}

    public void showWhatsNew( FragmentActivity activity ) {
        ChangeLogHelper changeLogHelper = new ChangeLogHelper();
        changeLogHelper.showWhatsNew(R.string.changelog_title, R.string.changelog_close, R.xml.changelog, activity);
    }

    public void showChangeLog( FragmentActivity activity ) {
        ChangeLogHelper changeLogHelper = new ChangeLogHelper();
        changeLogHelper.showFullChangeLog(R.string.changelog_title, R.string.changelog_close, R.xml.changelog, activity);
    }

    public void showSettings(FragmentActivity activity) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new SettingsFragment(), null).commit();
    }

    public void showLogin(FragmentActivity activity) {
        Intent oIntent = new Intent(activity, LoginActivity.class);
        activity.startActivity(oIntent);
        // no animation
        activity.overridePendingTransition(0,0);
    }
}
