package com.greengrass.student;



import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.content.ContentProvider;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.InfoForStudent;
import com.greengrass.models.Organization;
import com.greengrass.util.view.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class StudentFragment extends Fragment {

	private static final String[] PROJECTION = {
			"title",
			"body"
	};

	private static final String[] ORGPROJECTION = {
			"name",
			"body"
	};


	private Cursor cursor;

	static class StudentPagerItem{
		private String info;

		StudentPagerItem(String info) {
			this.info = info;
		}

		Fragment creteFragment(){
			return StudentInfoFragment.newInstance(info);
		}
	}

	private SlidingTabLayout mSlidingTabLayout;

	private ViewPager mViewPager;


	public static StudentFragment newInstance(int type) {
        StudentFragment fragment = new StudentFragment();
		Bundle args = new Bundle();
		args.putInt("TYPE", type);
		fragment.setArguments(args);
        return fragment;
    }
    public StudentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    final ContentResolver contentResolver = getActivity().getContentResolver();

	    int type = getArguments().getInt("TYPE");

		if (type == 0) {
			Uri uri = ContentProvider.createUri(InfoForStudent.class, null);
			cursor = contentResolver.query(uri, PROJECTION, null, null, null);
		} else{
			Uri uri = ContentProvider.createUri(Organization.class, null);
			cursor = contentResolver.query(uri, ORGPROJECTION, null, null, null);
		}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_student, container, false);
    }

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		mViewPager.setAdapter(new StudentFragmentPagerAdapter(getChildFragmentManager()));

		mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
		mSlidingTabLayout.setViewPager(mViewPager);
		mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.flat_dark_blue));
		mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return Color.parseColor("#FF6A00");
			}

			@Override
			public int getDividerColor(int position) {
				return Color.parseColor("#00000000");
			}
		});


	}

	class StudentFragmentPagerAdapter extends FragmentPagerAdapter{

		public StudentFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			cursor.moveToPosition(i);
			return StudentInfoFragment.newInstance(cursor.getString(1));
		}

		@Override
		public int getCount() {
			return cursor.getCount();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			cursor.moveToPosition(position);
			return cursor.getString(0);
		}
	}
}
