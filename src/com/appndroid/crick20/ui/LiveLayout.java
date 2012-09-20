package com.appndroid.crick20.ui;

import kankan.wheel.R;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LiveLayout extends Activity
{

    private static String matchUrl = "http://sifyscores.cricbuzz.com/data/2012/ICC_WT20_WARMUP/IND_PAK_SEP17/scores.xml";// "http://sifyscores.cricbuzz.com/data/2012/2012_SL_IND/SL_IND_JUL24/scores.xml";//
                                                                                                                         // "http://sifyscores.cricbuzz.com/data/2012/2012_SL_IND/SL_IND_JUL21/scores.xml";//
    // "http://sifyscores.cricbuzz.com/data/2012/2012_WI_NZ/WI_NZ_JUL16/scores.xml";

    private String liveScore = "";
    private String firstInng = null;
    private String battingTeam = "";
    private String xml = "";
    private String team1 = "Sri Lanka";
    private String team2 = "India";
    private String wheelTeam = "";
    ImageButton update = null;
    ImageView navigationImage;
    ProgressBar progBar = null;
    private boolean first = true;
    // Scrolling flag
    private boolean scrolling = false;
    ProgressDialog dialog;

    private Context con;
    WheelView country;
    Handler mHandler;
    protected Handler taskHandler = new Handler();
    protected Boolean isComplete = false;

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.score );
        Utils.setContext( this );
        String matchInfo = "";
        if( getIntent() != null )
            if( getIntent().getExtras() != null )
                if( getIntent().getExtras().getString( "match" ) != null )
                    matchInfo = getIntent().getExtras().getString( "match" );
        String[] mchInfoArray = matchInfo.split( "\\|\\|" );
        team1 = mchInfoArray[0];
        team2 = mchInfoArray[1];
        matchUrl = mchInfoArray[mchInfoArray.length - 1];
        progBar = (ProgressBar) findViewById( R.id.progressBar1 );
        con = getApplicationContext();
        initWheel( R.id.runs_1, 3 );
        initWheel( R.id.runs_2, 3 );
        initWheel( R.id.runs_3, 3 );
        initWheel1( R.id.wiks, 3 );

        setHeader( team1, team2 );
        dialog = ProgressDialog.show( LiveLayout.this, "", "Please wait...", true );

        country = (WheelView) findViewById( R.id.country );
        country.setVisibleItems( 3 );
        country.setCyclic( true );
        country.setEnabled( false );
        country.setViewAdapter( new CountryAdapter( this ) );

        country.addScrollingListener( new OnWheelScrollListener()
        {
            public void onScrollingStarted( WheelView wheel )
            {
                scrolling = true;
            }

            public void onScrollingFinished( WheelView wheel )
            {
                scrolling = false;
                CountryAdapter ca = new CountryAdapter( con );
                String tmp = battingTeam;
                setcountrywheel( tmp );
            }
        } );

        navigationImage = (ImageView) findViewById( R.id.nav );
        navigationImage.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                callEvent();

            }
        } );

        update = (ImageButton) findViewById( R.id.updatebtn );

        update.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
                XMLfunctions.mContext = LiveLayout.this;
                progBar.setVisibility( View.VISIBLE );
                Thread t = new Thread()
                {
                    public void run()
                    {
                        xml = XMLfunctions.getXML( matchUrl );
                        mHandler.sendEmptyMessage( 0 );
                    }
                };
                t.start();
                // Log.d(
                // "LiveLayout.onCreate(...).new OnClickListener() {...}-onClick()"
                // , "aaaa"+xml );

                mHandler = new Handler()
                {
                    @Override
                    public void handleMessage( Message msg )
                    {
                        super.handleMessage( msg );
                        if( xml != null && !xml.equals( "" ) && first )
                        {
                            country.scroll( -25 + (int) ( Math.random() * 50 ), 4000 );
                        }
                        else if( xml != null && !xml.equals( "" ) && !first )
                            displayInUI( xml );
                        progBar.setVisibility( View.INVISIBLE );
                    }
                };

            }
        } );
        // runNextTask();
        setTimer();

    }

    MenuDialog menuDialog;

    protected void setTimer()
    {
        final long elapse = 0;
        Runnable t = new Runnable()
        {
            public void run()
            {
                runNextTask();
                boolean autoupdate;
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( LiveLayout.this );
                autoupdate = sp.getBoolean( "spAutoUpdate", false );
                if( !isComplete && autoupdate )
                {
                    int item = sp.getInt( "spAutoDuration", 0 );
                    int timer = 0;
                    if( item == 0 )
                        timer = 60000;
                    else if( item == 1 )
                        timer = 120000;
                    else if( item == 2 )
                        timer = 180000;
                    else if( item == 3 )
                        timer = 300000;
                    if( timer > 0 )
                        taskHandler.postDelayed( this, timer ); // 60000 - 1min ,
                                                                // 90000 - 1.5 min ,
                                                                // 120000 - 2 min
                }
            }
        };
        taskHandler.postDelayed( t, elapse );
    }

    public void callEvent()
    {

        // if (menuDialog == null) {

        menuDialog = new MenuDialog( this, "LiveScore" );
        // }
        menuDialog.setCancelable( true );
        menuDialog.setCanceledOnTouchOutside( true );
        menuDialog.show();
    }

    @Override
    public boolean onKeyUp( int keyCode, KeyEvent event )
    {
        if( keyCode == KeyEvent.KEYCODE_MENU )
            callEvent();
        return super.onKeyUp( keyCode, event );
    }

    protected void runNextTask()
    {
        if( isInternetOn() )
        {

            downloadTask task = new downloadTask();
            task.dialog = dialog;
            dialog.setCancelable( true );
            task.execute();
        }
        else
        {
            String errorMsg = "Please connect your device to network and try again to get the Live Score.";
            Toast.makeText( LiveLayout.this, errorMsg, Toast.LENGTH_LONG ).show();
        }

    }

    public final boolean isInternetOn()
    {
        ConnectivityManager connec = (ConnectivityManager) LiveLayout.this.getSystemService( Context.CONNECTIVITY_SERVICE );
        // ARE WE CONNECTED TO THE NET
        if( connec.getNetworkInfo( 0 ).getState() == NetworkInfo.State.CONNECTED || connec.getNetworkInfo( 0 ).getState() == NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo( 1 ).getState() == NetworkInfo.State.CONNECTING || connec.getNetworkInfo( 1 ).getState() == NetworkInfo.State.CONNECTED )
        {
            // MESSAGE TO SCREEN FOR TESTING (IF REQ)
            // Toast.makeText(this, connectionType + ” connected”,
            // Toast.LENGTH_SHORT).show();
            return true;
        }
        else if( connec.getNetworkInfo( 0 ).getState() == NetworkInfo.State.DISCONNECTED || connec.getNetworkInfo( 1 ).getState() == NetworkInfo.State.DISCONNECTED )
        {
            // System.out.println(“Not Connected”);
            return false;
        }
        return false;
    }

    private class downloadTask extends AsyncTask< String, Void, String >
    {

        ProgressDialog dialog = null;

        @Override
        protected String doInBackground( String... arg0 )
        {
            // TODO Auto-generated method stub
			// Log.d( "aaaMatch URL", matchUrl );
            xml = XMLfunctions.getXML( matchUrl );
            // String
            // abc="<?xml version='1.0' encoding='UTF-16'?><scorecard><match><type>T20</type><series>India in South Africa one-off T20I</series><matchday>1</matchday><home>South Africa</home><away>India</away><venue>The Wanderers Stadium, Johannesburg</venue><matchdesc>One-off T20I</matchdesc><startdate>30 03 2012</startdate><enddate>30 03 2012</enddate><toss><winner>India</winner><decision>Fielding</decision></toss><state>inprogress</state><result type=''><winningteam/><wonbyruns/><wonbywickets/></result><manofmatch> </manofmatch><manofseries> </manofseries><umpires><Umpire1><Name>Johanes Cloete</Name><Country>RSA</Country></Umpire1><Umpire2><Name>Shaun George</Name><Country>RSA</Country></Umpire2><ThirdUmpire><Name>Adrian Holdstock</Name><Country>RSA</Country></ThirdUmpire><MatchReferee><Name>Chris Broad</Name><Country>Eng</Country></MatchReferee></umpires></match><currentscores><currentinningsno>1</currentinningsno><batteamname>South Africa</batteamname><bwlteamname>India</bwlteamname><batteamruns>209</batteamruns><batteamwkts>4</batteamwkts><batteamovers>19.4</batteamovers><batsman><playerid>484</playerid><name>Albie Morkel*</name><runs>6</runs><balls-faced>1</balls-faced><fours>0</fours><sixes>1</sixes></batsman><batsman><playerid>8283</playerid><name>Farhaan Behardien </name><runs>20</runs><balls-faced>11</balls-faced><fours>2</fours><sixes>0</sixes></batsman><bowler><playerid>413</playerid><name>Suresh Raina*</name><overs>3.4</overs><maidens>0</maidens><runs>39</runs><wickets>2</wickets></bowler><bowler><playerid>73</playerid><name>Irfan Pathan</name><overs>4</overs><maidens>0</maidens><runs>44</runs><wickets>1</wickets></bowler><maxovers/><target/><lastwicket><playerid>317</playerid><name>Justin Ontong</name><runs>203</runs><wicket-nbr>4</wicket-nbr></lastwicket><extras><byes>0</byes><wides>2</wides><noballs>0</noballs><legbyes>1</legbyes><penalty>0</penalty><total>3</total></extras><lastfiveovers><runs>68</runs><wickets>3</wickets><fours>6</fours><sixes>4</sixes></lastfiveovers><curr-runrate>10.63</curr-runrate></currentscores><prevOvers>1 2 . 1 1 2 <b>&nbsp;&nbsp;|&nbsp;&nbsp;</b>1 6 4 4 1 4 <b>&nbsp;&nbsp;|&nbsp;&nbsp;</b>W 2 1 1 1 6 <b>&nbsp;&nbsp;|&nbsp;&nbsp;</b>2 4 2 1 4 1</prevOvers><innings no='1'><totalruns>209</totalruns><totalwickets>4</totalwickets><totalovers>19.4</totalovers><declared/><followon/><batteam name='South Africa'><players><player><playerid>2234</playerid><name>Richard Levi</name><captain>no</captain><keeper>no</keeper><runs>19</runs><balls>7</balls><fours>4</fours><sixes>0</sixes><status>caught</status><bowler>Irfan Pathan</bowler><fielder>R Sharma</fielder></player><player><playerid>213</playerid><name>Jacques Kallis</name><captain>no</captain><keeper>no</keeper><runs>61</runs><balls>42</balls><fours>5</fours><sixes>2</sixes><status>caught</status><bowler>R Ashwin</bowler><fielder>R Sharma</fielder></player><player><playerid>7664</playerid><name>Colin Ingram</name><captain>no</captain><keeper>no</keeper><runs>78</runs><balls>50</balls><fours>8</fours><sixes>3</sixes><status>caught</status><bowler>S Raina</bowler><fielder>R Sharma</fielder></player><player><playerid>8283</playerid><name>Farhaan Behardien </name><captain>no</captain><keeper>no</keeper><runs>20</runs><balls>11</balls><fours>2</fours><sixes>0</sixes><status>batting</status><bowler/><fielder/></player><player><playerid>317</playerid><name>Justin Ontong</name><captain>no</captain><keeper>no</keeper><runs>22</runs><balls>7</balls><fours>2</fours><sixes>2</sixes><status>bowled</status><bowler>S Raina</bowler><fielder/></player><player><playerid>484</playerid><name>Albie Morkel</name><captain>no</captain><keeper>no</keeper><runs>6</runs><balls>1</balls><fours>0</fours><sixes>1</sixes><status>batting</status><bowler/><fielder/></player><player><playerid>8099</playerid><name>Dane Vilas</name><captain>no</captain><keeper>yes</keeper><runs>0</runs><balls>0</balls><fours>0</fours><sixes>0</sixes><status>dnb</status><bowler/><fielder/></player><player><playerid>487</playerid><name>Johan Botha</name><captain>yes</captain><keeper>no</keeper><runs>0</runs><balls>0</balls><fours>0</fours><sixes>0</sixes><status>dnb</status><bowler/><fielder/></player><player><playerid>1692</playerid><name>Wayne Parnell</name><captain>no</captain><keeper>no</keeper><runs>0</runs><balls>0</balls><fours>0</fours><sixes>0</sixes><status>dnb</status><bowler/><fielder/></player><player><playerid>1689</playerid><name>Lonwabo Tsotsobe</name><captain>no</captain><keeper>no</keeper><runs>0</runs><balls>0</balls><fours>0</fours><sixes>0</sixes><status>dnb</status><bowler/><fielder/></player><player><playerid>6320</playerid><name>Juan Theron</name><captain>no</captain><keeper>no</keeper><runs>0</runs><balls>0</balls><fours>0</fours><sixes>0</sixes>"
            // +"<status>dnb</status><bowler/><fielder/></player></players></batteam><fallofwickets><wicket><playerid>2234</playerid><nbr>1</nbr><runs>22</runs><overs>1.4</overs><batsman>Richard Levi</batsman></wicket><wicket><playerid>213</playerid><nbr>2</nbr><runs>141</runs><overs>14.6</overs><batsman>Jacques Kallis</batsman></wicket><wicket><playerid>7664</playerid><nbr>3</nbr><runs>168</runs><overs>17.1</overs><batsman>Colin Ingram</batsman></wicket><wicket><playerid>317</playerid><nbr>4</nbr><runs>203</runs><overs>19.3</overs><batsman>Justin Ontong</batsman></wicket></fallofwickets><extras><byes>0</byes><wides>2</wides><noballs>0</noballs><legbyes>1</legbyes><penalty>0</penalty><total>3</total></extras><bowlteam name='India'><players><player><playerid>592</playerid>name>Praveen Kumar</name><captain>no</captain><keeper>no</keeper><overs>2</overs><maidens>0</maidens><runsoff>22</runsoff><wickets>0</wickets><wides>1</wides><noballs>0</noballs></player><player><playerid>73</playerid><name>Irfan Pathan</name><captain>no</captain><keeper>no</keeper><overs>4</overs><maidens>0</maidens><runsoff>44</runsoff><wickets>1</wickets><wides>1</wides><noballs>0</noballs></player><player><playerid>1449</playerid><name>Vinay Kumar</name><captain>no</captain><keeper>no</keeper><overs>3</overs><maidens>0</maidens><runsoff>32</runsoff><wickets>0</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>1593</playerid><name>Ravichandran Ashwin</name><captain>no</captain><keeper>no</keeper><overs>4</overs><maidens>0</maidens><runsoff>33</runsoff><wickets>1</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>790</playerid><name>Yusuf Pathan</name><captain>no</captain><keeper>no</keeper><overs>1</overs><maidens>0</maidens><runsoff>9</runsoff><wickets>0</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>413</playerid><name>Suresh Raina</name><captain>no</captain><keeper>no</keeper><overs>3.4</overs><maidens>0</maidens><runsoff>39</runsoff><wickets>2</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>576</playerid><name>Rohit Sharma</name><captain>no</captain><keeper>no</keeper><overs>1</overs><maidens>0</maidens><runsoff>14</runsoff><wickets>0</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>1413</playerid><name>Virat Kohli</name><captain>no</captain><keeper>no</keeper><overs>1</overs><maidens>0</maidens><runsoff>15</runsoff><wickets>0</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>527</playerid><name>Robin Uthappa</name><captain>no</captain><keeper>no</keeper><overs>0</overs><maidens>0</maidens><runsoff>0</runsoff><wickets>0</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>308</playerid><name>Gautam Gambhir</name><captain>no</captain><keeper>no</keeper><overs>0</overs><maidens>0</maidens><runsoff>0</runsoff><wickets>0</wickets><wides>0</wides><noballs>0</noballs></player><player><playerid>265</playerid><name>MS Dhoni</name><captain>no</captain><keeper>no</keeper><overs>0</overs><maidens>0</maidens><runsoff>0</runsoff><wickets>0</wickets><wides>0</wides><noballs>0</noballs></player></players></bowlteam></innings></scorecard>";
            return xml;
        }

        @Override
        protected void onPostExecute( String result )
        {
            dialog.dismiss();
            // if (!result.equals(null) && !result.equals(""))
            // displayInUI(result);
            if( result != null && !result.equals( "" ) && first )
            {
                country.scroll( -25 + (int) ( Math.random() * 50 ), 4000 );
            }
            else if( result != null && !result.equals( "" ) && !first )
                displayInUI( result );

        }
    }

    private void setHeader( String team1, String team2 )
    {
        getDrawable iconObj = new getDrawable();
        ImageView img = (ImageView) findViewById( R.id.head_team1logo );
        img.setBackgroundResource( iconObj.getIcon_square( team1 ) );
        TextView txt = (TextView) findViewById( R.id.head_team1name );
        txt.setText( team1 );
        img = (ImageView) findViewById( R.id.head_team2logo );
        img.setBackgroundResource( iconObj.getIcon_square( team2 ) );
        txt = (TextView) findViewById( R.id.head_team2name );
        txt.setText( team2 );
    }

    protected String convertString( String str )
    {
        int len = str.length();

        int noOfZero = 3 - len;
        String returnStr = "";
        for( int i = 0; i < noOfZero; i++ )
            returnStr = returnStr + "0";

        returnStr += str;
        return returnStr;
    }

    // Wheel scrolled flag
    private boolean wheelScrolled = false;

    // Wheel scrolled listener
    OnWheelScrollListener scrolledListener = new OnWheelScrollListener()
    {
        public void onScrollingStarted( WheelView wheel )
        {
            wheelScrolled = true;
        }

        public void onScrollingFinished( WheelView wheel )
        {
            wheelScrolled = false;
            // updateStatus();
        }
    };

    /**
     * Initializes wheel
     * @param id the wheel widget Id
     */
    private void initWheel( int id, int items )
    {
        WheelView wheel = getWheel( id );
        wheel.setVisibleItems( items );
        wheel.setViewAdapter( new NumericWheelAdapter( this, 0, 9 ) );
        wheel.setCurrentItem( 0 );

        // wheel.addChangingListener(changedListener);
        wheel.addScrollingListener( scrolledListener );
        wheel.setCyclic( true );
        wheel.setEnabled( false );
        wheel.setInterpolator( new AnticipateOvershootInterpolator() );
    }

    private void initWheel1( int id, int items )
    {
        WheelView wheel = getWheel( id );

        wheel.setVisibleItems( items );
        wheel.setViewAdapter( new NumericWheelAdapter( this, 0, 10 ) );
        wheel.setCurrentItem( 0 );

        // wheel.addChangingListener(changedListener);
        wheel.addScrollingListener( scrolledListener );
        wheel.setCyclic( true );
        wheel.setEnabled( false );
        wheel.setInterpolator( new AnticipateOvershootInterpolator() );

    }

    /**
     * Returns wheel by Id
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel( int id )
    {
        return (WheelView) findViewById( id );
    }

    /**
     * Mixes wheel
     * @param id the wheel id
     */

    private void displayInUI( String xml )
    {

        Document doc = XMLfunctions.XMLfromString( xml );

        NodeList matchNode = doc.getElementsByTagName( "match" );

        int len = matchNode.getLength();

        Element matchElement = (Element) matchNode.item( 0 );

        String homeTeam = XMLfunctions.getValue( matchElement, "home" );
        String awayTeam = XMLfunctions.getValue( matchElement, "away" );
        String venue = XMLfunctions.getValue( matchElement, "venue" );

        TextView batsmanTxt = (TextView) findViewById( R.id.batsmanTxt );
        TextView bowlerTxt = (TextView) findViewById( R.id.bowlerTxt );

        TextView batsmenOne = (TextView) findViewById( R.id.batsmanOne );
        TextView batsmenTwo = (TextView) findViewById( R.id.batsmanTwo );
        TextView bowlerOne = (TextView) findViewById( R.id.bowlerOne );
        TextView bowlerTwo = (TextView) findViewById( R.id.bowlerTwo );
        TextView manOfTheMatch = (TextView) findViewById( R.id.manOfTheMatch );
        TextView matchDetails = (TextView) findViewById( R.id.scorematchDetails );

        batsmanTxt.setVisibility( View.GONE );
        bowlerTxt.setVisibility( View.GONE );
        batsmenOne.setVisibility( View.GONE );
        batsmenTwo.setVisibility( View.GONE );
        bowlerOne.setVisibility( View.GONE );
        bowlerTwo.setVisibility( View.GONE );
        manOfTheMatch.setVisibility( View.GONE );
        matchDetails.setVisibility( View.GONE );

        TextView venueTxtView = (TextView) findViewById( R.id.matchVenue );
        venueTxtView.setText( venue );

        String matchState = XMLfunctions.getValue( matchElement, "state" );

        NodeList currentScoreNode = doc.getElementsByTagName( "currentscores" );

        NodeList Innings = doc.getElementsByTagName( "innings" );
        String numberOfInningsNode = Integer.toString( Innings.getLength() );

        String firstinngRuns = "";
        String firstinngWkts = "";
        String firstinngOve = "";

        String secondinngRuns = "";
        String secondinngWkts = "";
        String secondinngOve = "";
        firstInng = "";
        liveScore = "";
        getDrawable iconObj = new getDrawable();

        if( Innings.getLength() > 1 )
        {
            LinearLayout layout = (LinearLayout) findViewById( R.id.prevInng );
            layout.setVisibility( View.VISIBLE );
            Element e = (Element) Innings.item( 0 );
            e.getNodeValue();

            // team icon toggling
            NodeList battingTeamnode = e.getElementsByTagName( "batteam" );
            Element batTeamEle = (Element) battingTeamnode.item( 0 );
            String batTeam = batTeamEle.getAttribute( "name" );

            batTeam = iconObj.getTeamShortCode( batTeam );
            TextView teamname = (TextView) findViewById( R.id.team2_name );
            teamname.setText( batTeam );

            ImageView teamAicon = (ImageView) findViewById( R.id.image2 );
            teamAicon.setBackgroundResource( iconObj.getIcon_square( batTeam ) );

            if( batTeam.equalsIgnoreCase( homeTeam ) )
            {
                firstInng = homeTeam;
            }
            else
            {
                firstInng = awayTeam;
            }

            firstinngRuns = XMLfunctions.getValue( e, "totalruns" );
            firstinngWkts = XMLfunctions.getValue( e, "totalwickets" );
            firstinngOve = XMLfunctions.getValue( e, "totalovers" );

            firstinngRuns = convertString( firstinngRuns );
            EditText txt = (EditText) findViewById( R.id.firsting1 );
            txt.setText( String.valueOf( firstinngRuns.charAt( 0 ) ) );
            txt = (EditText) findViewById( R.id.firsting2 );
            txt.setText( String.valueOf( firstinngRuns.charAt( 1 ) ) );
            txt = (EditText) findViewById( R.id.firsting3 );
            txt.setText( String.valueOf( firstinngRuns.charAt( 2 ) ) );

            txt = (EditText) findViewById( R.id.firstingWiks );
            txt.setText( firstinngWkts );

            TextView txt1 = (TextView) findViewById( R.id.team2_ovrs );
            txt1.setText( firstinngOve + " Ovrs" );

            firstInng += " " + firstinngRuns + "/" + firstinngWkts + " in " + firstinngOve + " ovrs";

            Element e1 = (Element) Innings.item( 1 );
            e1.getNodeValue();
            NodeList battingTeamnodesecond = e1.getElementsByTagName( "batteam" );
            Element batTeamElesecond = (Element) battingTeamnodesecond.item( 0 );
            String batTeamsecond = batTeamElesecond.getAttribute( "name" );

            battingTeam = iconObj.getTeamShortCode( batTeamsecond );
            if( !battingTeam.equalsIgnoreCase( wheelTeam ) )
                first = true;
            if( first )
                setcountrywheel( battingTeam );

            secondinngRuns = XMLfunctions.getValue( e1, "totalruns" );
            secondinngWkts = XMLfunctions.getValue( e1, "totalwickets" );
            secondinngOve = XMLfunctions.getValue( e1, "totalovers" );

            secondinngRuns = convertString( secondinngRuns );

            WheelView wheel = getWheel( R.id.runs_1 );
            wheel.setCurrentItem( Integer.parseInt( String.valueOf( secondinngRuns.charAt( 0 ) ) ), true );

            wheel = getWheel( R.id.runs_2 );
            wheel.setCurrentItem( Integer.parseInt( String.valueOf( secondinngRuns.charAt( 1 ) ) ), true );

            wheel = getWheel( R.id.runs_3 );
            wheel.setCurrentItem( Integer.parseInt( String.valueOf( secondinngRuns.charAt( 2 ) ) ), true );

            wheel = getWheel( R.id.wiks );
            wheel.setCurrentItem( Integer.parseInt( secondinngWkts ), true );

            txt1 = (TextView) findViewById( R.id.team1_ovrs );
            txt1.setText( secondinngOve + " Ovrs" );

            // country.scroll(-25 + (int)(Math.random() * 50), 4000);

            liveScore = firstInng + " " + batTeamsecond + secondinngRuns + "/" + secondinngWkts + " in " + secondinngOve + " ovrs";

        }
        else if( Innings.getLength() == 1 )
        {
            try
            {
                LinearLayout layout = (LinearLayout) findViewById( R.id.prevInng );
                layout.setVisibility( View.GONE );
                Element e = (Element) Innings.item( 0 );
                e.getNodeValue();

                NodeList battingTeamnode = e.getElementsByTagName( "batteam" );
                Element batTeamEle = (Element) battingTeamnode.item( 0 );
                String batTeam = batTeamEle.getAttribute( "name" );

                battingTeam = iconObj.getTeamShortCode( batTeam );
                if( first )
                    setcountrywheel( battingTeam );

                String otherTeam = "";
                if( team1.equalsIgnoreCase( batTeam ) )
                    otherTeam = team2;
                else
                    otherTeam = team1;

                TextView teamname = (TextView) findViewById( R.id.team2_name );
                teamname.setText( iconObj.getTeamShortCode( otherTeam ) );

                ImageView teamAicon = (ImageView) findViewById( R.id.image2 );
                teamAicon.setBackgroundResource( iconObj.getIcon_square( otherTeam ) );

                firstinngRuns = XMLfunctions.getValue( e, "totalruns" );
                firstinngWkts = XMLfunctions.getValue( e, "totalwickets" );
                firstinngOve = XMLfunctions.getValue( e, "totalovers" );

                // match still in preview mode
                if( firstinngRuns.equals( "" ) && firstinngWkts.equals( "" ) && firstinngOve.equals( "" ) )
                {

                }
                else
                {
                    if( batTeam.equalsIgnoreCase( homeTeam ) )
                    {

                        liveScore = homeTeam;
                    }
                    else
                    {

                        liveScore = awayTeam;

                    }

                    firstinngRuns = convertString( firstinngRuns );

                    WheelView wheel = getWheel( R.id.runs_1 );
                    wheel.setCurrentItem( Integer.parseInt( String.valueOf( firstinngRuns.charAt( 0 ) ) ), true );

                    wheel = getWheel( R.id.runs_2 );
                    wheel.setCurrentItem( Integer.parseInt( String.valueOf( firstinngRuns.charAt( 1 ) ) ), true );

                    wheel = getWheel( R.id.runs_3 );
                    wheel.setCurrentItem( Integer.parseInt( String.valueOf( firstinngRuns.charAt( 2 ) ) ), true );

                    wheel = getWheel( R.id.wiks );
                    wheel.setCurrentItem( Integer.parseInt( firstinngWkts ), true );

                    TextView txt1 = (TextView) findViewById( R.id.team1_ovrs );
                    txt1.setText( firstinngOve + " Ovrs" );

                    liveScore += " " + firstinngRuns + "/" + firstinngWkts + " in " + firstinngOve + " ovrs";

                }
            }
            catch( Exception ex )
            {
                Toast.makeText( getBaseContext(), ex.toString(), Toast.LENGTH_LONG ).show();
            }

        }
        else
        {

        }

        if( currentScoreNode.getLength() > 0 )
        {
            Element e = (Element) currentScoreNode.item( 0 );
            String currentinningsno = XMLfunctions.getValue( e, "currentinningsno" );
            String battingTeam = XMLfunctions.getValue( e, "batteamname" );
            String bowlingteam = XMLfunctions.getValue( e, "bwlteamname" );
            String runs = XMLfunctions.getValue( e, "batteamruns" );
            String wickets = XMLfunctions.getValue( e, "batteamwkts" );
            String overs = XMLfunctions.getValue( e, "batteamovers" );

            if( matchState.toLowerCase().equals( "complete" ) )
            {
                NodeList resultNode = matchElement.getElementsByTagName( "result" );
                Element resultElement = (Element) resultNode.item( 0 );
                String resultIS = resultElement.getAttribute( "type" );

                if( resultIS.toLowerCase().equals( "win" ) )
                {
                    String winningTeam = XMLfunctions.getValue( resultElement, "winningteam" );
                    String winByRuns = XMLfunctions.getValue( resultElement, "wonbyruns" );
                    String winByWkts = XMLfunctions.getValue( resultElement, "wonbywickets" );

                    matchDetails.setVisibility( View.VISIBLE );
                    if( winByRuns.equals( "" ) && !winByWkts.equals( "" ) )
                        matchDetails.setText( winningTeam + " won by " + winByWkts + " wickets." );
                    else if( winByWkts.equals( "" ) && !winByRuns.equals( "" ) )
                        matchDetails.setText( winningTeam + " won by " + winByRuns + " runs." );
                    // else
                    // matchDetails.setText("match drawn");

                    NodeList momNode = matchElement.getElementsByTagName( "manofmatch" );
                    Element momElement = (Element) momNode.item( 0 );
                    String strMOM = XMLfunctions.getElementValue( momElement );
                    strMOM = strMOM.replace( "\n", "" );
                    strMOM = strMOM.replace( "\t", "" );

                    manOfTheMatch.setVisibility( View.VISIBLE );
                    manOfTheMatch.setText( "Man of the match : " + strMOM );

                }
            }
            else if( matchState.toLowerCase().equals( "inprogress" ) )
            {

                NodeList batsmanNode = e.getElementsByTagName( "batsman" );
                String numberOfbatsmanNode = Integer.toString( batsmanNode.getLength() );

                Element b1 = (Element) batsmanNode.item( 0 );
                b1.getNodeValue();

                String batsman1 = XMLfunctions.getValue( b1, "name" );
                String runs1 = XMLfunctions.getValue( b1, "runs" );
                String bowlfaced1 = XMLfunctions.getValue( b1, "balls-faced" );
                String fours1 = XMLfunctions.getValue( b1, "fours" );
                String sixes1 = XMLfunctions.getValue( b1, "sixes" );

                batsmanTxt.setVisibility( View.VISIBLE );
                batsmenOne.setVisibility( View.VISIBLE );
                batsmenOne.setText( batsman1 + " " + runs1 + "(" + bowlfaced1 + ")" + "		4:" + " " + fours1 + "  6:" + " " + sixes1 );

                if( numberOfbatsmanNode.equals( "2" ) )
                {
                    b1 = (Element) batsmanNode.item( 1 );
                    b1.getNodeValue();

                    String batsman2 = XMLfunctions.getValue( b1, "name" );
                    String runs2 = XMLfunctions.getValue( b1, "runs" );
                    String bowlfaced2 = XMLfunctions.getValue( b1, "balls-faced" );
                    String fours2 = XMLfunctions.getValue( b1, "fours" );
                    String sixes2 = XMLfunctions.getValue( b1, "sixes" );

                    if( !batsman2.equals( "" ) )
                    {

                        batsmenTwo.setVisibility( View.VISIBLE );
                        batsmenTwo.setText( batsman2 + " " + runs2 + "(" + bowlfaced2 + ")" + "		4:" + " " + fours2 + "  6:" + " " + sixes2 );
                    }
                }

                // bowlers
                NodeList bowlerNode = e.getElementsByTagName( "bowler" );
                String numberOfbowler = Integer.toString( bowlerNode.getLength() );
                String bowlStr = "";

                if( bowlerNode.getLength() > 0 )
                {
                    Element bo1 = (Element) bowlerNode.item( 0 );
                    bo1.getNodeValue();

                    String bowler1 = XMLfunctions.getValue( bo1, "name" );
                    String bowlerovers1 = XMLfunctions.getValue( bo1, "overs" );
                    String bowlerruns1 = XMLfunctions.getValue( bo1, "runs" );
                    String bowlerwickets1 = XMLfunctions.getValue( bo1, "wickets" );
                    bowlStr = bowler1 + "  " + bowlerwickets1 + "/" + bowlerruns1 + " (" + bowlerovers1 + ")";

                    bowlerTxt.setVisibility( View.VISIBLE );
                    bowlerOne.setVisibility( View.VISIBLE );
                    bowlerOne.setText( bowlStr );

                    if( numberOfbowler.equals( "2" ) )
                    {
                        bowlStr = "";
                        bo1 = (Element) bowlerNode.item( 1 );
                        bo1.getNodeValue();

                        String bowler2 = XMLfunctions.getValue( bo1, "name" );
                        String bowlerovers2 = XMLfunctions.getValue( bo1, "overs" );
                        String bowlerruns2 = XMLfunctions.getValue( bo1, "runs" );
                        String bowlerwickets2 = XMLfunctions.getValue( bo1, "wickets" );

                        if( !bowler2.equals( "" ) )
                        {
                            bowlStr += bowler2 + "  " + bowlerwickets2 + "/" + bowlerruns2 + " (" + bowlerovers2 + ")";
                            bowlerTwo.setVisibility( View.VISIBLE );
                            bowlerTwo.setText( bowlStr );

                        }

                    }
                }

            }
            else if( matchState.toLowerCase().equals( "preview" ) )
            {

                NodeList tossNode = doc.getElementsByTagName( "toss" );
                int leng = tossNode.getLength();
                Element tossElement = (Element) tossNode.item( 0 );
                String winner = XMLfunctions.getValue( tossElement, "winner" );
                String decision = XMLfunctions.getValue( tossElement, "decision" );
                matchDetails.setVisibility( View.VISIBLE );
                if( winner.equals( "" ) || winner.equals( null ) )
                    matchDetails.setText( "Match not started yet." );
                else
                    matchDetails.setText( winner + " decided to " + decision.replace( "ing", "" ).replace( "tt", "t" ) + " first." );

            }
            else if( matchState.toLowerCase().equals( "stump" ) )
            {
                matchDetails.setVisibility( View.VISIBLE );
                matchDetails.setText( "Match state is stumps." );
            }
            else if( matchState.toLowerCase().equals( "draw" ) )
            {
                matchDetails.setVisibility( View.VISIBLE );
                matchDetails.setText( "Match state is Draw." );
            }
            else if( matchState.toLowerCase().equals( "innings break" ) )
            {
                matchDetails.setVisibility( View.VISIBLE );
                matchDetails.setText( "Innings Break." );

            }
            else if( matchState.toLowerCase().equals( "lunch" ) )
            {
                matchDetails.setVisibility( View.VISIBLE );
                matchDetails.setText( "Innings Break." );

            }
            else if( matchState.toLowerCase().equals( "rain" ) )
            {
                matchDetails.setVisibility( View.VISIBLE );
                matchDetails.setText( "Match delayed due to rain." );
            }
            else if( matchState.toLowerCase().equals( "dinner" ) )
            {
                matchDetails.setVisibility( View.VISIBLE );
                matchDetails.setText( "Dinner." );
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

    private void setcountrywheel( String name )
    {
        if( name != "" && first )
        {
            CountryAdapter ca = new CountryAdapter( con );
            String[] tmp = ca.countries;

            for( int i = 0; i < tmp.length; i++ )
            {
                if( tmp[i].equals( name ) )
                    country.setCurrentItem( i, true );
                first = false;
                wheelTeam = name;

            }
        }
        else if( xml != null && !xml.equals( "" ) )
            displayInUI( xml );

    }

    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter
    {
        // Countries names
        public String countries[] = new String[] { "AFG", "AUS", "BAN", "ENG", "IND", "IRE", "NZ", "PAK", "RSA", "SL", "WI", "ZIM" };
        // Countries flags
        private int flags[] = new int[] { R.drawable.afghanistan_sq, R.drawable.australia_sq, R.drawable.bangladesh_sq, R.drawable.england_sq, R.drawable.india_sq,
                R.drawable.ireland_sq, R.drawable.newzealand_sq, R.drawable.pakistan_sq, R.drawable.southafrica_sq, R.drawable.srilanka_sq, R.drawable.westindies_sq,
                R.drawable.zimbabwe_sq };

        /**
         * Constructor
         */
        protected CountryAdapter( Context context )
        {
            super( context, R.layout.country_layout, NO_RESOURCE );

            setItemTextResource( R.id.country_name );
        }

        @Override
        public View getItem( int index, View cachedView, ViewGroup parent )
        {
            View view = super.getItem( index, cachedView, parent );
            ImageView img = (ImageView) view.findViewById( R.id.flag );
            img.setImageResource( flags[index] );
            return view;
        }

        @Override
        public int getItemsCount()
        {
            return countries.length;
        }

        @Override
        protected CharSequence getItemText( int index )
        {
            return countries[index];
        }
    }

}
