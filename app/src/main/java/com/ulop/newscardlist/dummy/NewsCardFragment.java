package com.ulop.newscardlist.dummy;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ulop.chuvsu.app.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.ulop.newscardlist.dummy.NewsCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NewsCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsCardFragment newInstance(String param1, String param2) {
        NewsCardFragment fragment = new NewsCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public NewsCardFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_of_news, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listView1);
        NewsCardAdapter cardAdapter = new NewsCardAdapter(rootView.getContext());
        listView.setAdapter(cardAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), NewsCardAdapter.objects.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }


}
