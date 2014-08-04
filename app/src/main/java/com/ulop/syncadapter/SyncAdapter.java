package com.ulop.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.activeandroid.TableInfo;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.ulop.chuvsu.app.R;
import com.ulop.models.AbiturientNews;
import com.ulop.models.Address;
import com.ulop.models.Entry;
import com.ulop.models.Faculty;
import com.ulop.models.InfoForStudent;
import com.ulop.models.Organization;
import com.ulop.models.Phone;
import com.ulop.parsers.AbiturientNewsParser;
import com.ulop.parsers.AddressParser;
import com.ulop.parsers.FacultyInfoParser;
import com.ulop.parsers.FeedParser;
import com.ulop.parsers.InfoForStudentParser;
import com.ulop.parsers.OrganizationParser;
import com.ulop.parsers.PhoneParser;
import com.ulop.syncadapter.Info.InfoContract;
import com.ulop.syncadapter.accounts.AuthenticatorService;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ulop on 12.03.14.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
    private String ORGANIZATION_URL = getContext().getString(R.string.organization_url);
    private String FEED_URL = getContext().getString(R.string.feed_url);
    private String FACULTY_URL = getContext().getString(R.string.faculty_info_url);
    private String ABITNEWS_URL = getContext().getString(R.string.abiturient_news_url);
    private String PHONE_URL = getContext().getString(R.string.phones_url);
    private String ADDRESS_URL = getContext().getString(R.string.address_url);
    private String STUDENT_URL = getContext().getString(R.string.student_url);

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


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mContentResolver.setSyncAutomatically(AuthenticatorService.GetAccount(), InfoContract.CONTENT_AUTHORITY, true);


        //mContentResolver.re
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        //ActiveAndroid.initialize(context);

    }


    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        try {
            URL location = new URL(FEED_URL);
            InputStream stream = null;
            try {
                stream = downloadUrl(location);
                updateInfoForNews(stream, syncResult);

                location = new URL(FACULTY_URL);
                stream = downloadUrl(location);
                updateInfoForFaculty(stream, syncResult);

                location = new URL(ABITNEWS_URL);
                stream = downloadUrl(location);
                updateInfoForAbiturientNews(stream, syncResult);

                location = new URL(PHONE_URL);
                stream = downloadUrl(location);
                updateInfoForPhones(stream, syncResult);

                location = new URL(ADDRESS_URL);
                stream = downloadUrl(location);
                updateInfoForAddresses(stream, syncResult);

                location = new URL(STUDENT_URL);
                stream = downloadUrl(location);
                updateInfoForStudents(stream, syncResult);

                location = new URL(ORGANIZATION_URL);
                stream = downloadUrl(location);
                updateInfoForOrganizations(stream, syncResult);

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
        }
        Log.i(TAG, "Network synchronization complete");
        // ActiveAndroid.dispose();
    }

    /*
    private void updateNews(InputStream stream, SyncResult syncResult) throws IOException, RemoteException, OperationApplicationException {
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
    }
    */

    private InputStream downloadUrl(final URL url) {
        Log.i(TAG, "Streaming data from network: " + url);
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert conn != null;
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS /* milliseconds */);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS /* milliseconds */);
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
        // Starts the query
        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Плохой, плохой и ещё раз плохой код
    //Стыдно (x_x])
    private void updateInfoForFaculty(InputStream inputStream, SyncResult syncResult) {
        FacultyInfoParser fParser = new FacultyInfoParser();
        List<Faculty> facultyList = fParser.getAsModelList(inputStream);
        Log.i(TAG, "Get " + facultyList.size() + "faculty info");

        List<Faculty> currentList = new Select().from(Faculty.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local faculty");

        HashMap<String, Faculty> facultyHashMap = new HashMap<String, Faculty>();
        for (Faculty e : facultyList) {
            facultyHashMap.put(e.faculty_id, e);
        }

        new TableInfo(Faculty.class).getFields();

        for (int i = 0; i < currentList.size(); i++) {
            syncResult.stats.numEntries++;
            String fId = currentList.get(i).faculty_id;
            String fName = currentList.get(i).faculty_name;
            Faculty match = facultyHashMap.get(fId);
            if (match != null) {
                Uri uri = ContentProvider.createUri(Faculty.class, Long.valueOf(fId));
                if (match.faculty_name != null ||
                        match.faculty_name.equals(fName)) {
                    currentList.get(i).delete();
                    facultyHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                facultyHashMap.remove(fId);
            } else {
                new Delete().from(Faculty.class).
                        where("faculty_id = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (Faculty e : facultyHashMap.values()) {
            e.save();
        }
    }

    private void updateInfoForNews(InputStream inputStream, SyncResult syncResult) {
        FeedParser fParser = new FeedParser();
        List<Entry> newInfoList = fParser.getAsModelList(inputStream);
        Log.i(TAG, "Get " + newInfoList.size() + "feed info");

        List<Entry> currentList = new Select().from(Entry.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local feed");

        HashMap<String, Entry> newInfoHashMap = new HashMap<String, Entry>();
        for (Entry e : newInfoList) {
            newInfoHashMap.put(e.news_id, e);
        }

        new TableInfo(Faculty.class).getFields();

        for (int i = 0; i < currentList.size(); i++) {
            syncResult.stats.numEntries++;
            String fId = currentList.get(i).news_id;
            String fName = currentList.get(i).title;
            Entry match = newInfoHashMap.get(fId);
            if (match != null) {
                Uri uri = ContentProvider.createUri(Entry.class, Long.valueOf(fId));
                if (match.title != null ||
                        match.title.equals(fName)) {
                    currentList.get(i).delete();
                    newInfoHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                newInfoHashMap.remove(fId);
            } else {
                new Delete().from(Entry.class).
                        where("news_id = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (Entry e : newInfoHashMap.values()) {
            e.save();
        }
    }

    private void updateInfoForAbiturientNews(InputStream inputStream, SyncResult syncResult) {
        AbiturientNewsParser fParser = new AbiturientNewsParser();
        List<AbiturientNews> newInfoList = fParser.getAsModelList(inputStream);
        Log.i(TAG, "Get " + newInfoList.size() + "AbiturientNews info");

        List<AbiturientNews> currentList = new Select().from(AbiturientNews.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local AbiturientNews");

        HashMap<Integer, AbiturientNews> newInfoHashMap = new HashMap<Integer, AbiturientNews>();

        for (AbiturientNews e : newInfoList) {
            newInfoHashMap.put(e.news_id, e);
        }

        new TableInfo(Faculty.class).getFields();

        for (int i = 0; i < currentList.size(); i++) {
            syncResult.stats.numEntries++;
            Integer fId = currentList.get(i).news_id;
            String fName = currentList.get(i).title;
            AbiturientNews match = newInfoHashMap.get(fId);
            if (match != null) {
                Uri uri = ContentProvider.createUri(AbiturientNews.class, Long.valueOf(fId));
                if (match.title != null ||
                        match.title.equals(fName)) {
                    currentList.get(i).delete();
                    newInfoHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                newInfoHashMap.remove(fId);
            } else {
                new Delete().from(AbiturientNews.class).
                        where("news_id = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (AbiturientNews e : newInfoHashMap.values()) {
            e.save();
        }
    }

    private void updateInfoForPhones(InputStream inputStream, SyncResult syncResult) {
        PhoneParser fParser = new PhoneParser();
        List<Phone> newInfoList = fParser.getAsModelList(inputStream);
        Log.i(TAG, "Get " + newInfoList.size() + "phone info");

        List<Phone> currentList = new Select().from(Phone.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local phone");

        HashMap<Integer, Phone> newInfoHashMap = new HashMap<Integer, Phone>();
        for (Phone e : newInfoList) {
            newInfoHashMap.put(e.phone_id, e);
        }


        for (int i = 0; i < currentList.size(); i++) {
            syncResult.stats.numEntries++;
            Integer fId = currentList.get(i).phone_id;
            String fName = currentList.get(i).title;
            Phone match = newInfoHashMap.get(fId);
            if (match != null) {
                if (match.title != null ||
                        match.title.equals(fName)) {
                    currentList.get(i).delete();
                    newInfoHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                newInfoHashMap.remove(fId);
            } else {
                new Delete().from(Phone.class).
                        where("phone_id = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (Phone e : newInfoHashMap.values()) {
            e.save();
        }
    }

    private void updateInfoForAddresses(InputStream inputStream, SyncResult syncResult) {
        AddressParser fParser = new AddressParser();
        List<Address> newInfoList = fParser.getAsModelList(inputStream);
        Log.i(TAG, "Get " + newInfoList.size() + "address info");

        List<Address> currentList = new Select().from(Address.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local address");

        HashMap<Integer, Address> newInfoHashMap = new HashMap<Integer, Address>();
        for (Address e : newInfoList) {
            newInfoHashMap.put(e.address_id, e);
        }


        for (int i = 0; i < currentList.size(); i++) {
            syncResult.stats.numEntries++;
            Integer fId = currentList.get(i).address_id;
            String fName = currentList.get(i).title;
            Address match = newInfoHashMap.get(fId);
            if (match != null) {
                if (match.title != null ||
                        match.title.equals(fName)) {
                    currentList.get(i).delete();
                    newInfoHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                newInfoHashMap.remove(fId);
            } else {
                new Delete().from(Address.class).
                        where("address_id = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (Address e : newInfoHashMap.values()) {
            e.save();
        }
    }

    private void updateInfoForStudents(InputStream inputStream, SyncResult syncResult) {
        InfoForStudentParser fParser = new InfoForStudentParser();
        List<InfoForStudent> newInfoList = fParser.getAsModelList(inputStream);
        Log.i(TAG, "Get " + newInfoList.size() + "info for student's");

        List<InfoForStudent> currentList = new Select().from(InfoForStudent.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local student info");

        HashMap<Integer, InfoForStudent> newInfoHashMap = new HashMap<Integer, InfoForStudent>();
        for (InfoForStudent e : newInfoList) {
            newInfoHashMap.put(e.infoID, e);
        }


        for (InfoForStudent currentItem : currentList) {
            syncResult.stats.numEntries++;
            Integer fId = currentItem.infoID;
            String fName = currentItem.title;
            InfoForStudent match = newInfoHashMap.get(fId);
            if (match != null) {
                if (match.title != null ||
                        match.title.equals(fName)) {
                    currentItem.delete();
                    newInfoHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                newInfoHashMap.remove(fId);
            } else {
                new Delete().from(InfoForStudent.class).
                        where("infoID = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (InfoForStudent e : newInfoHashMap.values()) {
            e.save();
        }
    }

    private void updateInfoForOrganizations(InputStream inputStream, SyncResult syncResult) {
        OrganizationParser fParser = new OrganizationParser();
        List<Organization> newInfoList = fParser.getAsModelList(inputStream);
        Log.i(TAG, "Get " + newInfoList.size() + "info about organization's");

        List<Organization> currentList = new Select().from(Organization.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local student info");

        HashMap<Integer, Organization> newInfoHashMap = new HashMap<Integer, Organization>();
        for (Organization e : newInfoList) {
            newInfoHashMap.put(e.organizationID, e);
        }


        for (Organization currentItem : currentList) {
            syncResult.stats.numEntries++;
            Integer fId = currentItem.organizationID;
            String fName = currentItem.name;
            Organization match = newInfoHashMap.get(fId);
            if (match != null) {
                if (match.name != null ||
                        match.name.equals(fName)) {
                    currentItem.delete();
                    newInfoHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                newInfoHashMap.remove(fId);
            } else {
                new Delete().from(Organization.class).
                        where("organizationID = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (Organization e : newInfoHashMap.values()) {
            e.save();
        }
    }
}
