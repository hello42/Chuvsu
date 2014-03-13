package com.ulop.NewsFullView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ulop.chuvsu.app.R;

import java.util.Locale;

public class NewsFullActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    int nPosition;
     String title;
     String content;
     String pTime;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_full);
        Intent intent = getIntent();

        nPosition = intent.getIntExtra("position", 7);
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        pTime = intent.getStringExtra("time");


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_full, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            position += nPosition;
            return PlaceholderFragment.newInstance(title, content, pTime);
            //return NewsFullViewFragment.newInstance(title, content, pTime);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
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

        private static final String ARG_TITLE = "section_title";
        private static final String ARG_CONTENT = "section_content";
        private static final String ARG_PTIME = "section_time";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String title, String content, String pTime) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_TITLE, title);
            args.putString(ARG_CONTENT, content);
            args.putString(ARG_PTIME, pTime);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news_full_view, container, false);
            //int pos = getArguments().getInt(ARG_SECTION_NUMBER);

            String title = getArguments().getString(ARG_TITLE);
            String content = getArguments().getString(ARG_CONTENT);
            String pTime = getArguments().getString(ARG_PTIME);

            TextView titleTextView = (TextView) rootView.findViewById(R.id.newTitle);
            TextView contentTextView = (TextView) rootView.findViewById(R.id.body);
            TextView dateTextView = (TextView) rootView.findViewById(R.id.publicDate);

            //NewsCardAdapter newList = MainActivity.newList;
            //NewsCardAdapter.NewsCard card = (NewsCardAdapter.NewsCard) newList.getItem(pos);

            titleTextView.setText(title);
            contentTextView.setText(Html.fromHtml(content));
            dateTextView.setText(pTime);

            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }


    }

}
