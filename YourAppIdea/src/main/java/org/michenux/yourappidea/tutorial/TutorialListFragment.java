package org.michenux.yourappidea.tutorial;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.michenux.android.db.utils.CursorUtils;
import org.michenux.yourappidea.R;
import org.michenux.yourappidea.YourApplication;
import org.michenux.yourappidea.tutorial.contentprovider.TutorialContentProvider;
import org.michenux.yourappidea.tutorial.sync.TutorialSyncHelper;

import java.util.Date;

import javax.inject.Inject;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class TutorialListFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor>,
        OnRefreshListener, SyncStatusObserver {

    private SimpleCursorAdapter mAdapter;

    private String[] mAdapterFromColumns = new String[] {TutorialContentProvider.TITLE_COLUMN, TutorialContentProvider.DESCRIPTION_COLUMN, TutorialContentProvider.DATECREATION_COLUMN};
    private int[] mAdapterToViews = new int[] { R.id.tutorial_title, R.id.tutorial_desc, R.id.tutorial_date};

    private PullToRefreshLayout mPullToRefreshLayout;

    private Object mSyncObserverHandle;

    @Inject
    TutorialSyncHelper mTutorialSyncHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((YourApplication) getActivity().getApplication()).inject(this);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.tutorial_listview);
        listView.setOnItemClickListener(this);
        fillData(listView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup viewGroup = (ViewGroup) view;
        mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());
        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto(viewGroup)
                .theseChildrenArePullable(R.id.tutorial_listview)
                .listener(this)
                .setup(mPullToRefreshLayout);
        if (mTutorialSyncHelper.isActiveOrPending()) {
            Log.d(YourApplication.LOG_TAG, "onViewCreated: synchronizing");
            updateRefresh(true);
        }
        else {
            Log.d(YourApplication.LOG_TAG, "onViewCreated: not synchronizing");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE ;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
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

    public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
        String[] projection = { TutorialContentProvider.ID_COLUMN,
                TutorialContentProvider.TITLE_COLUMN, TutorialContentProvider.DESCRIPTION_COLUMN, TutorialContentProvider.URL_COLUMN, TutorialContentProvider.DATECREATION_COLUMN };
        CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
                TutorialContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        this.mAdapter.changeCursor(cursor);
    }

    public void onLoaderReset(Loader<Cursor> cursor) {
        this.mAdapter.changeCursor(null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setListShownNoAnimation(true);
    }

    @Override
    public void onRefreshStarted(View view) {
        mTutorialSyncHelper.performSync();
    }

    @Override
    public void onStatusChanged(int which) {
        if (!mTutorialSyncHelper.isActiveOrPending()) {
            Log.d(YourApplication.LOG_TAG, "onStatusChanged: Sync finished !");

            // Notify PullToRefreshLayout that the refresh has finished
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateRefresh(false);
                }
            });
        }
    }

    private void updateRefresh(final boolean isSyncing) {
        if ( !isSyncing) {
            Log.d(YourApplication.LOG_TAG, "show as not refreshing");
            mPullToRefreshLayout.setRefreshComplete();
        }
        else {
            Log.d(YourApplication.LOG_TAG, "show as refreshing");
            mPullToRefreshLayout.setRefreshing(true);
        }
    }
}
