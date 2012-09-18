package com.appndroid.crick20.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appndroid.crick20.R;
import com.appndroid.crick20.ui.NetworkManager.HttpAsyncConnector;

public class HomeScreen extends Activity
{

    LinearLayout scheduleClick, pointsTableClick, aboutClick, settingsClick;
    ImageView mainIcon;
    WebView webview;
    Context mcontext;
    private NetworkManager networkmanager;
    Gallery gallery;
    Cursor mCursor;
    BaseAdapter mAdapter;
    List< String > teamA;
    List< String > teamB;
    List< String > matchDates;
    getDrawable drawable;

    LinearLayout mDotsLayout;
    TextView counter1;
    TextView counter2;

    // private int mDotsCount;
    // static TextView mDotsText[];

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.home_new );

        mcontext = this;

        // WebView wv = (WebView) findViewById(R.id.browser_home);
        // wv.getSettings().setJavaScriptEnabled(true);
        // wv.setBackgroundColor(Color.TRANSPARENT);
        // String html =
        // "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadboltads.net/show_app_ad.js?section_id=475192381'></script></body></html>";
        // wv.loadData(html, "text/html", "utf-8");

        gallery = (Gallery) findViewById( R.id.home_gallery );
        mDotsLayout = (LinearLayout) findViewById( R.id.dotsLayout );
        mDotsLayout.setVisibility( View.GONE );
        counter1 = (TextView) findViewById( R.id.counter1 );
        counter2 = (TextView) findViewById( R.id.counter2 );

        drawable = new getDrawable();
        Utils.getDB( this );
        mCursor = Utils.db.query( "schedule", null, null, null, null, null, null );
        // mCursor = Utils.db.query( "schedule", null, "MatchUrl != '' AND MatchResult == '' ", null, null, null, null
        // );
        mCursor.moveToFirst();

        Log.d( "HomeScreen-onCreate", " Cursor Count for LIVE MATCHES" );

        if( mCursor.getCount() > 0 )
        {
            teamA = new ArrayList< String >();
            teamB = new ArrayList< String >();
            matchDates = new ArrayList< String >();
            do
            {
                teamA.add( mCursor.getString( mCursor.getColumnIndex( "TeamA" ) ) );
                teamB.add( mCursor.getString( mCursor.getColumnIndex( "TeamB" ) ) );
                matchDates.add( mCursor.getString( mCursor.getColumnIndex( "Date" ) ) );
            }
            while( mCursor.moveToNext() );

            Log.d( "HomeScreen-onCreate", " Total items in A added : " + teamA.size() );
            mAdapter = new MyAdapter( this );
            gallery.setAdapter( mAdapter );

            if( mCursor.getCount() > 1 )
            {
                mDotsLayout.setVisibility( View.VISIBLE );
                counter1.setBackgroundResource( R.drawable.countershape_selected );
                counter2.setBackgroundResource( R.drawable.countershape );
            }
            gallery.setOnItemClickListener( new OnItemClickListener()
            {
                public void onItemClick( AdapterView parent, View v, int position, long id )
                {

                    Log.d( "Gallery", "Position = " + position );
                }
            } );

            gallery.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected( AdapterView adapterView, View view, int pos, long l )
                {
                    if( mDotsLayout.getVisibility() == View.VISIBLE )
                    {
                        if( pos == 0 )
                        {
                            Log.d( "", "FIRSTTTTT" );
                            counter1.setBackgroundResource( R.drawable.countershape_selected );
                            counter2.setBackgroundResource( R.drawable.countershape );
                        }
                        else
                        {
                            Log.d( "", "POSSSS" + pos );
                            counter2.setBackgroundResource( R.drawable.countershape_selected );
                            counter1.setBackgroundResource( R.drawable.countershape );
                        }
                    }
                }

                @Override
                public void onNothingSelected( AdapterView adapterView )
                {

                }
            } );

        }
        networkmanager = new NetworkManager( HomeScreen.this );

        if( !NetworkManager.isDataFetched )
        {

            HttpAsyncConnector httpConnect = networkmanager.new HttpAsyncConnector();
            httpConnect.setTaskParams( ApplicationDefines.CommandType.COMMAND_SCHEDULE );
            httpConnect.execute();
        }

        scheduleClick = (LinearLayout) findViewById( R.id.ll_schedule );
        scheduleClick.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
                Intent schIntent = new Intent( mcontext, Schedule.class );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                mcontext.startActivity( schIntent );
            }
        } );

        pointsTableClick = (LinearLayout) findViewById( R.id.ll_points );
        pointsTableClick.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( mcontext );
                boolean isGroupStage = sp.getBoolean( "isGroupStage", true );
                Intent schIntent;
                if( isGroupStage )
                    schIntent = new Intent( mcontext, GroupTab.class );
                else
                    schIntent = new Intent( mcontext, tabtest.class );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                mcontext.startActivity( schIntent );
            }
        } );

        settingsClick = (LinearLayout) findViewById( R.id.ll_settings );
        settingsClick.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
                Intent schIntent = new Intent( mcontext, Settings.class );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                mcontext.startActivity( schIntent );
            }
        } );

        aboutClick = (LinearLayout) findViewById( R.id.ll_about );
        aboutClick.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
                Intent schIntent = new Intent( mcontext, About.class );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                mcontext.startActivity( schIntent );
            }
        } );
        mainIcon = (ImageView) findViewById( R.id.iv_main_icon );
        mainIcon.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
                Intent schIntent = new Intent( mcontext, LiveLayout.class );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                schIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                mcontext.startActivity( schIntent );

            }
        } );

        // aboutClick = (ImageView) findViewById(R.id.about_img);
        // settingsClick = (ImageView) findViewById(R.id.settings_img);
        // scheduleClick=(ImageView)findViewById(R.id.sch_img);
        // pointsTableClick = (ImageView) findViewById(R.id.standings_img);
        //
        // scheduleClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // Intent schIntent = new Intent(mcontext,
        // Schedule.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        //
        // }
        // });
        //
        // pointsTableClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // Intent schIntent = new Intent(mcontext,
        // GroupTab.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        //
        // }
        // });
        //
        // settingsClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // Intent schIntent = new Intent(mcontext,
        // Settings.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        // }
        // });
        //
        // aboutClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // Intent schIntent = new Intent(mcontext,
        // About.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        // }
        // });
        //
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        NetworkManager.isDataFetched = false;
    }

    public class MyAdapter extends BaseAdapter
    {
        Context context;

        MyAdapter( Context c )
        {
            context = c;
        }

        @Override
        public int getCount()
        {
            // TODO Auto-generated method stub
            return teamA.size();
        }

        @Override
        public Object getItem( int position )
        {
            // TODO Auto-generated method stub
            return teamA.toArray()[position];
        }

        @Override
        public long getItemId( int position )
        {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            // TODO Auto-generated method stub

            View rowView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.galleryitem, null );
            TextView textDate = (TextView) rowView.findViewById( R.id.galleryitem_text1 );
            TextView text2 = (TextView) rowView.findViewById( R.id.galleryitem_text2 );
            TextView text3 = (TextView) rowView.findViewById( R.id.galleryitem_text3 );
            ImageView flag1 = (ImageView) rowView.findViewById( R.id.galleryitem_flag1 );
            ImageView flag2 = (ImageView) rowView.findViewById( R.id.galleryitem_flag2 );
            TextView textTeamA = (TextView) rowView.findViewById( R.id.galleryitem_team1 );
            TextView textTeamB = (TextView) rowView.findViewById( R.id.galleryitem_team2 );

            textDate.setText( matchDates.get( position ).toString() );
            textTeamA.setText( drawable.getTeamShortCode( teamA.get( position ).toString() ) );
            textTeamB.setText( drawable.getTeamShortCode( teamB.get( position ).toString() ) );
            flag1.setImageResource( drawable.getIcon( teamA.get( position ).toString() ) );
            flag2.setImageResource( drawable.getIcon( teamB.get( position ).toString() ) );
            return rowView;
        }

    }
}
