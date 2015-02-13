package org.michenux.yourappidea.donations;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

public class DonateFragment extends Fragment implements OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View view = inflater.inflate(R.layout.donations_fragment, container, false);
		final Button button = (Button) view.findViewById(R.id.donations_paypal_donate_button);
        button.setOnClickListener(this);
		return view;
	}
    @Override
	public void onClick(View v) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("https").authority("www.paypal.com").path("cgi-bin/webscr");
        uriBuilder.appendQueryParameter("cmd", "_donations");

        uriBuilder.appendQueryParameter("business", "lmichenaud@gmail.com");
        uriBuilder.appendQueryParameter("lc", "US");
        uriBuilder.appendQueryParameter("item_name", "yourappidea");
        uriBuilder.appendQueryParameter("no_note", "1");
        uriBuilder.appendQueryParameter("no_shipping", "1");
        uriBuilder.appendQueryParameter("currency_code", "EUR");
        Uri payPalUri = uriBuilder.build();
    	
       	Intent viewIntent = new Intent(Intent.ACTION_VIEW, payPalUri);
       	startActivity(viewIntent);
	}
}
