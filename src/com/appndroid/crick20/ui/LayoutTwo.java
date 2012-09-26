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
import com.appndroid.crick20.ui.LayoutOne.upcomingAdapter;

public class LayoutTwo extends Fragment
{
    SQLiteDatabase db;
    getDrawable drawable;
    static ListView lv1;
    static Cursor m_cursor1;
    static ListAdapter  m_adapter1;
    ListView upcominglv1;
    int[] to;
    TextView  textHeader2;
    fillList ptList;
    String[] from;
    Context context;

    public static Fragment newInstance( Context context )
    {
        LayoutTwo f = new LayoutTwo();

        return f;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {
        ViewGroup root = (ViewGroup) inflater.inflate( R.layout.contact_layout, null );
        
        lv1 = (ListView) root.findViewById( R.id.pointsTable_group2 );
        lv1.setEnabled( false );

        upcominglv1 = (ListView) root.findViewById( R.id.upcomignlv_group2 );
        textHeader2 = (TextView) root.findViewById( R.id.header_group2 );
        
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
        int recordCount = getDataFromDB( "Group2" );
        fillData( lv1 );

        super.onResume();
        db = getActivity().openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        m_cursor1 = db.rawQuery( "select * from schedule where gang ='" + "Group2" + "' AND WinnerTeam =''", null );
        m_cursor1.moveToFirst();
        m_adapter1 = new upcomingAdapter( context, m_cursor1, true );
        upcominglv1.setAdapter( m_adapter1 );

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

        textHeader2.setText( "Points Table" );
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
