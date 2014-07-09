package com.ulop.syncadapter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.ulop.syncadapter.Feed.FeedContract;


public class ChuvsuContentProvider extends ContentProvider {


    private static ChuvsuDatabase mDatabaseHelper;
    private static final String AUTHORITY = com.ulop.syncadapter.Feed.FeedContract.CONTENT_AUTHORITY;

    private static final int ROUTE_ENTRIES = 1;
    private static final int ROUTE_ENTRIES_ID = 2;
    private static final int ROUTE_FACULTIES = 3;
    private static final int ROUTE_FACULTIES_ID = 4;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "entries", ROUTE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "entries/*", ROUTE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "faculties", ROUTE_FACULTIES);
        sUriMatcher.addURI(AUTHORITY, "faculties/*", ROUTE_FACULTIES_ID);
    }

    public ChuvsuContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        String id;
        switch (match) {
            case ROUTE_ENTRIES:
                count = builder.table(com.ulop.syncadapter.Feed.FeedContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_ENTRIES_ID:
                id = uri.getLastPathSegment();
                count = builder.table(com.ulop.syncadapter.Feed.FeedContract.Entry.TABLE_NAME)
                        .where(com.ulop.syncadapter.Feed.FeedContract.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_FACULTIES:
                count = builder.table(FeedContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_FACULTIES_ID:
                id = uri.getLastPathSegment();
                count = builder.table(FeedContract.Faculty.TABLE_NAME)
                        .where(FeedContract.Faculty._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_ENTRIES:
                return com.ulop.syncadapter.Feed.FeedContract.Entry.CONTENT_TYPE;
            case ROUTE_ENTRIES_ID:
                return com.ulop.syncadapter.Feed.FeedContract.Entry.CONTENT_ITEM_TYPE;
            case ROUTE_FACULTIES:
                return FeedContract.Faculty.CONTENT_TYPE;
            case ROUTE_FACULTIES_ID:
                return FeedContract.Faculty.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        long id;
        switch (match) {
            case ROUTE_ENTRIES:
                id = db.insertOrThrow(com.ulop.syncadapter.Feed.FeedContract.Entry.TABLE_NAME, null, values);
                result = Uri.parse(com.ulop.syncadapter.Feed.FeedContract.Entry.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_FACULTIES:
                id = db.insertOrThrow(FeedContract.Faculty.TABLE_NAME, null, values);
                result = Uri.parse(FeedContract.Faculty.CONTENT_URI + "/" + id);
                break;
            case ROUTE_FACULTIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new ChuvsuDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Log.d("db", new String(db.getPath()));
        SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_ENTRIES_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(com.ulop.syncadapter.Feed.FeedContract.Entry._ID + "=?", id);
            case ROUTE_ENTRIES:
                // Return all known entries.
                builder.table(com.ulop.syncadapter.Feed.FeedContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            case ROUTE_FACULTIES_ID:
                // Return a single entry, by ID.
                String id2 = uri.getLastPathSegment();
                builder.where(FeedContract.Faculty._ID + "=?", id2);
            case ROUTE_FACULTIES:
                // Return all known entries.
                builder.table(FeedContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c2 = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx2 = getContext();
                assert ctx2 != null;
                c2.setNotificationUri(ctx2.getContentResolver(), uri);
               // db.close();
                return c2;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_ENTRIES:
                count = builder.table(com.ulop.syncadapter.Feed.FeedContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_ENTRIES_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(com.ulop.syncadapter.Feed.FeedContract.Entry.TABLE_NAME)
                        .where(com.ulop.syncadapter.Feed.FeedContract.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_FACULTIES:
                count = builder.table(FeedContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_FACULTIES_ID:
                String id2 = uri.getLastPathSegment();
                count = builder.table(FeedContract.Faculty.TABLE_NAME)
                        .where(FeedContract.Faculty._ID + "=?", id2)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
}
