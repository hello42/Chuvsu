package com.ulop.dictionary;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;
import com.squareup.picasso.Picasso;
import com.ulop.chuvsu.app.R;
import com.ulop.models.Address;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddressFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final Integer COLUMN_IMAGE = 4;
    private static final String TAG = "ADDRESS";

    private OnFragmentInteractionListener mListener;
    private SimpleCursorAdapter mAdapter;

    private static final String[] FROM_COLUMNS = new String[]{
            "title",
            "image",
            "address"
    };

    private static final int[] TO_FIELDS = new int[]{
            R.id.placeName,
            R.id.placeImage,
            R.id.placeDescription};

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance() {
        AddressFragment fragment = new AddressFragment();
        return fragment;
    }
    public AddressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_address, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.placeList);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),       // Current context
                R.layout.card_of_address,  // Layout for individual rows
                null,                // Cursor
                FROM_COLUMNS,        // Cursor columns to use
                TO_FIELDS,           // Layout fields to use
                0                    // No flags
        );

        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == COLUMN_IMAGE) {
                    String str = cursor.getString(columnIndex);
                    //final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.loading);

                    Picasso.with(getActivity()).
                            load(str).
                            into((ImageView) view);
                    return true;
                } else {
                    return false;
                }
            }
        });

        list.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "i = " + i);               
                Address address;
                address = new Select()
                        .from(Address.class)
                        .where("address_id = ?", i + 1)
                        .executeSingle();
                String labelLocation = address.title;

                String[] coordinate = address.coordinates.split(" ");
                String mLatitude = coordinate[1];
                String mLongitude = coordinate[0];

                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:<" +
                                mLatitude  + ">,<" +
                                mLongitude + ">?q=<" +
                                mLatitude  + ">,<" +
                                mLongitude + ">(" +
                                labelLocation + ")"));

                startActivity(intent);
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),  // Context
                ContentProvider.createUri(Address.class, null), // URI
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
