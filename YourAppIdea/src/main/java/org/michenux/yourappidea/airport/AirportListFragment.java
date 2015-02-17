package org.michenux.yourappidea.airport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.lucasr.twowayview.widget.TwoWayView;
import org.michenux.drodrolib.network.volley.GsonRequest;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import java.util.ArrayList;
import java.util.Collections;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

//http://www.flightradar24.com/AirportInfoService.php?airport=ORY&type=in
//LFRS
public class AirportListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	private Menu mOptionsMenu;

	private RequestQueue mRequestQueue;
	
	private boolean mRequestRunning = false;

	private String mCurrentMode = "in" ;

    private AirportRecyclerAdapter mAirportAdapter;

    private SwipeRefreshLayout mSwipeRefreshWidget;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
		    Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreate");
        }
		
		//setRetainInstance(true);
		setHasOptionsMenu(true);
		
		this.mAirportAdapter = new AirportRecyclerAdapter(new ArrayList<Flight>(), this.getActivity(), this.mCurrentMode);

		this.mRequestQueue = Volley.newRequestQueue(this.getActivity());
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreateView");
        }
        View view = inflater.inflate(R.layout.airport_listfragment, container, false);
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.airport_swiperefreshlayout);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        TwoWayView recyclerView = (TwoWayView) view.findViewById(R.id.airport_recyclerview);
        recyclerView.setAdapter(this.mAirportAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.startRequest();
    }

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreateOptionsMenu");
        }
		this.mOptionsMenu = menu;
		inflater.inflate(R.menu.airport_menu, menu);
		
		MenuItem modeMenuItem =  menu.findItem(R.id.airport_menuMode);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(modeMenuItem).findViewById(R.id.airport_mode_spinner);
		if ( this.mCurrentMode.equals("in")) {
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
					AirportListFragment.this.mCurrentMode = "in";
				}
				else {
					AirportListFragment.this.mCurrentMode = "out";
				}
				AirportListFragment.this.startRequest();
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
				
			}
		});
		
		if ( this.mRequestRunning) {
            mSwipeRefreshWidget.setRefreshing(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

        case R.id.airport_menu_info:
            FragmentManager fm = getChildFragmentManager();

            new MaterialDialog.Builder(this.getActivity())
                    .title(R.string.airport_info_title)
                    .items(R.array.airport_info_details)
                    .positiveText(R.string.close)
                    .show();
            return true;
        }

		return super.onOptionsItemSelected(item);
	}

	private void startRequest() {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.startRequest");
        }
		if ( !mRequestRunning && getActivity() != null) {
			AirportListFragment.this.mRequestRunning = true ;
			
			String url = getString(R.string.airport_rest_url, this.mCurrentMode);
			GsonRequest<AirportRestResponse> jsObjRequest = new GsonRequest<AirportRestResponse>(
					Method.GET, url,
					AirportRestResponse.class, null,
					this.createAirportRequestSuccessListener(),
					this.createAirportRequestErrorListener());
			jsObjRequest.setShouldCache(false);
            mSwipeRefreshWidget.setRefreshing(true);
			this.mRequestQueue.add(jsObjRequest);
		}
		else if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "  request is already running or no activity attached");
		}
	}
	
	private void cancelRequests() {
		this.mRequestQueue.cancelAll(this);
	}

	private Response.Listener<AirportRestResponse> createAirportRequestSuccessListener() {
		return new Response.Listener<AirportRestResponse>() {
			@Override
			public void onResponse(AirportRestResponse response) {
                if (BuildConfig.DEBUG) {
                    Log.i(YourApplication.LOG_TAG, "AirportListFragment.onResponse");
                }
                AirportRecyclerAdapter adapter = (AirportRecyclerAdapter) AirportListFragment.this.mAirportAdapter;
                adapter.setMode(AirportListFragment.this.mCurrentMode);
				adapter.clear();
				Collections.sort(response.getFlights(),
						new FlightEtaComparator());
				//Min Level 11: adapter.addAll(response.getFlights());
                for( Flight flight: response.getFlights()) {
                    adapter.add(flight);
                }
				adapter.notifyDataSetChanged();
				AirportListFragment.this.mSwipeRefreshWidget.setRefreshing(false);
				AirportListFragment.this.mRequestRunning = false ;
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
                AirportListFragment.this.mSwipeRefreshWidget.setRefreshing(false);
				AirportListFragment.this.mRequestRunning = false ;

                if ( !isDetached()) {
                    Crouton.makeText(
                            AirportListFragment.this.getActivity(),
                            getString(R.string.error_retrievingdata),
                            Style.ALERT).show();
                }
			}
		};
	}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.cancelRequests();
        Crouton.cancelAllCroutons();
    }

    @Override
    public void onRefresh() {
        AirportListFragment.this.cancelRequests();
        AirportListFragment.this.startRequest();
    }
}
