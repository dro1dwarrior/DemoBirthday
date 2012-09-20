package com.appndroid.crick20.ui;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.appndroid.crick20.R;
import com.google.android.gcm.GCMRegistrar;

public class LogoActivity extends Activity
{

    private final Handler mHandler = new Handler();
    private Drawable mCurrentDrawable;
    private Timer myTimer;
    int count;
    private SeekBar seekBar;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.main );
        mCurrentDrawable = getResources().getDrawable( R.drawable.clip );
        findViewById( R.id.testView ).setBackgroundDrawable( mCurrentDrawable );

        new GCMTask().execute();

        Calendar c = Calendar.getInstance();
        int milli = c.getTimeZone().getOffset( TimeZone.LONG );
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences( this );
        Editor edit = sp.edit();
        edit.putInt( "offset", milli );
        edit.commit();

        seekBar = (SeekBar) findViewById( R.id.seekBar );
        seekBar.setMax( 10000 );
        seekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
        {

            public void onStopTrackingTouch( SeekBar seekBar )
            {
            }

            public void onStartTrackingTouch( SeekBar seekBar )
            {
            }

            public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser )
            {
                mCurrentDrawable.setLevel( progress );
            }

        } );

        myTimer = new Timer();
        myTimer.schedule( new TimerTask()
        {
            @Override
            public void run()
            {
                TimerMethod();
            }

        }, 0, 100 );
    }

    private void TimerMethod()
    {
        // This method is called directly by the timer
        // and runs in the same thread as the timer.

        // We call the method that will work with the UI
        // through the runOnUiThread method.
        this.runOnUiThread( Timer_Tick );
    }

    private Runnable Timer_Tick = new Runnable()
    {
        public void run()
        {

            // This method runs in the same thread as the UI.

            // Do something to the UI thread here
            count += 400;
            seekBar.setProgress( count );

            if( count > 10000 )
            {
                try
                {
                    Thread.sleep( 100 );
                }
                catch( InterruptedException e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Intent intent;
                intent = new Intent( getApplicationContext(), com.appndroid.crick20.ui.FlagsActivity.class );
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                LogoActivity.this.finish();
                myTimer.cancel();
                startActivity( intent );
                finish();
            }

        }
    };

    @Override
    public boolean onKeyDown( int keyCode, KeyEvent event )
    {
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            myTimer.cancel();
            finish();
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess( pid );
            System.exit( 0 );
            return true;

        }
        // return super.onKeyUp(keyCode, event);
        return false;
    }

    private class GCMTask extends AsyncTask< Void, Void, Void >
    {
        @Override
        protected Void doInBackground( Void... arg0 )
        {
            // GCM Start
            int build_version = Integer.parseInt( Build.VERSION.SDK );
            if( build_version >= 8 )
            {
                GCMRegistrar.checkDevice( LogoActivity.this );
                GCMRegistrar.checkManifest( LogoActivity.this );
                final String regId = GCMRegistrar.getRegistrationId( LogoActivity.this );
                if( regId.equals( "" ) )
                {
                    GCMRegistrar.register( LogoActivity.this, "899727754395" );
                    Log.d( "LogoActivity-GCMRegister()", "GCM register call check passed" );
                }
                else
                {
                    Log.d( "LogoActivity-GCMRegister()", "GCM registered Id" + regId );
                }
            }
            else
                Log.d( "LogoActivity-GCMRegister()", "Push notification not supported" );
            // GCM End
            return null;
        }
    }
}
