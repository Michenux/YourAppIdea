package org.michenux.yourappidea.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.michenux.android.eula.Eula;
import org.michenux.android.ui.fragment.dialog.ConfirmDialog;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.activity.MyPreferences;
import org.michenux.yourappidea.activity.RequestCodes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

@Singleton
public class NavigationController {

	@Inject public NavigationController() {
		
	}
	
	public void startAppRating(Context context) {
		context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
				.parse("market://details?id=" + context.getPackageName())));
	}

	public void confirmEula(FragmentActivity activity) {
		Eula.show(activity, R.string.eula_title, R.string.eula_accept,
				R.string.eula_refuse);
	}

	public void showEula(FragmentActivity activity) {
		Eula.show(activity, R.string.eula_title, R.string.eula_close);
	}

	public void showExitDialog(final FragmentActivity activity) {
		ConfirmDialog newFragment = ConfirmDialog.newInstance(
				R.string.confirm_quit, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						activity.finish();
					}
				}, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		newFragment.show(activity.getSupportFragmentManager(), "dialog");
	}

	public void showSettings(FragmentActivity activity) {
		activity.startActivityForResult(new Intent(activity,
				MyPreferences.class), RequestCodes.PREFERENCES_RESULTCODE);
	}
}
