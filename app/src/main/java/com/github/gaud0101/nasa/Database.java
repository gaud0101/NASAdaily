package com.github.gaud0101.nasa;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.GregorianCalendar;

public class Database {
    public final static String COLUMN_TITLE = "title";
    public final static String COLUMN_DATE = "date";
    private final static String TABLE = "favorites";

    private static class Helper extends SQLiteOpenHelper {
        public static final int VERSION = 1;
        public static final String NAME = "nasa.db";

        public Helper(Context context) {
            super(context, NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "
                    + TABLE
                    + " ("
                    + BaseColumns._ID
                    + " INTEGER PRIMARY KEY, "
                    + COLUMN_TITLE
                    + " TEXT NOT NULL, "
                    + COLUMN_DATE
                    + " TEXT NOT NULL UNIQUE)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private final Helper helper;

    public Database(Context context) {
        helper = new Helper(context);
    }

    public long favorite(DownloadTask.Result item) {
        return favorite(item.title, item.date);
    }

    public long favorite(String title, String date) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DATE, date);

        return db.insert(TABLE, null, values);
    }

    public void unfavorite(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(TABLE, BaseColumns._ID + " = ?", new String[] {Long.toString(id)});
    }

    public Cursor listFavorites() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                COLUMN_DATE,
                COLUMN_TITLE,
        };

        return db.query(TABLE, projection, null, null, null, null, null);
    }

    public long getMillis(long id) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                COLUMN_DATE,
        };

        String selection = BaseColumns._ID + "= ?";
        String[] selectionArgs = {Long.toString(id)};

        Cursor cur = db.query(TABLE, projection, selection, selectionArgs, null, null, null);

        cur.moveToNext();
        int idx = cur.getColumnIndexOrThrow(COLUMN_DATE);
        String date = cur.getString(idx);

        GregorianCalendar cal = new GregorianCalendar();
        cal.clear();

        String[] parts = date.split("-");
        cal.set(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
        return cal.getTimeInMillis();
    }
}
