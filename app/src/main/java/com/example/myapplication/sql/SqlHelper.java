package com.example.myapplication.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;

public class SqlHelper
{
    private static SqlHelper instance;

    private static final String DATABASE_NAME = "myDatabase.db";

    private SQLiteDatabase myDatabase;

    private LinksSQL linksSQL;

    private SqlHelper(Context baseContext)
    {
        startDB(baseContext);
    }

    public void startDB(Context context)
    {
        myDatabase = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        if (myDatabase == null)
        {
            return;
        }

        linksSQL = new LinksSQL();
        linksSQL.onCreate(myDatabase);
    }

    public LinksSQL getLinksSQL()
    {
        return linksSQL;
    }

    public static void createInstance(Context context)
    {
        try
        {
            if (instance == null)
            {
                instance = new SqlHelper(context);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static SqlHelper getInstance()
    {
        return instance;
    }
}
