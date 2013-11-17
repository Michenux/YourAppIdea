package org.michenux.yourappidea.tutorial;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.michenux.yourappidea.R;
import org.michenux.yourappidea.tutorial.contentprovider.TutorialContentProvider;

public class TutorialListFragment extends Fragment implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mAdapter;

    private String[] mAdapterFromColumns = new String[] {TutorialContentProvider.TITLE_COLUMN};
    private int[] mAdapterToViews = new int[] { R.id.tutorial_title };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.airport_menuRefresh:
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void fillData( ListView listView) {

        this.getLoaderManager().initLoader(0, null, this);
        this.mAdapter = new SimpleCursorAdapter(this.getActivity(),
                R.layout.tutorial_listitem, null, mAdapterFromColumns, mAdapterToViews, 0);

        this.mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor paramCursor,
                                        int paramInt) {

//                if (view.getId() == R.id.friend_name) {
//
//                    String title = CursorUtils.getString(TutorialContentProvider.TITLE_COLUMN, paramCursor);
//
//                    TextView textView = (TextView) view;
//                    textView.setText(friendName);
//                    return true;
//                }
                return false;
            }
        });

        listView.setAdapter(this.mAdapter);
    }

    public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
        String[] projection = { TutorialContentProvider.ID_COLUMN,
                TutorialContentProvider.TITLE_COLUMN };
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
}
