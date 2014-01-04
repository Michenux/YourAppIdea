package org.michenux.drodrolib.ui.listener;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author Michenux
 *
 */
public class StartActivityOnClickListener implements View.OnClickListener {

	/**
	 * 
	 */
	private Context context ;
	
	/**
	 * 
	 */
	private Intent intent ;
		
	/**
	 * 
	 */
	private Fragment fragment ;
	
	/**
	 * @param context
	 * @param objectClass
	 */
	public StartActivityOnClickListener(Context context, Class<?> objectClass) {
		super();
		this.context = context ;
		this.intent = new Intent(this.context, objectClass);
	}
	
	/**
	 * @param fragment
	 * @param objectClass
	 */
	public StartActivityOnClickListener(Fragment fragment, Class<?> objectClass) {
		super();
		this.fragment = fragment ;
		this.intent = new Intent(fragment.getActivity(), objectClass);
	}
	
	/**
	 * @param context
	 * @param intent
	 */
	public StartActivityOnClickListener(Context context, Intent intent) {
		super();
		this.context = context ;
		this.intent = intent;
	}

	/**
	 * {@inheritDoc}
	 */
	public void onClick(View view) {
		if ( this.context != null ) {
			this.context.startActivity(this.intent);
		}
		else {
			this.fragment.startActivity(this.intent);
		}
	}
}
