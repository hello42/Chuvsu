package com.greengrass.chuvsu.app;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.content.ContentProvider;
import com.greengrass.models.Entry;
import com.greengrass.syncadapter.Info.InfoContract;
import com.squareup.picasso.Picasso;
import com.greengrass.chuvsu.app.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsFullActivity extends ActionBarActivity {

    private static final String TAG = "FullView";
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_full);

	    Toolbar toolbar = (Toolbar) findViewById(R.id.support_actionbar);
	    setSupportActionBar(toolbar);

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

	        final SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	        final SimpleDateFormat output = new SimpleDateFormat("EEEE, dd.MM.yyy");


	        String sortOrder = InfoContract.Entry.COLUMN_NAME_PUBLISHED;
            Uri uri = ContentProvider.createUri(Entry.class, null);
            Cursor cursor = contentResolver.query(uri, PROJECTION, null, null, sortOrder + " desc");

            int pos = getArguments().getInt(ARG_SECTION_NUMBER);

            cursor.move(pos);
            Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
	        Typeface mediumTypeface =
			        Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");


	        titleTextView.setText(cursor.getString(COLUMN_TITLE));
	        //titleTextView.setTypeface(mediumTypeface);

            contentTextView.setText(cursor.getString(COLUMN_CONTENT));
            contentTextView.setTypeface(typeface);
	        String str = cursor.getString(COLUMN_PUBLISHED);
	        Date date = null;
	        try {
		        date = input.parse(str);
	        } catch (ParseException e) {
		        e.printStackTrace();
	        }
	        str = output.format(date);


	        dateTextView.setText(str);

	        str = cursor.getString(COLUMN_IMAGE);
            Log.i(TAG, str);
            Picasso.with(getActivity())
                    .load(str)
                   // .fit()
                    .into(imageView);

        /*    final ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.scroll);
            int barHeight;


            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                       Log.i(TAG, String.valueOf(scrollView.getScrollY()));
                       if (scrollView.getScrollY() <= 2){
                           //getActivity().getActionBar().
                       }
                    return false;
                }
            });*/

            return rootView;
        }


    }

}
