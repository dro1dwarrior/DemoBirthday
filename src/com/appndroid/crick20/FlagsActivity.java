package com.appndroid.crick20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;

public class FlagsActivity extends Activity
{

    protected boolean _active = true;
    protected int _splashTime = 5800;
    protected boolean _display = true;
    Thread splashTread;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.groupsplash );

        final ImageView splashImageView = (ImageView) findViewById( R.id.SplashImageView );
        splashImageView.setBackgroundResource( R.drawable.flag );
        final AnimationDrawable frameAnimation = (AnimationDrawable) splashImageView.getBackground();
        splashImageView.post( new Runnable()
        {
            @Override
            public void run()
            {
                frameAnimation.start();
            }
        } );

        // thread for displaying the SplashScreen
        splashTread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    int waited = 0;
                    while( _active && ( waited < _splashTime ) )
                    {
                        sleep( 20 );
                        if( _active )
                        {
                            waited += 20;
                        }
                    }
                }
                catch( Exception e )
                {
                    // do nothing
                    e.printStackTrace();
                }
                finally
                {
                    // splashTread.stop();
                    if( _display )
                    {
                        finish();
                        Intent intent = new Intent( getApplicationContext(), Schedule.class );
                        intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity( intent );
                    }

                }
            }
        };
        splashTread.start();
    }

    @Override
    public boolean onTouchEvent( MotionEvent event )
    {
        if( event.getAction() == MotionEvent.ACTION_DOWN )
        {
            try
            {

                splashTread.sleep( 400 );

                synchronized( splashTread )
                {
                    splashTread.notifyAll();
                }

            }
            catch( InterruptedException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            _active = false;
        }
        return true;
    }

    @Override
    public boolean onKeyUp( int keyCode, KeyEvent event )
    {
        if( keyCode == KeyEvent.KEYCODE_BACK )
        {
            _active = false;
            _display = false;
            return true;
        }
        return super.onKeyUp( keyCode, event );
    }

}
