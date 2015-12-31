package org.michenux.yourappidea.friends;

import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.michenux.drodrolib.ui.fragment.FragmentHelper;
import org.michenux.drodrolib.ui.recyclerview.DividerItemDecoration;
import org.michenux.drodrolib.ui.recyclerview.ItemClickSupport;
import org.michenux.yourappidea.R;

public class FriendListFragment extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>, ItemClickSupport.OnItemClickListener {

	private FriendRecyclerAdapter mAdapter;

    private RecyclerView mRecyclerView ;

	private boolean dualPanel;

	public static FriendListFragment newInstance() {
		// Bundle args = new Bundle();
		// frag.setArguments(args);
		return new FriendListFragment();
	}

    /**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.friendlist, container, false);
        mRecyclerView = (RecyclerView) mainView.findViewById(R.id.friend_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(this);
		fillData();
		return mainView;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.dualPanel = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	private void fillData() {

		this.getLoaderManager().initLoader(0, null, this);
		this.mAdapter = new FriendRecyclerAdapter(null);
        mRecyclerView.setAdapter(this.mAdapter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 *      android.os.Bundle)
	 */
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		String[] projection = { FriendContentProvider.ID_COLUMN,
                FriendContentProvider.NAME_COLUMN, FriendContentProvider.FACE_COLUMN };
		return new CursorLoader(this.getActivity(),
				FriendContentProvider.CONTENT_URI, projection, null, null, null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader,
	 *      Object)
	 */
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		this.mAdapter.swapCursor(cursor);
	}

	/**
	 * {@inheritDoc}
	 *
     * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	public void onLoaderReset(Loader<Cursor> cursor) {
		this.mAdapter.swapCursor(null);
	}

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Uri detailUri = Uri.parse(FriendContentProvider.CONTENT_URI + "/" + mAdapter.getItemId(position));
        FriendDetailFragment detailFragment = FriendDetailFragment
                .newInstance(detailUri);
        if (!this.dualPanel) {
            FragmentHelper.initFragmentWithBackstack(detailFragment,
                    R.id.friendmain_fragment, this.getParentFragment().getChildFragmentManager());
        } else {
            FragmentHelper.initFragment(detailFragment,
                    R.id.frienddetail_fragment, this.getParentFragment().getChildFragmentManager());
        }
    }
}
