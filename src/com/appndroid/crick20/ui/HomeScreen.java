package com.appndroid.crick20.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appndroid.crick20.R;
import com.appndroid.crick20.ui.NetworkManager.HttpAsyncConnector;
import com.google.android.gcm.GCMRegistrar;

public class HomeScreen extends Activity {

	LinearLayout scheduleClick, pointsTableClick, aboutClick, settingsClick;
	ImageView mainIcon;
	WebView webview;
	Context mcontext;
	private NetworkManager networkmanager;
	Gallery gallery;
	Cursor mCursor;
	BaseAdapter mAdapter;
	List<String> teamA = new ArrayList<String>();
	List<String> teamB = new ArrayList<String>();
	List<String> match = new ArrayList<String>();
	List<String> stadium = new ArrayList<String>();
	List<String> time = new ArrayList<String>();
	List<String> date = new ArrayList<String>();
	List<String> group = new ArrayList<String>();
	List<String> matchURL = new ArrayList<String>();

	getDrawable drawable;

	LinearLayout mDotsLayout;
	TextView counter1;
	TextView counter2;

	private boolean m_isWorldCup = false;
	private String currentTag;
	int m_xmlTagId = 0;
	private ArrayList<String> matchesArray = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_new);

		mcontext = this;
		Utils.setContext(this);

		// GCM Start

		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		// GCM Start
		int build_version = Integer.parseInt(Build.VERSION.SDK);
		String regId = "";
		if (build_version >= 8) {
			GCMRegistrar.checkDevice(HomeScreen.this);
			GCMRegistrar.checkManifest(HomeScreen.this);
			regId = GCMRegistrar.getRegistrationId(HomeScreen.this);
			if (regId.equals("")) {
				GCMRegistrar.register(HomeScreen.this, "899727754395");
				// Log.d("HomeScreen-GCMRegister()",
				// "GCM register call check passed");
			} else {
				if (!Utils.getIsPushStatusPostedOnServer(this)) {
					String szServer = "http://buyholdsell.in/ipl/add-device-id.php?device_id=";
					String szCompleteUrl = szServer + regId + "&salt=";
					szCompleteUrl = szCompleteUrl
							+ Utils.getMd5Hash("G6derTY" + regId);
					try {

						if ((mobNetInfo != null && mobNetInfo.isAvailable() && mobNetInfo
								.isConnected())
								|| (activeNetInfo != null
										&& activeNetInfo.isAvailable() && activeNetInfo
											.isConnected())
								|| (wifiInfo != null && wifiInfo.isAvailable() && wifiInfo
										.isConnected())) {
							// Log.d("HomeScreen", "URL is: " + szCompleteUrl);
							HttpClient httpclient = new DefaultHttpClient();
							HttpPost httppost = new HttpPost(szCompleteUrl);
							HttpResponse response = httpclient
									.execute(httppost);
							String szResponse = inputStreamToString(
									response.getEntity().getContent())
									.toString();
							Log.d("HomeScreen", "Response is :" + szResponse);
							if (szResponse.equalsIgnoreCase("1"))
								Utils.setIsPushStatusPostedOnServer(this, true);
						} else {
							Log.d("HomeScreen", "NETWORK NOT AVAILABLE");
							return;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						Log.d("HomeScreen", "EXCEPTION");
						e.printStackTrace();
					}

				}
			}
		}
		// GCM End

		// WebView wv = (WebView) findViewById(R.id.browser_home);
		// wv.getSettings().setJavaScriptEnabled(true);
		// wv.setBackgroundColor(Color.TRANSPARENT);
		// String html =
		// "<html><body style='margin:0;padding:0;'><script type='text/javascript' src='http://ad.leadboltads.net/show_app_ad.js?section_id=475192381'></script></body></html>";
		// wv.loadData(html, "text/html", "utf-8");

		if (!Utils.isDataMatchURLparsed) {
			new fetchURLTask().execute();
		}
		gallery = (Gallery) findViewById(R.id.home_gallery);
		mAdapter = new MyAdapter(this);
		mDotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
		mDotsLayout.setVisibility(View.INVISIBLE);
		counter1 = (TextView) findViewById(R.id.counter1);
		counter2 = (TextView) findViewById(R.id.counter2);
		drawable = new getDrawable();

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
				// Intent schIntent = new Intent( mcontext, LiveLayout.class
				// );
				// schIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
				// schIntent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
				// mcontext.startActivity( schIntent );

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

		AppRater.app_launched(this);
	}

	private StringBuilder inputStreamToString(InputStream content)
			throws IOException {
		// TODO Auto-generated method stub
		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		return total;
	}

	private void populateGallery() {
		// TODO Auto-generated method stub
		if (mCursor.getCount() > 0) {
			TextView txt = (TextView) findViewById(R.id.txtTodaysMatches);
			txt.setVisibility(View.VISIBLE);
			teamA.clear();
			teamB.clear();
			match.clear();
			stadium.clear();
			date.clear();
			time.clear();
			group.clear();
			matchURL.clear();
			do {
				String szTeamA = mCursor.getString(mCursor
						.getColumnIndex("TeamA"));
				String szTeamB = mCursor.getString(mCursor
						.getColumnIndex("TeamB"));
				teamA.add(szTeamA);
				teamB.add(szTeamB);
				stadium.add(mCursor.getString(mCursor.getColumnIndex("Stadium")));
				group.add(mCursor.getString(mCursor.getColumnIndex("gang")));
				match.add(drawable.getTeamShortCode(szTeamA) + " vs "
						+ drawable.getTeamShortCode(szTeamB));

				String time = mCursor.getString(mCursor.getColumnIndex("GMT"));
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(HomeScreen.this);
				int milli_offset = sp.getInt("offset", 0);
				SimpleDateFormat df1 = new SimpleDateFormat("HH:mm:ss");
				Date d = null;
				try {
					d = df1.parse(time + ":00");
				} catch (Exception e) {
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
				this.time.add(time + " GMT / " + d2.getHours() + ":" + min
						+ " Local");

				String date = mCursor.getString(mCursor.getColumnIndex("Date"));
				String[] strarr = date.split(" ");
				this.date.add(strarr[0] + " "
						+ drawable.getMonthName(strarr[1]) + " ("
						+ mCursor.getString(mCursor.getColumnIndex("Other1"))
						+ ")");
				matchURL.add(mCursor.getString(mCursor
						.getColumnIndex("MatchUrl")));

			} while (mCursor.moveToNext());

			// Log.d( "HomeScreen-populateGallery", " Total items in A added : "
			// + teamA.size() );
			gallery.setAdapter(mAdapter);

			if (mCursor.getCount() > 1) {
				mDotsLayout.setVisibility(View.VISIBLE);
				counter1.setBackgroundResource(R.drawable.countershape_selected);
				counter2.setBackgroundResource(R.drawable.countershape);
			}
			gallery.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView parent, View v,
						int position, long id) {
					TextView txtTeamA = (TextView) v
							.findViewById(R.id.textview_stadium);
					TextView txtTeamB = (TextView) v
							.findViewById(R.id.textview_match);
					TextView txtMatchURL = (TextView) v
							.findViewById(R.id.textview_date);

					String szTeamA = txtTeamA.getTag().toString();
					String szTeamB = txtTeamB.getTag().toString();
					String szMatchURL = txtMatchURL.getTag().toString();

					Intent scoreIntent = new Intent(HomeScreen.this,
							LiveLayout.class);
					scoreIntent.putExtra("match", szTeamA + "||" + szTeamB
							+ "||" + szMatchURL);
					startActivity(scoreIntent);
				}
			});

			gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView adapterView, View view,
						int pos, long l) {
					if (mDotsLayout.getVisibility() == View.VISIBLE) {
						if (pos == 0) {
							counter1.setBackgroundResource(R.drawable.countershape_selected);
							counter2.setBackgroundResource(R.drawable.countershape);
						} else {
							counter2.setBackgroundResource(R.drawable.countershape_selected);
							counter1.setBackgroundResource(R.drawable.countershape);
						}
					}
				}

				@Override
				public void onNothingSelected(AdapterView adapterView) {

				}
			});

		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		NetworkManager.isDataFetched = false;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Utils.getDB(this);
		// mCursor = Utils.db.query( "schedule", null, null, null, null, null,
		// null );
		mCursor = Utils.db
				.query("schedule", null,
						"MatchUrl != '' AND MatchResult == '' ", null, null,
						null, null);
		mCursor.moveToFirst();

		populateGallery();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		mCursor = Utils.db
				.query("schedule", null,
						"MatchUrl != '' AND MatchResult == '' ", null, null,
						null, null);
		mCursor.moveToFirst();

		populateGallery();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mCursor.close();
		mCursor.deactivate();
	}

	public class MyAdapter extends BaseAdapter {
		Context context;

		MyAdapter(Context c) {
			context = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return teamA.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return teamA.toArray()[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			// View rowView = LayoutInflater.from( parent.getContext()
			// ).inflate( R.layout.galleryitem, null );
			View rowView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.galleryitem_new, null);
			ImageView flag1 = (ImageView) rowView
					.findViewById(R.id.galleryitem_flag1);
			ImageView flag2 = (ImageView) rowView
					.findViewById(R.id.galleryitem_flag2);

			TextView txtstadium = (TextView) rowView
					.findViewById(R.id.textview_stadium);
			TextView txtmatch = (TextView) rowView
					.findViewById(R.id.textview_match);
			TextView txtdate = (TextView) rowView
					.findViewById(R.id.textview_date);
			TextView txttime = (TextView) rowView
					.findViewById(R.id.textview_time);
			TextView txtgroup = (TextView) rowView
					.findViewById(R.id.textview_group);

			txtstadium.setText(stadium.get(position).toString());
			txtstadium.setTag(teamA.get(position).toString());
			txtmatch.setText(match.get(position).toString());
			txtmatch.setTag(teamB.get(position).toString());
			txtdate.setText(date.get(position).toString());
			txtdate.setTag(matchURL.get(position).toString());
			txttime.setText(time.get(position).toString());
			txtgroup.setText(group.get(position).toString());
			flag1.setImageResource(drawable.getIcon(teamA.get(position)
					.toString()));
			flag2.setImageResource(drawable.getIcon(teamB.get(position)
					.toString()));
			return rowView;
		}
	}

	private void fetchliveurls() {
		HttpClient hc;
		String szResponse = "";
		boolean bSuccess = false;
		HttpGet get = null;
		String str = null;
		try {
			hc = new DefaultHttpClient();
			get = new HttpGet("http://synd.cricbuzz.com/sify/");
			HttpResponse rp = hc.execute(get);
			InputStream data = rp.getEntity().getContent();
			// szResponse =
			// DhamakaApplication.getApplication().generateString(data);
			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = data.read(b)) != -1;) {
				buffer.append(new String(b, 0, n));
			}
			str = buffer.toString();
			// System.out.println(str);

			str = str.replace("\n", "");
			str = str.replace("\t", "");
			// str="<matches><match><seriesName>England in Sri Lanka 2012</seriesName><team1>Sri Lanka</team1><team2>England</team2><startdate>03 04 2012</startdate><enddate>07 04 2012</enddate><type>TEST</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2083</series-statistics-url></match><match><seriesName>Indian Premier League 2012</seriesName><team1>Kolkata Knight Riders</team1><team2>Delhi Daredevils</team2><startdate>05 04 2012</startdate><enddate>05 04 2012</enddate><type>T20</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2115</series-statistics-url></match><match><seriesName>Indian Premier League 2012</seriesName><team1>Chennai Super Kings</team1><team2>Mumbai Indians</team2><startdate>05 04 2012</startdate><enddate>05 04 2012</enddate><type>T20</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2115</series-statistics-url></match></matches>";
			xmlParseMatch(str);
		} catch (SocketException e) {

		} catch (UnknownHostException ex) {

		} catch (Exception e) {

		}
	}

	private void xmlParseMatch(String xmlData) {

		matchesArray.clear();
		try {
			Calendar c = Calendar.getInstance();
			int currentdate = c.get(Calendar.DATE);
			int date = 0;

			Date d = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
			String CDate = dateFormat.format(d);

			String teamA = null, teamB = null, scoreUrl = null, matchDate = null;
			ContentValues values = new ContentValues();
			// TODO Auto-generated method stub
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			xpp.setInput(new StringReader(xmlData));
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					System.out.println("Start document");
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
					System.out.println("End document");
				} else if (eventType == XmlPullParser.START_TAG) {
					System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getName() != null
							&& xpp.getName().equalsIgnoreCase("match")
							&& m_isWorldCup) {
						Log.d("Ongoing", "Current Date is :: " + currentdate);
						Log.d("Ongoing", "Match Date is :: " + date);
						// if(date == currentdate)
						// {
						// mContext.getApplicationContext().getContentResolver()
						// .insert(DataProvider.Ongoing.CONTENT_URI,
						// values);
						// mContext.getApplicationContext().getContentResolver()
						// .notifyChange(DataProvider.Ongoing.CONTENT_URI,
						// null);
						//
						// }

					}
					System.out.println("End tag " + xpp.getName());
				} else if (eventType == XmlPullParser.TEXT) {
					System.out.println("Text " + xpp.getText());
				}
				if (xpp.getName() != null)
					currentTag = xpp.getName();
				if (eventType == XmlPullParser.START_TAG) {
					if (currentTag.equalsIgnoreCase("matches")) {
						// Do nothing

					} else if (currentTag.equalsIgnoreCase("match")) {
						// Do nothing
						m_isWorldCup = false;
					} else if (currentTag.equalsIgnoreCase("seriesName")) {
						m_xmlTagId = 1;

					} else if (currentTag.equalsIgnoreCase("team1")
							&& m_isWorldCup) {
						m_xmlTagId = 2;

					} else if (currentTag.equalsIgnoreCase("team2")
							&& m_isWorldCup) {
						m_xmlTagId = 3;
					} else if (currentTag.equalsIgnoreCase("scores-url")
							&& m_isWorldCup) {
						m_xmlTagId = 4;
					} else if (currentTag
							.equalsIgnoreCase("full-commentary-url")
							&& m_isWorldCup) {
						m_xmlTagId = 5;
					} else if (currentTag.equalsIgnoreCase("squads-url")
							&& m_isWorldCup) {
						m_xmlTagId = 6;
					} else if (currentTag.equalsIgnoreCase("highlights-url")
							&& m_isWorldCup) {
						m_xmlTagId = 7;
					} else if (currentTag.equalsIgnoreCase("startdate")
							&& m_isWorldCup) {
						m_xmlTagId = 8;
					} else if ((currentTag.equalsIgnoreCase("type") || currentTag
							.equalsIgnoreCase("enddate")) && m_isWorldCup) {
						m_xmlTagId = 100;
					}
				}

				else if (eventType == XmlPullParser.TEXT) {
					switch (m_xmlTagId) {
					case 1: // seriesName
						if (xpp.getText()
								.equalsIgnoreCase("ICC World T20 2012")) {
							m_isWorldCup = true;
						}
						break;
					case 2: // team1
						if (!xpp.getText().equalsIgnoreCase("\n"))
							teamA = xpp.getText();
						break;
					case 3: // team2
						if (!xpp.getText().equalsIgnoreCase("\n"))
							teamB = xpp.getText();
						break;
					case 4: // scores-url
						if (!xpp.getText().equalsIgnoreCase("\n"))
							scoreUrl = xpp.getText();
						// if(CDate.equals(matchDate))
						// {
						if (teamA != null && teamB != null && scoreUrl != null) {

							// matchesArray.add(teamA + "||" + teamB + "||"
							// + scoreUrl);
							ContentValues cvalues = new ContentValues();
							cvalues.put("MatchUrl", scoreUrl);

							Utils.getDB(this);
							int i = Utils.db.update("schedule", cvalues,
									"TeamA=? AND TeamB = ? AND Date=?",
									new String[] { teamA.replaceAll(" ", ""),
											teamB.replaceAll(" ", ""),
											matchDate });
							if (i > 0
									&& Utils.currentContext == HomeScreen.this) {
								gallery.clearAnimation();
								mAdapter.notifyDataSetChanged();
								mCursor = Utils.db
										.query("schedule",
												null,
												"MatchUrl != '' AND MatchResult == '' ",
												null, null, null, null);
								mCursor.moveToFirst();
								Intent intent = new Intent(this,
										HomeScreen.class);
								startActivity(intent);
								this.finish();
							}
							teamA = null;
							teamB = null;
							scoreUrl = null;

						}

						// }

						break;
					case 5: // full-commentary-url

						break;
					case 6: // squads-url

						break;
					case 7: // highlights-url

						break;
					case 8:// date
						if (!xpp.getText().equalsIgnoreCase("\n"))
							matchDate = xpp.getText();

						break;
					default:
						break;
					}
				}

				eventType = xpp.next();

			}
			// DhamakaApplication.getApplication().setOngoingDownloaded(true);
			// Intent intent = new Intent( mContext,IPLLauncher.class );
			// mContext.startActivity(intent);
			// Activity activity =
			// DhamakaApplication.getApplication().getCurrentActivity();
			// Log.d( "Ongoing-xmlParseMatch()" , "current activity is :: "
			// +activity);
			// activity.finish();

		} catch (Exception e) {
			// TODO: handle exception

			e.printStackTrace();
		}

	}

	private class fetchURLTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				fetchliveurls();
				Utils.isDataMatchURLparsed = true;
			} catch (Exception e) {
				e.printStackTrace();
				Utils.isDataMatchURLparsed = false;
			}
			return null;
		}
	}
}
