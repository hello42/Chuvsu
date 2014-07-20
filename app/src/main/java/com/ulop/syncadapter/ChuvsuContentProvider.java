package com.ulop.syncadapter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.ulop.syncadapter.Info.InfoContract;


public class ChuvsuContentProvider extends ContentProvider {


    private static ChuvsuDatabase mDatabaseHelper;
    private static final String AUTHORITY = InfoContract.CONTENT_AUTHORITY;

    private static final int ROUTE_ENTRIES = 1;
    private static final int ROUTE_ENTRIES_ID = 2;
    private static final int ROUTE_FACULTIES = 3;
    private static final int ROUTE_FACULTIES_ID = 4;
    private static final int ROUTE_ABITNEWS = 5;
    private static final int ROUTE_ABITNEWS_ID = 6;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "entries", ROUTE_ENTRIES);
        sUriMatcher.addURI(AUTHORITY, "entries/*", ROUTE_ENTRIES_ID);
        sUriMatcher.addURI(AUTHORITY, "faculties", ROUTE_FACULTIES);
        sUriMatcher.addURI(AUTHORITY, "faculties/*", ROUTE_FACULTIES_ID);
        sUriMatcher.addURI(AUTHORITY, "abitnews", ROUTE_ABITNEWS);
        sUriMatcher.addURI(AUTHORITY, "abitnews/*", ROUTE_ABITNEWS_ID);
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
                count = builder.table(InfoContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_ENTRIES_ID:
                id = uri.getLastPathSegment();
                count = builder.table(InfoContract.Entry.TABLE_NAME)
                        .where(InfoContract.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_FACULTIES:
                count = builder.table(InfoContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_FACULTIES_ID:
                id = uri.getLastPathSegment();
                count = builder.table(InfoContract.Faculty.TABLE_NAME)
                        .where(InfoContract.Faculty._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_ABITNEWS:
                count = builder.table(InfoContract.AbitNews.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_ABITNEWS_ID:
                id = uri.getLastPathSegment();
                count = builder.table(InfoContract.AbitNews.TABLE_NAME)
                        .where(InfoContract.AbitNews._ID + "=?", id)
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
                return InfoContract.Entry.CONTENT_TYPE;
            case ROUTE_ENTRIES_ID:
                return InfoContract.Entry.CONTENT_ITEM_TYPE;
            case ROUTE_FACULTIES:
                return InfoContract.Faculty.CONTENT_TYPE;
            case ROUTE_FACULTIES_ID:
                return InfoContract.Faculty.CONTENT_ITEM_TYPE;
            case ROUTE_ABITNEWS:
                return InfoContract.AbitNews.CONTENT_TYPE;
            case ROUTE_ABITNEWS_ID:
                return InfoContract.AbitNews.CONTENT_ITEM_TYPE;
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
                id = db.insertOrThrow(InfoContract.Entry.TABLE_NAME, null, values);
                result = Uri.parse(InfoContract.Entry.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ENTRIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_FACULTIES:
                id = db.insertOrThrow(InfoContract.Faculty.TABLE_NAME, null, values);
                result = Uri.parse(InfoContract.Faculty.CONTENT_URI + "/" + id);
                break;
            case ROUTE_FACULTIES_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            case ROUTE_ABITNEWS:
                id = db.insertOrThrow(InfoContract.AbitNews.TABLE_NAME, null, values);
                result = Uri.parse(InfoContract.AbitNews.CONTENT_URI + "/" + id);
                break;
            case ROUTE_ABITNEWS_ID:
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
                builder.where(InfoContract.Entry._ID + "=?", id);
            case ROUTE_ENTRIES:
                // Return all known entries.
                builder.table(InfoContract.Entry.TABLE_NAME)
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
                builder.where(InfoContract.Faculty._ID + "=?", id2);
            case ROUTE_FACULTIES:
                // Return all known entries.
                builder.table(InfoContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c2 = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx2 = getContext();
                assert ctx2 != null;
                c2.setNotificationUri(ctx2.getContentResolver(), uri);
               // db.close();
                return c2;
            case ROUTE_ABITNEWS_ID:
                // Return a single entry, by ID.
                String id3 = uri.getLastPathSegment();
                builder.where(InfoContract.AbitNews._ID + "=?", id3);
            case ROUTE_ABITNEWS:
                // Return all known entries.
                builder.table(InfoContract.AbitNews.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c3 = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx3 = getContext();
                assert ctx3 != null;
                c3.setNotificationUri(ctx3.getContentResolver(), uri);
               // db.close();
                return c3;
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
                count = builder.table(InfoContract.Entry.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_ENTRIES_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(InfoContract.Entry.TABLE_NAME)
                        .where(InfoContract.Entry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_FACULTIES:
                count = builder.table(InfoContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_FACULTIES_ID:
                String id2 = uri.getLastPathSegment();
                count = builder.table(InfoContract.Faculty.TABLE_NAME)
                        .where(InfoContract.Faculty._ID + "=?", id2)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_ABITNEWS:
                count = builder.table(InfoContract.AbitNews.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_ABITNEWS_ID:
                String id3 = uri.getLastPathSegment();
                count = builder.table(InfoContract.AbitNews.TABLE_NAME)
                        .where(InfoContract.Faculty._ID + "=?", id3)
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
