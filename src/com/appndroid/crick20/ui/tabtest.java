package com.appndroid.crick20.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appndroid.crick20.R;
import com.appndroid.crick20.ui.GroupDetail.upcomingAdapter;
import com.appndroid.crick20.ui.SimpleGestureFilter.SimpleGestureListener;

public class tabtest extends Activity implements AnimationListener
{
    private static ViewFlipper flipper;
    private SimpleGestureFilter detector;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_MAX_DOWN_PATH = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    static boolean isSwipeToGroupApossible = false;
    static boolean isSwipeToGroupBpossible = true;
    private static boolean isCalBtnClicked = false;
    private static View view, view1;
    FrameLayout mFrameLayout;
    View menu;
    boolean menuOut = false;
    Animation anim;

    SQLiteDatabase db;
    getDrawable drawable;
    static ListView lv, lv1;
    static Cursor m_cursor, m_cursor1;
    static ListAdapter m_adapter, m_adapter1;
    ListView upcominglv, upcominglv1;
    int[] to;
    TextView textHeader1, textHeader2;
    fillList ptList;
    String[] from, from1, from2;

    private Animation inFromLeftAnimation()
    {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation( 2, -1.0F, 2, 0.0F, 2, 0.0F, 2, 0.0F );
        localTranslateAnimation.setDuration( 500L );
        localTranslateAnimation.setInterpolator( new AccelerateInterpolator() );
        return localTranslateAnimation;
    }

    private Animation inFromRightAnimation()
    {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation( 2, 1.0F, 2, 0.0F, 2, 0.0F, 2, 0.0F );
        localTranslateAnimation.setDuration( 500L );
        localTranslateAnimation.setInterpolator( new AccelerateInterpolator() );
        return localTranslateAnimation;
    }

    private Animation outToLeftAnimation()
    {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation( 2, 0.0F, 2, -1.0F, 2, 0.0F, 2, 0.0F );
        localTranslateAnimation.setDuration( 500L );
        localTranslateAnimation.setInterpolator( new AccelerateInterpolator() );
        return localTranslateAnimation;
    }

    private Animation outToRightAnimation()
    {
        TranslateAnimation localTranslateAnimation = new TranslateAnimation( 2, 0.0F, 2, 1.0F, 2, 0.0F, 2, 0.0F );
        localTranslateAnimation.setDuration( 500L );
        localTranslateAnimation.setInterpolator( new AccelerateInterpolator() );
        return localTranslateAnimation;
    }

    public void onCreate( Bundle paramBundle )
    {
        super.onCreate( paramBundle );
        drawable = new getDrawable();
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.infoflipper_layout );
        Utils.setContext( this );
        flipper = ( (ViewFlipper) findViewById( R.id.flip ) );

        gestureDetector = new GestureDetector( new MyGestureDetector() );
        gestureListener = new View.OnTouchListener()
        {
            public boolean onTouch( View v, MotionEvent event )
            {
                if( gestureDetector.onTouchEvent( event ) )
                {
                    return true;
                }
                return false;
            }
        };

        final Button superEightA = (Button) findViewById( R.id.Button0001 );
        final Button superEightB = (Button) findViewById( R.id.Button0002 );

        lv = (ListView) findViewById( R.id.pointsTable_group1 );
        lv.setEnabled( false );

        lv1 = (ListView) findViewById( R.id.pointsTable_group2 );
        lv1.setEnabled( false );

        upcominglv = (ListView) findViewById( R.id.upcomignlv_group1 );
        upcominglv.setEnabled( false );

        upcominglv1 = (ListView) findViewById( R.id.upcomignlv_group2 );
        upcominglv1.setEnabled( false );

        textHeader1 = (TextView) findViewById( R.id.header_group1 );
        textHeader2 = (TextView) findViewById( R.id.header_group2 );

        to = new int[] { R.id.stat_item1, R.id.stat_item2, R.id.stat_item3, R.id.stat_item4, R.id.stat_item5, R.id.stat_item6, R.id.stat_item7 };

        superEightA.setBackgroundDrawable( getResources().getDrawable( R.drawable.titleback ) );
        superEightB.setBackgroundDrawable( getResources().getDrawable( R.drawable.titlebackselected ) );

        mFrameLayout = (FrameLayout) this.findViewById( R.id.frameflipper );
        menu = mFrameLayout.findViewById( R.id.menu );

        Context context = getApplicationContext();
        LayoutInflater inflater = getLayoutInflater();
        View toastRoot = inflater.inflate( R.layout.my_toast, null );
        TextView t1 = (TextView) toastRoot.findViewById( R.id.toasttext );
        t1.setText( "Swipe option is also available. Swipe from right to left and vice versa" );
        Toast toast = new Toast( context );
        toast.setView( toastRoot );
        toast.setGravity( Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0 );
        toast.show();

        superEightA.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View paramView )
            {

                if( isSwipeToGroupApossible == true && isSwipeToGroupBpossible == false )
                {
                    superEightA.setBackgroundDrawable( getResources().getDrawable( R.drawable.titleback ) );
                    superEightB.setBackgroundDrawable( getResources().getDrawable( R.drawable.titlebackselected ) );

                    try
                    {

                        flipper.setInAnimation( tabtest.this.inFromLeftAnimation() );
                        flipper.setOutAnimation( tabtest.this.outToRightAnimation() );
                        flipper.showPrevious();

                        isSwipeToGroupApossible = false;
                        isSwipeToGroupBpossible = true;

                    }
                    catch( StackOverflowError e )
                    {
                        // TODO: handle exception
                        String exc = e.getMessage();
                    }
                }
            }
        } );

        superEightB.setOnClickListener( new View.OnClickListener()
        {
            public void onClick( View paramView )
            {

                if( isSwipeToGroupApossible == false && isSwipeToGroupBpossible == true )
                {

                    superEightB.setBackgroundDrawable( getResources().getDrawable( R.drawable.titleback ) );
                    superEightA.setBackgroundDrawable( getResources().getDrawable( R.drawable.titlebackselected ) );

                    isCalBtnClicked = true;
                    flipper.setInAnimation( tabtest.this.inFromRightAnimation() );
                    flipper.setOutAnimation( tabtest.this.outToLeftAnimation() );
                    flipper.showNext();

                    isSwipeToGroupApossible = true;
                    isSwipeToGroupBpossible = false;
                }

            }
        } );

        final ImageView navigationImage = (ImageView) findViewById( R.id.nav );
        navigationImage.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub

                // Animation anim;
                // if (!menuOut) {
                // navigationImage
                // .setBackgroundResource(R.drawable.navigationselected);
                // menu.setVisibility(View.VISIBLE);
                // ViewUtils.printView("menu", menu);
                // anim = AnimationUtils.loadAnimation(tabtest.this,
                // R.anim.push_right_in);
                // } else {
                // navigationImage
                // .setBackgroundResource(R.drawable.navigationunselected);
                // anim = AnimationUtils.loadAnimation(tabtest.this,
                // R.anim.push_left_out);
                // }
                // anim.setAnimationListener(tabtest.this);
                // // out.setAnimationListener(me);
                // menu.startAnimation(anim);
                //
                callEvent();
            }

        } );

    }

    MenuDialog menuDialog;

    public void callEvent()
    {

        // if (menuDialog == null) {

        menuDialog = new MenuDialog( this, "standings" );
        // }
        menuDialog.setCancelable( true );
        menuDialog.setCanceledOnTouchOutside( true );
        menuDialog.show();
    }

    public int getDataFromDB( String groupName )
    {

        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        from = new String[] { "Team", "P", "W", "L", "NR", "Pts", "NRR" };

        ptList = new fillList( from );
        Cursor cur = db.query( groupName, null, "Team !=''", null, null, null, null );
        ptList.fillRecordList( cur, ptList, "currentStats" );

        return cur.getCount();

    }

    public void fillData( ListView lst )
    {

        textHeader1.setText( "Points Table" );
        @SuppressWarnings("unchecked")
        SimpleAdapter adapter = new overrideAdapter( this, ptList.getFilledList(), R.layout.singlecurrntstat_layout, from, to, "currentStats" );
        lst.setAdapter( adapter );

    }

    @Override
    public boolean onKeyUp( int keyCode, KeyEvent event )
    {
        // TODO Auto-generated method stub
        if( keyCode == KeyEvent.KEYCODE_MENU )
            callEvent();
        return super.onKeyUp( keyCode, event );
    }

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        int recordCount = getDataFromDB( "Group1" );
        fillData( lv );

        recordCount = getDataFromDB( "Group2" );
        fillData( lv1 );
        super.onResume();
        db = openOrCreateDatabase( "worldcupt20.db", SQLiteDatabase.CREATE_IF_NECESSARY, null );

        m_cursor = db.rawQuery( "select * from schedule where gang ='" + "Group1" + "' AND WinnerTeam =''", null );
        m_cursor.moveToFirst();
        m_adapter = new upcomingAdapter( this, m_cursor, true );
        upcominglv.setAdapter( m_adapter );

        m_cursor1 = db.rawQuery( "select * from schedule where gang ='" + "Group2" + "' AND WinnerTeam =''", null );
        m_cursor1.moveToFirst();
        m_adapter1 = new upcomingAdapter( this, m_cursor1, true );
        upcominglv1.setAdapter( m_adapter1 );
        // Log.d("cursor", "cursor count is " + m_cursor.getCount() + "-"
        // + m_cursor1.getCount());
        if( menuOut )
        {
            menu.setVisibility( View.INVISIBLE );
            menuOut = false;
        }
    }

    class MyGestureDetector extends SimpleOnGestureListener
    {
        @Override
        public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY )
        {
            try
            {
                if( e1.getY() - e2.getY() > SWIPE_MAX_DOWN_PATH && isSwipeToGroupApossible )
                {
                    upcominglv1.setSelection( 4 );
                }
                else if( e2.getY() - e1.getY() > SWIPE_MAX_DOWN_PATH && isSwipeToGroupApossible )
                {
                    upcominglv1.setSelection( 1 );
                }
                else if( e1.getY() - e2.getY() > SWIPE_MAX_DOWN_PATH && isSwipeToGroupBpossible )
                {
                    upcominglv.setSelection( 4 );
                }
                else if( e2.getY() - e1.getY() > SWIPE_MAX_DOWN_PATH && isSwipeToGroupBpossible )
                {
                    upcominglv.setSelection( 1 );
                }
                // right to left swipe
                if( e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs( velocityX ) > SWIPE_THRESHOLD_VELOCITY
                        && ( isSwipeToGroupApossible == false && isSwipeToGroupBpossible == true ) )
                {
                    flipper.setInAnimation( inFromRightAnimation() );
                    flipper.setOutAnimation( outToLeftAnimation() );
                    flipper.showNext();
                    isSwipeToGroupApossible = true;
                    isSwipeToGroupBpossible = false;
                    final Button superEightA = (Button) findViewById( R.id.Button0001 );
                    final Button superEightB = (Button) findViewById( R.id.Button0002 );

                    superEightA.setBackgroundDrawable( getResources().getDrawable( R.drawable.titlebackselected ) );
                    superEightB.setBackgroundDrawable( getResources().getDrawable( R.drawable.titleback ) );

                }
                else if( e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs( velocityX ) > SWIPE_THRESHOLD_VELOCITY
                        && ( isSwipeToGroupApossible == true && isSwipeToGroupBpossible == false ) )
                {
                    flipper.setInAnimation( inFromLeftAnimation() );
                    flipper.setOutAnimation( outToRightAnimation() );
                    flipper.showPrevious();

                    isSwipeToGroupApossible = false;
                    isSwipeToGroupBpossible = true;

                    final Button superEightA = (Button) findViewById( R.id.Button0001 );
                    final Button superEightB = (Button) findViewById( R.id.Button0002 );

                    superEightA.setBackgroundDrawable( getResources().getDrawable( R.drawable.titleback ) );
                    superEightB.setBackgroundDrawable( getResources().getDrawable( R.drawable.titlebackselected ) );
                }
            }
            catch( Exception e )
            {
                // nothing
            }
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent( MotionEvent event )
    {
        super.dispatchTouchEvent( event );
        return gestureDetector.onTouchEvent( event );
    }

    public void onDoubleTap()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationEnd( Animation animation )
    {
        // TODO Auto-generated method stub
        ViewUtils.printView( "menu", menu );
        menuOut = !menuOut;
        if( !menuOut )
        {
            menu.setVisibility( View.INVISIBLE );
        }

    }

    @Override
    public void onAnimationRepeat( Animation animation )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart( Animation animation )
    {
        // TODO Auto-generated method stub

    }

    public class upcomingAdapter extends CursorAdapter
    {
        private LayoutInflater inflater;

        public upcomingAdapter( Context context, Cursor c, boolean autoRequery )
        {
            super( context, c, autoRequery );
            // TODO Auto-generated constructor stub
            inflater = LayoutInflater.from( context );
        }

        @Override
        public void bindView( View view, Context context, Cursor cursor )
        {
            ImageView imgTeamA = (ImageView) view.findViewById( R.id.upcoming_TeamAicon );
            ImageView imgTeamB = (ImageView) view.findViewById( R.id.upcoming_TeamBicon );

            TextView TeamAName = (TextView) view.findViewById( R.id.upcoming_TeamAName );
            TextView TeamBName = (TextView) view.findViewById( R.id.upcoming_TeamBName );

            TextView txtdate = (TextView) view.findViewById( R.id.upcoming_date );
            TextView txttime = (TextView) view.findViewById( R.id.upcoming_time );
            TextView txtvenue = (TextView) view.findViewById( R.id.upcoming_venue );

            String szTeamA = cursor.getString( cursor.getColumnIndex( "TeamA" ) );
            imgTeamA.setImageResource( drawable.getIcon( szTeamA ) );

            String szTeamB = cursor.getString( cursor.getColumnIndex( "TeamB" ) );
            imgTeamB.setImageResource( drawable.getIcon( szTeamB ) );

            TeamAName.setText( szTeamA );
            TeamBName.setText( szTeamB );

            String strDt = cursor.getString( cursor.getColumnIndex( "Date" ) ).trim();
            String[] strarr = strDt.split( " " );
            txtdate.setText( strarr[0].trim() + " " + drawable.getMonthName( strarr[1] ) + " (" + cursor.getString( cursor.getColumnIndex( "Other1" ) ).trim() + ")" );
            String time = cursor.getString( cursor.getColumnIndex( "GMT" ) ).trim();

            txttime.setText( time + " GMT" );
            txtvenue.setText( cursor.getString( cursor.getColumnIndex( "Venue" ) ).trim() );

        }

        @Override
        public View newView( Context context, Cursor cursor, ViewGroup parent )
        {
            // TODO Auto-generated method stub
            View view = inflater.inflate( R.layout.upcoming_row_2, parent, false );
            return view;
        }

    }

}
