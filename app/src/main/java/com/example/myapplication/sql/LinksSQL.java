package com.example.myapplication.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class LinksSQL
{
    public static final String TABLE_NAME = "LINKS";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINK = "LINK";
    public static final String COLUMN_COUNT = "COUNT";

    private SQLiteDatabase db;

    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COUNT + " INTEGER,"+
                COLUMN_LINK + " TEXT);");
        this.db = db;
    }

    public void insertLink(String link)
    {
        db.insert(TABLE_NAME,null, createContentValues(link,0));
    }

    public ArrayList<Link> selectLinks()
    {
        ArrayList<Link> allLinks  = new ArrayList<>();
        String[] columns = { COLUMN_ID, COLUMN_COUNT, COLUMN_LINK};

        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do
            {
                int id = Integer.parseInt(cursor.getString(0));
                Long count = cursor.getLong(1);
                String link = cursor.getString(2);
                allLinks.add(new Link(id, link, count));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return allLinks;
    }

    public void deleteLink(int id)
    {
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE _id = " + id);
    }

    public void updateCount(int id, long count)
    {
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_COUNT + " = " + count + " WHERE _id = " + id);
    }

    private ContentValues createContentValues(String link, long count)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LINK, link);
        cv.put(COLUMN_COUNT, count);
        return cv;
    }

    public class Link
    {
        private int id;
        private String name;
        private Long count;

        public Link(int id, String name, Long count)
        {
            this.id = id;
            this.name = name;
            this.count = count;
        }

        public int getId()
        {
            return id;
        }

        public Long getCount()
        {
            return count;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
