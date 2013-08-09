package org.michenux.android.ui.eula;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class EulaDialogFragment extends DialogFragment {

    public static final int ACCEPTREFUSE_MODE = 0;
    public static final int VIEW_MODE = 1;

    private OnEulaAgreement onEulaAgreement;

    public static EulaDialogFragment newInstance(int mode, int title, int acceptLabel, int refuseLabel, int closeLabel, String eula, OnEulaAgreement onEulaAgreement) {
        EulaDialogFragment eulaDialog = new EulaDialogFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putInt("title", title);
        args.putInt("acceptLabel", acceptLabel);
        args.putInt("refuseLabel", refuseLabel);
        args.putInt("closeLabel", closeLabel);
        args.putString("eula", eula);
        eulaDialog.setArguments(args);
        eulaDialog.setOnEulaAgreement(onEulaAgreement);
        return eulaDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int mode = getArguments().getInt("mode");
        int title = getArguments().getInt("title");
        int acceptLabel = getArguments().getInt("acceptLabel");
        int refuseLabel = getArguments().getInt("refuseLabel");
        int closeLabel = getArguments().getInt("closeLabel");
        String eula = getArguments().getString("eula");

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(eula);

        if (mode == ACCEPTREFUSE_MODE) {
            this.setCancelable(false);
            builder.setPositiveButton(acceptLabel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onEulaAgreement.onAccept();
                        }
                    });
            builder.setNegativeButton(refuseLabel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onEulaAgreement.onRefuse();
                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    onEulaAgreement.onRefuse();
                }
            });
        } else {
            this.setCancelable(true);
            builder.setNeutralButton(closeLabel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
        }
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.onEulaAgreement.onNewActivityAttached(activity);
    }

    public OnEulaAgreement getOnEulaAgreement() {
        return onEulaAgreement;
    }

    public void setOnEulaAgreement(OnEulaAgreement onEulaAgreement) {
        this.onEulaAgreement = onEulaAgreement;
    }

    @Override
    public void onDestroyView() // necessary for restoring the dialog
    {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);

        super.onDestroyView();
    }

    public static interface OnEulaAgreement {

        public void onAccept();

        public void onRefuse();

        public void onNewActivityAttached(Activity activity);
    }
}
