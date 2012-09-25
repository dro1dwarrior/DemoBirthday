package com.appndroid.crick20.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.appndroid.crick20.R;

public class ViewPagerStyle1Activity extends FragmentActivity
{
    private ViewPager _mViewPager;
    private ViewPagerAdapter _adapter;
    TextView txt2;
    TextView txt1;
    ImageView navigationImage;

    /** Called when the activity is first created. */
    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.main_pager );
        Utils.setContext( this );
        setUpView();
        setTab();
        
        navigationImage = (ImageView) findViewById( R.id.nav );
        navigationImage.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                
                callEvent();
                //
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


    private void setUpView()
    {
        _mViewPager = (ViewPager) findViewById( R.id.viewPager );
        _adapter = new ViewPagerAdapter( getApplicationContext(), getSupportFragmentManager() );
        _mViewPager.setAdapter( _adapter );
        _mViewPager.setCurrentItem( 0 );
        txt2 = (TextView) findViewById( R.id.textView2 );
        txt1 = (TextView) findViewById( R.id.textView1 );
        txt2.setTextColor( Color.parseColor( "#FFFFFF" ) );
        txt1.setTextColor( Color.parseColor( "#69D2E7" ) );
        
    }

    private void setTab()
    {
        _mViewPager.setOnPageChangeListener( new OnPageChangeListener()
        {

            @Override
            public void onPageScrollStateChanged( int position )
            {
            }

            @Override
            public void onPageScrolled( int arg0, float arg1, int arg2 )
            {
            }

            @Override
            public void onPageSelected( int position )
            {
                // TODO Auto-generated method stub
                switch( position )
                {
                case 0:
                    findViewById( R.id.first_tab ).setVisibility( View.VISIBLE );
                    findViewById( R.id.second_tab ).setVisibility( View.INVISIBLE );
                    
                    txt2.setTextColor( Color.parseColor( "#FFFFFF" ) );
                    txt1.setTextColor( Color.parseColor( "#69D2E7" ) );
                    break;

                case 1:
                    findViewById( R.id.first_tab ).setVisibility( View.INVISIBLE );
                    findViewById( R.id.second_tab ).setVisibility( View.VISIBLE );
                    
                    txt1.setTextColor( Color.parseColor( "#FFFFFF" ) );
                    txt2.setTextColor( Color.parseColor( "#69D2E7" ) );
                    break;
                }
            }

        } );

    }
}
