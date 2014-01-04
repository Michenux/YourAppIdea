package org.michenux.drodrolib.ui.listener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

public class StartFragmentOnClickListener implements View.OnClickListener {

	private Class<? extends Fragment> fragmentClass ;
	
	private FragmentManager fragmentManager ;
	
	private int destination ;

	public StartFragmentOnClickListener(int destination, Class<? extends Fragment> fragmentClass, FragmentManager fragmentManager) {
		this.fragmentClass = fragmentClass;
		this.fragmentManager = fragmentManager;
		this.destination = destination;
	}

	@Override
	public void onClick(View view) {
		try {
			fragmentManager.beginTransaction().replace(this.destination, fragmentClass.newInstance()).commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
