package com.ulop.dictionary;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;


import com.ulop.chuvsu.app.R;
import com.ulop.models.Address;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dictionary#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Dictionary extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(ARG_PARAM1, "onViewStateRestored");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(ARG_PARAM1, "onPause");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(ARG_PARAM1, "onViewCreated");

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dictionary.
     */
    // TODO: Rename and change types and number of parameters
    public static Dictionary newInstance(String param1, String param2) {
        Dictionary fragment = new Dictionary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public Dictionary() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("yet", true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            Boolean b = savedInstanceState.getBoolean("yet");
            Log.i("DICT", String.valueOf(b));
        }

        FragmentManager fragmentManager = getFragmentManager();

        final View rootView = inflater.inflate(R.layout.fragment_dictionary, container, false);

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        // создаем вкладку и указываем тег
        tabSpec = tabHost.newTabSpec("phone");
        // название вкладки
        tabSpec.setIndicator("Телефонные номера");
        // указываем id компонента из FrameLayout, он и стаенет содержимым
        tabSpec.setContent(R.id.tab1);
        // добавляем в корневой элемент
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("address").
                setIndicator("Адреса").
                setContent(R.id.tab2);

        tabHost.addTab(tabSpec);

        fragmentManager.beginTransaction()
                .replace(R.id.fragment1, PhoneFragment.newInstance("", ""))
                .commit();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment2, AddressFragment.newInstance())
                .commit();

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {

            }
        });

        return rootView;
    }



}
