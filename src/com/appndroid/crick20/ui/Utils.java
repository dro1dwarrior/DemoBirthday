package com.appndroid.crick20.ui;

import java.math.BigInteger;
import java.security.MessageDigest;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public class Utils
{
    public static SQLiteDatabase db = null;
    public static Context currentContext = null;
    public static boolean isDataMatchURLparsed = false;
    public static boolean rowUpdatedAfterLiveURLFetch = false;
    public static boolean isDocsFetched = false;

    public static void getDB( Context context )
    {
        db = context.openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
    }

    public static void setContext( Context context )
    {
        currentContext = context;
    }

    public static void setIsPushStatusPostedOnServer( Context context, boolean bValue )
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean( "push_status_posted", bValue );
        editor.commit();
    }

    public static boolean getIsPushStatusPostedOnServer( Context context )
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
        return sharedPreferences.getBoolean( "push_status_posted", false );
    }

    public static String getMd5Hash( String szSource )
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            byte[] bmessageDigest = md.digest( szSource.getBytes() );
            // We convert the result into a big integer, so that we can convert
            // it into a Hexadecimal;
            BigInteger convertedToNumber = new BigInteger( 1, bmessageDigest );
            String szmd5 = convertedToNumber.toString( 16 ); // convert to hex
                                                             // string

            // stuff with leading zeroes which are removed when we convert the
            // byte array to a bigInteger;
            if( szmd5.length() < 32 )
            {
                while( szmd5.length() < 32 )
                    szmd5 = "0" + szmd5;
            }

            return szmd5;
        }
        catch( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean getNetworkStatus( Context context )
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI );
        Log.d( "", "activeNetInfo " + activeNetInfo + " mobNetInfo " + mobNetInfo );

        if( ( mobNetInfo != null && mobNetInfo.isAvailable() && mobNetInfo.isConnected() )
                || ( activeNetInfo != null && activeNetInfo.isAvailable() && activeNetInfo.isConnected() )
                || ( wifiInfo != null && wifiInfo.isAvailable() && wifiInfo.isConnected() ) )
        {
            Log.d( "", "NETWORK AVAILABLE" );
            return true;
        }
        else
        {
            Log.d( "SplashScreen-onCreate()", "NETWORK NOT AVAILABLE" );
            return false;
        }
    }

    public static boolean getDBDeleted( Context context )
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
        return sharedPreferences.getBoolean( "db_deleted", false );
    }

    public static void setDBDeleted( Context context, boolean bValue )
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean( "db_deleted", bValue );
        editor.commit();
    }

}
