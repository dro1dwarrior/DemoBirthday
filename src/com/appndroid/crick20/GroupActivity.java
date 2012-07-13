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

public class GroupActivity extends Activity {

	 protected boolean _active = true;
	    protected int _splashTime = 4000;
	    protected boolean _disply = true;
	    Thread splashTread;

	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate( Bundle savedInstanceState )
	    {
	        super.onCreate( savedInstanceState );
	        setContentView( R.layout.groupsplash );

	        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService( Context.CONNECTIVITY_SERVICE );
	        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
	        NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );
	        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI );

	        // C2DM Start
//	        int build_version = Integer.parseInt( Build.VERSION.SDK );
//	        if( build_version >= 8 )
//	        {
//	            if( C2DMessaging.getRegistrationId( this ) == "" )
//	            {
//	                C2DMessaging.register( this, "ipltwenty12@gmail.com" );
//	                Log.d( "SplashScreen-C2DMregister()", "C2DM register call check passed" );
//	            }
//	            else
//	            {
//	                String szRegistrationId = C2DMessaging.getRegistrationId( this );
//	                Log.d( "SplashScreen-C2DMregister()", "C2DM Registered Id " + szRegistrationId );
//	                if( !Crick20Activity.getPushNotificationServer( this ).equals( "" ) && !Crick20Activity.getIsPushStatusPostedOnServer( this ) )
//	                {
//	                    String szServer = Crick20Activity.getPushNotificationServer( this );
//	                    String szCompleteUrl = szServer + szRegistrationId;
//
//	                    URL url;
//	                    try
//	                    {
//
//	                        if( ( mobNetInfo != null && mobNetInfo.isAvailable() && mobNetInfo.isConnected() )
//	                                || ( activeNetInfo != null && activeNetInfo.isAvailable() && activeNetInfo.isConnected() )
//	                                || ( wifiInfo != null && wifiInfo.isAvailable() && wifiInfo.isConnected() ) )
//	                        {
//	                            Log.d( "Crick20Activity", "URL is: " + szCompleteUrl );
//	                            HttpClient httpclient = new DefaultHttpClient();
//	                            HttpPost httppost = new HttpPost( szCompleteUrl );
//	                            HttpResponse response = httpclient.execute( httppost );
//	                            String szResponse = inputStreamToString( response.getEntity().getContent() ).toString();
//	                            Log.d( "Crick20Activity", "Response is :" + szResponse );
//	                            Crick20Activity.setIsPushStatusPostedOnServer( this, true );
//	                        }
//	                        else
//	                        {
//	                            Log.d( "splashScreen", "NETWORK NOT AVAILABLE" );
//	                            return;
//	                        }
//	                    }
//	                    catch( Exception e )
//	                    {
//	                        // TODO Auto-generated catch block
//	                        Log.d( "splashScreen", "EXCEPTION" );
//	                        e.printStackTrace();
//	                    }
//
//	                }
//	            }
//
//	        }
//	        // C2DM End

	        final ImageView splashImageView = (ImageView) findViewById( R.id.SplashImageView );
	        splashImageView.setBackgroundResource(R.drawable.flag);
	        final AnimationDrawable frameAnimation = (AnimationDrawable) splashImageView.getBackground();
	        splashImageView.post( new Runnable()
	        {
	            @Override
	            public void run()
	            {
	                frameAnimation.start();
	            }
	        } );

	        final GroupActivity sPlashScreen = this;

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
	                        sleep( 100 );
	                        if( _active )
	                        {
	                            waited += 100;
	                        }
	                    }
	                }
	                catch( InterruptedException e )
	                {
	                    // do nothing
	                }
	                finally
	                {
	                    // splashTread.stop();
	                    finish();
	                    if( _disply )
	                    {
	                    	finish();
	                        Intent schIntent = new Intent( getApplicationContext(), Hello.class );
	                        startActivity( schIntent );
	                    }

	                }
	            }
	        };
	        splashTread.start();
	    }

	    private StringBuilder inputStreamToString( InputStream content ) throws IOException
	    {
	        // TODO Auto-generated method stub
	        String line = "";
	        StringBuilder total = new StringBuilder();

	        BufferedReader rd = new BufferedReader( new InputStreamReader( content ) );
	        while( ( line = rd.readLine() ) != null )
	        {
	            total.append( line );
	        }
	        return total;
	    }

	    @Override
	    public boolean onTouchEvent( MotionEvent event )
	    {
	        if( event.getAction() == MotionEvent.ACTION_DOWN )
	        {
	            try
	            {

	                splashTread.sleep( 700 );

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
	            _disply = false;

	            return true;
	        }
	        return super.onKeyUp( keyCode, event );
	    }

}
