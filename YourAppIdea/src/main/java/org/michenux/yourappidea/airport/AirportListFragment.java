package org.michenux.yourappidea.airport;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
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

import org.michenux.drodrolib.ui.recyclerview.DividerItemDecoration;
import org.michenux.drodrolib.ui.snackbar.SnackbarHelper;
import org.michenux.yourappidea.BuildConfig;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;

import java.util.ArrayList;
import java.util.Collections;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AirportListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

	private boolean mRequestRunning = false;

	private String mCurrentMode = "in" ;

    private AirportRecyclerAdapter mAirportAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

	private AirportInfoService mAirportInfoService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
		    Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreate");
        }
		setHasOptionsMenu(true);

		this.mAirportAdapter = new AirportRecyclerAdapter(new ArrayList<>(), this.getActivity(), this.mCurrentMode);
		mAirportInfoService = AirportInfoServiceFactory.create(this.getContext());
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onCreateView");
        }
		View view = inflater.inflate(R.layout.airport_listfragment, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.airport_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.airport_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
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
		inflater.inflate(R.menu.airport_menu, menu);

		MenuItem modeMenuItem =  menu.findItem(R.id.airport_menuMode);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(modeMenuItem).findViewById(R.id.airport_mode_spinner);
		if ( this.mCurrentMode.equals("in")) {
			spinner.setSelection(0, false);
		}
		else {
			spinner.setSelection(1, false);
		}

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long row) {
                if (position == 0) {
                    AirportListFragment.this.mCurrentMode = "in";
                } else {
                    AirportListFragment.this.mCurrentMode = "out";
                }
                AirportListFragment.this.startRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
	}


    @Override
    public void onResume() {
        super.onResume();
        if ( mRequestRunning ) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void onPause() {
        // Disable SwipeRefreshLayout
        // because of that bug:
        //   http://stackoverflow.com/questions/27057449/when-switch-fragment-with-swiperefreshlayout-during-refreshing-fragment-freezes
        mSwipeRefreshLayout.setRefreshing(false);
        mSwipeRefreshLayout.destroyDrawingCache();
        mSwipeRefreshLayout.clearAnimation();
        super.onPause();
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

        case R.id.airport_menu_info:
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

            Observable<AirportRestResponse> observable =
                mAirportInfoService.getFlights(getString(R.string.airport_code), mCurrentMode);

            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<AirportRestResponse>() {
                    @Override
                    public void onCompleted() {
                        AirportListFragment.this.mSwipeRefreshLayout.setRefreshing(false);
                        AirportListFragment.this.mRequestRunning = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onError", e);
                        }
                        AirportListFragment.this.mSwipeRefreshLayout.setRefreshing(false);
                        AirportListFragment.this.mRequestRunning = false;

                        SnackbarHelper.showErrorLongMessageWithAction(AirportListFragment.this.getView(),
                                R.string.error_retrievingdata, R.string.error_retrievingdata_retry, view -> onRefresh());
                    }

                    @Override
                    public void onNext(AirportRestResponse response) {
                        if (BuildConfig.DEBUG) {
                            Log.i(YourApplication.LOG_TAG, "AirportListFragment.onNext");
                        }
                        AirportRecyclerAdapter adapter = AirportListFragment.this.mAirportAdapter;
                        adapter.setMode(AirportListFragment.this.mCurrentMode);
                        adapter.clear();
                        Collections.sort(response.getFlights(), new FlightEtaComparator());
                        adapter.addAll(response.getFlights());
                        adapter.notifyDataSetChanged();
                    }
                });

            mSwipeRefreshLayout.setRefreshing(true);
		}
		else if (BuildConfig.DEBUG) {
            Log.i(YourApplication.LOG_TAG, "  request is already running or no activity attached");
		}
	}

    @Override
    public void onDestroyView() {
		super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        AirportListFragment.this.startRequest();
    }
}
