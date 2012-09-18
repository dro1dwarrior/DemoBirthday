package com.appndroid.crick20.ui;

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
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appndroid.crick20.R;

public class Schedule extends ListActivity implements AnimationListener
{
    static Cursor m_cursor;
    static ListAdapter m_adapter;
    getDrawable drawable;
    int milli_offset = 0;
    static ListView lv;

    FrameLayout mFrameLayout;
    View menu;
    boolean menuOut = false;
    Animation anim;
    ImageView navigationImage;
    static int listItemToSelect = -1;
    WebView wv;

    private static final int GROUP_TABLE = Menu.FIRST;
    private static final int SUPER8 = 2;
    private static final int ABOUT = 3;

    public static SQLiteDatabase db;

    private String[] winnerTeamCounter = null;
    private String[] matchUrl = null;
    private String[] teamA = null;
    private String[] teamB = null;

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

        Utils.setContext( this );
        lv = getListView();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( this );
        milli_offset = sp.getInt( "offset", 0 );

        drawable = new getDrawable();
        // SQLiteDatabase db;
        // MyPagerAdapter adapter = new MyPagerAdapter();
        // ViewPager myPager = (ViewPager) findViewById( R.id.myfivepanelpager
        // );
        // myPager.setAdapter( adapter );
        // myPager.setCurrentItem( 0 );
        // fetchliveurls();

        mFrameLayout = (FrameLayout) this.findViewById( R.id.flipper );
        menu = mFrameLayout.findViewById( R.id.menu );

        navigationImage = (ImageView) findViewById( R.id.nav );
        navigationImage.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub

                // Animation anim;
                // if (!menuOut) {
                // navigationImage
                // .setBackgroundResource(R.drawable.navigationselected);
                // menu.setVisibility(View.VISIBLE);
                // ViewUtils.printView("menu", menu);
                // anim = AnimationUtils.loadAnimation(Schedule.this,
                // R.anim.push_right_in);
                // } else {
                // navigationImage
                // .setBackgroundResource(R.drawable.navigationunselected);
                // anim = AnimationUtils.loadAnimation(Schedule.this,
                // R.anim.push_left_out);
                // }
                // anim.setAnimationListener(Schedule.this);
                // // out.setAnimationListener(me);
                // menu.startAnimation(anim);
                callEvent();
                //
            }
        } );

        // wv = (WebView) findViewById(R.id.browser_schedule);
        // wv.getSettings().setJavaScriptEnabled(true);
        // wv.setBackgroundColor(Color.TRANSPARENT);
        // String html =
        // "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadboltads.net/show_app_ad.js?section_id=581427376'></script></body></html>";
        // wv.loadData(html, "text/html", "utf-8");

    }

    MenuDialog menuDialog;

    public void callEvent()
    {

        // if (menuDialog == null) {

        menuDialog = new MenuDialog( this, "schedule" );
        // }
        menuDialog.setCancelable( true );
        menuDialog.setCanceledOnTouchOutside( true );
        menuDialog.show();
    }

    @Override
    public boolean onKeyUp( int keyCode, KeyEvent event )
    {
        // TODO Auto-generated method stub
        // if (keyCode == KeyEvent.KEYCODE_BACK) {
        // if (menuOut) {
        // anim = AnimationUtils.loadAnimation(Schedule.this,
        // R.anim.push_left_out);
        // anim.setAnimationListener(Schedule.this);
        // menu.startAnimation(anim);
        //
        // navigationImage
        // .setBackgroundResource(R.drawable.navigationunselected);
        // return false;
        // }
        if( keyCode == KeyEvent.KEYCODE_MENU )
            callEvent();
        return super.onKeyUp( keyCode, event );
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu) {
    // super.onCreateOptionsMenu(menu);
    // try {
    // menu.add(0, GROUP_TABLE, 0, "Group Table").setShortcut('2', 'a')
    // .setIcon(android.R.drawable.ic_menu_my_calendar);
    // menu.add(0, SUPER8, 0, "Super 8").setIcon(
    // android.R.drawable.ic_menu_my_calendar);
    // menu.add(0, ABOUT, 0, "About").setIcon(
    // android.R.drawable.ic_menu_info_details);
    // } catch (Exception e) {
    // System.out.println(e.toString());
    // }
    // return true;
    // }
    //
    // @Override
    // public boolean onOptionsItemSelected(MenuItem item) {
    //
    // try {
    // switch (item.getItemId()) {
    //
    // case GROUP_TABLE:
    // Intent intent3 = new Intent(this, GroupTab.class);
    // startActivity(intent3);
    // return true;
    //
    // case SUPER8:
    // Intent intent4 = new Intent(this, tabtest.class);
    // startActivity(intent4);
    // return true;
    //
    // case ABOUT:
    // Intent i = new Intent(this, About.class);
    // startActivity(i);
    // return true;
    // }
    // } catch (Exception e) {
    //
    // System.out.println(e.toString() + item.getItemId());
    // return false;
    // }
    //
    // return super.onOptionsItemSelected(item);
    // }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if( db != null )
            db.close();
    };

    @Override
    protected void onRestart()
    {
        // TODO Auto-generated method stub
        Log.d( "schedule", "on restart" );
        super.onRestart();
        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        m_cursor = db.rawQuery( "select * from schedule", null );

    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        m_cursor = db.rawQuery( "select * from schedule", null );
        m_cursor.moveToFirst();
        m_adapter = new scheduleAdapter( this, m_cursor, false );
        setListAdapter( m_adapter );
        m_cursor.moveToFirst();
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat( "dd MM yyyy" );
        String currentDate = dateFormat.format( d );
        boolean isSelected = false;
        do
        {
            String date = m_cursor.getString( m_cursor.getColumnIndex( "Date" ) ).trim();
            if( date.equals( currentDate ) && !isSelected )
            {
                listItemToSelect = m_cursor.getPosition();
                isSelected = true;
                break;
            }

        }
        while( m_cursor.moveToNext() );
        if( listItemToSelect > -1 )
            lv.setSelection( listItemToSelect );
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
    }

    public static void reloadView( final Context context )
    {
        Utils.getDB( context );
        m_cursor = Utils.db.rawQuery( "select * from schedule", null );
        if( lv != null )
        {
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

            if( cursor.getString( cursor.getColumnIndex( "gang" ) ).contains( "1" ) || cursor.getString( cursor.getColumnIndex( "gang" ) ).contains( "2" ) )
                txtgroup.setText( "Super Eight's" );
            else
                txtgroup.setText( cursor.getString( cursor.getColumnIndex( "gang" ) ).replace( "Group", "Group " ) );
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

            String strDt = cursor.getString( cursor.getColumnIndex( "Date" ) ).trim();
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
        teamA = new String[counter];
        teamB = new String[counter];
        cur.moveToFirst();
        while( cur.isAfterLast() == false )
        {
            int currentposotion = cur.getPosition();
            String winnerTeam = cur.getString( cur.getColumnIndex( "WinnerTeam" ) ).trim();
            // Log.d("on item click", "match number" +
            // cur.getString(cur.getColumnIndex("Match")).trim()
            // + "match url" +
            // cur.getString(cur.getColumnIndex("MatchUrl")).trim());
            winnerTeamCounter[currentposotion] = winnerTeam;
            matchUrl[currentposotion] = cur.getString( cur.getColumnIndex( "MatchUrl" ) ).trim();
            teamA[currentposotion] = cur.getString( cur.getColumnIndex( "TeamA" ) ).trim();
            teamB[currentposotion] = cur.getString( cur.getColumnIndex( "TeamB" ) ).trim();
            cur.moveToNext();

        }

        cur.close();

        if( winnerTeamCounter[nPosition].equals( "" ) && matchUrl[nPosition].equals( "" ) )
        {
            Toast.makeText( Schedule.this, "Match not started yet", Toast.LENGTH_SHORT ).show();

        }
        else if( !matchUrl[nPosition].equals( "" ) && winnerTeamCounter[nPosition].equals( "" ) )
        {
            // Toast.makeText( Schedule.this, "open live score activity"+teamA[nPosition]+teamB[nPosition],
            // Toast.LENGTH_SHORT ).show();
            Intent scoreIntent = new Intent( Schedule.this, LiveLayout.class );
            scoreIntent.putExtra( "match", teamA[nPosition] + "||" + teamB[nPosition] + "||" + matchUrl[nPosition] );
            startActivity( scoreIntent );
        }
        else
        {

            // Toast.makeText( Schedule.this,
            // "Result has been declared."+nPosition, Toast.LENGTH_SHORT
            // ).show();
            Intent scoreIntent = new Intent( Schedule.this, PastMatches.class );
            scoreIntent.putExtra( "schId", nPosition + 1 );
            startActivity( scoreIntent );

        }

    }

    @Override
    public void onAnimationEnd( Animation animation )
    {
        // TODO Auto-generated method stub
        ViewUtils.printView( "menu", menu );
        menuOut = !menuOut;
        if( !menuOut )
        {
            menu.setVisibility( View.INVISIBLE );
        }

    }

    @Override
    public void onAnimationRepeat( Animation animation )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart( Animation animation )
    {
        // TODO Auto-generated method stub

    }

}
