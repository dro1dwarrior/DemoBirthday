package com.appndroid.crick20;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class LogoActivity extends Activity {

	private final Handler mHandler = new Handler();
	private Drawable mCurrentDrawable;
	private Timer myTimer;
	int count;
	private SeekBar seekBar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mCurrentDrawable = getResources().getDrawable(R.drawable.clip);
		findViewById(R.id.testView).setBackgroundDrawable(mCurrentDrawable);

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(10000);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mCurrentDrawable.setLevel(progress);
			}

		});

		myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				TimerMethod();
			}

		}, 0, 100);
	}

	private void TimerMethod() {
		// This method is called directly by the timer
		// and runs in the same thread as the timer.

		// We call the method that will work with the UI
		// through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick);
	}

	private Runnable Timer_Tick = new Runnable() {
		public void run() {

			// This method runs in the same thread as the UI.

			// Do something to the UI thread here
			count += 400;
			seekBar.setProgress(count);

			if (count > 10000) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent intent;
				intent = new Intent(getApplicationContext(),
						com.appndroid.crick20.GroupActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				finish();
				startActivity(intent);
			}

		}
	};
}