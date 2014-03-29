package org.michenux.yourappidea.tutorial;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.michenux.drodrolib.db.utils.CursorUtils;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.tutorial.contentprovider.TutorialContentProvider;
import org.michenux.yourappidea.tutorial.sync.TutorialSyncAdapter;
import org.michenux.yourappidea.tutorial.sync.TutorialSyncHelper;

import javax.inject.Inject;

import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class TutorialListFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    private boolean mHasSync = false;

    private SimpleCursorAdapter mAdapter;

    private String[] mAdapterFromColumns = new String[] {TutorialContentProvider.TITLE_COLUMN, TutorialContentProvider.DESCRIPTION_COLUMN, TutorialContentProvider.DATECREATION_COLUMN};
    private int[] mAdapterToViews = new int[] { R.id.tutorial_title, R.id.tutorial_desc, R.id.tutorial_date};

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
        mSwipeRefreshWidget.setColorScheme(R.color.color1, R.color.color2, R.color.color3,
                R.color.color4);
        ListView listView = (ListView) view.findViewById(R.id.tutorial_listview);
        listView.setOnItemClickListener(this);
        fillData(listView);
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
                SimpleDialogFragment.createBuilder(this.getActivity(), this.getActivity().getSupportFragmentManager())
                        .setMessage(Html.fromHtml(getString(R.string.tutorial_info_details)))
                        .setTitle(R.string.tutorial_info_title)
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAdapter.getCursor().moveToPosition(position);
        String uri = CursorUtils.getString(TutorialContentProvider.URL_COLUMN, mAdapter.getCursor());
        Intent viewTutoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        this.startActivity(viewTutoIntent);
    }

    private void fillData( ListView listView) {

        this.getLoaderManager().initLoader(0, null, this);
        this.mAdapter = new SimpleCursorAdapter(this.getActivity(),
                R.layout.tutorial_listitem, null, mAdapterFromColumns, mAdapterToViews, 0);

        this.mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor,
                                        int columnIndex) {

                if (view.getId() == R.id.tutorial_title) {

                    String title = CursorUtils.getString(TutorialContentProvider.TITLE_COLUMN, cursor);
                    TextView textView = (TextView) view;
                    textView.setText(Html.fromHtml(title));

                    return true;
                }
                else if (view.getId() == R.id.tutorial_desc) {

                    String desc = CursorUtils.getString(TutorialContentProvider.DESCRIPTION_COLUMN, cursor);
                    TextView textView = (TextView) view;
                    textView.setText(Html.fromHtml(desc));

                    return true;
                }
                else if ( view.getId() == R.id.tutorial_date ) {

                    long date = CursorUtils.getLong(TutorialContentProvider.DATECREATION_COLUMN, cursor) * 1000;
                    TextView textView = (TextView) view;
                    int flags = 0;
                    flags |= DateUtils.FORMAT_SHOW_DATE;
                    flags |= DateUtils.FORMAT_ABBREV_MONTH;
                    flags |= DateUtils.FORMAT_SHOW_YEAR;

                    textView.setText(DateUtils.formatDateTime(getActivity(), date, flags));
                    return true;
                }
                return false;
            }
        });

        listView.setAdapter(this.mAdapter);
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
