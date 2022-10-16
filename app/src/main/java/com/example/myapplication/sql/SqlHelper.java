package com.example.myapplication.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;


public class SqlHelper  {
    private static final String DATABASE_NAME = "myDatabase.db";

    private SQLiteDatabase myDatabase;
    private FlatSQL flatSQL;
    private LinksSQL linksSQL;

    public SqlHelper(Context baseContext)  throws Exception{
        startDB(baseContext);
    }

    public void startDB(Context context){

        myDatabase = context.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);

        if (myDatabase == null) ;
        flatSQL = new FlatSQL();
        flatSQL.onCreate(myDatabase);
        linksSQL = new LinksSQL();
        linksSQL.onCreate(myDatabase);
    }

    public FlatSQL getFlatSQL(){
        return flatSQL;
    }

    public LinksSQL getLinksSQL(){
        return linksSQL;
    }
}
