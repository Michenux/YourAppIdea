package org.michenux.yourappidea.friends;

import org.michenux.android.db.utils.CursorUtils;
import org.michenux.android.resources.ResourceUtils;
import org.michenux.yourappidea.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Michenux
 * 
 */
public class FriendDetailFragment extends Fragment {

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(FriendDetailFragment.class);

	/**
	 * 
	 */
	private Uri detailUri;

	/**
	 * 
	 */
	private TextView nameField;
	
	/**
	 * 
	 */
	private TextView jobField ;
	
	/**
	 * 
	 */
	private ImageView faceField ;

    /**
     * @return
     */
    public static FriendDetailFragment newInstance() {
        FriendDetailFragment frag = new FriendDetailFragment();
        return frag;
    }

    /**
	 * @param detailUri
	 * @return
	 */
	public static FriendDetailFragment newInstance(Uri detailUri) {
		FriendDetailFragment frag = new FriendDetailFragment();
		Bundle args = new Bundle();
		args.putParcelable(FriendContentProvider.CONTENT_ITEM_TYPE, detailUri);
		frag.setArguments(args);
		return frag;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 *      android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		log.debug("FriendDetailFragment.onCreateView");
		
		View mainView = inflater.inflate(R.layout.frienddetail, container,
				false);
		this.nameField = (TextView) mainView
				.findViewById(R.id.friend_name_value);
		this.jobField = (TextView) mainView
				.findViewById(R.id.friend_job_value);
		this.faceField = (ImageView) mainView
				.findViewById(R.id.friend_face_value);
		
		this.detailUri = (savedInstanceState == null) ? null
				: (Uri) savedInstanceState
						.getParcelable(FriendContentProvider.CONTENT_ITEM_TYPE);
		log.debug("detailUri from savedInstanceState : {}", this.detailUri);

		Bundle extras = getArguments();
		if (extras != null && this.detailUri == null) {
			
			this.detailUri = extras
					.getParcelable(FriendContentProvider.CONTENT_ITEM_TYPE);
			log.debug("detailUri from intent : {}", this.detailUri);
		}

		if (this.detailUri != null) {
			fillData(this.detailUri);
		}

		return mainView;
	}

	/**
	 * @param uri
	 */
	private void fillData(Uri uri) {
		String[] projection = { FriendTable.Fields.NAME.name(),  
				FriendTable.Fields.JOB.name(),
				FriendTable.Fields.FACE.name()};
		Cursor cursor = this.getActivity().getContentResolver()
				.query(uri, projection, null, null, null);
		if (cursor != null) {
			try {
				cursor.moveToFirst();
				this.nameField
						.setText(
							CursorUtils.getString(FriendTable.Fields.NAME
									.name(), cursor));
				this.jobField
					.setText(CursorUtils.getString(FriendTable.Fields.JOB
							.name(), cursor));

				String faceImg = CursorUtils.getString(FriendTable.Fields.FACE
						.name(), cursor);
				this.faceField.setImageDrawable(
					ResourceUtils.getDrawableByName(
							faceImg, this.getActivity()));
				
			} finally {
				cursor.close();
			}
		}
	}

	/**
	 * @param selectedItemUri
	 */
	public void update(Uri selectedItemUri) {
		this.detailUri = selectedItemUri;
		fillData(this.detailUri);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		log.debug("save FriendDetailFragment state : {}", this.detailUri);
		outState.putParcelable(FriendContentProvider.CONTENT_ITEM_TYPE,
				this.detailUri);
	}

	public Uri getDetailUri() {
		return detailUri;
	}
}
