package com.ulop.faculty;

import android.accounts.Account;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Select;
import com.ulop.chuvsu.app.R;
import com.ulop.models.Faculty;
import com.ulop.syncadapter.Info.InfoContract;
import com.ulop.syncadapter.accounts.AuthenticatorService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class FacultyFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "NewsCardFragment";

    private SimpleCursorAdapter mAdapter;

    private Object mSyncObserverHandle;

    private Menu mOptionsMenu;
    ValueAnimator mAnimator;


    private static final int COLUMN_LOGO = 4;
    private static final String[] FROM_COLUMNS = new String[]{
            InfoContract.Faculty.COLUMN_NAME_FACULTY_NAME,
            InfoContract.Faculty.COLUMN_NAME_INFO,
            InfoContract.Faculty.COLUMN_NAME_LOGO
    };

    private static final int[] TO_FIELDS = new int[]{
            R.id.facultyName,
            R.id.facultyInfo,
            R.id.facultyLogo};


    // TODO: Rename and change types of parameters
    public static FacultyFragment newInstance() {
        FacultyFragment fragment = new FacultyFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FacultyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faculty_list, container, false);

        mAdapter = new SimpleCursorAdapter(
                getActivity(),       // Current context
                R.layout.card_of_faculty_withaut_info,  // Layout for individual rows
                null,                // Cursor
                FROM_COLUMNS,        // Cursor columns to use
                TO_FIELDS,           // Layout fields to use
                0                    // No flags
        );


        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(final View view, Cursor cursor, int columnIndex) {
                if (columnIndex == COLUMN_LOGO) {
                    String str = cursor.getString(columnIndex);
                    //imageLoader.displayImage(str, ((ImageView) view), options);
                    Picasso.with(getActivity()).load(str).
                            resize(96, 96).
                            into((ImageView) view);
                    return true;
                } else if (columnIndex == 3) {
                    ((TextView) view).setText(cursor.getString(columnIndex));
                    //view.setVisibility(View.GONE);
                    view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);
                            view.setVisibility(View.GONE);

                            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            view.measure(widthSpec, heightSpec);

                            mAnimator = slideAnimator(0, view.getMeasuredHeight(), view);
                            mAnimator.start();
                            return true;
                        }
                    });
                    return true;
                } else {
                    // Let SimpleCursorAdapter handle other fields automatically
                    return false;
                }

            }
        });

        //mAdapter.set


        AbsListView listView = (AbsListView) view.findViewById(R.id.fctlist);

        listView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(i);
                String url = cursor.getString(5);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView fInfo = (TextView) view.findViewById(R.id.facultyInfo);
                if (fInfo.getVisibility() == View.GONE) {
                  // view.setVisibility(View.VISIBLE);
                    expand(fInfo);
                } else {
                    //view.setVisibility(View.VISIBLE);
                    collapse(fInfo);
                }
                return true;
            }
        });


        return view;
    }

    private void collapse(final View view) {
        int finalHeight = view.getHeight();
        Log.i(TAG, "I'm must collapse! But... " + finalHeight);

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, view);


        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }

        });
        mAnimator.start();
    }

    private void expand(View view) {
        //set Visible
        view.setVisibility(View.VISIBLE);
        Log.i(TAG, "I'm must expand! But...");
/*
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

*/
        mAnimator.start();

    }

    private ValueAnimator slideAnimator(int start, int end, final View view) {
        ValueAnimator animator = new ValueAnimator().ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Cursor loader create");
        Uri uri = ContentProvider.createUri(Faculty.class, null);
        CursorLoader c = new CursorLoader(getActivity(),  // Context
                uri, // URI
                null,                // Projection
                null,                           // Selection
                null,                           // Selection args
                null);

        return c;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    void setRefreshActionButtonState(boolean refreshing) {
        if (mOptionsMenu == null) {
            return;
        }

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }

    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = AuthenticatorService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        setRefreshActionButtonState(false);
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, InfoContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, InfoContract.CONTENT_AUTHORITY);
                    setRefreshActionButtonState(syncActive || syncPending);
                }
            });
        }
    };


}
