package com.appndroid.crick20;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService
{
    public GCMIntentService()
    {
        // TODO Auto-generated constructor stub
        super( "899727754395" );
    }

    @Override
    protected void onError( Context context, String errorId )
    {
        // TODO Auto-generated method stub
        Log.d( "GCMIntentService-onError", errorId );

    }

    @Override
    protected void onMessage( Context context, Intent intent )
    {
        // TODO Auto-generated method stub
        String szMessageData = intent.getStringExtra( "message" );
        Log.d( "GCMIntentService-onMessage", "Message Received**************************" + szMessageData );
        // handle message received
        // ShowNotification( context,"Message Received" );
        // Toast.makeText( context, "Message Received" , Toast.LENGTH_LONG).show();
        // if( IMServiceConnection.getIMService() == null )
        // {
        // Log.d( "GCMIntentService-onMessage", "Ifffff *********************" );
        // Intent intentService = new Intent( "com.geodesic.android.universalIM.controller.IMService" );
        // startService( intentService );
        // }
        // else
        // {
        // Log.d( "GCMIntentService-onMessage", "Else *********************" );
        // IMServiceConnection.getIMService().sendPollRequestforC2DM();
        // }
    }

    @Override
    protected void onRegistered( Context context, String registrationId )
    {
        // TODO Auto-generated method stub
        Log.d( "GCMIntentService-onRegistered", "GCM Registered Id " + registrationId );
        // IMServiceConnection.getIMService().sendRegistrationtoUM( registrationId );
        Log.d( " *** Inside GCMIntentService.onRegistered()", " ***" );

    }

    @Override
    protected void onUnregistered( Context context, String arg1 )
    {
        // TODO Auto-generated method stub
        Log.d( "GCMIntentService-onUnregistered", "got here!" + arg1 );

    }

    void ShowNotification( Context context, String szText )
    {
//        Intent notificationIntent = new Intent( this, favorites.class );
//        notificationIntent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
//        PendingIntent contentIntent = PendingIntent.getActivity( this, 0, notificationIntent, 0 );
//
//        Notification notification = new Notification( R.drawable.icon, szText, System.currentTimeMillis() );
//        notification.setLatestEventInfo( getApplicationContext(), "Push Notification", szText, contentIntent );
//
//        // look up the notification manager service
//        NotificationManager nm = (NotificationManager) context.getSystemService( context.NOTIFICATION_SERVICE );
//        nm.notify( 23434, notification );
    }

}
