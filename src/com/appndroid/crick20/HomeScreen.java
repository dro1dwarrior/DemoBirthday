package com.appndroid.crick20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomeScreen extends Activity {

	LinearLayout scheduleClick, pointsTableClick, aboutClick, settingsClick;
	ImageView mainIcon;
	Context mcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_new);

		mcontext = this;

		scheduleClick = (LinearLayout) findViewById(R.id.ll_schedule);
		scheduleClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent schIntent = new Intent(mcontext, Schedule.class);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mcontext.startActivity(schIntent);
			}
		});

		pointsTableClick = (LinearLayout) findViewById(R.id.ll_points);
		pointsTableClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent schIntent = new Intent(mcontext, GroupTab.class);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mcontext.startActivity(schIntent);
			}
		});

		settingsClick = (LinearLayout) findViewById(R.id.ll_settings);
		settingsClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent schIntent = new Intent(mcontext, Settings.class);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mcontext.startActivity(schIntent);
			}
		});

		aboutClick = (LinearLayout) findViewById(R.id.ll_about);
		aboutClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent schIntent = new Intent(mcontext, About.class);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mcontext.startActivity(schIntent);
			}
		});
		mainIcon=(ImageView) findViewById(R.id.iv_main_icon);
		mainIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		

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
}
