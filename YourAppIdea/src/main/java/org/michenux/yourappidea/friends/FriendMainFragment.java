package org.michenux.yourappidea.friends;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import org.michenux.drodrolib.ui.fragment.MasterDetailFragmentHelper;
import org.michenux.drodrolib.ui.fragment.MasterDetailFragments;
import org.michenux.yourappidea.R;

public class FriendMainFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRetainInstance(false);
        setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends, container, false);

        MasterDetailFragments currentFragments = MasterDetailFragmentHelper
                .getCurrentFragments(R.id.friendmain_fragment,
                        R.id.frienddetail_fragment, FriendDetailFragment.class,
                        getChildFragmentManager());
        if ( currentFragments.master == null ) {
            currentFragments.master = FriendListFragment.newInstance();
        }

        MasterDetailFragmentHelper.initFragments(currentFragments, R.id.friendmain_fragment,
                R.id.frienddetail_fragment, getResources().getConfiguration(), getChildFragmentManager());

		return view ;
	}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friends_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.friends_menu_info:
                new MaterialDialog.Builder(this.getActivity())
                        .title(R.string.friends_info_title)
                        .items(R.array.friends_info_details)
                        .positiveText(R.string.close)
                        .show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
