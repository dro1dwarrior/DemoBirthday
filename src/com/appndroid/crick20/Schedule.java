package com.appndroid.crick20;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class Schedule extends ListActivity
{
    Cursor m_cursor;
    getDrawable drawable;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        setContentView( R.layout.schedule );

        drawable = new getDrawable();
        SQLiteDatabase db;
        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );
        m_cursor = db.rawQuery( "select * from schedule", null );
        m_cursor.moveToFirst();
        ListAdapter adapter = new scheduleAdapter( this, m_cursor, true );
        setListAdapter( adapter );
        // MyPagerAdapter adapter = new MyPagerAdapter();
        // ViewPager myPager = (ViewPager) findViewById( R.id.myfivepanelpager );
        // myPager.setAdapter( adapter );
        // myPager.setCurrentItem( 0 );

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

            String szTeamA = cursor.getString( cursor.getColumnIndex( "TeamA" ) );
            imgTeamA.setImageResource( drawable.getIcon( szTeamA ) );
            String szTeamB = cursor.getString( cursor.getColumnIndex( "TeamB" ) );
            imgTeamB.setImageResource( drawable.getIcon( szTeamB ) );
            txtmatch.setText( drawable.getTeamShortCode( szTeamA ) + " vs " + drawable.getTeamShortCode( szTeamB ) );
            txtstadium.setText( cursor.getString( cursor.getColumnIndex( "Stadium" ) ) );
            txtdate.setText( cursor.getString( cursor.getColumnIndex( "Date" ) ) );
        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent )
        {
            // TODO Auto-generated method stub
            View view = inflater.inflate( R.layout.one, parent, false );
            return view;
        }

    }

}
