package com.greengrass.newscardlist.dummy;


import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.greengrass.chuvsu.app.NewsFullActivity;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.Entry;
import com.greengrass.syncadapter.Info.InfoContract;
import com.greengrass.syncadapter.accounts.AuthenticatorService;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.greengrass.newscardlist.dummy.NewsCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NewsCardFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "NewsCardFragment";

    private SimpleCursorAdapter mAdapter;

    private Object mSyncObserverHandle;
	private SwipeRefreshLayout swipe;

	/**
     * Options menu used to populate ActionBar.
     */
    private Menu mOptionsMenu;
    private static final int COLUMN_PUBLISHED = 4;
    private static final int COLUMN_IMAGE = 2;

    /**
     * List of Cursor columns to read from when preparing an adapter to populate the ListView.
     */
    private static final String[] FROM_COLUMNS = new String[]{
            InfoContract.Entry.COLUMN_NAME_TITLE,
            InfoContract.Entry.COLUMN_NAME_CONTENT,
            InfoContract.Entry.COLUMN_NAME_PUBLISHED,
            InfoContract.Entry.COLUMN_NAME_IMAGE
    };

    private static final int[]      TO_FIELDS = new int[]{
            R.id.newTitle,
            R.id.textView,
            R.id.textView2,
            R.id.facultyLogo};

    Account mAccount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsCardFragment newInstance(String param1, String param2) {
        NewsCardFragment fragment = new NewsCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public NewsCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(false);

        mAccount = AuthenticatorService.GetAccount();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_univernews_list, container, false);

        Log.i(TAG, "NewsCardFragment view create");
        mAdapter = new SimpleCursorAdapter(
                getActivity(),       // Current context
                R.layout.card_news,  // Layout for individual rows
                null,                // Cursor
                FROM_COLUMNS,        // Cursor columns to use
                TO_FIELDS,           // Layout fields to use
                0                    // No flags
        );

	    final SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    final SimpleDateFormat output = new SimpleDateFormat("EEEE, dd.MM.yyy");


	    mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
             if (i == COLUMN_IMAGE) {
                    String str = cursor.getString(i);

	             //final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.loading);

                 Picasso.with(getActivity()).
                         load(Uri.parse(Uri.decode(str))).
                         placeholder(R.drawable.loading).
                         fit().
                         into((ImageView) view);
                    return true;
                } else
             if (i == COLUMN_PUBLISHED){
                 TextView tW = (TextView) view.findViewById(R.id.textView2);
	             String str = cursor.getString(COLUMN_PUBLISHED);
	             Date date = null;
	             try {
		             date = input.parse(str);
	             } catch (ParseException e) {
		             e.printStackTrace();
	             }
	             str = output.format(date);

	             tW.setText(str);
                 return  true;
             }
             else {
                    // Let SimpleCursorAdapter handle other fields automatically
                    return false;
                }

            }
        });

        ListView listView = (ListView) rootView.findViewById(R.id.listView1);

        listView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsFullActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("count", ((SimpleCursorAdapter) parent.getAdapter()).getCursor().getCount());
                Log.i("cards", Integer.toString(position));

                startActivity(intent);
            }


        });

        swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        swipe.setColorScheme(
                R.color.yellow,
                R.color.blue,
                R.color.red,
                R.color.white);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public static final String AUTHORITY = InfoContract.CONTENT_AUTHORITY;

            @Override
            public void onRefresh() {
                Log.i(TAG, "I'm swiped down!");
                Bundle settingsBundle = new Bundle();
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_MANUAL, true);
                settingsBundle.putBoolean(
                        ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

                ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);


            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Cursor loader create");
        return new CursorLoader(getActivity(),  // Context
                ContentProvider.createUri(Entry.class, null), // URI
                null,                // Projection
                null,                           // Selection
                null,                           // Selection args
                InfoContract.Entry.COLUMN_NAME_PUBLISHED + " desc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    void setRefreshActionButtonState(boolean refreshing) {

            swipe.setRefreshing(refreshing);

    }

    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = AuthenticatorService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, InfoContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, InfoContract.CONTENT_AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };

}
