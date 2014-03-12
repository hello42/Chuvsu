package com.ulop.newscardlist.dummy;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ulop.NewsFullView.NewsFullActivity;
import com.ulop.SyncAdapter.FeedContract;
import com.ulop.SyncAdapter.SyncUtils;
import com.ulop.chuvsu.app.MainActivity;
import com.ulop.chuvsu.app.R;

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
            FeedContract.Entry.COLUMN_NAME_PUBLISHED
    };


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

        SyncUtils.CreateSyncAccount(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_of_news, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView1);
        NewsCardAdapter cardAdapter = MainActivity.newList;
        listView.setAdapter(cardAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsFullActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }


        });
        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
