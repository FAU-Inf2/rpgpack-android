package de.fau.mad.sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.fau.mad.sqlite.KoboldEntryDAO;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler {

    private Context context;

    private KoboldDBHelper kDBHelper;

    public DatabaseHandler(Context context) {
        this.context = context;
        kDBHelper = new KoboldDBHelper(this.context);
    }

    public long insertKoboldEntry(KoboldEntryDAO koboldEntryDAO) {
        // Gets the data repository in write mode
        SQLiteDatabase db = kDBHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(KoboldDBContract.KoboldEntry.COLUMN_NAME_ENTRY_ID, koboldEntryDAO.getEntryID());
        values.put(KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE, koboldEntryDAO.getFeature());
        values.put(KoboldDBContract.KoboldEntry.COLUMN_NAME_VALUE, koboldEntryDAO.getValue());
        values.put(KoboldDBContract.KoboldEntry.COLUMN_NAME_DESCRIPTION, koboldEntryDAO.getDescription());
        
        Log.w("", "Values" + values.size());
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                KoboldDBContract.KoboldEntry.TABLE_NAME,
                null,
                values);

        return newRowId;
    }

    public List<KoboldEntryDAO> readKoboldEntries() {
    	Log.w("", "Getting readable database");
        SQLiteDatabase db = kDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
        		KoboldDBContract.KoboldEntry._ID,
        		KoboldDBContract.KoboldEntry.COLUMN_NAME_ENTRY_ID,
        		KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE,
        		KoboldDBContract.KoboldEntry.COLUMN_NAME_VALUE,
        		KoboldDBContract.KoboldEntry.COLUMN_NAME_DESCRIPTION
        };

        // How you want the results sorted in the resulting Cursor
        // ASC-Sorting scheme in which the sort starts from the smallest or lowest value (0, 1 or A, for example) and proceeds to the largest or highest value. 
        String sortOrder =
        		KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE + " ASC";

        // WHERE-Condition
        String selection = "";
        // WHERE-Arguments
        String[] selectionArgs = {};
        
        Log.w("", "Getting cursor");

        Cursor cursor = db.query(
        		KoboldDBContract.KoboldEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        
        List<KoboldEntryDAO> results = new ArrayList<KoboldEntryDAO>();
        
        cursor.moveToFirst();
        
        String entryID;
        String feature;
        String value;
        String description;
//        int i = 0;
        
        //while (!cursor.isAfterLast() && i++ < 10){
        while (!cursor.isAfterLast()){
        	Log.w("", "Cursor step...");
            entryID = cursor.getString(
                    cursor.getColumnIndexOrThrow(KoboldDBContract.KoboldEntry.COLUMN_NAME_ENTRY_ID));
            feature = cursor.getString(
                    cursor.getColumnIndexOrThrow(KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE));
            value = cursor.getString(
                    cursor.getColumnIndexOrThrow(KoboldDBContract.KoboldEntry.COLUMN_NAME_VALUE));
            description = cursor.getString(
                    cursor.getColumnIndexOrThrow(KoboldDBContract.KoboldEntry.COLUMN_NAME_DESCRIPTION));
            KoboldEntryDAO dao = new KoboldEntryDAO(entryID, feature, value, description);
            cursor.moveToNext();
            results.add(dao);
        }
        Log.w("", "Fetched number of results: "+results.size());
        return results;
    }

    public List<String> readKoboldFeatures() {
        SQLiteDatabase db = kDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
        		KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "";
               // KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE + " ASC";

        // WHERE-Condition
        String selection = "";
        // WHERE-Arguments
        String[] selectionArgs = {};

        Cursor cursor = db.query(
        		KoboldDBContract.KoboldEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<String> results = new ArrayList<String>();

        String feature;



        while (cursor.moveToNext()){

            feature = cursor.getString(
                    cursor.getColumnIndexOrThrow(KoboldDBContract.KoboldEntry.COLUMN_NAME_FEATURE));

            results.add(feature);
        }

        cursor.close();
        return results;
    }

    public void close() {
    	SQLiteDatabase db = kDBHelper.getWritableDatabase();
    	db.close();
	}
    
    public void deleteKoboldEntry(long koboldEntryID) {
        SQLiteDatabase db = kDBHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = KoboldDBContract.KoboldEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(koboldEntryID) };
        /* Issue SQL statement. */
        db.delete(KoboldDBContract.KoboldEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void update(String koboldEntryID, String column, String newValue) {
        SQLiteDatabase db = kDBHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(column, newValue);

        // Which row to update, based on the ID
        String selection = KoboldDBContract.KoboldEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(koboldEntryID) };

        int count = db.update(
        		KoboldDBContract.KoboldEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
