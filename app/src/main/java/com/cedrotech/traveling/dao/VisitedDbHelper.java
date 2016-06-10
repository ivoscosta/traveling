package com.cedrotech.traveling.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ivo on 06/06/16.
 */
public class VisitedDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + VisitedContract.Country.TABLE_NAME + " (" +
                    VisitedContract.Country.COLUMN_ID + " INTEGER PRIMARY KEY " + COMMA_SEP +
                    VisitedContract.Country.COLUMN_ISO + TEXT_TYPE + COMMA_SEP +
                    VisitedContract.Country.COLUMN_SHORTNAME + TEXT_TYPE + COMMA_SEP +
                    VisitedContract.Country.COLUMN_LONGNAME + TEXT_TYPE + COMMA_SEP +
                    VisitedContract.Country.COLUMN_CALLINGCODE + TEXT_TYPE + COMMA_SEP +
                    VisitedContract.Country.COLUMN_STATUS + " INTEGER" + COMMA_SEP +
                    VisitedContract.Country.COLUMN_CULTURE + TEXT_TYPE + COMMA_SEP +
                    VisitedContract.Country.COLUMN_VISITED_DATE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + VisitedContract.Country.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Traveling.db";

    public VisitedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}