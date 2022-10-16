package com.example.myapplication.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FlatSQL {
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "FLATS";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINK = "LINK";
    public static final String COLUMN_CHECKED = "CHECKED";
    public static final String COLUMN_FAVORITE = "FAVORITE";

    private SQLiteDatabase myDatabase;

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  " + TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LINK + " TEXT,"+
                COLUMN_CHECKED + " INTEGER,"+
                COLUMN_FAVORITE + " INTEGER);");
        myDatabase = db;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }


}
