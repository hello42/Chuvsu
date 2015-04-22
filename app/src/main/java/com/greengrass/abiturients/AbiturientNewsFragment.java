package com.greengrass.abiturients;


import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TabHost;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.squareup.picasso.Picasso;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.AbiturientNews;
import com.greengrass.syncadapter.Info.InfoContract;
import com.greengrass.syncadapter.accounts.AuthenticatorService;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AbiturientNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AbiturientNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AbiturientNewsFragment extends Fragment {

    private static final String TAG = "AbitNews";
    private OnFragmentInteractionListener mListener;


    public static AbiturientNewsFragment newInstance() {
        AbiturientNewsFragment fragment = new AbiturientNewsFragment();
        return fragment;
    }
    public AbiturientNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRoot = inflater.inflate(R.layout.fragment_abiturient_pager, container, false);

        FragmentManager fragmentManager = getFragmentManager();

        TabHost tabHost = (TabHost) mRoot.findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("AbiturientNews").
                setIndicator("Новости").
                setContent(R.id.tab1);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("ImportantNews").
                setIndicator("Важно").
                setContent(R.id.tab2);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("LinksList").
                setIndicator("Ссылки").
                setContent(R.id.tab3);
        tabHost.addTab(tabSpec);

        fragmentManager.beginTransaction()
                .replace(R.id.anews, NewsFragment.newInstance(0))
                .commit();

        fragmentManager.beginTransaction()
                .replace(R.id.impnews, NewsFragment.newInstance(1))
                .commit();

        fragmentManager.beginTransaction()
                .replace(R.id.links, LinksFragment.newInstance(0))
                .commit();

        return mRoot;
    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void showActionBarTabs(){
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a LinksFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return NewsFragment.newInstance(0);
                case 1:
                    return NewsFragment.newInstance(1);
                case 2:
                    return LinksFragment.newInstance(3);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Новости";
                case 1:
                    return "Важно";
                case 2:
                    return "Ссылки";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class LinksFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LinksFragment newInstance(int sectionNumber) {
            LinksFragment fragment = new LinksFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public LinksFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_faculty_list, container, false);
            int pos = getArguments().getInt(ARG_SECTION_NUMBER);

            ListView listView = (ListView) rootView.findViewById(R.id.fctlist);
            final AbiturientLinksAdapter linksAdapter = new AbiturientLinksAdapter(getActivity());
            listView.setAdapter(linksAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String url = linksAdapter.getLinkItem(i).link;

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });

            return rootView;
        }


    }

    public static class NewsFragment extends Fragment
            implements LoaderManager.LoaderCallbacks<Cursor>{

        private SimpleCursorAdapter mAdapter;
        private Object mSyncObserverHandle;
        private Menu mOptionsMenu;




        private static final int COLUMN_PUBLISHED = 5;
        private static final int COLUMN_IMAGE = 2;

        /**
         * List of Cursor columns to read from when preparing an adapter to populate the ListView.
         */
        private static final String[] FROM_COLUMNS = new String[]{
                InfoContract.AbitNews.COLUMN_NAME_TITLE,
                InfoContract.AbitNews.COLUMN_NAME_CONTENT,
                InfoContract.AbitNews.COLUMN_NAME_PUBLISHED,
                InfoContract.AbitNews.COLUMN_NAME_IMAGE
        };

        private static final int[] TO_FIELDS = new int[]{
                R.id.newTitle,
                R.id.textView,
                R.id.textView2,
                R.id.facultyLogo};



        private int NEWS_TYPE;

        public static NewsFragment newInstance(int newsType){
            NewsFragment fragment = new NewsFragment();

            Bundle args = new Bundle();
            args.putInt("NEWS_TYPE", newsType);
            fragment.setArguments(args);

            return  fragment;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.i(TAG, "Cursor loader create");
            int nType = getArguments().getInt("NEWS_TYPE");
            String selection;

            if (nType == 0) {
                selection = InfoContract.AbitNews.COLUMN_NAME_NOTIFICATE + "=0";
            } else {
                selection = InfoContract.AbitNews.COLUMN_NAME_NOTIFICATE + "=1";
            }


            return new CursorLoader(getActivity(),  // Context
                    ContentProvider.createUri(AbiturientNews.class, null), // URI
                    null,                // Projection
                    selection,                           // Selection
                    null,                           // Selection args
                    InfoContract.AbitNews.COLUMN_NAME_PUBLISHED + " desc");
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
        public void onPause() {
            super.onPause();
            if (mSyncObserverHandle != null) {
                ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
                mSyncObserverHandle = null;
            }
        }


        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.changeCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.changeCursor(null);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                NEWS_TYPE = getArguments().getInt("NEWS_TYPE");
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_faculty_list, container, false);

            mAdapter = new SimpleCursorAdapter(
                    (AppCompatActivity) getActivity(),       // Current context
                    R.layout.card_news,  // Layout for individual rows
                    null,                // Cursor
                    FROM_COLUMNS,        // Cursor columns to use
                    TO_FIELDS,           // Layout fields to use
                    0                    // No flags
            );

            Cursor c = mAdapter.getCursor();

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

            AbsListView listView = (AbsListView) rootView.findViewById(R.id.fctlist);

            listView.setAdapter(mAdapter);
            getLoaderManager().initLoader(0, null, this);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), AbitNewsFullActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("count", ((SimpleCursorAdapter) parent.getAdapter()).getCursor().getCount());
                    Log.i("cards", Integer.toString(position));

                    startActivity(intent);
                }
            });

            return rootView;
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
            }        };

    }
}
