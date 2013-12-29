package org.michenux.yourappidea.airport;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.michenux.android.network.volley.GsonRequest;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.home.InfoDialog;

import java.util.ArrayList;
import java.util.Collections;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

//http://www.flightradar24.com/AirportInfoService.php?airport=ORY&type=in
//LFRS
public class AirportListFragment extends Fragment implements AdapterView.OnItemClickListener {

	private Menu optionsMenu;

	private RequestQueue requestQueue;
	
	private boolean requestRunning = false;

	private String currentMode = "in" ;

    private AirportAdapter airportAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
		    Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreate");
        }
		
		//setRetainInstance(true);
		setHasOptionsMenu(true);
		
		this.airportAdapter = new AirportAdapter(this.getActivity(),
				R.id.flight_name, new ArrayList<Flight>());

		this.requestQueue = Volley.newRequestQueue(this.getActivity());
		this.startRequest();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreateView");
        }
        View view = inflater.inflate(R.layout.airport_listfragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.airport_listview);
        listView.setAdapter(this.airportAdapter);
        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreateOptionsMenu");
        }
		this.optionsMenu = menu;
		inflater.inflate(R.menu.airport_menu, menu);
		
		MenuItem modeMenuItem =  menu.findItem(R.id.airport_menuMode);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(modeMenuItem).findViewById(R.id.airport_mode_spinner);
		if ( this.currentMode.equals("in")) {
			spinner.setSelection(0);
		}
		else {
			spinner.setSelection(1);
		}
		
		spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long row) {
				
				AirportListFragment.this.cancelRequests();
				if ( position == 0 ) {
					AirportListFragment.this.currentMode = "in";
				}
				else {
					AirportListFragment.this.currentMode = "out";
				}
				AirportListFragment.this.startRequest();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				
			}
		});
		
		if ( this.requestRunning) {
			this.setRefreshActionButtonState(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.airport_menuRefresh:
			this.startRequest();
			return true;

        case R.id.airport_menu_info:
            FragmentManager fm = getChildFragmentManager();
            InfoDialog infoDialog = InfoDialog.newInstance(R.string.airport_info_title, R.string.airport_info_details);
            infoDialog.show(fm, "airport_info_dialog");
            return true;
        }

		return super.onOptionsItemSelected(item);
	}

	private void startRequest() {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.startRequest");
        }
		if ( !requestRunning ) {
			AirportListFragment.this.requestRunning = true ;
			
			String url = getString(R.string.airport_rest_url, this.currentMode);
			GsonRequest<AirportRestResponse> jsObjRequest = new GsonRequest<AirportRestResponse>(
					Method.GET, url,
					AirportRestResponse.class, null,
					this.createAirportRequestSuccessListener(),
					this.createAirportRequestErrorListener());
			jsObjRequest.setShouldCache(false);
			this.setRefreshActionButtonState(true);
			this.requestQueue.add(jsObjRequest);
		}
		else if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "  request is already running");
		}
	}
	
	private void cancelRequests() {
		this.requestQueue.cancelAll(this);
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.airport_menuRefresh);
			if (refreshItem != null) {
				if (refreshing) {
                    MenuItemCompat.setActionView(refreshItem, R.layout.actionbar_indeterminate_progress);
					//refreshItem
					//		.setActionView(R.layout.actionbar_indeterminate_progress);
				} else {
                    MenuItemCompat.setActionView(refreshItem, null);
					//refreshItem.setActionView(null);
				}
			}
		}
	}

	private Response.Listener<AirportRestResponse> createAirportRequestSuccessListener() {
		return new Response.Listener<AirportRestResponse>() {
			@Override
			public void onResponse(AirportRestResponse response) {
                if (BuildConfig.DEBUG) {
                    Log.i(YourApplication.LOG_TAG, "AirportListFragment.onResponse");
                }
				AirportAdapter adapter = (AirportAdapter) AirportListFragment.this.airportAdapter;
				adapter.clear();
				Collections.sort(response.getFlights(),
						new FlightEtaComparator());
				//Min Level 11: adapter.addAll(response.getFlights());
                for( Flight flight: response.getFlights()) {
                    adapter.add(flight);
                }
				adapter.notifyDataSetChanged();
				AirportListFragment.this.setRefreshActionButtonState(false);
				AirportListFragment.this.requestRunning = false ;
			}
		};
	}

	private Response.ErrorListener createAirportRequestErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
                if (BuildConfig.DEBUG) {
                    Log.i(YourApplication.LOG_TAG, "AirportListFragment.onErrorResponse");
                }
				AirportListFragment.this.setRefreshActionButtonState(false);
				AirportListFragment.this.requestRunning = false ;
				
				Crouton.makeText(
					AirportListFragment.this.getActivity(),
					getString(R.string.error_retrievingdata),
					Style.ALERT).show();
				
			}
		};
	}

    @Override
    public void onDestroy() {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onDestroy");
        }
        this.cancelRequests();
        Crouton.cancelAllCroutons();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
