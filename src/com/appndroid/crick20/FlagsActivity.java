package com.appndroid.crick20;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView( R.layout.groupsplash );

        new CopyDBTask().execute();
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
                        Intent intent = new Intent( getApplicationContext(), HomeScreen.class );
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

    private class CopyDBTask extends AsyncTask< Void, Void, Void >
    {
        @Override
        protected Void doInBackground( Void... arg0 )
        {
            copyFromAssetsDatabase();
            return null;
        }

        @Override
        protected void onPostExecute( Void result )
        {
            super.onPostExecute( result );
        }
    }

    public void copyFromAssetsDatabase()
    {
        // TODO Auto-generated method stub

        DataBaseHelper myDbHelper = new DataBaseHelper( this );

        try
        {

            myDbHelper.createDataBase();
            Log.d( "MyTest", "Databse Created" );

        }
        catch( IOException ioe )
        {

            throw new Error( "Unable to create database" );

        }

        try
        {

            myDbHelper.openDataBase();

        }
        catch( SQLException sqle )
        {

            throw sqle;

        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if( _display && keyCode== KeyEvent.KEYCODE_BACK)
        {
            finish();
            Intent intent = new Intent( getApplicationContext(), HomeScreen.class );
            intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( intent );
        }
    	return true;
    	
    }

}
