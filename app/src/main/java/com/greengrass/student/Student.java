package com.greengrass.student;



import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.InfoForStudent;
import com.greengrass.util.TabsAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Student#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Student extends Fragment {

	private TabHost tabHost;


    TabsAdapter adapter = new TabsAdapter(new String[]{
            "Стипендия",
            "Общежитие",
		    "Учебный процесс",
		    "Экскурсии",
            "Оздоровление"
    }, new Integer[]{
            R.id.scholarship,
            R.id.campus,
		    R.id.education,
            R.id.tour,
            R.id.health
    });

    Integer[] fragments = new Integer[]{
            R.id.scholarshipFragment,
            R.id.campusFragment,
		    R.id.educationFragment,
            R.id.tourFragment,
            R.id.healthFragment
    };

    // TODO: Rename and change types and number of parameters
    public static Student newInstance() {
        Student fragment = new Student();
        return fragment;
    }

    public Student() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_test, container, false);
/*
	    getActivity()
			    .getActionBar()
			    .*/

        FragmentManager fragmentManager = getFragmentManager();

        tabHost = (TabHost) rootView.findViewById(R.id.tabHost);
	    final View[] tabContent = new View[1];

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        for (int i = 0; i < adapter.getCount(); i++) {
            tabSpec = tabHost.newTabSpec(adapter.getTagByID(i)).
                    setIndicator(adapter.getTitleByID(i)).
                    setContent(new TabHost.TabContentFactory() {
	                    @Override
	                    public View createTabContent(String s) {
		                    tabContent[0] = getLayoutInflater(savedInstanceState).inflate(R.layout.student_studen_layout, container, false);
	                        return tabContent[0];
	                     }
                    });


            tabHost.addTab(tabSpec);
			tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundResource(R.drawable.chuvsu_tab_indicator);
	        ((TextView) tabHost.getTabWidget()
			        .getChildAt(i).findViewById(android.R.id.title))
			        .setTextColor(Color.parseColor("#ffffff"));

	        ((TextView) tabHost.getTabWidget()
			        .getChildAt(i).findViewById(android.R.id.title))
					.setPadding(16,16,16,16);


	        fragmentManager.beginTransaction()
                    .replace(tabContent[0].getId(), StudentInfoFragment.newInstance(adapter.getTitleByID(i)))
                    .commit();
        }

        return rootView;
    }





}
