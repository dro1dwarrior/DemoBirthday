package com.appndroid.crick20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class GroupDetail extends Activity implements AnimationListener {

	SQLiteDatabase db;
	TextView textHeader1;
	ListView lv;
	String[] from, from1, from2;
	int[] to;
	fillList ptList;
	
	
	TabHost m_tabHost;
	FrameLayout mFrameLayout;
	View menu;
	boolean menuOut = false;
	Animation anim;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (menuOut) {
			menu.setVisibility(View.INVISIBLE);
			menuOut = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.airport);
		
		lv = (ListView) findViewById(R.id.currentstatslistview);
		lv.setEnabled(false);
		
		textHeader1 = (TextView) findViewById(R.id.record1);

		to = new int[] { R.id.stat_item1, R.id.stat_item2, R.id.stat_item3,
				R.id.stat_item4, R.id.stat_item5, R.id.stat_item6,
				R.id.stat_item7 };
		
		if (!NetworkManager.isNetworkConnection) {

			NetworkManager networkmanager = new NetworkManager(GroupDetail.this);

			com.appndroid.crick20.NetworkManager.HttpAsyncConnector httpConnect = networkmanager.new HttpAsyncConnector();
			httpConnect
					.setTaskParams(ApplicationDefines.CommandType.COMMAND_SCHEDULE);
			httpConnect.execute();
		}

		ProgressDialog dialog = ProgressDialog.show(GroupDetail.this, "",
				"Please wait...", true);
		fillRecordTask task = new fillRecordTask();
		task.dialog = dialog;
		task.execute();

		if (!NetworkManager.isNetworkConnection) {
			Toast.makeText(getApplicationContext(),
					"Please connect your device to network and try again for latest update",
					Toast.LENGTH_LONG).show();

		}

		String names = getIntent().getExtras().getString("teamnames");
		TextView t1 = (TextView) findViewById(R.id.title);
		t1.setText(names);

		mFrameLayout = (FrameLayout) this.findViewById(R.id.flipper);
		menu = mFrameLayout.findViewById(R.id.menu);

		final ImageView navigationImage = (ImageView) findViewById(R.id.nav);
		navigationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Animation anim;
				if (!menuOut) {
					navigationImage.setBackgroundResource(R.drawable.navigationselected);
					menu.setVisibility(View.VISIBLE);
					ViewUtils.printView("menu", menu);
					anim = AnimationUtils.loadAnimation(GroupDetail.this,
							R.anim.push_right_in);
				} else {
					navigationImage.setBackgroundResource(R.drawable.navigationunselected);
					anim = AnimationUtils.loadAnimation(GroupDetail.this,
							R.anim.push_left_out);
				}
				anim.setAnimationListener(GroupDetail.this);
				// out.setAnimationListener(me);
				menu.startAnimation(anim);

			}
		});

		Context context = getApplicationContext();
		LayoutInflater inflater = getLayoutInflater();
		View toastRoot = inflater.inflate(R.layout.my_toast, null);
		TextView t11 = (TextView) toastRoot.findViewById(R.id.toasttext);
		t11.setText(getIntent().getExtras().getString("group"));
		Toast toast = new Toast(context);
		toast.setView(toastRoot);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();

	}
	
	public int getDataFromDB() {

		db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

		from = new String[] { "Team", "P", "W", "L", "NR", "Pts", "NRR" };

		ptList = new fillList(from);
		Cursor cur = db.query(getIntent().getExtras().getString("group"), null, null, null, null, null, null);
		ptList.fillRecordList(cur, ptList,"currentStats");
		
		return cur.getCount();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		ViewUtils.printView("menu", menu);
		menuOut = !menuOut;
		if (!menuOut) {
			menu.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
	
	public void fillData() {

		textHeader1.setText("Point Table");
		@SuppressWarnings("unchecked")
		SimpleAdapter adapter = new overrideAdapter(this,
				ptList.getFilledList(), R.layout.singlecurrntstat_layout, from,
				to, "currentStats");
		lv.setAdapter(adapter);

	}
	
	private class fillRecordTask extends AsyncTask<String, Void, String> {

		ProgressDialog dialog = null;
		int recordCount;

		// can use UI thread here
		protected void onPreExecute() {

		}

		// automatically done on worker thread (separate from UI thread)
		protected String doInBackground(final String... args) {
			recordCount = getDataFromDB();
			return "";
		}

		// can use UI thread here
		protected void onPostExecute(final String result) {
			if (dialog != null) {
				dialog.dismiss();
				dialog = null;
			}

			fillData();

		}

	}
}
