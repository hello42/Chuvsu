package com.ulop.student;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.ulop.chuvsu.app.R;
import com.ulop.models.InfoForStudent;
import com.ulop.util.TabsAdapter;

import in.uncod.android.bypass.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Student#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Student extends Fragment {

    private static int SCHOLAR_SHIP_SECTION = 0;
    private static int CAMPUS_SECTION = 1;
    private static int SINCE_SECTION = 2;
    private static int SPORT_SECTION = 3;
    private static int CULTURE_SECTION = 4;
    private static int TOUR_SECTION = 5;
    private static int HEALTH_SECTION = 6;

    TabsAdapter adapter = new TabsAdapter(new String[]{
            "Стипендия",
            "Общежитие",
            "Наука",
            "Спорт",
            "Культурные события",
            "Экскурсии",
            "Оздровление"
    }, new Integer[]{
            R.id.scholarship,
            R.id.campus,
            R.id.since,
            R.id.sport,
            R.id.culture_events,
            R.id.tour,
            R.id.health
    });

    Integer[] fragments = new Integer[]{
            R.id.scholarshipFragment,
            R.id.campusFragment,
            R.id.sinceFragment,
            R.id.sportFragment,
            R.id.cultureFragment,
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student, container, false);

        FragmentManager fragmentManager = getFragmentManager();

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        for (int i = 0; i < adapter.getCount(); i++) {
            tabSpec = tabHost.newTabSpec(adapter.getTagByID(i)).
                    setIndicator(adapter.getTitleByID(i)).
                    setContent(adapter.getResourceID(i));

            tabHost.addTab(tabSpec);

            fragmentManager.beginTransaction()
                    .replace(fragments[i], StudentInfoFragment.newInstance(i))
                    .commit();
        }

        return rootView;
    }


    public static class StudentInfoFragment extends Fragment {

        public static StudentInfoFragment newInstance(int infoType) {
            StudentInfoFragment fragment = new StudentInfoFragment();

            Bundle args = new Bundle();
            args.putInt("INFO_TYPE", infoType);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.text_view_layout, container, false);
            int newsType = getArguments().getInt("INFO_TYPE");
            InfoForStudent info = new Select()
                    .from(InfoForStudent.class)
                    .where("infoID = ?", newsType + 1)
                    .executeSingle();

            if (info.title != null) {
                ((TextView) rootView).setText(Html.fromHtml(info.body));
            }
            return rootView;
        }
    }
}
