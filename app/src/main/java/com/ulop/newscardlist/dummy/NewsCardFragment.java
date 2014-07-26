package com.ulop.newscardlist.dummy;


import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ulop.NewsFullView.NewsFullActivity;
import com.ulop.chuvsu.app.R;
import com.ulop.syncadapter.Info.InfoContract;
import com.ulop.syncadapter.SyncService;
import com.ulop.syncadapter.SyncUtils;
import com.ulop.syncadapter.accounts.AuthenticatorService;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.ulop.newscardlist.dummy.NewsCardFragment#newInstance} factory method to
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


    /**
     * Options menu used to populate ActionBar.
     */
    private Menu mOptionsMenu;

    /**
     * Projection for querying the content provider.
     */
    private static final String[] PROJECTION = new String[]{
            InfoContract.Entry._ID,
            InfoContract.Entry.COLUMN_NAME_TITLE,
            InfoContract.Entry.COLUMN_NAME_CONTENT,
            InfoContract.Entry.COLUMN_NAME_PUBLISHED,
            InfoContract.Entry.COLUMN_NAME_IMAGE
    };

    private static final int COLUMN_ID = 0;
    /** Column index for title */
    private static final int COLUMN_TITLE = 1;
    /** Column index for link */
    private static final int COLUMN_CONTENT = 2;
    /** Column index for published */
    private static final int COLUMN_PUBLISHED = 3;
    private static final int COLUMN_IMAGE = 4;

    /**
     * List of Cursor columns to read from when preparing an adapter to populate the ListView.
     */
    private static final String[] FROM_COLUMNS = new String[]{
            InfoContract.Entry.COLUMN_NAME_TITLE,
            InfoContract.Entry.COLUMN_NAME_CONTENT,
            InfoContract.Entry.COLUMN_NAME_PUBLISHED,
            InfoContract.Entry.COLUMN_NAME_IMAGE
    };

    private static final int[] TO_FIELDS = new int[]{
            R.id.Title,
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
                (ActionBarActivity) getActivity(),       // Current context
                R.layout.card_news,  // Layout for individual rows
                null,                // Cursor
                FROM_COLUMNS,        // Cursor columns to use
                TO_FIELDS,           // Layout fields to use
                0                    // No flags
        );



        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
             if (i == COLUMN_IMAGE) {
                    String str = cursor.getString(i);
                 //final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.loading);

                 Picasso.with(getActivity()).
                         load(str).
                         into((ImageView) view);
                    return true;
                } else
             if (i == COLUMN_PUBLISHED){
                 TextView tW = (TextView) view.findViewById(R.id.textView2);
                 String str = cursor.getString(i);
                 str = str.substring(0, 10);
                 tW.setText(str);
                 return  true;
             }
             else {
                    // Let SimpleCursorAdapter handle other fields automatically
                    return false;
                }

            }
        });

        AbsListView listView = (AbsListView) rootView.findViewById(R.id.listView1);

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

        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
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

                swipe.setRefreshing(false);
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
                InfoContract.Entry.CONTENT_URI, // URI
                PROJECTION,                // Projection
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
        if (mOptionsMenu == null) {
            return;
        }

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }
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
