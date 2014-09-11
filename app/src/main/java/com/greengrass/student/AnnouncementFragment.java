package com.greengrass.student;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.Announcement;
import com.greengrass.syncadapter.Info.InfoContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class AnnouncementFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = "ANNOUNCEMENT";
	private static final String[] FROM_COLUMNS = new String[]{
			"title",
			"date"
	};
	private static final int[] TO_FEILDS = {
			R.id.title,
			R.id.date
	};
	private SimpleCursorAdapter mAdapter;


	public static AnnouncementFragment newInstance() {
		AnnouncementFragment fragment = new AnnouncementFragment();
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AnnouncementFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Typeface mediumTypeface =
				Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");

		final SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		final SimpleDateFormat output = new SimpleDateFormat("dd MMMM yyyy-го года");

		mAdapter = new SimpleCursorAdapter(getActivity(),
				R.layout.announcement_item,
				null,
				FROM_COLUMNS,
				TO_FEILDS,
				0);
		mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			@Override
			public boolean setViewValue(View view, Cursor cursor, int i) {
				Log.i(TAG, String.valueOf(i));
				if (i == 5) {
					TextView tW = (TextView) view.findViewById(R.id.title);
					String str = cursor.getString(i);
					tW.setText(str);
					tW.setTypeface(mediumTypeface);
					return true;
				} else if (i == 3) {
					TextView tW = (TextView) view.findViewById(R.id.date);
					String str = cursor.getString(i);
					Date date = null;
					try {
						date = input.parse(str);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					str = output.format(date);
					
					tW.setText(str);
					tW.setTypeface(mediumTypeface);
					return true;
				} else {
					return false;
				}
			}
		});
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(0, null, this);


	}


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

	}


	@Override
	public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
		Log.i(TAG, "Cursor loader create");
		return new CursorLoader(getActivity(),  // Context
				ContentProvider.createUri(Announcement.class, null), // URI
				null,                // Projection
				null,                           // Selection
				null,                           // Selection args
				"date" + " desc");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
		mAdapter.changeCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader) {
		mAdapter.changeCursor(null);
	}
}
