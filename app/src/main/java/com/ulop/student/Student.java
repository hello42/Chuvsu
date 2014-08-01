package com.ulop.student;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.ulop.chuvsu.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Student#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Student extends Fragment {

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


        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        for (int i = 0; i < adapter.getCount(); i++) {
            tabSpec = tabHost.newTabSpec(adapter.getTagByID(i)).
                    setIndicator(adapter.getTitleByID(i)).
                    setContent(adapter.getResourceID(i));

            tabHost.addTab(tabSpec);
        }

        return rootView;
    }

    public class TabsAdapter{
        List<String> tList;
        List<Integer> vList;


        public TabsAdapter(String[] tabs, Integer[] views) {
            tList = new ArrayList<String>();
            vList = new ArrayList<Integer>();
            for (String e:tabs){
                tList.add(e);
            }

            for (Integer e:views){
                vList.add(e);
            }
        }

        public int getCount(){
            return tList.size();
        }

        public String getTitleByID(int id){
            return tList.get(id);
        }

        public String getTagByID(int id){
            return String.valueOf(tList.get(id).hashCode());
        }

        public Integer getResourceID(int id){
            return vList.get(id);
        }


    }

}
