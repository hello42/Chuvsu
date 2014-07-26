package com.ulop.syncadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ulop.syncadapter.Info.InfoContract;


/**
 * Created by ulop on 09.07.14.
 */
public class ChuvsuDatabase extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 3;
    /** Filename for SQLite file. */
    public static final String DATABASE_NAME = "chuvsu.db";

    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String COMMA_SEP = ",";
    /** SQL statement to create "entry" table. */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + InfoContract.Entry.TABLE_NAME + " (" +
                    InfoContract.Entry._ID + " INTEGER PRIMARY KEY," +
                    InfoContract.Entry.COLUMN_NAME_NEWS_ID + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Entry.COLUMN_NAME_TITLE    + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Entry.COLUMN_NAME_CONTENT + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Entry.COLUMN_NAME_PUBLISHED + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Entry.COLUMN_NAME_IMAGE + TYPE_TEXT + ")";

    /** SQL statement to drop "entry" table. */
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + InfoContract.Entry.TABLE_NAME;

    /** SQL statement to create "Faculty" table. */
    private static final String SQL_CREATE_FACULTIES =
            "CREATE TABLE " + InfoContract.Faculty.TABLE_NAME + " (" +
                    InfoContract.Faculty._ID + " INTEGER PRIMARY KEY," +
                    InfoContract.Faculty.COLUMN_NAME_FACULTY_NAME + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Faculty.COLUMN_NAME_FACULTY_ID    + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Faculty.COLUMN_NAME_LOGO + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Faculty.COLUMN_NAME_URL + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Faculty.COLUMN_NAME_INFO + TYPE_TEXT + ")";

    /** SQL statement to drop "faculty" table. */
    private static final String SQL_DELETE_FACULTIES =
            "DROP TABLE IF EXISTS " + InfoContract.Faculty.TABLE_NAME;


    /** SQL statement to create "abitnews" table. */
    private static final String SQL_CREATE_ABITNEWS =
            "CREATE TABLE " + InfoContract.AbitNews.TABLE_NAME + " (" +
                    InfoContract.AbitNews._ID + " INTEGER PRIMARY KEY," +
                    InfoContract.AbitNews.COLUMN_NAME_NEWS_ID + TYPE_INTEGER + COMMA_SEP +
                    InfoContract.AbitNews.COLUMN_NAME_TITLE    + TYPE_TEXT + COMMA_SEP +
                    InfoContract.AbitNews.COLUMN_NAME_CONTENT + TYPE_TEXT + COMMA_SEP +
                    InfoContract.AbitNews.COLUMN_NAME_PUBLISHED + TYPE_TEXT + COMMA_SEP +
                    InfoContract.AbitNews.COLUMN_NAME_IMAGE + TYPE_TEXT + COMMA_SEP +
                    InfoContract.AbitNews.COLUMN_NAME_NOTIFICATE + TYPE_INTEGER + ")";

    /** SQL statement to drop "entry" table. */
    private static final String SQL_DELETE_ABITNEWS =
            "DROP TABLE IF EXISTS " + InfoContract.AbitNews.TABLE_NAME;

    private static final String SQL_CREATE_PHONES =
            "CREATE TABLE" + InfoContract.Phones.TABLE_NAME + " (" +
                    InfoContract.Phones._ID + " INTEGER PRIMARY KEY, " +
                    InfoContract.Phones.COLUMN_NAME_PHONE_ID + TYPE_INTEGER + COMMA_SEP +
                    InfoContract.Phones.COLUMN_NAME_TITLE + TYPE_TEXT + COMMA_SEP +
                    InfoContract.Phones.COLUMN_NAME_PHONE_NUMBER + TYPE_TEXT + ")";

    private static final String SQL_DELETE_PHONES =
            "DROP TABLE IF EXISTS " + InfoContract.Phones.TABLE_NAME;

    public ChuvsuDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_FACULTIES);
        db.execSQL(SQL_CREATE_ABITNEWS);
        db.execSQL(SQL_CREATE_PHONES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_FACULTIES);
        db.execSQL(SQL_DELETE_ABITNEWS);
        db.execSQL(SQL_DELETE_PHONES);
        onCreate(db);
    }
}
