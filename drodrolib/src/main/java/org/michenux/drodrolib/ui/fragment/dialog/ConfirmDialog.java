package org.michenux.drodrolib.ui.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.michenux.drodrolib.R;

public class ConfirmDialog extends DialogFragment {

	private int message;

	private OnClickListener positiveListener;

	private OnClickListener negativeListener;

	public static ConfirmDialog newInstance(int message,
			OnClickListener positiveListener, OnClickListener negativeListener) {
		ConfirmDialog frag = new ConfirmDialog();
		frag.setMessage(message);
		frag.setPositiveListener(positiveListener);
		frag.setNegativeListener(negativeListener);
		Bundle args = new Bundle();
		frag.setArguments(args);
		return frag;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		return new AlertDialog.Builder(getActivity())
				.setMessage(this.message)
				.setPositiveButton(R.string.yes, this.positiveListener)
				.setNegativeButton(R.string.no, this.negativeListener).create();
	}

    public void setMessage(int message) {
		this.message = message;
	}

	public void setPositiveListener(OnClickListener positiveListener) {
		this.positiveListener = positiveListener;
	}

	public void setNegativeListener(OnClickListener negativeListener) {
		this.negativeListener = negativeListener;
	}

    @Override
    public void onDestroyView() // necessary for restoring the dialog
    {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);

        super.onDestroyView();
    }
}
