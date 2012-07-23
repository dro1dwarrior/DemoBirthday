package com.appndroid.crick20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class LogoActivity extends Activity
{

    private Drawable mCurrentDrawable;
    int count = 0;
    private SeekBar seekBar;
    Thread displayImage;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        this.requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.main );
        mCurrentDrawable = getResources().getDrawable( R.drawable.clip );
        findViewById( R.id.testView ).setBackgroundDrawable( mCurrentDrawable );

        seekBar = (SeekBar) findViewById( R.id.seekBar );
        seekBar.setMax( 100000 );
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

        showSplash( this, null );

    }

    private void showSplash( final Context context, final Intent intentShortCut )
    {
        // The thread to wait for splash screen events
        displayImage = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    synchronized( this )
                    {
                        // Wait given period of time or exit on touch
                        wait( 100 );
                    }
                    do
                    {
                        count += 2;
                        seekBar.setProgress( count / 2 );
                    }
                    while( count < 60000 );

                    if( count >= 60000 )
                    {
                        count = 0;
                        Intent intent;
                        intent = new Intent( getApplicationContext(), com.appndroid.crick20.FlagsActivity.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        LogoActivity.this.finish();
                        startActivity( intent );
                    }
                }
                catch( Exception ex )
                {
                    ex.printStackTrace();
                }

                finish();
            }
        };

        displayImage.start();
    }
}
