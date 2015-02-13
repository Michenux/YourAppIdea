package org.michenux.yourappidea.tutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import org.lucasr.twowayview.ItemClickSupport;
import org.lucasr.twowayview.widget.TwoWayView;
import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.tutorial.sync.TutorialContentProvider;
import org.michenux.yourappidea.tutorial.sync.TutorialSyncAdapter;
import org.michenux.yourappidea.tutorial.sync.TutorialSyncHelper;

import javax.inject.Inject;

public class TutorialListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, ItemClickSupport.OnItemClickListener {

    private boolean mHasSync = false;

    private TutorialRecyclerAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshWidget;

    @Inject
    TutorialSyncHelper mTutorialSyncHelper;


    private BroadcastReceiver onFinishSyncReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateRefresh(false);
            mHasSync = true;
        }
    };

    private BroadcastReceiver onStartSyncReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateRefresh(true);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_list, container, false);
        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.tutorial_swiperefreshlayout);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        TwoWayView recyclerView = (TwoWayView) view.findViewById(R.id.tutorial_listview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLongClickable(false);
        final ItemClickSupport itemClick = ItemClickSupport.addTo(recyclerView);
        itemClick.setOnItemClickListener(this);
        fillData(recyclerView);
        mSwipeRefreshWidget.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup viewGroup = (ViewGroup) view;
        if ( ((YourApplication) getActivity().getApplication()).isSyncAdapterRunning()) {
            Log.d(YourApplication.LOG_TAG, "onViewCreated: synchronizing");
            updateRefresh(true);
        }
        else {
            Log.d(YourApplication.LOG_TAG, "onViewCreated: not synchronizing");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tutorial_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.aroundme_menu_info:
                new MaterialDialog.Builder(this.getActivity())
                        .title(R.string.tutorial_info_title)
                        .items(R.array.tutorial_info_details)
                        .positiveText(R.string.close)
                        .show();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(onFinishSyncReceiver, new IntentFilter(TutorialSyncAdapter.SYNC_FINISHED));
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(onStartSyncReceiver, new IntentFilter(TutorialSyncAdapter.SYNC_STARTED));

        if ( !mHasSync ) {
            mTutorialSyncHelper.performSync();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(onFinishSyncReceiver);
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(onStartSyncReceiver);

    }

    private void fillData( TwoWayView recyclerView) {

        this.getLoaderManager().initLoader(0, null, this);

        this.mAdapter = new TutorialRecyclerAdapter(null);
        recyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int row, long column) {
        mAdapter.getCursor().moveToPosition(row);
        String uri = CursorUtils.getString(TutorialContentProvider.URL_COLUMN, mAdapter.getCursor());
        Intent viewTutoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        this.startActivity(viewTutoIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
        String[] projection = { TutorialContentProvider.ID_COLUMN,
                TutorialContentProvider.TITLE_COLUMN, TutorialContentProvider.DESCRIPTION_COLUMN, TutorialContentProvider.URL_COLUMN, TutorialContentProvider.DATECREATION_COLUMN };
        CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
                TutorialContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.mAdapter.changeCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
        this.mAdapter.changeCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        mTutorialSyncHelper.performSync();
    }

    private void updateRefresh(final boolean isSyncing) {
        if ( !isSyncing) {
            Log.d(YourApplication.LOG_TAG, "show as not refreshing");
            mSwipeRefreshWidget.setRefreshing(false);
        }
        else {
            Log.d(YourApplication.LOG_TAG, "show as refreshing");
            mSwipeRefreshWidget.setRefreshing(true);
        }
    }
}
