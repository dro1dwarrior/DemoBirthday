package com.appndroid.crick20;

import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class LogoActivity extends Activity
{

    private final Handler mHandler = new Handler();
    private Drawable mCurrentDrawable;
    private Timer myTimer;
    int count = 0;
    private SeekBar seekBar;
    Thread displayImage;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
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

        displayImage = new Thread()
        {
            public void run()
            {
                do
                {
                    count += 3;
                    seekBar.setProgress( count );
                }
                while( count < 100000 );

                if( count >= 100000 )
                {
                    count = 0;
                    try
                    {
                        displayImage.sleep( 100 );
                    }
                    catch( Exception e )
                    {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    Intent intent;
                    intent = new Intent( getApplicationContext(), com.appndroid.crick20.FlagsActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    LogoActivity.this.finish();
                    startActivity( intent );
                }

            }
        };
        displayImage.start();
    }

}
