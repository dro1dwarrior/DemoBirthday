package com.appndroid.crick20;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LiveLayout extends Activity {

	private static String matchUrl = "http://sifyscores.cricbuzz.com/data/2012/2012_SL_IND/SL_IND_JUL24/scores.xml";// "http://sifyscores.cricbuzz.com/data/2012/2012_SL_IND/SL_IND_JUL21/scores.xml";//
																													// "http://sifyscores.cricbuzz.com/data/2012/2012_WI_NZ/WI_NZ_JUL16/scores.xml";

	private String liveScore = "";
	private String firstInng = null;
	private String battingTeam = "";
	private String xml = "";
	private String team1 = "Sri Lanka";
	private String team2 = "India";
	Button update = null;
	private boolean first = true;
	// Scrolling flag
	private boolean scrolling = false;

	private Context con;
	WheelView country;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.score);
		con = getApplicationContext();
		initWheel(R.id.runs_1, 3);
		initWheel(R.id.runs_2, 3);
		initWheel(R.id.runs_3, 3);
		initWheel1(R.id.wiks, 3);

		setHeader(team1, team2);

		country = (WheelView) findViewById(R.id.country);
		country.setVisibleItems(3);
		country.setCyclic(true);
		country.setEnabled(false);
		country.setViewAdapter(new CountryAdapter(this));

		country.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				CountryAdapter ca = new CountryAdapter(con);
				String tmp = battingTeam;
				setcountrywheel(tmp);
			}
		});

		update = (Button) findViewById(R.id.updatebtn);

		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				XMLfunctions.mContext = LiveLayout.this;
				xml = XMLfunctions.getXML(matchUrl);
//				 Log.d(
//				 "LiveLayout.onCreate(...).new OnClickListener() {...}-onClick()"
//				 , "aaaa"+xml );

				if (xml != null && !xml.equals("") && first) {
					country.scroll(-25 + (int) (Math.random() * 50), 4000);
					// displayInUI(xml);
				} else if (xml!= null && !xml.equals("") && !first)
					displayInUI(xml);

			}
		});

	}

	private void setHeader(String team1, String team2) {
		getDrawable iconObj = new getDrawable();
		ImageView img = (ImageView) findViewById(R.id.head_team1logo);
		img.setBackgroundResource(iconObj.getIcon(team1));
		TextView txt = (TextView) findViewById(R.id.head_team1name);
		txt.setText(team1);
		img = (ImageView) findViewById(R.id.head_team2logo);
		img.setBackgroundResource(iconObj.getIcon(team2));
		txt = (TextView) findViewById(R.id.head_team2name);
		txt.setText(team2);
	}

	protected String convertString(String str) {
		int len = str.length();

		int noOfZero = 3 - len;
		String returnStr = "";
		for (int i = 0; i < noOfZero; i++)
			returnStr = returnStr + "0";

		returnStr += str;
		return returnStr;
	}

	// Wheel scrolled flag
	private boolean wheelScrolled = false;

	// Wheel scrolled listener
	OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
		public void onScrollingStarted(WheelView wheel) {
			wheelScrolled = true;
		}

		public void onScrollingFinished(WheelView wheel) {
			wheelScrolled = false;
			// updateStatus();
		}
	};

	/**
	 * Initializes wheel
	 * 
	 * @param id
	 *            the wheel widget Id
	 */
	private void initWheel(int id, int items) {
		WheelView wheel = getWheel(id);
		wheel.setVisibleItems(items);
		wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
		wheel.setCurrentItem(0);

		// wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());
	}

	private void initWheel1(int id, int items) {
		WheelView wheel = getWheel(id);

		wheel.setVisibleItems(items);
		wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 10));
		wheel.setCurrentItem(0);

		// wheel.addChangingListener(changedListener);
		wheel.addScrollingListener(scrolledListener);
		wheel.setCyclic(true);
		wheel.setEnabled(false);
		wheel.setInterpolator(new AnticipateOvershootInterpolator());

	}

	/**
	 * Returns wheel by Id
	 * 
	 * @param id
	 *            the wheel Id
	 * @return the wheel with passed Id
	 */
	private WheelView getWheel(int id) {
		return (WheelView) findViewById(id);
	}

	/**
	 * Mixes wheel
	 * 
	 * @param id
	 *            the wheel id
	 */
	
	private void displayInUI(String xml) {

		Document doc = XMLfunctions.XMLfromString(xml);

		NodeList matchNode = doc.getElementsByTagName("match");

		int len = matchNode.getLength();

		Element matchElement = (Element) matchNode.item(0);

		String homeTeam = XMLfunctions.getValue(matchElement, "home");
		String awayTeam = XMLfunctions.getValue(matchElement, "away");
		String venue = XMLfunctions.getValue(matchElement, "venue");

		TextView batsmanTxt = (TextView) findViewById(R.id.batsmanTxt);
		TextView bowlerTxt = (TextView) findViewById(R.id.bowlerTxt);

		TextView batsmenOne = (TextView) findViewById(R.id.batsmanOne);
		TextView batsmenTwo = (TextView) findViewById(R.id.batsmanTwo);
		TextView bowlerOne = (TextView) findViewById(R.id.bowlerOne);
		TextView bowlerTwo = (TextView) findViewById(R.id.bowlerTwo);
		TextView manOfTheMatch = (TextView) findViewById(R.id.manOfTheMatch);
		TextView matchDetails = (TextView) findViewById(R.id.scorematchDetails);
		
		batsmanTxt.setVisibility(View.GONE);
		bowlerTxt.setVisibility(View.GONE);
		batsmenOne.setVisibility(View.GONE);
		batsmenTwo.setVisibility(View.GONE);
		bowlerOne.setVisibility(View.GONE);
		bowlerTwo.setVisibility(View.GONE);
		manOfTheMatch.setVisibility(View.GONE);
		matchDetails.setVisibility(View.GONE);
		
		TextView venueTxtView = (TextView) findViewById(R.id.matchVenue);
		venueTxtView.setText(venue);

		String matchState = XMLfunctions.getValue(matchElement, "state");

		NodeList currentScoreNode = doc.getElementsByTagName("currentscores");

		NodeList Innings = doc.getElementsByTagName("innings");
		String numberOfInningsNode = Integer.toString(Innings.getLength());

		String firstinngRuns = "";
		String firstinngWkts = "";
		String firstinngOve = "";

		String secondinngRuns = "";
		String secondinngWkts = "";
		String secondinngOve = "";
		firstInng = "";
		liveScore = "";
		getDrawable iconObj = new getDrawable();

		if (Innings.getLength() > 1) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.prevInng);
			layout.setVisibility(View.VISIBLE);
			Element e = (Element) Innings.item(0);
			e.getNodeValue();

			// team icon toggling
			NodeList battingTeamnode = e.getElementsByTagName("batteam");
			Element batTeamEle = (Element) battingTeamnode.item(0);
			String batTeam = batTeamEle.getAttribute("name");

			batTeam = iconObj.getTeamShortCode(batTeam);
			TextView teamname = (TextView) findViewById(R.id.team2_name);
			teamname.setText(batTeam);

			ImageView teamAicon = (ImageView) findViewById(R.id.image2);
			teamAicon.setBackgroundResource(iconObj.getIcon(batTeam));

			if (batTeam.equalsIgnoreCase(homeTeam)) {
				firstInng = homeTeam;
			} else {
				firstInng = awayTeam;
			}

			firstinngRuns = XMLfunctions.getValue(e, "totalruns");
			firstinngWkts = XMLfunctions.getValue(e, "totalwickets");
			firstinngOve = XMLfunctions.getValue(e, "totalovers");

			Log.d("aaaaaaaaaaaaaaaaaaaa", "first" + firstinngRuns + "/"
					+ firstinngWkts + " in " + firstinngOve + " ovrs");

			firstinngRuns = convertString(firstinngRuns);
			EditText txt = (EditText) findViewById(R.id.firsting1);
			txt.setText(String.valueOf(firstinngRuns.charAt(0)));
			txt = (EditText) findViewById(R.id.firsting2);
			txt.setText(String.valueOf(firstinngRuns.charAt(1)));
			txt = (EditText) findViewById(R.id.firsting3);
			txt.setText(String.valueOf(firstinngRuns.charAt(2)));

			txt = (EditText) findViewById(R.id.firstingWiks);
			txt.setText(firstinngWkts);

			TextView txt1 = (TextView) findViewById(R.id.team2_ovrs);
			txt1.setText(firstinngOve + " Ovrs");

			firstInng += " " + firstinngRuns + "/" + firstinngWkts + " in "
					+ firstinngOve + " ovrs";

			Element e1 = (Element) Innings.item(1);
			e1.getNodeValue();
			NodeList battingTeamnodesecond = e1.getElementsByTagName("batteam");
			Element batTeamElesecond = (Element) battingTeamnodesecond.item(0);
			String batTeamsecond = batTeamElesecond.getAttribute("name");

			battingTeam = iconObj.getTeamShortCode(batTeamsecond);
			if (first)
				setcountrywheel(battingTeam);

			secondinngRuns = XMLfunctions.getValue(e1, "totalruns");
			secondinngWkts = XMLfunctions.getValue(e1, "totalwickets");
			secondinngOve = XMLfunctions.getValue(e1, "totalovers");

			Log.d("aaaaaaaaaaa", "second" + secondinngRuns + "/"
					+ secondinngWkts + " in " + secondinngOve + " ovrs");
			secondinngRuns = convertString(secondinngRuns);

			WheelView wheel = getWheel(R.id.runs_1);
			wheel.setCurrentItem(
					Integer.parseInt(String.valueOf(secondinngRuns.charAt(0))),
					true);

			wheel = getWheel(R.id.runs_2);
			wheel.setCurrentItem(
					Integer.parseInt(String.valueOf(secondinngRuns.charAt(1))),
					true);

			wheel = getWheel(R.id.runs_3);
			wheel.setCurrentItem(
					Integer.parseInt(String.valueOf(secondinngRuns.charAt(2))),
					true);

			wheel = getWheel(R.id.wiks);
			wheel.setCurrentItem(Integer.parseInt(secondinngWkts), true);

			txt1 = (TextView) findViewById(R.id.team1_ovrs);
			txt1.setText(secondinngOve + " Ovrs");

			// country.scroll(-25 + (int)(Math.random() * 50), 4000);

			liveScore = firstInng + " " + batTeamsecond + secondinngRuns + "/"
					+ secondinngWkts + " in " + secondinngOve + " ovrs";

		} else if (Innings.getLength() == 1) {
			try {
				LinearLayout layout = (LinearLayout) findViewById(R.id.prevInng);
				layout.setVisibility(View.GONE);
				Element e = (Element) Innings.item(0);
				e.getNodeValue();

				NodeList battingTeamnode = e.getElementsByTagName("batteam");
				Element batTeamEle = (Element) battingTeamnode.item(0);
				String batTeam = batTeamEle.getAttribute("name");

				battingTeam = iconObj.getTeamShortCode(batTeam);
				if (first)
					setcountrywheel(battingTeam);

				String otherTeam = "";
				if (team1.equalsIgnoreCase(batTeam))
					otherTeam = team2;
				else
					otherTeam = team1;

				TextView teamname = (TextView) findViewById(R.id.team2_name);
				teamname.setText(iconObj.getTeamShortCode(otherTeam));

				ImageView teamAicon = (ImageView) findViewById(R.id.image2);
				teamAicon.setBackgroundResource(iconObj.getIcon(otherTeam));

				firstinngRuns = XMLfunctions.getValue(e, "totalruns");
				firstinngWkts = XMLfunctions.getValue(e, "totalwickets");
				firstinngOve = XMLfunctions.getValue(e, "totalovers");

				// match still in preview mode
				if (firstinngRuns.equals("") && firstinngWkts.equals("")
						&& firstinngOve.equals("")) {

				} else {
					if (batTeam.equalsIgnoreCase(homeTeam)) {

						liveScore = homeTeam;
					} else {

						liveScore = awayTeam;

					}
					Log.d("aaaaaaaaaaaa", "first" + firstinngRuns + "/"
							+ firstinngWkts + " in " + firstinngOve + " ovrs");

					firstinngRuns = convertString(firstinngRuns);

					WheelView wheel = getWheel(R.id.runs_1);
					wheel.setCurrentItem(Integer.parseInt(String
							.valueOf(firstinngRuns.charAt(0))), true);

					wheel = getWheel(R.id.runs_2);
					wheel.setCurrentItem(Integer.parseInt(String
							.valueOf(firstinngRuns.charAt(1))), true);

					wheel = getWheel(R.id.runs_3);
					wheel.setCurrentItem(Integer.parseInt(String
							.valueOf(firstinngRuns.charAt(2))), true);

					wheel = getWheel(R.id.wiks);
					wheel.setCurrentItem(Integer.parseInt(firstinngWkts), true);

					TextView txt1 = (TextView) findViewById(R.id.team1_ovrs);
					txt1.setText(firstinngOve + " Ovrs");

					liveScore += " " + firstinngRuns + "/" + firstinngWkts
							+ " in " + firstinngOve + " ovrs";

				}
			} catch (Exception ex) {
				Toast.makeText(getBaseContext(), ex.toString(),
						Toast.LENGTH_LONG).show();
			}

		} else {

		}
		
		if (currentScoreNode.getLength() > 0) {
			Element e = (Element) currentScoreNode.item(0);
			String currentinningsno = XMLfunctions.getValue(e,
					"currentinningsno");
			String battingTeam = XMLfunctions.getValue(e, "batteamname");
			String bowlingteam = XMLfunctions.getValue(e, "bwlteamname");
			String runs = XMLfunctions.getValue(e, "batteamruns");
			String wickets = XMLfunctions.getValue(e, "batteamwkts");
			String overs = XMLfunctions.getValue(e, "batteamovers");

			if (matchState.toLowerCase().equals("complete")) {
				NodeList resultNode = matchElement
						.getElementsByTagName("result");
				Element resultElement = (Element) resultNode.item(0);
				String resultIS = resultElement.getAttribute("type");

				if (resultIS.toLowerCase().equals("win")) {
					String winningTeam = XMLfunctions.getValue(resultElement,
							"winningteam");
					String winByRuns = XMLfunctions.getValue(resultElement,
							"wonbyruns");
					String winByWkts = XMLfunctions.getValue(resultElement,
							"wonbywickets");

					matchDetails.setVisibility(View.VISIBLE);
					if (winByRuns.equals("") && !winByWkts.equals(""))
						matchDetails.setText(winningTeam + " won by "
								+ winByWkts + " wickets.");
					else if (winByWkts.equals("") && !winByRuns.equals(""))
						matchDetails.setText(winningTeam + " won by "
								+ winByRuns + " runs.");
					// else
					// matchDetails.setText("match drawn");

					NodeList momNode = matchElement
							.getElementsByTagName("manofmatch");
					Element momElement = (Element) momNode.item(0);
					String strMOM = XMLfunctions.getElementValue(momElement);
					strMOM = strMOM.replace("\n", "");
					strMOM = strMOM.replace("\t", "");
					
					manOfTheMatch.setVisibility(View.VISIBLE);
					manOfTheMatch.setText("Man of the match : " + strMOM);

				}
			} else if (matchState.toLowerCase().equals("inprogress")) {

				NodeList batsmanNode = e.getElementsByTagName("batsman");
				String numberOfbatsmanNode = Integer.toString(batsmanNode
						.getLength());

				Element b1 = (Element) batsmanNode.item(0);
				b1.getNodeValue();

				String batsman1 = XMLfunctions.getValue(b1, "name");
				String runs1 = XMLfunctions.getValue(b1, "runs");
				String bowlfaced1 = XMLfunctions.getValue(b1, "balls-faced");
				String fours1 = XMLfunctions.getValue(b1, "fours");
				String sixes1 = XMLfunctions.getValue(b1, "sixes");

				batsmanTxt.setVisibility(View.VISIBLE);
				batsmenOne.setVisibility(View.VISIBLE);
				batsmenOne.setText(batsman1 + " " + runs1 + "(" + bowlfaced1
						+ ")" + "		4:" + " " + fours1 + "  6:" + " " + sixes1);

				if (numberOfbatsmanNode.equals("2")) {
					b1 = (Element) batsmanNode.item(1);
					b1.getNodeValue();

					String batsman2 = XMLfunctions.getValue(b1, "name");
					String runs2 = XMLfunctions.getValue(b1, "runs");
					String bowlfaced2 = XMLfunctions
							.getValue(b1, "balls-faced");
					String fours2 = XMLfunctions.getValue(b1, "fours");
					String sixes2 = XMLfunctions.getValue(b1, "sixes");

					if (!batsman2.equals("")) {

						batsmenTwo.setVisibility(View.VISIBLE);
						batsmenTwo.setText(batsman2 + " " + runs2 + "("
								+ bowlfaced2 + ")" + "		4:" + " " + fours2
								+ "  6:" + " " + sixes2);
					}
				}

				// bowlers
				NodeList bowlerNode = e.getElementsByTagName("bowler");
				String numberOfbowler = Integer
						.toString(bowlerNode.getLength());
				String bowlStr = "";

				if (bowlerNode.getLength() > 0) {
					Element bo1 = (Element) bowlerNode.item(0);
					bo1.getNodeValue();

					String bowler1 = XMLfunctions.getValue(bo1, "name");
					String bowlerovers1 = XMLfunctions.getValue(bo1, "overs");
					String bowlerruns1 = XMLfunctions.getValue(bo1, "runs");
					String bowlerwickets1 = XMLfunctions.getValue(bo1,
							"wickets");
					bowlStr = bowler1 + "  " + bowlerwickets1 + "/"
							+ bowlerruns1 + " (" + bowlerovers1 + ")";

					bowlerTxt.setVisibility(View.VISIBLE);
					bowlerOne.setVisibility(View.VISIBLE);
					bowlerOne.setText(bowlStr);

					if (numberOfbowler.equals("2")) {
						bowlStr = "";
						bo1 = (Element) bowlerNode.item(1);
						bo1.getNodeValue();

						String bowler2 = XMLfunctions.getValue(bo1, "name");
						String bowlerovers2 = XMLfunctions.getValue(bo1,
								"overs");
						String bowlerruns2 = XMLfunctions.getValue(bo1, "runs");
						String bowlerwickets2 = XMLfunctions.getValue(bo1,
								"wickets");

						if (!bowler2.equals("")) {
							bowlStr += bowler2 + "  " + bowlerwickets2 + "/"
									+ bowlerruns2 + " (" + bowlerovers2 + ")";
							bowlerTwo.setVisibility(View.VISIBLE);
							bowlerTwo.setText(bowlStr);

						}

					}
				}

			} else if (matchState.toLowerCase().equals("preview")) {

				NodeList tossNode = doc.getElementsByTagName("toss");
				int leng = tossNode.getLength();
				Element tossElement = (Element) tossNode.item(0);
				String winner = XMLfunctions.getValue(tossElement, "winner");
				String decision = XMLfunctions
						.getValue(tossElement, "decision");
				matchDetails.setVisibility(View.VISIBLE);
				if (winner.equals("") || winner.equals(null))
					matchDetails.setText("Match not started yet.");
				else
					matchDetails.setText(winner + " decided to "
							+ decision.replace("ing", "").replace("tt", "t")
							+ " first.");

			} else if (matchState.toLowerCase().equals("stump")) {
				matchDetails.setVisibility(View.VISIBLE);
				matchDetails.setText("Match state is stumps.");
			} else if (matchState.toLowerCase().equals("draw")) {
				matchDetails.setVisibility(View.VISIBLE);
				matchDetails.setText("Match state is Draw.");
			} else if (matchState.toLowerCase().equals("innings break")) {
				matchDetails.setVisibility(View.VISIBLE);
				matchDetails.setText("Innings Break.");

			} else if (matchState.toLowerCase().equals("lunch")) {
				matchDetails.setVisibility(View.VISIBLE);
				matchDetails.setText("Innings Break.");

			} else if (matchState.toLowerCase().equals("rain")) {
				matchDetails.setVisibility(View.VISIBLE);
				matchDetails.setText("Match delayed due to rain.");
			}
			else if (matchState.toLowerCase().equals("dinner")) {
				matchDetails.setVisibility(View.VISIBLE);
				matchDetails.setText("Dinner.");
			}

		}
	}

	// country.addScrollingListener( new OnWheelScrollListener() {
	// public void onScrollingStarted(WheelView wheel) {
	// scrolling = true;
	// }
	// public void onScrollingFinished(WheelView wheel) {
	// scrolling = false;
	// //updateCities(city, cities, country.getCurrentItem());
	// //Log.d("aaaaaaaaaa", "aaaaaaaaaaaaaa"+country.getCurrentItem());
	// CountryAdapter ca= new CountryAdapter(con);
	// String tmp=battingTeam;
	// setcountrywheel(tmp);
	// }
	// });
	//
	//
	// country.setCurrentItem(0);

	private void setcountrywheel(String name) {
		if (name != "" && first) {
			CountryAdapter ca = new CountryAdapter(con);
			String[] tmp = ca.countries;

			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].equals(name))
					country.setCurrentItem(i, true);
				first = false;

			}
		} else if (xml!=null && !xml.equals(""))
				displayInUI(xml);

	}

	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
		public String countries[] = new String[] { "AFG", "AUS", "BAN", "ENG",
				"IND", "IRE", "NZ", "PAK", "RSA", "SL", "WI", "ZIM" };
		// Countries flags
		private int flags[] = new int[] { R.drawable.afghanistan,
				R.drawable.australia, R.drawable.bangladesh,
				R.drawable.england, R.drawable.india, R.drawable.ireland,
				R.drawable.newzealand, R.drawable.pakistan,
				R.drawable.southafrica, R.drawable.srilanka,
				R.drawable.westindies, R.drawable.zimbabwe };

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.country_layout, NO_RESOURCE);

			setItemTextResource(R.id.country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			ImageView img = (ImageView) view.findViewById(R.id.flag);
			img.setImageResource(flags[index]);
			return view;
		}

		@Override
		public int getItemsCount() {
			return countries.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return countries[index];
		}
	}

}
