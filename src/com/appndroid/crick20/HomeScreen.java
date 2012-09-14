package com.appndroid.crick20;

import com.appndroid.crick20.NetworkManager.HttpAsyncConnector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomeScreen extends Activity {

	LinearLayout scheduleClick, pointsTableClick, aboutClick, settingsClick;
	ImageView mainIcon;
	WebView webview;
	Context mcontext;
	private NetworkManager networkmanager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_new);

		mcontext = this;
		
		WebView wv = (WebView) findViewById(R.id.browser_home);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setBackgroundColor(Color.TRANSPARENT);
        String html = "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadboltads.net/show_app_ad.js?section_id=475192381'></script></body></html>";
        wv.loadData(html, "text/html", "utf-8");
		
		networkmanager = new NetworkManager(HomeScreen.this);

        if (!NetworkManager.isDataFetched) {

            HttpAsyncConnector httpConnect = networkmanager.new HttpAsyncConnector();
            httpConnect
                    .setTaskParams(ApplicationDefines.CommandType.COMMAND_SCHEDULE);
            httpConnect.execute();
        }
        
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
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(mcontext);
				boolean isGroupStage = sp.getBoolean("isGroupStage", true);
				Intent schIntent;
				if (isGroupStage)
					schIntent = new Intent(mcontext, GroupTab.class);
				else
					schIntent = new Intent(mcontext, tabtest.class);
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
		mainIcon = (ImageView) findViewById(R.id.iv_main_icon);
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
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		NetworkManager.isDataFetched = false;
	}
}
