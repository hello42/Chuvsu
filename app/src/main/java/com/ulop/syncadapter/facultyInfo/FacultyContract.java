package com.ulop.syncadapter.facultyInfo;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ulop on 01.07.14.
 */
public class FacultyContract {
    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.ulop.syncadapter";

    /**
     * Base URI. (content://com.example.android.network.sync.basicsyncadapter)
     */
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_ENTRIES = "faculties";

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
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

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
        public static final String COLUMN_NAME_FACULTY_NAME = "name";
        /**
         * Article hyperlink. Corresponds to the rel="alternate" link in the
         * Atom spec.
         */
        public static final String COLUMN_NAME_LOGO = "logo";
        /**
         * Date article was published.
         */
        public static final String COLUMN_NAME_URL = "url";

        public static final String COLUMN_NAME_INFO = "info";
    }
}
