package com.appndroid.crick20;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageView;

public class MenuDialog extends AlertDialog {

	ImageView scheduleClick, standingsClick, aboutClick, settingsClick;
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

		scheduleClick = (ImageView) menu.findViewById(R.id.dlgSettings_img);

		// scheduleClick.setBackgroundDrawable(d);

		aboutClick = (ImageView) menu.findViewById(R.id.dlgAbout_img);
		settingsClick = (ImageView) menu.findViewById(R.id.dlgSettings_img);

		standingsClick = (ImageView) menu.findViewById(R.id.dlgStandings_img);

		scheduleClick.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (schActivity.equals("schedule")) {
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

					Intent schIntent = new Intent(mcontext, GroupTab.class);
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
