package com.appndroid.crick20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class HomeScreen extends Activity
{


    ImageView scheduleClick, pointsTableClick, aboutClick, settingsClick;
    Context mcontext;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home_new );

        mcontext = this;
        // aboutClick = (ImageView) findViewById(R.id.about_img);
        // settingsClick = (ImageView) findViewById(R.id.settings_img);
        // scheduleClick=(ImageView)findViewById(R.id.sch_img);
        // pointsTableClick = (ImageView) findViewById(R.id.standings_img);
        //
        // scheduleClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // Intent schIntent = new Intent(mcontext,
        // Schedule.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        //
        // }
        // });
        //
        // pointsTableClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        //
        // Intent schIntent = new Intent(mcontext,
        // GroupTab.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        //
        // }
        // });
        //
        // settingsClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // Intent schIntent = new Intent(mcontext,
        // Settings.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        // }
        // });
        //
        // aboutClick.setOnClickListener(new View.OnClickListener() {
        //
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // Intent schIntent = new Intent(mcontext,
        // About.class);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // schIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mcontext.startActivity(schIntent);
        //
        // }
        // });
        //
    }
}
