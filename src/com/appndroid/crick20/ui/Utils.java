package com.appndroid.crick20.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Utils
{
    public static SQLiteDatabase db;
    public static Context currentContext = null;

    public static void getDB( Context context )
    {
        db = context.openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
    }

    public static void setContext( Context context )
    {
        currentContext = context;
    }

}
