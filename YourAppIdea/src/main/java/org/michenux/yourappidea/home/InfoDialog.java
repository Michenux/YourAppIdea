package org.michenux.yourappidea.home;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;

public class InfoDialog extends DialogFragment {

    private int mTitle ;

    private int mDetails ;

    DialogInterface.OnClickListener positiveClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            InfoDialog.this.dismiss();
        }
    };

    public static InfoDialog newInstance(int title, int details) {
        InfoDialog f = new InfoDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        args.putInt("details", details);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getInt("title");
        mDetails = getArguments().getInt("details");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Html.fromHtml(getString(mDetails)));
        builder.setPositiveButton("Close", positiveClick);
        builder.setTitle(mTitle);
        Dialog dialog = builder.create();
        return dialog;
    }
}
