package org.michenux.yourappidea.friends;

import org.michenux.android.ui.fragment.MasterDetailFragmentHelper;
import org.michenux.android.ui.fragment.MasterDetailFragments;
import org.michenux.yourappidea.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendMainFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        Log.d("LMI", "FriendMainFragment.onCreate");
		super.onCreate(savedInstanceState);
        setRetainInstance(false);
        //-------------------------------------
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        Log.d("LMI", "FriendMainFragment.onCreateView");
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
}
