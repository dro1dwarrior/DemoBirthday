package com.appndroid.crick20.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.appndroid.crick20.R;

public class LayoutOne extends Fragment
{

    SQLiteDatabase db;
    getDrawable drawable;
    static ListView lv;
    static Cursor m_cursor;
    static ListAdapter m_adapter;
    ListView upcominglv;
    int[] to;
    TextView textHeader1;
    fillList ptList;
    String[] from;
    Context context;

    public static Fragment newInstance( Context context )
    {
        LayoutOne f = new LayoutOne();
        return f;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        ViewGroup root = (ViewGroup) inflater.inflate( R.layout.info_layout, null );

        lv = (ListView) root.findViewById( R.id.pointsTable_group1 );
        lv.setEnabled( false );

        upcominglv = (ListView) root.findViewById( R.id.upcomignlv_group1 );

        textHeader1 = (TextView) root.findViewById( R.id.header_group1 );
        

        to = new int[] { R.id.stat_item1, R.id.stat_item2, R.id.stat_item3, R.id.stat_item4, R.id.stat_item5, R.id.stat_item6, R.id.stat_item7 };
        return root;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        context = this.getActivity();
        drawable = new getDrawable();
    }

    @Override
    public void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        int recordCount = getDataFromDB( "Group1" );
        fillData( lv );

        super.onResume();
        db = getActivity().openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        m_cursor = db.rawQuery( "select * from schedule where gang ='" + "Group1" + "' AND WinnerTeam =''", null );
        m_cursor.moveToFirst();
        m_adapter = new upcomingAdapter( context, m_cursor, true );
        upcominglv.setAdapter( m_adapter );

    }

    public int getDataFromDB( String groupName )
    {

        db = getActivity().openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        from = new String[] { "Team", "P", "W", "L", "NR", "Pts", "NRR" };

        ptList = new fillList( from );
        Cursor cur = db.query( groupName, null, "Team <>''", null, null, null, null );
        ptList.fillRecordList( cur, ptList, "currentStats" );

        return cur.getCount();

    }

    public void fillData( ListView lst )
    {

        textHeader1.setText( "Points Table" );
        @SuppressWarnings("unchecked")
        SimpleAdapter adapter = new overrideAdapter( getActivity(), ptList.getFilledList(), R.layout.singlecurrntstat_layout, from, to, "currentStats" );
        lst.setAdapter( adapter );

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

            String strDt = cursor.getString( cursor.getColumnIndex( "Date" ) ).trim();
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
            View view = inflater.inflate( R.layout.upcoming_row_2, parent, false );
            return view;
        }

    }

}
