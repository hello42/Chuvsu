package com.ulop.NewsFullView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.ulop.chuvsu.app.R;
import com.ulop.syncadapter.Feed.FeedContract;

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
    int count;


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
        count = intent.getIntExtra("count", -1);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(nPosition);


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

            return PlaceholderFragment.newInstance(position + 1);
            //return NewsFullViewFragment.newInstance(title, content, pTime);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return count;
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
    public static class PlaceholderFragment extends Fragment{
        private static DisplayImageOptions options;

        private static final String[] PROJECTION = new String[]{
                FeedContract.Entry._ID,
                FeedContract.Entry.COLUMN_NAME_TITLE,
                FeedContract.Entry.COLUMN_NAME_CONTENT,
                FeedContract.Entry.COLUMN_NAME_PUBLISHED,
                FeedContract.Entry.COLUMN_NAME_IMAGE
        };

        private static final int COLUMN_ID = 0;
        /** Column index for title */
        private static final int COLUMN_TITLE = 1;
        /** Column index for link */
        private static final int COLUMN_CONTENT = 2;
        /** Column index for published */
        private static final int COLUMN_PUBLISHED = 3;
        private static final int COLUMN_IMAGE = 4;


        protected ImageLoader imageLoader = ImageLoader.getInstance();

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";



        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int position) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);


            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.abc_ic_clear)
                    .showImageOnFail(R.drawable.abc_ic_go)
                    .resetViewBeforeLoading(true)
                    .cacheOnDisc(true)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();


            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_news_full_view, container, false);

            TextView titleTextView = (TextView) rootView.findViewById(R.id.newTitle);
            TextView contentTextView = (TextView) rootView.findViewById(R.id.body);
            TextView dateTextView = (TextView) rootView.findViewById(R.id.publicDate);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

            final ContentResolver contentResolver = getActivity().getContentResolver();
            String sortOrder = FeedContract.Entry.COLUMN_NAME_NEWS_ID;
            Uri uri = FeedContract.Entry.CONTENT_URI;
            Cursor cursor = contentResolver.query(uri, PROJECTION, null, null, sortOrder + " desc");
            int pos = getArguments().getInt(ARG_SECTION_NUMBER);
            Log.i("full", new Integer(pos).toString());
            cursor.move(pos);

            titleTextView.setText(cursor.getString(COLUMN_TITLE));
            contentTextView.setText(cursor.getString(COLUMN_CONTENT));
            dateTextView.setText(cursor.getString(COLUMN_PUBLISHED).substring(0, 10));
            String str = cursor.getString(COLUMN_IMAGE);
            imageLoader.displayImage(str, imageView, options);

            return rootView;
        }


    }

}
