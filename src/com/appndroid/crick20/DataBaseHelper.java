package com.appndroid.crick20;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper
{

    // The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.appndroid.crick20/databases/";
    private static String DB_NAME = "worldcupt20.db";

    private static SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor Takes and keeps a reference of the passed context in order to access to the application assets and
     * resources.
     * @param context
     */
    public DataBaseHelper( Context context )
    {

        super( context, DB_NAME, null, 1 );
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException
    {

        boolean dbExist = checkDataBase();

        if( dbExist )
        {
            // do nothing - database already exist
        }
        else
        {

            // By calling this method and empty database will be created into
            // the default system path
            // of your application so we are gonna be able to overwrite that
            // database with our database.

            this.getReadableDatabase();

            try
            {

                copyDataBase();

            }
            catch( IOException e )
            {

                throw new Error( "Error copying database" );

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase()
    {

        SQLiteDatabase checkDB = null;

        try
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase( myPath, null, SQLiteDatabase.OPEN_READONLY );

        }
        catch( SQLiteException e )
        {

            // database does't exist yet.

        }

        if( checkDB != null )
        {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the system folder, from
     * where it can be accessed and handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException
    {

        try
        {
            File fileTest = myContext.getFileStreamPath( DB_NAME );
            boolean exists = fileTest.exists();
            if( !exists )
            {
                // Open the empty db as the output stream
                OutputStream databaseOutputStream = new FileOutputStream( DB_PATH + DB_NAME );
                InputStream databaseInputStream;

                databaseInputStream = myContext.getAssets().open( DB_NAME );
                byte[] buffer = new byte[1024];
                int length;
                while( ( length = databaseInputStream.read( buffer ) ) > 0 )
                {
                    databaseOutputStream.write( buffer );
                }
                databaseInputStream.close();

                databaseInputStream = myContext.getAssets().open( DB_NAME );
                while( ( length = databaseInputStream.read( buffer ) ) > 0 )
                {
                    databaseOutputStream.write( buffer );
                }

                // Close the streams
                databaseInputStream.close();
                databaseOutputStream.flush();
                databaseOutputStream.close();
            }

        }
        catch( Exception ex )
        {
            Log.d( "myTest", "copyDataBase : " + ex.getMessage() );

        }

    }

    public void openDataBase() throws SQLException
    {

        try
        {
            // Open the database
            String myPath = DB_PATH + DB_NAME;
            myDataBase = SQLiteDatabase.openDatabase( myPath, null, SQLiteDatabase.OPEN_READONLY );

        }
        catch( Exception ex1 )
        {
            Log.d( "myTest", "copyDataBase : " + ex1.getMessage() );
        }

    }

    @Override
    public synchronized void close()
    {

        if( myDataBase != null )
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {

    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {

    }

    // Add your public helper methods to access and get content from the
    // database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd
    // be easy
    // to you to create adapters for your views.

}
