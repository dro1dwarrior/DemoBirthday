package com.appndroid.crick20;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class GroupDetail extends ListActivity implements AnimationListener
{

    SQLiteDatabase db;
    static Cursor m_cursor;
    static ListAdapter m_adapter;
    TextView textHeader1;
    getDrawable drawable;
    ListView lv;
    ListView upcominglv;
    String[] from, from1, from2;
    int[] to;
    int milli_offset = 0;
    fillList ptList;

    TabHost m_tabHost;
    FrameLayout mFrameLayout;
    View menu;
    boolean menuOut = false;
    Animation anim;

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        // ProgressDialog dialog = ProgressDialog.show(GroupDetail.this, "",
        // "Please wait...", true);
        // fillRecordTask task = new fillRecordTask();
        // task.dialog = dialog;
        // task.execute();
        int recordCount = getDataFromDB();
        fillData();

        // if (!NetworkManager.isNetworkConnection) {
        // Toast.makeText(
        // getApplicationContext(),
        // "Please connect your device to network and try again for latest update",
        // Toast.LENGTH_LONG).show();
        //
        // }
        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        m_cursor = db.rawQuery( "select * from schedule where gang ='" + getIntent().getExtras().getString( "group" ) + "' AND WinnerTeam =''", null );
        Log.d( "cursor count", "" + m_cursor.getCount() );
        // RelativeLayout tv=(RelativeLayout)findViewById(R.id.upcominglayout);
        // RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
        // RelativeLayout.LayoutParams.WRAP_CONTENT, m_cursor.getCount()*95);
        // lp.setMargins(0, 10, 0, 0);
        // lp.addRule(RelativeLayout.BELOW, R.id.currentStastlayout1);
        // tv.setLayoutParams(lp);

        m_cursor.moveToFirst();
        m_adapter = new upcomingAdapter( this, m_cursor, true );
        setListAdapter( m_adapter );
        if( menuOut )
        {
            menu.setVisibility( View.INVISIBLE );
            menuOut = false;
        }
    }

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        setContentView( R.layout.airport );

        drawable = new getDrawable();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( this );
        milli_offset = sp.getInt( "offset", 0 );

        lv = (ListView) findViewById( R.id.currentstatslistview );
        lv.setEnabled( false );

        upcominglv = getListView();

        textHeader1 = (TextView) findViewById( R.id.record1 );

        to = new int[] { R.id.stat_item1, R.id.stat_item2, R.id.stat_item3, R.id.stat_item4, R.id.stat_item5, R.id.stat_item6, R.id.stat_item7 };

        if( !NetworkManager.isNetworkConnection )
        {

            NetworkManager networkmanager = new NetworkManager( GroupDetail.this );

            com.appndroid.crick20.NetworkManager.HttpAsyncConnector httpConnect = networkmanager.new HttpAsyncConnector();
            httpConnect.setTaskParams( ApplicationDefines.CommandType.COMMAND_SCHEDULE );
            httpConnect.execute();
        }

        String names = getIntent().getExtras().getString( "teamnames" );
        TextView t1 = (TextView) findViewById( R.id.title );
        t1.setText( names );

        mFrameLayout = (FrameLayout) this.findViewById( R.id.flipper );
        menu = mFrameLayout.findViewById( R.id.menu );

        final ImageView navigationImage = (ImageView) findViewById( R.id.nav );
        navigationImage.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub

                Animation anim;
                if( !menuOut )
                {
                    navigationImage.setBackgroundResource( R.drawable.navigationselected );
                    menu.setVisibility( View.VISIBLE );
                    ViewUtils.printView( "menu", menu );
                    anim = AnimationUtils.loadAnimation( GroupDetail.this, R.anim.push_right_in );
                }
                else
                {
                    navigationImage.setBackgroundResource( R.drawable.navigationunselected );
                    anim = AnimationUtils.loadAnimation( GroupDetail.this, R.anim.push_left_out );
                }
                anim.setAnimationListener( GroupDetail.this );
                // out.setAnimationListener(me);
                menu.startAnimation( anim );

            }
        } );

        // Context context = getApplicationContext();
        // LayoutInflater inflater = getLayoutInflater();
        // View toastRoot = inflater.inflate(R.layout.my_toast, null);
        // TextView t11 = (TextView) toastRoot.findViewById(R.id.toasttext);
        // t11.setText(getIntent().getExtras().getString("group"));
        // Toast toast = new Toast(context);
        // toast.setView(toastRoot);
        // toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
        // 0, 0);
        // toast.show();

    }

    public int getDataFromDB()
    {

        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        from = new String[] { "Team", "P", "W", "L", "NR", "Pts", "NRR" };

        ptList = new fillList( from );
        Cursor cur = db.query( getIntent().getExtras().getString( "group" ), null, null, null, null, null, null );
        ptList.fillRecordList( cur, ptList, "currentStats" );

        return cur.getCount();

    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if( db != null )
            db.close();
    };

    @Override
    public void onAnimationEnd( Animation animation )
    {
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
    }

    @Override
    public void onAnimationStart( Animation animation )
    {
    }

    public void fillData()
    {

        textHeader1.setText( "Points Table" );
        @SuppressWarnings("unchecked")
        SimpleAdapter adapter = new overrideAdapter( this, ptList.getFilledList(), R.layout.singlecurrntstat_layout, from, to, "currentStats" );
        lv.setAdapter( adapter );

    }

    private class fillRecordTask extends AsyncTask< String, Void, String >
    {

        ProgressDialog dialog = null;
        int recordCount;

        // can use UI thread here
        protected void onPreExecute()
        {

        }

        // automatically done on worker thread (separate from UI thread)
        protected String doInBackground( final String... args )
        {
            recordCount = getDataFromDB();
            return "";
        }

        // can use UI thread here
        protected void onPostExecute( final String result )
        {
            if( dialog != null )
            {
                dialog.dismiss();
                dialog = null;
            }

            fillData();

        }

    }

    public class upcomingAdapter extends CursorAdapter
    {
        private LayoutInflater inflater;

        public upcomingAdapter( Context context, Cursor c, boolean autoRequery )
        {
            super( context, c, autoRequery );
            // TODO Auto-generated constructor stub
            inflater = LayoutInflater.from( context );
        }

        @Override
        public void bindView( View view, Context context, Cursor cursor )
        {
            ImageView imgTeamA = (ImageView) view.findViewById( R.id.upcoming_TeamAicon );
            ImageView imgTeamB = (ImageView) view.findViewById( R.id.upcoming_TeamBicon );

            TextView TeamAName = (TextView) view.findViewById( R.id.upcoming_TeamAName );
            TextView TeamBName = (TextView) view.findViewById( R.id.upcoming_TeamBName );

            TextView txtdate = (TextView) view.findViewById( R.id.upcoming_date );
            TextView txttime = (TextView) view.findViewById( R.id.upcoming_time );
            TextView txtvenue = (TextView) view.findViewById( R.id.upcoming_venue );

            String szTeamA = cursor.getString( cursor.getColumnIndex( "TeamA" ) );
            imgTeamA.setImageResource( drawable.getIcon( szTeamA ) );

            String szTeamB = cursor.getString( cursor.getColumnIndex( "TeamB" ) );
            imgTeamB.setImageResource( drawable.getIcon( szTeamB ) );

            TeamAName.setText( szTeamA );
            TeamBName.setText( szTeamB );

            String strDt = cursor.getString( cursor.getColumnIndex( "Date" ) );
            String[] strarr = strDt.split( " " );
            txtdate.setText( strarr[0].trim() + " " + drawable.getMonthName( strarr[1] ) + " (" + cursor.getString( cursor.getColumnIndex( "Other1" ) ).trim() + ")" );
            String time = cursor.getString( cursor.getColumnIndex( "GMT" ) ).trim();

            txttime.setText( time + " GMT" );
            txtvenue.setText( cursor.getString( cursor.getColumnIndex( "Venue" ) ).trim() );

        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent )
        {
            // TODO Auto-generated method stub
            View view = inflater.inflate( R.layout.upcoming_row, parent, false );
            return view;
        }

    }
}
