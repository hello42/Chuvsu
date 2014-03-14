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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ulop.NewsFullView.NewsFullActivity;
import com.ulop.chuvsu.app.R;
import com.ulop.syncadapter.Feed.FeedContract;
import com.ulop.syncadapter.accounts.AuthenticatorService;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
            FeedContract.Entry._ID,
            FeedContract.Entry.COLUMN_NAME_TITLE,
            FeedContract.Entry.COLUMN_NAME_CONTENT,
            FeedContract.Entry.COLUMN_NAME_PUBLISHED
    };

    private static final int COLUMN_ID = 0;
    /** Column index for title */
    private static final int COLUMN_TITLE = 1;
    /** Column index for link */
    private static final int COLUMN_CONTENT = 2;
    /** Column index for published */
    private static final int COLUMN_PUBLISHED = 3;

    /**
     * List of Cursor columns to read from when preparing an adapter to populate the ListView.
     */
    private static final String[] FROM_COLUMNS = new String[]{
            FeedContract.Entry.COLUMN_NAME_TITLE,
            FeedContract.Entry.COLUMN_NAME_CONTENT,
            FeedContract.Entry.COLUMN_NAME_PUBLISHED
    };

    private static final int[] TO_FIELDS = new int[]{
            R.id.Title,
            R.id.newsText,
            R.id.publishing_date};


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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "NewsCardFragment view create");
        mAdapter = new SimpleCursorAdapter(
                getActivity(),       // Current context
                R.layout.news_card,  // Layout for individual rows
                null,                // Cursor
                FROM_COLUMNS,        // Cursor columns to use
                TO_FIELDS,           // Layout fields to use
                0                    // No flags
        );



        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int i) {
                if (i == COLUMN_CONTENT) {
                    String str = cursor.getString(i);
                    ((TextView) view).setText(Html.fromHtml(str));
                    return true;
                } else {
                    // Let SimpleCursorAdapter handle other fields automatically
                    return false;
                }


            }
        });

        View rootView = inflater.inflate(R.layout.list_of_news, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView1);

        //ursor cursor = (Cursor) mAdapter.getCursor();




        listView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((SimpleCursorAdapter) parent.getAdapter()).getCursor().getString(COLUMN_TITLE);
                String content = ((SimpleCursorAdapter) parent.getAdapter()).getCursor().getString(COLUMN_CONTENT);
                String pTime = ((SimpleCursorAdapter) parent.getAdapter()).getCursor().getString(COLUMN_PUBLISHED);
                Intent intent = new Intent(getActivity(), NewsFullActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("time", pTime);
                startActivity(intent);
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
                FeedContract.Entry.CONTENT_URI, // URI
                PROJECTION,                // Projection
                null,                           // Selection
                null,                           // Selection args
                FeedContract.Entry.COLUMN_NAME_NEWS_ID + " asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    public void setRefreshActionButtonState(boolean refreshing) {
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
                            account, FeedContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, FeedContract.CONTENT_AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };
}
