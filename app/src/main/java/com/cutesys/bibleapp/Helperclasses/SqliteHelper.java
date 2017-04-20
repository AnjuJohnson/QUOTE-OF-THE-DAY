package com.cutesys.bibleapp.Helperclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Athira on 2/17/2017.
 */

public class SqliteHelper extends SQLiteOpenHelper {

    ContentValues cv;
    public Context mContext;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Thoughtfortheday.db";

    public static final String THOUGHT_DETAILS_TABLE = "thought_details_table";
    public static final String THOUGHT__ID = "thought_id";
    public static final String THOUGHT = "thought";
    public static final String DATE = "date";

    private static final String CREATE_THOUGHT_TABLE =
            "create table " + THOUGHT_DETAILS_TABLE + " ("
                    + THOUGHT__ID + " text, "
                    + THOUGHT + " text, "
                    + DATE + " text)";

    public SqliteHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_THOUGHT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void Insert_thought_details(List<HashMap<String, String>> list) {
        for(int i=0;i<list.size();i++) {

            HashMap<String, String> map = new HashMap<String, String>();
            map = list.get(i);

            cv = new ContentValues();
            cv.put(SqliteHelper.THOUGHT__ID, map.get("thought_id"));
            cv.put(SqliteHelper.THOUGHT, map.get("thought"));
            cv.put(SqliteHelper.DATE, map.get("date"));

            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(SqliteHelper.THOUGHT_DETAILS_TABLE, null, cv);
            db.close(); // Closing database connection
        }
    }

    public List<HashMap<String, String>> getthoughtdetails() {

        String selectQuery = "SELECT * FROM " + THOUGHT_DETAILS_TABLE;
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null) {
                cursor.moveToFirst();
                do {
                    map = new HashMap<String, String>();

                    map.put("thought_id", cursor.getString(cursor.getColumnIndexOrThrow("thought_id")));
                    map.put("thought", cursor.getString(cursor.getColumnIndexOrThrow("thought")));
                    map.put("date", cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    fillMaps.add(map);
                } while (cursor.moveToNext());
            }
            // closing connection
            cursor.close();
            db.close();
        } catch (Exception e) {
        }
        return fillMaps;
    }

    public void Delete_thought_details() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SqliteHelper.THOUGHT_DETAILS_TABLE, null, null);
        db.close(); // Closing database connection
    }
}