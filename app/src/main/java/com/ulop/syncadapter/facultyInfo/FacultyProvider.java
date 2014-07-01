package com.ulop.syncadapter.facultyInfo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import com.ulop.syncadapter.SelectionBuilder;

/**
 * Created by ulop on 01.07.14.
 */
public class FacultyProvider extends ContentProvider {
    public static FacultyDatabase mDatabaseHelper;
    private static final String AUTHORITY = FacultyContract.CONTENT_AUTHORITY;

    public static final int ROUTE_FACULTIES = 1;
    public static final int ROUTE_FACULTIES_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(AUTHORITY, "entries", ROUTE_FACULTIES);
        sUriMatcher.addURI(AUTHORITY, "entries/*", ROUTE_FACULTIES_ID);
    }


    public FacultyProvider() {
        //final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        //Log.i("db",  db.getPath());
    }

    public String getPath(){
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        return db.getPath();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_FACULTIES:
                count = builder.table(FacultyContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_FACULTIES_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(FacultyContract.Faculty.TABLE_NAME)
                        .where(FacultyContract.Faculty._ID + "=?", id)
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
            case ROUTE_FACULTIES:
                return FacultyContract.Faculty.CONTENT_TYPE;
            case ROUTE_FACULTIES_ID:
                return FacultyContract.Faculty.CONTENT_ITEM_TYPE;
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
        switch (match) {
            case ROUTE_FACULTIES:
                long id = db.insertOrThrow(FacultyContract.Faculty.TABLE_NAME, null, values);
                result = Uri.parse(FacultyContract.Faculty.CONTENT_URI + "/" + id);
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
        mDatabaseHelper = new FacultyDatabase(getContext());
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
            case ROUTE_FACULTIES_ID:
                // Return a single entry, by ID.
                String id = uri.getLastPathSegment();
                builder.where(FacultyContract.Faculty._ID + "=?", id);
            case ROUTE_FACULTIES:
                // Return all known entries.
                builder.table(FacultyContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs);
                Cursor c = builder.query(db, projection, sortOrder);
                // Note: Notification URI must be manually set here for loaders to correctly
                // register ContentObservers.
                Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
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
            case ROUTE_FACULTIES:
                count = builder.table(FacultyContract.Faculty.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_FACULTIES_ID:
                String id = uri.getLastPathSegment();
                count = builder.table(FacultyContract.Faculty.TABLE_NAME)
                        .where(FacultyContract.Faculty._ID + "=?", id)
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

    /**
     * SQLite backend for @{link FeedProvider}.
     *
     * Provides access to an disk-backed, SQLite datastore which is utilized by FeedProvider. This
     * database should never be accessed by other parts of the application directly.
     */
    public static class FacultyDatabase extends SQLiteOpenHelper {
        /** Schema version. */
        public static final int DATABASE_VERSION = 1;
        /** Filename for SQLite file. */
        public static final String DATABASE_NAME = "chuvsu.db";

        private static final String TYPE_TEXT = " TEXT";
        private static final String TYPE_INTEGER = " INTEGER";
        private static final String COMMA_SEP = ",";
        /** SQL statement to create "entry" table. */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FacultyContract.Faculty.TABLE_NAME + " (" +
                        FacultyContract.Faculty._ID + " INTEGER PRIMARY KEY," +
                        FacultyContract.Faculty.COLUMN_NAME_FACULTY_NAME + TYPE_TEXT + COMMA_SEP +
                        FacultyContract.Faculty.COLUMN_NAME_FACULTY_ID    + TYPE_INTEGER + COMMA_SEP +
                        FacultyContract.Faculty.COLUMN_NAME_LOGO + TYPE_TEXT + COMMA_SEP +
                        FacultyContract.Faculty.COLUMN_NAME_URL + TYPE_TEXT + COMMA_SEP +
                        FacultyContract.Faculty.COLUMN_NAME_INFO + TYPE_TEXT + ")";

        /** SQL statement to drop "entry" table. */
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FacultyContract.Faculty.TABLE_NAME;

        public FacultyDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over

            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

    }
}
