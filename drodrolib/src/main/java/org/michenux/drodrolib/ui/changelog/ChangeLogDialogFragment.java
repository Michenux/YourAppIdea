package org.michenux.drodrolib.ui.changelog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.webkit.WebView;

import org.michenux.drodrolib.info.VersionUtils;

public class ChangeLogDialogFragment extends DialogFragment {

    public static ChangeLogDialogFragment newInstance(int title, int closeLabel, String changeLog) {
        ChangeLogDialogFragment changelogDialog = new ChangeLogDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("closeLabel", closeLabel);
        args.putString("changeLog", changeLog);
        changelogDialog.setArguments(args);
        return changelogDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int resTitle = getArguments().getInt("title");
        int closeLabel = getArguments().getInt("closeLabel");
        String changeLog = getArguments().getString("changeLog");

        String title = getString(resTitle, VersionUtils.getVersionName(this.getActivity()));

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        this.setCancelable(true);

        final WebView webView = new WebView(this.getActivity());
        webView.loadDataWithBaseURL(null, changeLog, "text/html", "utf-8", null);
        builder.setView(webView);
        builder.setNeutralButton(closeLabel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
