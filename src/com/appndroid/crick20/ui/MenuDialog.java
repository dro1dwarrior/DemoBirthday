package com.appndroid.crick20.ui;

import com.appndroid.crick20.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class MenuDialog extends AlertDialog {

	ImageView scheduleClick, standingsClick, aboutClick, settingsClick,
			homeClick;
	Context mcontext;
	String schActivity = "";

	public MenuDialog(final Context context) {
		super(context);
		// setTitle("Custom Menu");
		mcontext = context;
		setMenuDialog();
	}

	public MenuDialog(final Context context, String activityName) {
		super(context);
		// setTitle("Custom Menu");
		schActivity = activityName;
		mcontext = context;
		setMenuDialog();
	}

	public void setMenuDialog() {
		View menu = getLayoutInflater().inflate(R.layout.popup_dialog, null);
		setView(menu);

		// http://overoid.tistory.com/29

		scheduleClick = (ImageView) menu.findViewById(R.id.dlgSch_img);

		// scheduleClick.setBackgroundDrawable(d);

		aboutClick = (ImageView) menu.findViewById(R.id.dlgAbout_img);
		settingsClick = (ImageView) menu.findViewById(R.id.dlgSettings_img);

		standingsClick = (ImageView) menu.findViewById(R.id.dlgStandings_img);

		homeClick = (ImageView) menu.findViewById(R.id.dlgHome_img);
		homeClick.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent schIntent = new Intent(mcontext, HomeScreen.class);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mcontext.startActivity(schIntent);
				dismiss();

			}
		});

		scheduleClick.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Menu dialog", "before if" + schActivity);
				if (schActivity.equals("schedule")) {
					Log.d("Menu dialog", "inside if" + schActivity);
					schActivity = "";
					dismiss();

				} else {

					Intent schIntent = new Intent(mcontext, Schedule.class);
					schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					mcontext.startActivity(schIntent);
					dismiss();

				}

			}
		});

		standingsClick.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (schActivity.equals("standings")) {
					schActivity = "";
					dismiss();

				} else {
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(mcontext);
					boolean isGroupStage = sp.getBoolean("isGroupStage", true);
					Intent schIntent;
					if (isGroupStage)
						schIntent = new Intent(mcontext, GroupTab.class);
					else
						schIntent = new Intent(mcontext, tabtest.class);
					mcontext.startActivity(schIntent);
					dismiss();
				}
			}
		});

		aboutClick.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aboutusIntent = new Intent(mcontext, About.class);
				mcontext.startActivity(aboutusIntent);
				dismiss();
			}
		});

		settingsClick.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent aboutusIntent = new Intent(mcontext, Settings.class);
				mcontext.startActivity(aboutusIntent);
				dismiss();
			}
		});

	}

	public final boolean isInternetOn() {
		ConnectivityManager connec = (ConnectivityManager) mcontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// ARE WE CONNECTED TO THE NET
		if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
				|| connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
			// MESSAGE TO SCREEN FOR TESTING (IF REQ)
			// Toast.makeText(this, connectionType + ” connected”,
			// Toast.LENGTH_SHORT).show();
			return true;
		} else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
				|| connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
			// System.out.println(“Not Connected”);
			return false;
		}
		return false;
	}

}
