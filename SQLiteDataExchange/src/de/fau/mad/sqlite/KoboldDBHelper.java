package de.fau.mad.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KoboldDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "KoboldDB1.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + KoboldDBContract.KoboldEntry.TABLE_NAME + " (" +
            		KoboldDBContract.KoboldEntry._ID + " INTEGER PRIMARY KEY," +
            		KoboldDBContract.KoboldEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                    KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE + TEXT_TYPE + COMMA_SEP +
                    KoboldDBContract.KoboldEntry.COLUMN_NAME_VALUE + TEXT_TYPE + COMMA_SEP +
                    KoboldDBContract.KoboldEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + KoboldDBContract.KoboldEntry.TABLE_NAME;

    public KoboldDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // TODO Change this later!
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
