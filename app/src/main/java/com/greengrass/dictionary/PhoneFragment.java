package com.greengrass.dictionary;



import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.Phone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PhoneFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private SimpleCursorAdapter mAdapter;

    private static final String[] FROM_COLUMNS = new String[]{
            "title",
            "number"
    };

    private static final int[] TO_FIELDS = new int[]{
            android.R.id.text1,
            android.R.id.text2
    };

    public static PhoneFragment newInstance() {
        PhoneFragment fragment = new PhoneFragment();
        return fragment;
    }
    public PhoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_phone, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.listView);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),       // Current context
                android.R.layout.simple_list_item_2,  // Layout for individual rows
                null,                // Cursor
                FROM_COLUMNS,        // Cursor columns to use
                TO_FIELDS,           // Layout fields to use
                0                    // No flags
        );

        list.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Phone phone = new Select()
                        .from(Phone.class)
                        .where("phone_id = ?", i + 1)
                        .executeSingle();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone.phone_number));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),  // Context
                ContentProvider.createUri(Phone.class, null), // URI
                null,                // Projection
                null,                           // Selection
                null,                           // Selection args
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }
}
