package org.michenux.android.eula;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Closeable;

/**
 * Displays an EULA ("End User License Agreement") that the user has to accept
 * before using the application. Your application should call
 * {@link Eula#show(android.app.Activity)} in the onCreate() method of the first
 * activity. If the user accepts the EULA, it will never be shown again. If the
 * user refuses, {@link android.app.Activity#finish()} is invoked on your
 * activity.
 */
public class Eula {
	private static final String ASSET_EULA = "EULA";
	private static final String PREFERENCE_EULA_ACCEPTED = "eula.accepted";
	private static final String PREFERENCES_EULA = "eula";

	/**
	 * callback to let the activity know when the user has accepted the EULA.
	 */
	static interface OnEulaAgreedTo {

		/**
		 * Called when the user has accepted the eula and the dialog closes.
		 */
		void onEulaAgreedTo();
	}

	/**
	 * Displays the EULA if necessary. This method should be called from the
	 * onCreate() method of your main Activity.
	 * 
	 * @param activity
	 *            The Activity to finish if the user rejects the EULA.
	 * @return Whether the user has agreed already.
	 */
	public static boolean show(final Activity activity, int title,
			int acceptLabel, int refuseLabel) {
		final SharedPreferences preferences = activity.getSharedPreferences(
				PREFERENCES_EULA, Activity.MODE_PRIVATE);
		if (!preferences.getBoolean(PREFERENCE_EULA_ACCEPTED, false)) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(
					activity);
			builder.setTitle(title);
			builder.setCancelable(true);
			builder.setPositiveButton(acceptLabel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							accept(preferences);
							if (activity instanceof OnEulaAgreedTo) {
								((OnEulaAgreedTo) activity).onEulaAgreedTo();
							}
						}
					});
			builder.setNegativeButton(refuseLabel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							refuse(activity);
						}
					});
			builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					refuse(activity);
				}
			});
			builder.setMessage(readEula(activity));
			builder.create().show();
			return false;
		}
		return true;
	}

	public static void show(final FragmentActivity activity, int title, int closeLabel) {

		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setCancelable(true);
		builder.setNeutralButton(closeLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setMessage(readEula(activity));
		builder.create().show();
	}

	private static void accept(SharedPreferences preferences) {
		preferences.edit().putBoolean(PREFERENCE_EULA_ACCEPTED, true).commit();
	}

	private static void refuse(Activity activity) {
		activity.finish();
	}

	private static CharSequence readEula(Activity activity) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(activity.getAssets()
					.open(ASSET_EULA)));
			String line;
			StringBuilder buffer = new StringBuilder();
			while ((line = in.readLine()) != null)
				buffer.append(line).append('\n');
			return buffer;
		} catch (IOException e) {
			return "";
		} finally {
			closeStream(in);
		}
	}

	/**
	 * Closes the specified stream.
	 * 
	 * @param stream
	 *            The stream to close.
	 */
	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				// Ignore
			}
		}
	}
}