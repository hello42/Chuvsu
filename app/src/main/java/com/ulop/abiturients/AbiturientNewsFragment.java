package com.ulop.abiturients;


import android.app.Activity;
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

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ulop.chuvsu.app.R;
import com.ulop.syncadapter.Info.InfoContract;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AbiturientNewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AbiturientNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AbiturientNewsFragment extends Fragment implements android.support.v7.app.ActionBar.TabListener{

    private static final String TAG = "AbitNews";
    private OnFragmentInteractionListener mListener;

    private View mRoot;
    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;


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
        mRoot = inflater.inflate(R.layout.fragment_abiturient_pager, container, false);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) mRoot.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(new ActionBar.TabListener() {
                                @Override
                                public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
                                    Log.i(TAG, String.valueOf(tab.getPosition()));

                                    mViewPager.setCurrentItem(tab.getPosition());
                                }

                                @Override
                                public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                                }

                                @Override
                                public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

                                }
                            }));
        }

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



    @Override
    public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }


    @Override
    public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

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
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return NewsFragment.newInstance(0);
                case 1:
                    return NewsFragment.newInstance(1);
                case 2:
                    return NewsFragment.newInstance(3);
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
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int NEWS_LIST = 0;
        private static int IMPORTANT_NEWS_LIST = 1;
        private static int LINKS_LIST = 2;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            int pos = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (pos){
                case 0: createNewsList(rootView); break;
                case 1: createImportantNewsList(rootView); break;
                case 2: createLinksList(rootView); break;
                default: new IllegalArgumentException("");

            }

            return rootView;
        }

        private void createLinksList(View view) {

        }

        private void createImportantNewsList(View view) {

        }

        private void createNewsList(View view) {
        }
    }

    public static class NewsFragment extends Fragment
            implements LoaderManager.LoaderCallbacks<Cursor>{

        private SimpleCursorAdapter mAdapter;

        private static final String[] PROJECTION = new String[]{
                InfoContract.AbitNews._ID,
                InfoContract.AbitNews.COLUMN_NAME_TITLE,
                InfoContract.AbitNews.COLUMN_NAME_CONTENT,
                InfoContract.AbitNews.COLUMN_NAME_PUBLISHED,
                InfoContract.AbitNews.COLUMN_NAME_IMAGE
        };

        private static final int COLUMN_PUBLISHED = 3;
        private static final int COLUMN_IMAGE = 4;

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
                R.id.Title,
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
                    InfoContract.AbitNews.CONTENT_URI, // URI
                    PROJECTION,                // Projection
                    selection,                           // Selection
                    null,                           // Selection args
                    InfoContract.AbitNews.COLUMN_NAME_PUBLISHED + " desc");
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
                    (ActionBarActivity) getActivity(),       // Current context
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
                        str = str.substring(0, 6);
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


            return rootView;
        }


    }
}
