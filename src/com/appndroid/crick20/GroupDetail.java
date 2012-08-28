package com.appndroid.crick20;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class GroupDetail extends Activity implements AnimationListener {

	TabHost m_tabHost;
	FrameLayout mFrameLayout;
	View menu;
	boolean menuOut = false;
	Animation anim;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (menuOut) {
			menu.setVisibility(View.INVISIBLE);
			menuOut = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.airport);

		String names = getIntent().getExtras().getString("teamnames");
		TextView t1 = (TextView) findViewById(R.id.title);
		t1.setText(names);

		mFrameLayout = (FrameLayout) this.findViewById(R.id.flipper);
		menu = mFrameLayout.findViewById(R.id.menu);

		final ImageView navigationImage = (ImageView) findViewById(R.id.nav);
		navigationImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Animation anim;
				if (!menuOut) {
					navigationImage.setBackgroundResource(R.drawable.navigationselected);
					menu.setVisibility(View.VISIBLE);
					ViewUtils.printView("menu", menu);
					anim = AnimationUtils.loadAnimation(GroupDetail.this,
							R.anim.push_right_in);
				} else {
					navigationImage.setBackgroundResource(R.drawable.navigationunselected);
					anim = AnimationUtils.loadAnimation(GroupDetail.this,
							R.anim.push_left_out);
				}
				anim.setAnimationListener(GroupDetail.this);
				// out.setAnimationListener(me);
				menu.startAnimation(anim);

			}
		});

		Context context = getApplicationContext();
		LayoutInflater inflater = getLayoutInflater();
		View toastRoot = inflater.inflate(R.layout.my_toast, null);
		TextView t11 = (TextView) toastRoot.findViewById(R.id.toasttext);
		t11.setText(getIntent().getExtras().getString("group"));
		Toast toast = new Toast(context);
		toast.setView(toastRoot);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		ViewUtils.printView("menu", menu);
		menuOut = !menuOut;
		if (!menuOut) {
			menu.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}
}
