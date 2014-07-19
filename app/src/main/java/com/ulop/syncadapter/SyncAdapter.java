package com.ulop.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.ulop.chuvsu.app.FacultyInfoParser;
import com.ulop.chuvsu.app.FeedParser;
import com.ulop.faculty.FacultyContent;
import com.ulop.newscardlist.dummy.NewsCardAdapter;
import com.ulop.syncadapter.Info.InfoContract;
import com.ulop.syncadapter.accounts.AuthenticatorService;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ulop on 12.03.14.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    private static final String FEED_URL = "http://chuvsu.vtrave.com/news/last.json";
    private static final String FACULTY_URL = "http://chuvsu.vtrave.com/facults.json";

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    private static final String[] PROJECTION = new String[] {
            InfoContract.Entry._ID,
            InfoContract.Entry.COLUMN_NAME_NEWS_ID,
            InfoContract.Entry.COLUMN_NAME_TITLE,
            InfoContract.Entry.COLUMN_NAME_CONTENT,
            InfoContract.Entry.COLUMN_NAME_PUBLISHED,
            InfoContract.Entry.COLUMN_NAME_IMAGE};

    // Constants representing column positions from PROJECTION.
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_NEWS_ID = 1;
    private static final int COLUMN_TITLE = 2;
    private static final int COLUMN_CONTENT = 3;
    private static final int COLUMN_PUBLISHED = 4;
    private static final int COLUMN_IMAGE = 5;

    //for faculties
    private static final String[] PROJECTION_FCT = new String[] {
            InfoContract.Faculty._ID,
            InfoContract.Faculty.COLUMN_NAME_FACULTY_NAME,
            InfoContract.Faculty.COLUMN_NAME_LOGO,
            InfoContract.Faculty.COLUMN_NAME_URL,
            InfoContract.Faculty.COLUMN_NAME_INFO};

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID_FCT = 0;
    private static final int COLUMN_FCT_ID = 1;
    private static final int COLUMN_NAME = 1;
    private static final int COLUMN_INFO = 4;
    private static final int COLUMN_URL = 3;
    private static final int COLUMN_LOGO = 2;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        ContentResolver.setSyncAutomatically(AuthenticatorService.GetAccount(), InfoContract.CONTENT_AUTHORITY, true);

        //mContentResolver.re
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        try {


            final URL location = new URL(FEED_URL);
            InputStream stream = null;


            try {
                Log.i(TAG, "Streaming data from network: " + location);
                stream = downloadUrl(location);
                updateLocalFeedData(stream, syncResult);

                //stream.close();

                //Log.i(TAG, "Streaming data from network: " + fctLocation);
                //stream = downloadUrl(fctLocation);
                //updateLocalFacultyData(stream, syncResult);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }

            }
        } catch (MalformedURLException e) {
            Log.wtf(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        }  catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(TAG, "Network synchronization complete");
    }

    void updateLocalFeedData(final InputStream stream, final SyncResult syncResult)
            throws IOException, RemoteException,
            OperationApplicationException, ParseException {
        final FeedParser feedParser = new FeedParser();
        //final ContentResolver contentResolver = getContext().getContentResolver();

        Log.i(TAG, "Parsing stream as News");
        final ArrayList<NewsCardAdapter.NewsCard> newsCards = feedParser.parse(stream);
        Log.i(TAG, "Parsing complete. Found " + newsCards.size() + " news");


        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, NewsCardAdapter.NewsCard> entryMap = new HashMap<String, NewsCardAdapter.NewsCard>();
        for (NewsCardAdapter.NewsCard e : newsCards) {
            entryMap.put(e.id, e);
        }

        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = InfoContract.Entry.CONTENT_URI; // Get all entries

        Log.i(TAG, "Will work with " + uri);
        String sortOrder = InfoContract.Entry.COLUMN_NAME_NEWS_ID;
        Cursor c = mContentResolver.query(uri, PROJECTION, null, null, sortOrder);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

        // Find stale data
        int id;
        String entryId;
        String title;
        String link;
        String published;
        String image;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID);
            entryId = c.getString(COLUMN_NEWS_ID);
            title = c.getString(COLUMN_TITLE);
            published = c.getString(COLUMN_PUBLISHED);
            link = c.getString(COLUMN_CONTENT);
            image = c.getString(COLUMN_IMAGE);
            NewsCardAdapter.NewsCard match = entryMap.get(entryId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(entryId);
                // Check to see if the entry needs to be updated
                Uri existingUri = InfoContract.Entry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                if ((match.title != null && !match.title.equals(title)) ||
                        (match.content != null && !match.content.equals(link)) ||
                        (!match.publicTime.equals(published))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(InfoContract.Entry.COLUMN_NAME_TITLE, title)
                            .withValue(InfoContract.Entry.COLUMN_NAME_CONTENT, link)
                            .withValue(InfoContract.Entry.COLUMN_NAME_PUBLISHED, published)
                            .withValue(InfoContract.Entry.COLUMN_NAME_IMAGE, image)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = InfoContract.Entry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (NewsCardAdapter.NewsCard e : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
            batch.add(ContentProviderOperation.newInsert(InfoContract.Entry.CONTENT_URI)
                    .withValue(InfoContract.Entry.COLUMN_NAME_NEWS_ID, e.id)
                    .withValue(InfoContract.Entry.COLUMN_NAME_TITLE, e.title)
                    .withValue(InfoContract.Entry.COLUMN_NAME_CONTENT, e.content)
                    .withValue(InfoContract.Entry.COLUMN_NAME_PUBLISHED, e.publicTime)
                    .withValue(InfoContract.Entry.COLUMN_NAME_IMAGE, e.image)
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(InfoContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(InfoContract.Entry.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.


        final FacultyInfoParser fctParser = new FacultyInfoParser();
        final ContentResolver contentResolver = getContext().getContentResolver();

        Log.i(TAG, "Parsing stream as Atom faculty");
        final ArrayList<FacultyContent.FacultyItem> fctCards = fctParser.parse(downloadUrl(new URL(FACULTY_URL)));
        Log.i(TAG, "Parsing complete. Found " + fctCards.size() + " faculties");


        batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, FacultyContent.FacultyItem> facultyItemHashMap = new HashMap<String, FacultyContent.FacultyItem>();
        for (FacultyContent.FacultyItem e : fctCards) {
            facultyItemHashMap.put(e.id, e);
        }

        // Get list of all items
        Log.i(TAG, "Fetching local faculties for merge");
        uri = InfoContract.Faculty.CONTENT_URI; // Get all entries
        Log.i(TAG, "Will work with " + uri + " " + contentResolver);

        sortOrder = InfoContract.Faculty.COLUMN_NAME_FACULTY_ID;
        //c = mContentResolver.query(uri, PROJECTION_FCT, null, null, sortOrder);
        c = getContext().getContentResolver().query(Uri.parse("content://com.ulop.syncadapter/faculties"), PROJECTION_FCT, null, null, sortOrder);
        Log.i(TAG, "I'm get cursor. " + c.toString());

        Log.i(TAG, "Found " + c.getCount() + " local faculties. Computing merge solution...");

        // Find stale data

        String fctId;
        String name;
        String url;
        String info;
        String logo;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID_FCT);
            fctId = c.getString(COLUMN_FCT_ID);
            name = c.getString(COLUMN_NAME);
            info = c.getString(COLUMN_INFO);
            url = c.getString(COLUMN_URL);
            logo = c.getString(COLUMN_LOGO);
            FacultyContent.FacultyItem match = facultyItemHashMap.get(fctId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                facultyItemHashMap.remove(fctId);
                // Check to see if the entry needs to be updated
                Uri existingUri = InfoContract.Faculty.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Existing uri  " + existingUri);

                if ((match.fctName != null && !match.fctName.equals(name)) ||
                        (match.content != null && !match.content.equals(info)) ||
                        (!match.link.equals(url))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_NAME, name)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_ID, fctId)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_INFO, info)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_URL, url)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_LOGO, logo)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = InfoContract.Faculty.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (FacultyContent.FacultyItem e : facultyItemHashMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
            batch.add(ContentProviderOperation.newInsert(InfoContract.Faculty.CONTENT_URI)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_ID, e.id)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_NAME, e.fctName)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_INFO, e.content)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_URL, e.link)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_LOGO, e.logo)
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(InfoContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                InfoContract.Faculty.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network


    }

    void updateLocalFacultyData(final InputStream stream, final SyncResult syncResult)
            throws IOException, RemoteException,
            OperationApplicationException, ParseException {
        final FacultyInfoParser fctParser = new FacultyInfoParser();
        final ContentResolver contentResolver = getContext().getContentResolver();

        Log.i(TAG, "Parsing stream as Atom faculty");
        final ArrayList<FacultyContent.FacultyItem> fctCards = fctParser.parse(stream);
        Log.i(TAG, "Parsing complete. Found " + fctCards.size() + " faculties");


        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, FacultyContent.FacultyItem> facultyItemHashMap = new HashMap<String, FacultyContent.FacultyItem>();
        for (FacultyContent.FacultyItem e : fctCards) {
            facultyItemHashMap.put(e.id, e);
        }

        // Get list of all items
        Log.i(TAG, "Fetching local faculties for merge");
        Uri uri = InfoContract.Faculty.CONTENT_URI; // Get all entries
        Log.i(TAG, "Will work with " + uri + " " + contentResolver);

        String sortOrder = InfoContract.Faculty.COLUMN_NAME_FACULTY_ID;
        Cursor c = contentResolver.query(uri, PROJECTION_FCT, null, null, sortOrder);
        Log.i(TAG, "I'm get cursor. " + c.toString());

        Log.i(TAG, "Found " + c.getCount() + " local faculties. Computing merge solution...");

        // Find stale data
        int id;
        String fctId;
        String name;
        String url;
        String info;
        String logo;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getInt(COLUMN_ID_FCT);
            fctId = c.getString(COLUMN_FCT_ID);
            name = c.getString(COLUMN_NAME);
            info = c.getString(COLUMN_INFO);
            url = c.getString(COLUMN_URL);
            logo = c.getString(COLUMN_LOGO);
            FacultyContent.FacultyItem match = facultyItemHashMap.get(fctId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                facultyItemHashMap.remove(fctId);
                // Check to see if the entry needs to be updated
                Uri existingUri = InfoContract.Faculty.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Existing uri  " + existingUri);

                if ((match.fctName != null && !match.fctName.equals(name)) ||
                        (match.content != null && !match.content.equals(info)) ||
                        (!match.link.equals(url))) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_NAME, name)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_ID, fctId)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_INFO, info)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_URL, url)
                            .withValue(InfoContract.Faculty.COLUMN_NAME_LOGO, logo)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = InfoContract.Faculty.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Add new items
        for (FacultyContent.FacultyItem e : facultyItemHashMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
            batch.add(ContentProviderOperation.newInsert(InfoContract.Faculty.CONTENT_URI)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_ID, e.id)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_FACULTY_NAME, e.fctName)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_INFO, e.content)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_URL, e.link)
                    .withValue(InfoContract.Faculty.COLUMN_NAME_LOGO, e.logo)
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(InfoContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                InfoContract.Faculty.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }

    private InputStream downloadUrl(final URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        return conn.getInputStream();
    }
}
