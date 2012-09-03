package com.appndroid.crick20;

import java.io.InputStream;
import java.io.StringReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appndroid.crick20.NetworkManager.HttpAsyncConnector;

public class Schedule extends ListActivity
{
    static Cursor m_cursor;
    static ListAdapter m_adapter;
    getDrawable drawable;
    int milli_offset = 0;
    static ListView lv;
    private NetworkManager networkmanager;

    private static final int GROUP_TABLE = Menu.FIRST;
    private static final int ABOUT = 2;

    public static SQLiteDatabase db;

    private String[] winnerTeamCounter = null;
    private String[] matchUrl = null;

    private boolean m_isIpl = false;
    private String currentTag;
    int m_xmlTagId = 0;
    private ArrayList< String > matchesArray = new ArrayList< String >();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        setContentView( R.layout.schedule );

        lv = getListView();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( this );
        milli_offset = sp.getInt( "offset", 0 );

        networkmanager = new NetworkManager( Schedule.this );

        if( !NetworkManager.isDataFetched )
        {

            HttpAsyncConnector httpConnect = networkmanager.new HttpAsyncConnector();
            httpConnect.setTaskParams( ApplicationDefines.CommandType.COMMAND_SCHEDULE );
            httpConnect.execute();
        }

        drawable = new getDrawable();
        // SQLiteDatabase db;
        // MyPagerAdapter adapter = new MyPagerAdapter();
        // ViewPager myPager = (ViewPager) findViewById( R.id.myfivepanelpager
        // );
        // myPager.setAdapter( adapter );
        // myPager.setCurrentItem( 0 );
        // fetchliveurls();

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        super.onCreateOptionsMenu( menu );
        try
        {
            menu.add( 0, GROUP_TABLE, 0, "Group Table" ).setShortcut( '2', 'a' ).setIcon( android.R.drawable.ic_menu_my_calendar );
            menu.add( 0, ABOUT, 0, "About" ).setIcon( android.R.drawable.ic_menu_info_details );
        }
        catch( Exception e )
        {
            System.out.println( e.toString() );
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {

        try
        {
            switch( item.getItemId() )
            {

            case GROUP_TABLE:
                Intent intent3 = new Intent( this, GroupTab.class );
                startActivity( intent3 );
                return true;

            case ABOUT:
                Intent i = new Intent( this, About.class );
                startActivity( i );
                return true;
            }
        }
        catch( Exception e )
        {

            System.out.println( e.toString() + item.getItemId() );
            return false;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if( db != null )
            db.close();
    };

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        m_cursor = db.rawQuery( "select * from schedule", null );
        m_cursor.moveToFirst();
        m_adapter = new scheduleAdapter( this, m_cursor, true );
        setListAdapter( m_adapter );
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        NetworkManager.isDataFetched = false;
    }

    public static void reloadView( final Context context )
    {
        m_cursor.requery();
        if( lv != null )
        {
            lv.invalidateViews();
            lv.invalidate();
        }
    }

    public class scheduleAdapter extends CursorAdapter
    {
        private LayoutInflater inflater;

        public scheduleAdapter( Context context, Cursor cursor, boolean autoRequery )
        {
            super( context, cursor, autoRequery );
            // TODO Auto-generated constructor stub
            inflater = LayoutInflater.from( context );
        }

        @Override
        public void bindView( View view, Context context, Cursor cursor )
        {
            // TODO Auto-generated method stub
            ImageView imgTeamA = (ImageView) view.findViewById( R.id.img1 );
            ImageView imgTeamB = (ImageView) view.findViewById( R.id.img2 );
            TextView txtstadium = (TextView) view.findViewById( R.id.textview_stadium );
            TextView txtmatch = (TextView) view.findViewById( R.id.textview_match );
            TextView txtdate = (TextView) view.findViewById( R.id.date );
            TextView txttime = (TextView) view.findViewById( R.id.textview_time );
            TextView txtgroup = (TextView) view.findViewById( R.id.textview_group );

            if( cursor.getString( cursor.getColumnIndex( "gang" ) ).contains( "SE" ) )
                txtgroup.setText( "Super Eight's" );
            else
                txtgroup.setText( cursor.getString( cursor.getColumnIndex( "gang" ) ) );
            String time = cursor.getString( cursor.getColumnIndex( "GMT" ) );

            SimpleDateFormat df1 = new SimpleDateFormat( "HH:mm:ss" );
            Date d = null;
            try
            {
                d = df1.parse( time + ":00" );
            }
            catch( ParseException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Long lngTime = d.getTime();
            lngTime += ( milli_offset );
            Date d2 = new Date( lngTime );
            int minutes = d2.getMinutes();
            String min;
            if( minutes == 0 )
                min = "00";
            else
                min = String.valueOf( minutes );
            String szTeamA = cursor.getString( cursor.getColumnIndex( "TeamA" ) );
            imgTeamA.setImageResource( drawable.getIcon( szTeamA ) );
            String szTeamB = cursor.getString( cursor.getColumnIndex( "TeamB" ) );
            imgTeamB.setImageResource( drawable.getIcon( szTeamB ) );
            txtmatch.setText( drawable.getTeamShortCode( szTeamA ) + " vs " + drawable.getTeamShortCode( szTeamB ) );
            txttime.setText( cursor.getString( cursor.getColumnIndex( "Venue" ) ) );

            String strDt = cursor.getString( cursor.getColumnIndex( "Date" ) );
            String[] strarr = strDt.split( " " );
            txtdate.setText( strarr[0] + " " + drawable.getMonthName( strarr[1] ) + " (" + cursor.getString( cursor.getColumnIndex( "Other1" ) ) + ")" );

            txtstadium.setText( time + " GMT / " + d2.getHours() + ":" + min + " Local" );
        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent )
        {
            // TODO Auto-generated method stub
            View view = inflater.inflate( R.layout.one, parent, false );
            return view;
        }

    }

    @Override
    protected void onListItemClick( android.widget.ListView l, View v, int nPosition, long id )
    {

        Cursor cur = db.query( "schedule", null, null, null, null, null, null );

        int counter = cur.getCount();
        winnerTeamCounter = new String[counter];
        matchUrl = new String[counter];
        cur.moveToFirst();
        while( cur.isAfterLast() == false )
        {
            int currentposotion = cur.getPosition();
            String winnerTeam = cur.getString( cur.getColumnIndex( "WinnerTeam" ) ).trim();
            // Log.d("on item click", "match number" + cur.getString(cur.getColumnIndex("Match")).trim()
            // + "match url" + cur.getString(cur.getColumnIndex("MatchUrl")).trim());
            winnerTeamCounter[currentposotion] = winnerTeam;
            matchUrl[currentposotion] = cur.getString( cur.getColumnIndex( "MatchUrl" ) ).trim();
            cur.moveToNext();

        }

        cur.close();

        if( winnerTeamCounter[nPosition].equals( "" ) && matchUrl[nPosition].equals( "" ) )
        {
            Toast.makeText( Schedule.this, "Match not started yet", Toast.LENGTH_SHORT ).show();

        }
        else if( !matchUrl[nPosition].equals( "" ) && winnerTeamCounter[nPosition].equals( "" ) )
        {
            Toast.makeText( Schedule.this, "open live score activity", Toast.LENGTH_SHORT ).show();
        }
        else
        {

            Toast.makeText( Schedule.this, "Result has been declared."+nPosition, Toast.LENGTH_SHORT ).show();
             Intent scoreIntent = new Intent( Schedule.this,PastMatches.class );
             scoreIntent.putExtra( "schId", nPosition + 1 );
             startActivity( scoreIntent );

        }

    }

    private void fetchliveurls()
    {
        HttpClient hc;
        String szResponse = "";
        boolean bSuccess = false;
        HttpGet get = null;
        String str = null;
        try
        {
            hc = new DefaultHttpClient();
            get = new HttpGet( "http://synd.cricbuzz.com/sify/" );
            HttpResponse rp = hc.execute( get );
            InputStream data = rp.getEntity().getContent();
            // szResponse =
            // DhamakaApplication.getApplication().generateString(data);
            StringBuffer buffer = new StringBuffer();
            byte[] b = new byte[4096];
            for( int n; ( n = data.read( b ) ) != -1; )
            {
                buffer.append( new String( b, 0, n ) );
            }
            str = buffer.toString();
            System.out.println( str );
            Log.d( "NetworkManager.HttpAsyncConnector-doInBackground()", "Response is ::: " + szResponse );

            str = str.replace( "\n", "" );
            str = str.replace( "\t", "" );
            // str="<matches><match><seriesName>England in Sri Lanka 2012</seriesName><team1>Sri Lanka</team1><team2>England</team2><startdate>03 04 2012</startdate><enddate>07 04 2012</enddate><type>TEST</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2083</series-statistics-url></match><match><seriesName>Indian Premier League 2012</seriesName><team1>Kolkata Knight Riders</team1><team2>Delhi Daredevils</team2><startdate>05 04 2012</startdate><enddate>05 04 2012</enddate><type>T20</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2115</series-statistics-url></match><match><seriesName>Indian Premier League 2012</seriesName><team1>Chennai Super Kings</team1><team2>Mumbai Indians</team2><startdate>05 04 2012</startdate><enddate>05 04 2012</enddate><type>T20</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2115</series-statistics-url></match></matches>";
            xmlParseMatch( str );

            Log.d( "aaaaaaaaa size", "" + matchesArray.size() );

        }
        catch( SocketException e )
        {

        }
        catch( UnknownHostException ex )
        {

        }
        catch( Exception e )
        {

        }
    }

    private void xmlParseMatch( String xmlData )
    {

        matchesArray.clear();
        try
        {
            Calendar c = Calendar.getInstance();
            int currentdate = c.get( Calendar.DATE );
            Log.d( "Date", "Date is :: " + currentdate );
            int date = 0;

            Date d = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat( "dd MM yyyy" );
            String CDate = dateFormat.format( d );

            String teamA = null, teamB = null, scoreUrl = null, matchDate = null;
            ContentValues values = new ContentValues();
            // TODO Auto-generated method stub
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware( true );
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( xmlData ) );
            int eventType = xpp.getEventType();
            while( eventType != XmlPullParser.END_DOCUMENT )
            {
                if( eventType == XmlPullParser.START_DOCUMENT )
                {
                    System.out.println( "Start document" );
                }
                else if( eventType == XmlPullParser.END_DOCUMENT )
                {
                    System.out.println( "End document" );
                }
                else if( eventType == XmlPullParser.START_TAG )
                {
                    System.out.println( "Start tag " + xpp.getName() );
                }
                else if( eventType == XmlPullParser.END_TAG )
                {
                    if( xpp.getName() != null && xpp.getName().equalsIgnoreCase( "match" ) && m_isIpl )
                    {
                        Log.d( "Ongoing", "Current Date is :: " + currentdate );
                        Log.d( "Ongoing", "Match Date is :: " + date );
                        // if(date == currentdate)
                        // {
                        // mContext.getApplicationContext().getContentResolver()
                        // .insert(DataProvider.Ongoing.CONTENT_URI,
                        // values);
                        // mContext.getApplicationContext().getContentResolver()
                        // .notifyChange(DataProvider.Ongoing.CONTENT_URI,
                        // null);
                        //
                        // }

                    }
                    System.out.println( "End tag " + xpp.getName() );
                }
                else if( eventType == XmlPullParser.TEXT )
                {
                    System.out.println( "Text " + xpp.getText() );
                }
                if( xpp.getName() != null )
                    currentTag = xpp.getName();
                if( eventType == XmlPullParser.START_TAG )
                {
                    if( currentTag.equalsIgnoreCase( "matches" ) )
                    {
                        // Do nothing

                    }
                    else if( currentTag.equalsIgnoreCase( "match" ) )
                    {
                        // Do nothing
                        m_isIpl = false;
                    }
                    else if( currentTag.equalsIgnoreCase( "seriesName" ) )
                    {
                        m_xmlTagId = 1;

                    }
                    else if( currentTag.equalsIgnoreCase( "team1" ) && m_isIpl )
                    {
                        m_xmlTagId = 2;

                    }
                    else if( currentTag.equalsIgnoreCase( "team2" ) && m_isIpl )
                    {
                        m_xmlTagId = 3;
                    }
                    else if( currentTag.equalsIgnoreCase( "scores-url" ) && m_isIpl )
                    {
                        m_xmlTagId = 4;
                    }
                    else if( currentTag.equalsIgnoreCase( "full-commentary-url" ) && m_isIpl )
                    {
                        m_xmlTagId = 5;
                    }
                    else if( currentTag.equalsIgnoreCase( "squads-url" ) && m_isIpl )
                    {
                        m_xmlTagId = 6;
                    }
                    else if( currentTag.equalsIgnoreCase( "highlights-url" ) && m_isIpl )
                    {
                        m_xmlTagId = 7;
                    }
                    else if( currentTag.equalsIgnoreCase( "startdate" ) && m_isIpl )
                    {
                        m_xmlTagId = 8;
                    }
                    else if( ( currentTag.equalsIgnoreCase( "type" ) || currentTag.equalsIgnoreCase( "enddate" ) ) && m_isIpl )
                    {
                        m_xmlTagId = 100;
                    }
                }

                else if( eventType == XmlPullParser.TEXT )
                {
                    switch( m_xmlTagId )
                    {
                    case 1: // seriesName
                        // if
                        // (xpp.getText().equals("Indian Premier League 2012"))
                        // {
                        m_isIpl = true;
                        // }
                        break;
                    case 2: // team1
                        Log.d( "aaa", "teamA" + xpp.getText() );
                        if( !xpp.getText().equalsIgnoreCase( "\n" ) )
                            teamA = xpp.getText();
                        break;
                    case 3: // team2
                        Log.d( "aaa", "teamB" + xpp.getText() );
                        if( !xpp.getText().equalsIgnoreCase( "\n" ) )
                            teamB = xpp.getText();
                        break;
                    case 4: // scores-url
                        Log.d( "aaa", "URL" + xpp.getText() );
                        if( !xpp.getText().equalsIgnoreCase( "\n" ) )
                            scoreUrl = xpp.getText();
                        // if(CDate.equals(matchDate))
                        // {
                        if( teamA != null && teamB != null && scoreUrl != null )
                        {

                            // matchesArray.add(teamA + "||" + teamB + "||"
                            // + scoreUrl);
                            ContentValues cvalues = new ContentValues();
                            cvalues.put( "MatchUrl", scoreUrl );

                            int i = db.update( "schedule", cvalues, "TeamA=? AND TeamB = ? AND Date=?", new String[] { "India", "Afghanistan", "19 09 2012" } );
                            // Log.d("aaaaaaaaa","number of updated columns"+i);
                            teamA = null;
                            teamB = null;
                            scoreUrl = null;

                        }

                        // }

                        break;
                    case 5: // full-commentary-url

                        break;
                    case 6: // squads-url

                        break;
                    case 7: // highlights-url

                        break;
                    case 8:// date
                        if( !xpp.getText().equalsIgnoreCase( "\n" ) )
                            matchDate = xpp.getText();

                        break;
                    default:
                        break;
                    }
                }

                eventType = xpp.next();

            }
            // DhamakaApplication.getApplication().setOngoingDownloaded(true);
            // Intent intent = new Intent( mContext,IPLLauncher.class );
            // mContext.startActivity(intent);
            // Activity activity =
            // DhamakaApplication.getApplication().getCurrentActivity();
            // Log.d( "Ongoing-xmlParseMatch()" , "current activity is :: "
            // +activity);
            // activity.finish();

        }
        catch( Exception e )
        {
            // TODO: handle exception

            e.printStackTrace();
        }

    }

}
