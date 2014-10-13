package com.greengrass.syncadapter;

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
import android.util.JsonReader;
import android.util.Log;

import com.activeandroid.TableInfo;
import com.activeandroid.content.ContentProvider;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.greengrass.chuvsu.app.R;
import com.greengrass.models.AbiturientNews;
import com.greengrass.models.Address;
import com.greengrass.models.Announcement;
import com.greengrass.models.Entry;
import com.greengrass.models.Faculty;
import com.greengrass.models.InfoForStudent;
import com.greengrass.models.Organization;
import com.greengrass.models.Phone;
import com.greengrass.models.StaticInfo;
import com.greengrass.parsers.AbiturientNewsParser;
import com.greengrass.parsers.AddressParser;
import com.greengrass.parsers.AnnouncementParser;
import com.greengrass.parsers.BaseParser;
import com.greengrass.parsers.FacultyInfoParser;
import com.greengrass.parsers.FeedParser;
import com.greengrass.parsers.InfoForStudentParser;
import com.greengrass.parsers.OrganizationParser;
import com.greengrass.parsers.PhoneParser;
import com.greengrass.parsers.StaticInfoParser;
import com.greengrass.syncadapter.Info.InfoContract;
import com.greengrass.syncadapter.accounts.AuthenticatorService;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by greengrass on 12.03.14.
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";
	private String ALL_INFO_URL = getContext().getString(R.string.all_info_url);
	private String PAGES_URL = getContext().getString(R.string.pages_url);
    private String ORGANIZATION_URL = getContext().getString(R.string.organization_url);
    private String FEED_URL = getContext().getString(R.string.feed_url);
    private String FACULTY_URL = getContext().getString(R.string.faculty_info_url);
    private String ABITNEWS_URL = getContext().getString(R.string.abiturient_news_url);
    private String PHONE_URL = getContext().getString(R.string.phones_url);
    private String ADDRESS_URL = getContext().getString(R.string.address_url);
    private String STUDENT_URL = getContext().getString(R.string.student_url);
	private String ANNOUNCEMENT_URL = getContext().getString(R.string.announcement_url);

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 40000;  // 10 seconds

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
	public void onPerformSync(Account account, Bundle extras, String authority,
	                          ContentProviderClient provider, SyncResult syncResult) {
		Log.i(TAG, "Beginning network synchronization");
		try {
			final URL location = new URL(ALL_INFO_URL);
			String response = null;

			try {
				Log.i(TAG, "Streaming data from network: " + location);
				response = downloadUrl(location);
				updateInfoForAll(response, syncResult);
				// Makes sure that the InputStream is closed after the app is
				// finished using it.
			} finally {
			}
		} catch (JSONException e){
			Log.e(TAG, "Failed parse JSON", e);
			syncResult.stats.numParseExceptions++;
		}
		catch (MalformedURLException e) {
			Log.e(TAG, "Feed URL is malformed", e);
			syncResult.stats.numParseExceptions++;
			return;
		} catch (IOException e) {
			Log.e(TAG, "Error reading from network: " + e.toString());
			syncResult.stats.numIoExceptions++;
			return;
		} catch (ParseException e) {
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

	private void updateInfoForAll(String stream, SyncResult syncResult)throws IOException, RemoteException,
			OperationApplicationException, ParseException, JSONException {
		BaseParser parser = new BaseParser();
		JSONObject obj = new JSONObject(stream);

		updateInfoForNews(obj.getJSONArray("news"), syncResult);
		updateAnouncementInfo(obj.getJSONArray("annonces"), syncResult);
		updateInfoForPages(obj.getJSONArray("pages"), syncResult);
		updateInfoForOrganizations(obj.getJSONArray("organisations"), syncResult);
		updateInfoForStudents(obj.getJSONArray("students"), syncResult);
		updateInfoForFaculty(obj.getJSONArray("facults"), syncResult);
		updateInfoForPhones(obj.getJSONArray("phones"), syncResult);
		updateInfoForAddresses(obj.getJSONArray("address"), syncResult);

	}


    /*
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


                location = new URL(PAGES_URL);
                stream = downloadUrl(location);
                updateInfoForPages(stream, syncResult);

	            location = new URL(ANNOUNCEMENT_URL);
	            stream = downloadUrl(location);
	            updateAnouncementInfo(stream, syncResult);
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
    }*/



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

    private String downloadUrl(final URL url) throws IOException {
        Log.i(TAG, "Streaming data from network: " + url);
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(NET_READ_TIMEOUT_MILLIS);
        conn.setConnectTimeout(NET_CONNECT_TIMEOUT_MILLIS);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

/*
	    OkHttpClient client = new OkHttpClient();
	    Request request = new Request.Builder()
			    .url(url)
			    .build();

	    Response response = client.newCall(request).execute();
*/


	    return new BufferedReader(new InputStreamReader(conn.getInputStream())).readLine();
    }

    //Плохой, плохой и ещё раз плохой код
    //Стыдно (x_x)
    private void updateInfoForFaculty(JSONArray jArray, SyncResult syncResult) throws IOException, RemoteException,
            OperationApplicationException, ParseException{
        FacultyInfoParser fParser = new FacultyInfoParser();
        List<Faculty> facultyList = fParser.getAsModelList(jArray);
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

    private void updateInfoForNews(JSONArray jArray, SyncResult syncResult) {
        FeedParser fParser = new FeedParser();
        List<Entry> newInfoList = fParser.getAsModelList(jArray);
        Log.i(TAG, "Get " + newInfoList.size() + "feed info");

        List<Entry> currentList = new Select().from(Entry.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local feed");

        HashMap<String, Entry> newInfoHashMap = new HashMap<String, Entry>();
        for (Entry e : newInfoList) {
            newInfoHashMap.put(e.news_id, e);
        }

//        new TableInfo(null).;

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

    private void updateInfoForAbiturientNews(JSONArray jArray, SyncResult syncResult) {
        AbiturientNewsParser fParser = new AbiturientNewsParser();
        List<AbiturientNews> newInfoList = fParser.getAsModelList(jArray);
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

    private void updateInfoForPhones(JSONArray jArray, SyncResult syncResult) {
        PhoneParser fParser = new PhoneParser();
        List<Phone> newInfoList = fParser.getAsModelList(jArray);
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

    private void updateInfoForAddresses(JSONArray jArray, SyncResult syncResult) {
        AddressParser fParser = new AddressParser();
        List<Address> newInfoList = fParser.getAsModelList(jArray);
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

    private void updateInfoForStudents(JSONArray jArray, SyncResult syncResult) {
        InfoForStudentParser fParser = new InfoForStudentParser();
        List<InfoForStudent> newInfoList = fParser.getAsModelList(jArray);
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

    private void updateInfoForOrganizations(JSONArray jArray, SyncResult syncResult) {
        OrganizationParser fParser = new OrganizationParser();
        List<Organization> newInfoList = fParser.getAsModelList(jArray);
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

    private void updateInfoForPages(JSONArray jArray, SyncResult syncResult) {
        StaticInfoParser fParser = new StaticInfoParser();
        List<StaticInfo> newInfoList = fParser.getAsModelList(jArray);
        Log.i(TAG, "Get " + newInfoList.size() + " static info");

        List<StaticInfo> currentList = new Select().from(StaticInfo.class).execute();
        Log.i(TAG, "Found " + currentList.size() + " local static info");

        HashMap<Integer, StaticInfo> newInfoHashMap = new HashMap<Integer, StaticInfo>();
        for (StaticInfo e : newInfoList) {
            newInfoHashMap.put(e.page_id, e);
        }


        for (StaticInfo currentItem : currentList) {
            syncResult.stats.numEntries++;
            Integer fId = currentItem.page_id;
            String fName = currentItem.title;
            StaticInfo match = newInfoHashMap.get(fId);
            if (match != null) {
                if (match.title != null ||
                        match.title.equals(fName)) {
                    currentItem.delete();
                    newInfoHashMap.get(fId).save();
                    syncResult.stats.numUpdates++;

                }
                newInfoHashMap.remove(fId);
            } else {
                new Delete().from(StaticInfo.class).
                        where("page_id = ?", fId).execute();
                syncResult.stats.numDeletes++;
            }
        }

        for (StaticInfo e : newInfoHashMap.values()) {
            e.save();
        }
    }

	private void updateAnouncementInfo(JSONArray jArray, SyncResult syncResult) {
		AnnouncementParser fParser = new AnnouncementParser();
		List<Announcement> newInfoList = fParser.getAsModelList(jArray);
		Log.i(TAG, "Get " + newInfoList.size() + " announcement info");

		List<Announcement> currentList = new Select().from(Announcement.class).execute();
		Log.i(TAG, "Found " + currentList.size() + " local static info");

		HashMap<Integer, Announcement> newInfoHashMap = new HashMap<Integer, Announcement>();
		for (Announcement e : newInfoList) {
			newInfoHashMap.put(e.announcement_id, e);
		}


		for (Announcement currentItem : currentList) {
			syncResult.stats.numEntries++;
			Integer fId = currentItem.announcement_id;
			String fName = currentItem.title;
			Announcement match = newInfoHashMap.get(fId);
			if (match != null) {
				if (match.title != null ||
						match.title.equals(fName)) {
					currentItem.delete();
					newInfoHashMap.get(fId).save();
					syncResult.stats.numUpdates++;

				}
				newInfoHashMap.remove(fId);
			} else {
				new Delete().from(Announcement.class).
						where("announcement_id = ?", fId).execute();
				syncResult.stats.numDeletes++;
			}
		}

		for (Announcement e : newInfoHashMap.values()) {
			e.save();
		}
	}
}
