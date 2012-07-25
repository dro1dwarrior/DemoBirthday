package com.appndroid.crick20;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class Schedule extends Activity
{

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        setContentView( R.layout.schedule );

        MyPagerAdapter adapter = new MyPagerAdapter();
        ViewPager myPager = (ViewPager) findViewById( R.id.myfivepanelpager );
        myPager.setAdapter( adapter );
        myPager.setCurrentItem( 0 );

    }

}
