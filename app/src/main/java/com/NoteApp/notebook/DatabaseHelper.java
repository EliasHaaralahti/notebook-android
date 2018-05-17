package com.NoteApp.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RECORD ("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "CREATOR TEXT, "
            + "CONTENT TEXT, "
            + "DATE TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int j) {
        // If database structure changes after installation.
        // Override required when implementing SQLiteOpenHelper.
    }

    public static long insertRecord(SQLiteDatabase db, String creator, String content, String date) {
        ContentValues recordValues = new ContentValues();
        recordValues.put("CREATOR", creator);
        recordValues.put("CONTENT", content);
        recordValues.put("DATE", date);

        // Insert the record and return new record ID
        return db.insert("RECORD", null, recordValues);
    }

    // Returns all database content as a CSV format string
    public static String getDatabaseContentsAsString(SQLiteDatabase db) {
        Cursor cursor = db.query("RECORD",
                new String[]{"_id", "CREATOR", "CONTENT", "DATE"},
                null, null, null, null, "_id ASC");

        String databaseAsString = System.getProperty("line.separator");

        // TODO: is this check necessary due to while logic
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }

        while ( cursor.moveToNext() ) {
            for (int i = 0; i < cursor.getColumnCount() - 1; i++) {
                databaseAsString += cursor.getString(i) + ",";
            }
            databaseAsString += System.getProperty("line.separator");
        }

        cursor.close();
        return databaseAsString;
    }

    // TODO: Get entry with SQL query
    public static String[] getDatabaseSingleEntry(SQLiteDatabase db, long id) {
        Cursor cursor = db.query("RECORD",
                new String[] { "_id", "CREATOR", "CONTENT", "DATE" },
                null, null, null, null, "_id ASC"
        );

        // TODO: is this check necessary due to while logic
        if (cursor.getCount() == 0) {
            cursor.close();
            return null;
        }

        String[] data = null;
        while ( cursor.moveToNext() ) {
            if (Long.valueOf(cursor.getString(0)) == id) {
                // Store creator, content and date
                data = new String[3];
                data[0] = cursor.getString(1);
                data[1] = cursor.getString(2);
                data[2] = cursor.getString(3);
                break;
            }
        }
        cursor.close();
        return data;
    }

    public static void updateRecord(SQLiteDatabase db, Long id, String creator, String content, String date) {
        ContentValues recordValues = new ContentValues();
        recordValues.put("CREATOR", creator);
        recordValues.put("CONTENT", content);
        recordValues.put("DATE", date);

        db.update("RECORD", recordValues, "_id = ?", new String[] {Long.toString(id)});
    }

    public static void deleteRecord(SQLiteDatabase db, Long id) {
        db.delete("RECORD", "_id=?", new String[] {Long.toString(id)});
    }

    public static void deleteAllRecords(SQLiteDatabase db) {
        db.delete("RECORD", null, null);
        // Reset primary key.
        db.delete("SQLITE_SEQUENCE","NAME = ?", new String[]{"RECORD"});
    }
}
