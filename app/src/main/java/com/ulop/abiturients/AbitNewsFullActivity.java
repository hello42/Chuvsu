package com.ulop.abiturients;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.InputDevice;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.squareup.picasso.Picasso;
import com.ulop.chuvsu.app.R;
import com.ulop.models.AbiturientNews;
import com.ulop.syncadapter.Info.InfoContract;

import java.util.Locale;

public class AbitNewsFullActivity extends ActionBarActivity {

    private static final String TAG = "FullViewAnews";
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_full);

        Intent intent = getIntent();

        int nPosition = intent.getIntExtra("position", 7);
        count = intent.getIntExtra("count", -1);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);

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
        ShareActionProvider mShareActionProvider = new ShareActionProvider(getApplication());
       // mShareActionProvider.
        int id = item.getItemId();
        if (id == R.id.share) {
            TextView contentTextView = (TextView) findViewById(R.id.body);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, contentTextView.getText());
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Поделиться новостью"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a LinksFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
            //return NewsFullViewFragment.newInstance(title, content, pTime);
        }

        @Override
        public int getCount() {            // Show 3 total pages.
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

        private static final String[] PROJECTION = new String[]{
                InfoContract.AbitNews._ID,
                InfoContract.AbitNews.COLUMN_NAME_TITLE,
                InfoContract.AbitNews.COLUMN_NAME_CONTENT,
                InfoContract.AbitNews.COLUMN_NAME_PUBLISHED,
                InfoContract.AbitNews.COLUMN_NAME_IMAGE
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
            ImageView imageView = (ImageView) rootView.findViewById(R.id.facultyLogo);

            final ContentResolver contentResolver = getActivity().getContentResolver();

            String sortOrder = InfoContract.AbitNews.COLUMN_NAME_PUBLISHED;
            Uri uri = ContentProvider.createUri(AbiturientNews.class, null);
            Cursor cursor = contentResolver.query(uri, PROJECTION, null, null, sortOrder + " desc");

            int pos = getArguments().getInt(ARG_SECTION_NUMBER);

            cursor.move(pos);

            titleTextView.setText(cursor.getString(COLUMN_TITLE));
            contentTextView.setText(cursor.getString(COLUMN_CONTENT));
            dateTextView.setText(cursor.getString(COLUMN_PUBLISHED).substring(0, 10));
            String str = cursor.getString(COLUMN_IMAGE);
            Picasso.with(getActivity()).load(str).into(imageView);

            return rootView;
        }


    }

}
