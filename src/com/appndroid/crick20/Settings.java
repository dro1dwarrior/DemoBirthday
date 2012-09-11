package com.appndroid.crick20;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Settings extends Activity {

	ToggleButton toggleButton1;
	TextView changeDur;
	Context m_context;
	SharedPreferences sp;
	final CharSequence[] items = { "1 Minute", "2 Minutes", "3 Minutes",
			"5 Minutes" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings);
		m_context = this;
		sp = PreferenceManager.getDefaultSharedPreferences(m_context);

		toggleButton1 = (ToggleButton) findViewById(R.id.toggleAutoUpdate);
		changeDur = (TextView) findViewById(R.id.changeDuration);
		toggleButton1.setChecked(sp.getBoolean("spAutoUpdate", false));
		changeDur.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDurationList();

			}
		});
		if (sp.getBoolean("spAutoUpdate", false)) {
			changeDur.setVisibility(View.VISIBLE);
		}
		toggleButton1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("aaaaaaaaaaa", "togg" + toggleButton1.getText());
				if (toggleButton1.getText().toString().equalsIgnoreCase("on")) {
					showDurationList();

				} else {
					Editor edit = sp.edit();
					edit.putBoolean("spAutoUpdate", false);
					edit.commit();
					changeDur.setVisibility(View.GONE);
				}
			}
		});
	}

	public void showDurationList() {
		AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
		builder.setTitle("Live score Auto update duration");
		int selPosition = sp.getInt("spAutoDuration", 0);
		builder.setSingleChoiceItems(items, selPosition,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						dialog.dismiss();
						Toast.makeText(m_context, items[item],
								Toast.LENGTH_SHORT).show();
						Editor edit = sp.edit();
						edit.putBoolean("spAutoUpdate", true);
						edit.putInt("spAutoDuration", item);
						edit.commit();
						changeDur.setVisibility(View.VISIBLE);
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}
}
