package com.greengrass.syncadapter.Info;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by greengrass on 12.03.14.
 */
public class InfoContract {
    public InfoContract() {
    }

    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.greengrass.chuvsu.app";

    /**
     * Base URI. (content://com.example.android.network.sync.basicsyncadapter)
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_ENTRIES = "entries";

    public static class Entry implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.chuvsu.entries";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.chuvsu.entry";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        /**
         * Table name where records are stored for "entry" resources.
         */
        public static final String TABLE_NAME = "entry";
        /**
         * Atom ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_NAME_NEWS_ID = "news_id";
        /**
         * Article title
         */
        public static final String COLUMN_NAME_TITLE = "title";
        /**
         * Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec.
         */
        public static final String COLUMN_NAME_CONTENT = "content";
        /**
         * Date article was published.
         */
        public static final String COLUMN_NAME_PUBLISHED = "published";

        public static final String COLUMN_NAME_IMAGE = "image";
    }

    private static final String PATH_FACULTIES = "faculty";

    public static class Faculty implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.chuvsu.faculties";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.chuvsu.faculty";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FACULTIES).build();

        /**
         * Table name where records are stored for "entry" resources.
         */
        public static final String TABLE_NAME = "faculty";
        /**
         * Atom ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_NAME_FACULTY_ID = "faculty_id";
        /**
         * Article title
         */
        public static final String COLUMN_NAME_FACULTY_NAME = "namefct";
        /**
         * Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec.
         */
        public static final String COLUMN_NAME_LOGO = "logo";
        /**
         * Date article was published.
         */
        public static final String COLUMN_NAME_URL = "urlfct";

        public static final String COLUMN_NAME_INFO = "info";
    }

    private static final String PATH_ABITNEWS = "abitnews";

    public static class AbitNews implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.chuvsu.abitnews";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.chuvsu.abitnew";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ABITNEWS).build();

        /**
         * Table name where records are stored for "entry" resources.
         */
        public static final String TABLE_NAME = "abitnew";
        /**
         * Atom ID. (Note: Not to be confused with the database primary key, which is _ID.
         */
        public static final String COLUMN_NAME_NEWS_ID = "news_id";
        /**
         * Article title
         */
        public static final String COLUMN_NAME_TITLE = "title";
        /**
         * Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec.
         */
        public static final String COLUMN_NAME_CONTENT = "content";

        public static final String COLUMN_NAME_PUBLISHED = "published";

        public static final String COLUMN_NAME_IMAGE = "image";

        public static final String COLUMN_NAME_NOTIFICATE = "notificate";
    }

    private static final String PATH_PHONES = "phones";

    public static class Phones implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.chuvsu.phones";

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.chuvsu.phone";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHONES).build();

        public static final String TABLE_NAME = "phones";

        public static final String COLUMN_NAME_PHONE_ID = "phone_id";

        public static final String COLUMN_NAME_TITLE = "title";

        public static final String COLUMN_NAME_PHONE_NUMBER = "number";

    }

    private static final String PATH_ADDRESSES = "addresses";

    public static class Addresses implements BaseColumns {
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.chuvsu.addresses";

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.chuvsu.address";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ADDRESSES).build();

        public static final String TABLE_NAME = "addresses";

        public static final String COLUMN_NAME_PHONE_ID = "address_id";

        public static final String COLUMN_NAME_TITLE = "title";

        public static final String COLUMN_NAME_ADDRESS = "address";

        public static final String COLUMN_NAME_IMAGE = "image";

    }
}
