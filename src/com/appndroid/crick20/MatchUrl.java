package com.appndroid.crick20;

import java.io.InputStream;
import java.io.StringReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class MatchUrl extends Activity{
	
	private boolean m_isIpl = false;
	private String currentTag;
	int m_xmlTagId = 0;
	private ArrayList<String> matchesArray = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.matchurl);
		fetchliveurls();
	}
	
	private void fetchliveurls()
	{
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
			System.out.println(str);
			Log.d("NetworkManager.HttpAsyncConnector-doInBackground()",
					"Response is ::: " + szResponse);

			str = str.replace("\n", "");
			str = str.replace("\t", "");
			//str="<matches><match><seriesName>England in Sri Lanka 2012</seriesName><team1>Sri Lanka</team1><team2>England</team2><startdate>03 04 2012</startdate><enddate>07 04 2012</enddate><type>TEST</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_SL_ENG/SL_ENG_APR03_APR07/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2083</series-statistics-url></match><match><seriesName>Indian Premier League 2012</seriesName><team1>Kolkata Knight Riders</team1><team2>Delhi Daredevils</team2><startdate>05 04 2012</startdate><enddate>05 04 2012</enddate><type>T20</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/KOL_DEL_APR05/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2115</series-statistics-url></match><match><seriesName>Indian Premier League 2012</seriesName><team1>Chennai Super Kings</team1><team2>Mumbai Indians</team2><startdate>05 04 2012</startdate><enddate>05 04 2012</enddate><type>T20</type><scores-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/scores.xml</scores-url><full-commentary-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/full-commentary.xml</full-commentary-url><squads-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/squads.xml</squads-url><highlights-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/highlights.xml</highlights-url><graphs-url>http://sifyscores.cricbuzz.com/data/2012/2012_IPL/CHN_MUM_APR04/graphs.xml</graphs-url><series-statistics-url>http://webclient.cricbuzz.com/statistics/series/xml/2115</series-statistics-url></match></matches>";
			xmlParseMatch(str);
			
			Log.d("aaaaaaaaa size",""+matchesArray.size());
		
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
			Log.d("Date", "Date is :: " + currentdate);
			int date = 0;
			
			Date d = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
			String CDate = dateFormat.format(d);
			
			String teamA = null, teamB = null, scoreUrl = null, matchDate=null;
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
							&& m_isIpl) {
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
						m_isIpl = false;
					} else if (currentTag.equalsIgnoreCase("seriesName")) {
						m_xmlTagId = 1;

					} else if (currentTag.equalsIgnoreCase("team1") && m_isIpl) {
						m_xmlTagId = 2;

					} else if (currentTag.equalsIgnoreCase("team2") && m_isIpl) {
						m_xmlTagId = 3;
					} else if (currentTag.equalsIgnoreCase("scores-url")
							&& m_isIpl) {
						m_xmlTagId = 4;
					} else if (currentTag
							.equalsIgnoreCase("full-commentary-url") && m_isIpl) {
						m_xmlTagId = 5;
					} else if (currentTag.equalsIgnoreCase("squads-url")
							&& m_isIpl) {
						m_xmlTagId = 6;
					} else if (currentTag.equalsIgnoreCase("highlights-url")
							&& m_isIpl) {
						m_xmlTagId = 7;
					} else if (currentTag.equalsIgnoreCase("startdate")
							&& m_isIpl) {
						m_xmlTagId = 8;
					} else if ((currentTag.equalsIgnoreCase("type") || currentTag
							.equalsIgnoreCase("enddate")) && m_isIpl) {
						m_xmlTagId = 100;
					}
				}

				else if (eventType == XmlPullParser.TEXT) {
					switch (m_xmlTagId) {
					case 1: // seriesName
						//if (xpp.getText().equals("Indian Premier League 2012")) {
							m_isIpl = true;
						//}
						break;
					case 2: // team1
						Log.d("aaa", "teamA" + xpp.getText());
						if (!xpp.getText().equalsIgnoreCase("\n"))
							teamA = xpp.getText();
						break;
					case 3: // team2
						Log.d("aaa", "teamB" + xpp.getText());
						if (!xpp.getText().equalsIgnoreCase("\n"))
							teamB = xpp.getText();
						break;
					case 4: // scores-url
						Log.d("aaa", "URL" + xpp.getText());
						if (!xpp.getText().equalsIgnoreCase("\n"))
						scoreUrl = xpp.getText();
						//if(CDate.equals(matchDate))
						//{
							if (teamA != null && teamB != null && scoreUrl != null) {

								matchesArray.add(teamA + "||" + teamB + "||"
										+ scoreUrl);
								teamA = null;
								teamB = null;
								scoreUrl = null;

							}
						
						//}					
						
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

}
