package com.appndroid.crick20.ui;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class NetworkManager
{

    Context mContext;
    private int m_nCommandType;
    private String currentTag;
    public static boolean isNetworkConnection = false;
    public static boolean isDataFetched = false;
    public static String isAvailable = "false", serverName = "";
    SQLiteDatabase db;

    public NetworkManager( Context context )
    {
        mContext = context;
        if( Utils.db == null )
            Utils.getDB( mContext );
        db = Utils.db;
    }

    public String generateString( InputStream stream )
    {
        InputStreamReader reader = new InputStreamReader( stream );
        BufferedReader buffer = new BufferedReader( reader );
        StringBuilder sb = new StringBuilder();
        try
        {
            String cur;
            while( ( cur = buffer.readLine() ) != null )
            {
                sb.append( cur + "\n" );
            }
        }
        catch( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
            stream.close();
        }
        catch( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }

    public class HttpAsyncConnector extends AsyncTask< String, String, String >
    {

        HttpClient hc;

        protected void onPreExecute()
        {

        }

        // automatically done on worker thread (separate from UI thread)
        protected String doInBackground( final String... args )
        {
            String szResponse = "";
            boolean bSuccess = false;
            HttpGet get = null;
            try
            {
                hc = new DefaultHttpClient();
                if( m_nCommandType == ApplicationDefines.CommandType.COMMAND_SCHEDULE )
                {
                    get = new HttpGet( ApplicationDefines.Constants.SCHEDULE_URL );
                    // Log.d("NetworkManager.HttpAsyncConnector-doInBackground()",
                    // "Request is ::: "
                    // + ApplicationDefines.Constants.SCHEDULE_URL);
                }

                HttpResponse rp = hc.execute( get );
                InputStream data = rp.getEntity().getContent();
                szResponse = generateString( data );
                isNetworkConnection = true;
                isDataFetched = true;
                // Log.d("NetworkManager.HttpAsyncConnector-doInBackground()",
                // "Response is ::: " + szResponse);
                return szResponse;

            }

            catch( Exception e )
            {
                // TODO Auto-generated catch block
                isNetworkConnection = false;
                e.printStackTrace();
                return "";
            }

        }

        // can use UI thread here
        protected void onPostExecute( final String szResponse )
        {
            super.onPostExecute( szResponse );
            // Log.d("NetworkManager.HttpAsyncConnector-onPostExecute()",
            // "Response is :: " + szResponse);
            if( szResponse.equals( "" ) )
                Utils.isDocsFetched = false;
            else
            {
                Utils.isDocsFetched = true;
                parseResponse( szResponse );
            }

        }

        private void parseResponse( String szResponse )
        {
            try
            {

                int matchnumber = 0;
                String winnerTeam = "", matchResult = "", teamAscore = "", teamBscore = "", manofthematch = "", other1 = "", other2 = "", other3 = "";

                int pointTableID = 1;
                String Team = "", Played = "", Won = "", Lost = "", NoResult = "", Points = "", NetRunRate = "", Position = "";

                String A1 = "", A2 = "", B1 = "", B2 = "", C1 = "", C2 = "", D1 = "", D2 = "";
                String TeamA = "", TeamB = "", matchType = "";

                int orangeCapId = 1;
                String Player = "", Runs = "", HS = "", Sixes = "", Fours = "";

                int purpleCapId = 1;
                String Wkts = "", BBI = "", Maidens = "", SR = "";

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware( true );
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( new StringReader( szResponse ) );
                int eventType = xpp.getEventType();

                while( eventType != XmlPullParser.END_DOCUMENT )
                {
                    if( eventType == XmlPullParser.START_DOCUMENT )
                    {
                        // System.out.println("Start document");
                    }
                    else if( eventType == XmlPullParser.END_DOCUMENT )
                    {
                        // System.out.println("End document");
                    }
                    else if( eventType == XmlPullParser.START_TAG )
                    {
                        // System.out.println("Start tag " + xpp.getName());
                    }
                    else if( eventType == XmlPullParser.END_TAG )
                    {
                        // System.out.println("End tag " + xpp.getName());
                    }
                    else if( eventType == XmlPullParser.TEXT )
                    {
                        // System.out.println("Text " + xpp.getText());
                    }
                    currentTag = xpp.getName();

                    // Toast.makeText(mContext, currentTag, Toast.LENGTH_LONG)
                    // .show();

                    if( eventType == XmlPullParser.START_TAG )
                    {

                        if( currentTag.equalsIgnoreCase( "matchupdate" ) )
                        {
                            // Do nothing
                        }
                        else if( currentTag.equals( "details" ) )
                        {

                            for( int attrCnt = 0; attrCnt < xpp.getAttributeCount(); attrCnt++ )
                            {
                                String key = xpp.getAttributeName( attrCnt );
                                if( key.equals( "matchnumber" ) )
                                {
                                    matchnumber = Integer.parseInt( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "winnerTeam" ) )
                                {
                                    winnerTeam = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "matchResult" ) )
                                {
                                    matchResult = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "teamAscore" ) )
                                {
                                    teamAscore = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "teamBscore" ) )
                                {
                                    teamBscore = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "manofthematch" ) )
                                {
                                    manofthematch = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                            }
                        }
                        else if( currentTag.equalsIgnoreCase( "superEights" ) )
                        {
                            for( int attrCnt = 0; attrCnt < xpp.getAttributeCount(); attrCnt++ )
                            {
                                String key = xpp.getAttributeName( attrCnt );
                                if( key.equals( "A1" ) )
                                {
                                    A1 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "A2" ) )
                                {
                                    A2 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "B1" ) )
                                {
                                    B1 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "B2" ) )
                                {
                                    B2 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "C1" ) )
                                {
                                    C1 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "C2" ) )
                                {
                                    C2 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "D1" ) )
                                {
                                    D1 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "D2" ) )
                                {
                                    D2 = ( xpp.getAttributeValue( attrCnt ) );
                                }
                            }

                        }
                        else if( currentTag.equals( "eliminators" ) )
                        {
                            // Do nothing
                        }
                        else if( currentTag.equalsIgnoreCase( "eliminator" ) )
                        {

                            for( int attrCnt = 0; attrCnt < xpp.getAttributeCount(); attrCnt++ )
                            {
                                String key = xpp.getAttributeName( attrCnt );
                                if( key.equals( "TeamA" ) )
                                {
                                    TeamA = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "TeamB" ) )
                                {
                                    TeamB = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "match" ) )
                                {
                                    matchType = ( xpp.getAttributeValue( attrCnt ) );
                                }
                            }
                        }
                        // else if (currentTag.equalsIgnoreCase("pointTable")) {
                        // // Do nothing
                        // } else if
                        // (currentTag.equalsIgnoreCase("teamStanding")) {
                        //
                        // Log.d("MY APP BATSMEN", "WE R IN");
                        //
                        // for (int attrCnt = 0; attrCnt < xpp
                        // .getAttributeCount(); attrCnt++) {
                        // String key = xpp.getAttributeName(attrCnt);
                        // if (key.equals("Team")) {
                        // Team = (xpp.getAttributeValue(attrCnt));
                        // } else if (key.equals("Played")) {
                        // Played = (xpp.getAttributeValue(attrCnt));
                        // } else if (key.equals("Won")) {
                        // Won = (xpp.getAttributeValue(attrCnt)
                        // .toString());
                        // } else if (key.equals("Lost")) {
                        // Lost = (xpp.getAttributeValue(attrCnt)
                        // .toString());
                        // } else if (key.equals("NoResult")) {
                        // NoResult = (xpp.getAttributeValue(attrCnt)
                        // .toString());
                        // } else if (key.equals("Points")) {
                        // Points = (xpp.getAttributeValue(attrCnt)
                        // .toString());
                        // } else if (key.equals("NetRunRate")) {
                        // NetRunRate = (xpp
                        // .getAttributeValue(attrCnt)
                        // .toString());
                        // }
                        //
                        // }
                        // }
                        // =======================
                        else if( currentTag.equalsIgnoreCase( "GroupStandings" ) )
                        {
                            // Do nothing
                        }
                        else if( currentTag.equalsIgnoreCase( "GroupA" ) || currentTag.equalsIgnoreCase( "GroupB" ) || currentTag.equalsIgnoreCase( "GroupC" )
                                || currentTag.equalsIgnoreCase( "GroupD" ) || currentTag.equalsIgnoreCase( "Group1" ) || currentTag.equalsIgnoreCase( "Group2" ) )
                        {

                            for( int attrCnt = 0; attrCnt < xpp.getAttributeCount(); attrCnt++ )
                            {
                                String key = xpp.getAttributeName( attrCnt );
                                if( key.equals( "Team" ) )
                                {
                                    Team = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "Played" ) )
                                {
                                    Played = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "Won" ) )
                                {
                                    Won = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "Lost" ) )
                                {
                                    Lost = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "NoResult" ) )
                                {
                                    NoResult = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "Points" ) )
                                {
                                    Points = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "NetRunRate" ) )
                                {
                                    NetRunRate = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }
                                else if( key.equals( "Standing" ) )
                                {
                                    Position = ( xpp.getAttributeValue( attrCnt ).toString() );
                                }

                            }
                        }
                        else if( currentTag.equalsIgnoreCase( "notificationServer" ) )
                        {
                            // Do nothing
                        }
                        else if( currentTag.equalsIgnoreCase( "serverdetails" ) )
                        {

                            for( int attrCnt = 0; attrCnt < xpp.getAttributeCount(); attrCnt++ )
                            {
                                String key = xpp.getAttributeName( attrCnt );
                                if( key.equals( "isAvailable" ) )
                                {
                                    isAvailable = ( xpp.getAttributeValue( attrCnt ) );
                                }
                                else if( key.equals( "serverName" ) )
                                {
                                    serverName = ( xpp.getAttributeValue( attrCnt ) );
                                }

                                // if (!serverName.equalsIgnoreCase("")) {
                                // //
                                // Crick20Activity.setIsPushNotificationAvailable(
                                // // this, true );
                                // Crick20Activity.setPushNotificationServer(
                                // mContext, serverName);
                                // } else {
                                // //
                                // Crick20Activity.setIsPushNotificationAvailable(
                                // // this, false );
                                // Crick20Activity.setPushNotificationServer(
                                // mContext, serverName);
                                // }

                            }

                        }

                    }
                    else if( eventType == XmlPullParser.TEXT )
                    {
                        String asd = "";
                        // result = xpp.getText();
                    }
                    else if( eventType == XmlPullParser.END_TAG )
                    {
                        if( currentTag.equalsIgnoreCase( "details" ) )
                        {
                            ContentValues values = new ContentValues();

                            values.put( "winnerTeam", winnerTeam );
                            values.put( "matchResult", matchResult );
                            values.put( "teamAscore", teamAscore );
                            values.put( "teamBscore", teamBscore );
                            values.put( "manofthematch", manofthematch );

                            int i = db.update( "schedule", values, "_id=?", new String[] { Long.toString( matchnumber ) } );

                            Log.d( "SCHEDULE DBUPDATE", "matchnumber is  :: " + matchnumber + " Schedule update : " + String.valueOf( i ) );

                        }
                        else if( currentTag.equalsIgnoreCase( "teamStanding" ) )
                        {

                            ContentValues values = new ContentValues();

                            values.put( "Team", Team );
                            values.put( "Played", Played );
                            values.put( "Won", Won );
                            values.put( "Lost", Lost );
                            values.put( "NoResult", NoResult );
                            values.put( "Points", Points );
                            values.put( "NetRunRate", NetRunRate );

                            // int i = Crick20Activity.db
                            // .update("pointTable", values, "_id=?",
                            // new String[] { Long
                            // .toString(pointTableID) });
                            //
                            // Log.d("POINT TABLE DBUPDATE",
                            // " Records update : "
                            // + String.valueOf(i));

                            pointTableID++;

                        }
                        else if( currentTag.equalsIgnoreCase( "GroupA" ) || currentTag.equalsIgnoreCase( "GroupB" ) || currentTag.equalsIgnoreCase( "GroupC" )
                                || currentTag.equalsIgnoreCase( "GroupD" ) || currentTag.equalsIgnoreCase( "Group1" ) || currentTag.equalsIgnoreCase( "Group2" ) )
                        {

                            ContentValues values = new ContentValues();

                            values.put( "Team", Team );
                            values.put( "Played", Played );
                            values.put( "Won", Won );
                            values.put( "Lost", Lost );
                            values.put( "NoResult", NoResult );
                            values.put( "Points", Points );
                            values.put( "NetRunRate", NetRunRate );

                            int i = db.update( currentTag, values, "_id=?", new String[] { Position } );

                            Log.d( "POINT TABLE DBUPDATE", " Records update : " + String.valueOf( i ) );

                            // pointTableID++;

                        }
                        else if( currentTag.equalsIgnoreCase( "orangeCapHolder" ) )
                        {

                            ContentValues values = new ContentValues();

                            values.put( "_id", orangeCapId );
                            values.put( "Player", Player );
                            values.put( "Runs", Runs );
                            values.put( "HS", HS );
                            values.put( "Sixes", Sixes );
                            values.put( "Fours", Fours );
                            values.put( "Team", Team );

                            // int i = Crick20Activity.db.update("orangeCap",
                            // values, "_id='" + orangeCapId + "'", null);

                            // int i = Crick20Activity.db
                            // .update("orangeCap", values, "_id=?",
                            // new String[] { Long
                            // .toString(ornageCapId) });

                            // Log.d("Ornage CAP TABLE DBUPDATE",
                            // " Records update : " + String.valueOf(i));

                            orangeCapId++;

                        }
                        else if( currentTag.equalsIgnoreCase( "purpleCapHolder" ) )
                        {

                            ContentValues values = new ContentValues();

                            values.put( "Player", Player );
                            values.put( "Wkts", Wkts );
                            values.put( "BBI", BBI );
                            values.put( "Maidens", Maidens );
                            values.put( "SR", SR );
                            values.put( "Team", Team );

                            // int i = Crick20Activity.db.update("purpleCap",
                            // values, "_id='" + purpleCapId + "'", null);
                            //
                            // Log.d("purple CAP TABLE DBUPDATE",
                            // " Records update : " + String.valueOf(i));

                            purpleCapId++;

                        }
                        else if( currentTag.equalsIgnoreCase( "superEights" ) )
                        {
                            ContentValues values = new ContentValues();
                            if( !A1.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", A1 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "A1" } );

                                values.clear();
                                values.put( "TeamB", A1 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "A1" } );

                                values.clear();
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( mContext );
                                Editor edit = sp.edit();
                                edit.putBoolean( "isGroupStage", false );
                                edit.commit();
                            }
                            if( !B1.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", B1 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "B1" } );

                                values.clear();
                                values.put( "TeamB", B1 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "B1" } );

                                values.clear();
                            }

                            if( !C1.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", C1 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "C1" } );

                                values.clear();
                                values.put( "TeamB", C1 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "C1" } );

                                values.clear();
                            }
                            if( !D1.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", D1 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "D1" } );

                                values.clear();
                                values.put( "TeamB", D1 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "D1" } );

                                values.clear();
                            }
                            if( !A2.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", A2 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "A2" } );

                                values.clear();
                                values.put( "TeamB", A2 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "A2" } );

                                values.clear();
                            }
                            if( !B2.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", B2 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "B2" } );

                                values.clear();
                                values.put( "TeamB", B2 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "B2" } );

                                values.clear();
                            }
                            if( !C2.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", C2 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "C2" } );

                                values.clear();
                                values.put( "TeamB", C2 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "C2" } );

                                values.clear();
                            }
                            if( !D2.equals( "" ) )
                            {
                                values.clear();
                                values.put( "TeamA", D2 );

                                int i = db.update( "schedule", values, "TeamA=?", new String[] { "D2" } );

                                values.clear();
                                values.put( "TeamB", D2 );

                                i = db.update( "schedule", values, "TeamB=?", new String[] { "D2" } );

                                values.clear();
                            }

                            // Log.d("super eights", "" + i);
                        }
                        else if( currentTag.equalsIgnoreCase( "eliminator" ) )
                        {

                            if( !TeamA.equals( "" ) && !TeamB.equals( "" ) )
                            {
                                ContentValues values = new ContentValues();

                                values.put( "TeamA", TeamA );
                                values.put( "TeamB", TeamB );

                                int i = db.update( "schedule", values, "Match=?", new String[] { matchType } );

                            }
                        }

                    }
                    eventType = xpp.next();
                }

            }

            catch( Exception e )
            {

                // Log.d("NetworkManager.DBUPDATE",
                // "EXCEPTION IS :: " + e.getMessage());
                e.printStackTrace();
            }
            Schedule.reloadView( mContext );
        }

        public void setTaskParams( int nCommandType )
        {
            m_nCommandType = nCommandType;
        }
    }

}
