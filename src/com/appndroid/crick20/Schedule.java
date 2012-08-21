package com.appndroid.crick20;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class Schedule extends ListActivity {
	Cursor m_cursor;
	getDrawable drawable;
	int milli_offset = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		milli_offset = sp.getInt("offset", 0);

		drawable = new getDrawable();
		SQLiteDatabase db;
		db = openOrCreateDatabase("worldcupt20.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		m_cursor = db.rawQuery("select * from schedule", null);
		m_cursor.moveToFirst();
		ListAdapter adapter = new scheduleAdapter(this, m_cursor, true);
		setListAdapter(adapter);
		// MyPagerAdapter adapter = new MyPagerAdapter();
		// ViewPager myPager = (ViewPager) findViewById( R.id.myfivepanelpager
		// );
		// myPager.setAdapter( adapter );
		// myPager.setCurrentItem( 0 );

	}

	public class scheduleAdapter extends CursorAdapter {
		private LayoutInflater inflater;

		public scheduleAdapter(Context context, Cursor cursor,
				boolean autoRequery) {
			super(context, cursor, autoRequery);
			// TODO Auto-generated constructor stub
			inflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			ImageView imgTeamA = (ImageView) view.findViewById(R.id.img1);
			ImageView imgTeamB = (ImageView) view.findViewById(R.id.img2);
			TextView txtstadium = (TextView) view
					.findViewById(R.id.textview_stadium);
			TextView txtmatch = (TextView) view
					.findViewById(R.id.textview_match);
			TextView txtdate = (TextView) view.findViewById(R.id.date);
			TextView txttime = (TextView) view.findViewById(R.id.textview_time);

			String time = cursor.getString(cursor.getColumnIndex("GMT"));

			SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
			Date d = null;
			try {
				d = df1.parse(time + ":00");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Long lngTime = d.getTime();
			lngTime += (milli_offset);
			Date d2 = new Date(lngTime);
			int minutes = d2.getMinutes();
			String min;
			if (minutes == 0)
				min = "00";
			else
				min = String.valueOf(minutes);
			String szTeamA = cursor.getString(cursor.getColumnIndex("TeamA"));
			imgTeamA.setImageResource(drawable.getIcon(szTeamA));
			String szTeamB = cursor.getString(cursor.getColumnIndex("TeamB"));
			imgTeamB.setImageResource(drawable.getIcon(szTeamB));
			txtmatch.setText(drawable.getTeamShortCode(szTeamA) + " vs "
					+ drawable.getTeamShortCode(szTeamB));
			txttime.setText(cursor.getString(cursor.getColumnIndex("Venue")));
			txtdate.setText(cursor.getString(cursor.getColumnIndex("Date")));

			txtstadium.setText(time + " GMT / " + d2.getHours() + ":" + min
					+ " Local");
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = inflater.inflate(R.layout.one, parent, false);
			return view;
		}

	}

	@Override
	protected void onListItemClick(android.widget.ListView l, View v,
			int nPosition, long id) {
		
	

	}

}
