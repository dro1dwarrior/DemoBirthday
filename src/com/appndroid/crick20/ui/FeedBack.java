package com.appndroid.crick20.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appndroid.crick20.R;

public class FeedBack extends Activity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        // TODO Auto-generated method stub
        super.onCreate( savedInstanceState );
        setContentView( R.layout.feedback );

        Utils.setContext( this );
        final Button btnSubmit = (Button) findViewById( R.id.btnSubmit );
        btnSubmit.setEnabled( false );
        final EditText etMessage = (EditText) findViewById( R.id.etMessage );
        etMessage.addTextChangedListener( new TextWatcher()
        {

            @Override
            public void afterTextChanged( Editable s )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after )
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count )
            {
                // TODO Auto-generated method stub
                if( etMessage.getText().length() > 9 )
                    btnSubmit.setEnabled( true );
            }

        } );

        btnSubmit.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
                Intent i = new Intent( Intent.ACTION_SEND );
                i.setType( "message/rfc822" );
                i.putExtra( Intent.EXTRA_EMAIL, new String[] { "appndroidt20worldcup@gmail.com" } );
                i.putExtra( Intent.EXTRA_SUBJECT, "feedback" );
                i.putExtra( Intent.EXTRA_TEXT, etMessage.getText().toString().trim() );
                try
                {
                    startActivity( Intent.createChooser( i, "E-mail Feedback!" ) );
                }
                catch( Exception ex )
                {
                    ex.printStackTrace();
                    Toast.makeText( FeedBack.this, "There are no email clients installed.", Toast.LENGTH_SHORT ).show();
                }
            }
        } );

    }
}
