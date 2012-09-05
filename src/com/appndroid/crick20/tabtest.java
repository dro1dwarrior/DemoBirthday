package com.appndroid.crick20;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appndroid.crick20.SimpleGestureFilter.SimpleGestureListener;

public class tabtest extends Activity implements SimpleGestureListener , AnimationListener {
	private static ViewFlipper flipper;
	private SimpleGestureFilter detector;

	static boolean isSwipeToGroupApossible = false;
	static boolean isSwipeToGroupBpossible = true;
	private static boolean isCalBtnClicked = false;
	private static View view, view1;
	FrameLayout mFrameLayout;
	View menu;
	boolean menuOut = false;
	Animation anim;
	
	SQLiteDatabase db;
	ListView lv;
	int[] to;
	TextView textHeader1;
	fillList ptList;
	String[] from, from1, from2;

	private Animation inFromLeftAnimation() {
		TranslateAnimation localTranslateAnimation = new TranslateAnimation(2,
				-1.0F, 2, 0.0F, 2, 0.0F, 2, 0.0F);
		localTranslateAnimation.setDuration(500L);
		localTranslateAnimation.setInterpolator(new AccelerateInterpolator());
		return localTranslateAnimation;
	}

	private Animation inFromRightAnimation() {
		TranslateAnimation localTranslateAnimation = new TranslateAnimation(2,
				1.0F, 2, 0.0F, 2, 0.0F, 2, 0.0F);
		localTranslateAnimation.setDuration(500L);
		localTranslateAnimation.setInterpolator(new AccelerateInterpolator());
		return localTranslateAnimation;
	}

	private Animation outToLeftAnimation() {
		TranslateAnimation localTranslateAnimation = new TranslateAnimation(2,
				0.0F, 2, -1.0F, 2, 0.0F, 2, 0.0F);
		localTranslateAnimation.setDuration(500L);
		localTranslateAnimation.setInterpolator(new AccelerateInterpolator());
		return localTranslateAnimation;
	}

	private Animation outToRightAnimation() {
		TranslateAnimation localTranslateAnimation = new TranslateAnimation(2,
				0.0F, 2, 1.0F, 2, 0.0F, 2, 0.0F);
		localTranslateAnimation.setDuration(500L);
		localTranslateAnimation.setInterpolator(new AccelerateInterpolator());
		return localTranslateAnimation;
	}

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.infoflipper_layout);
		flipper = ((ViewFlipper) findViewById(R.id.flip));

		detector = new SimpleGestureFilter(this, this);
		View gestureView = (View) findViewById(R.id.gestures);

		final Button superEightA = (Button) findViewById(R.id.Button0001);
		final Button superEightB = (Button) findViewById(R.id.Button0002);
		
		lv = (ListView) findViewById(R.id.currentstatslistview1);
		lv.setEnabled(false);
		
		textHeader1 = (TextView) findViewById(R.id.record11);

		to = new int[] { R.id.stat_item1, R.id.stat_item2, R.id.stat_item3,
				R.id.stat_item4, R.id.stat_item5, R.id.stat_item6,
				R.id.stat_item7 };

	
		superEightA.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.titleback));
		superEightB.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.titlebackselected));
		
		mFrameLayout = (FrameLayout) this.findViewById(R.id.frameflipper);
		menu = mFrameLayout.findViewById(R.id.menu);

		Context context = getApplicationContext();
		LayoutInflater inflater = getLayoutInflater();
		View toastRoot = inflater.inflate(R.layout.my_toast, null);
		TextView t1 = (TextView) toastRoot.findViewById(R.id.toasttext);
		t1.setText("Swipe option is also available. Swipe from right to left and vice versa");
		Toast toast = new Toast(context);
		toast.setView(toastRoot);
		toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
				0, 0);
		toast.show();

		superEightA.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {

				if (isSwipeToGroupApossible == true
						&& isSwipeToGroupBpossible == false) {
					superEightA.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.titleback));
					superEightB.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.titlebackselected));

					try {

						flipper.setInAnimation(tabtest.this
								.inFromLeftAnimation());
						flipper.setOutAnimation(tabtest.this
								.outToRightAnimation());
						flipper.showPrevious();

						isSwipeToGroupApossible = false;
						isSwipeToGroupBpossible = true;

					} catch (StackOverflowError e) {
						// TODO: handle exception
						String exc = e.getMessage();
					}
				}
			}
		});

		superEightB.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {

				if (isSwipeToGroupApossible == false
						&& isSwipeToGroupBpossible == true) {

					superEightB.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.titleback));
					superEightA.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.titlebackselected));

					isCalBtnClicked = true;
					flipper.setInAnimation(tabtest.this.inFromRightAnimation());
					flipper.setOutAnimation(tabtest.this.outToLeftAnimation());
					flipper.showNext();

					isSwipeToGroupApossible = true;
					isSwipeToGroupBpossible = false;
				}

			}
		});
		
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
					anim = AnimationUtils.loadAnimation(tabtest.this,
							R.anim.push_right_in);
				} else {
					navigationImage.setBackgroundResource(R.drawable.navigationunselected);
					anim = AnimationUtils.loadAnimation(tabtest.this,
							R.anim.push_left_out);
				}
				anim.setAnimationListener(tabtest.this);
				// out.setAnimationListener(me);
				menu.startAnimation(anim);

			}
		});

	}
	public int getDataFromDB() {

		db = openOrCreateDatabase("worldcupt20.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);

		from = new String[] { "Team", "P", "W", "L", "NR", "Pts", "NRR" };

		ptList = new fillList(from);
		Cursor cur = db.query("Group1", null,
				null, null, null, null, null);
		ptList.fillRecordList(cur, ptList, "currentStats");

		return cur.getCount();

	}
	
	public void fillData() {

		textHeader1.setText("Point Table");
		@SuppressWarnings("unchecked")
		SimpleAdapter adapter = new overrideAdapter(this,
				ptList.getFilledList(), R.layout.singlecurrntstat_layout, from,
				to, "currentStats");
		lv.setAdapter(adapter);

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		int recordCount = getDataFromDB();
		fillData();
		super.onResume();
		db = openOrCreateDatabase("worldcupt20.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		if (menuOut) {
			menu.setVisibility(View.INVISIBLE);
			menuOut = false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
        
		switch (direction) {

		case SimpleGestureFilter.SWIPE_LEFT:
			if (isSwipeToGroupApossible == false
					&& isSwipeToGroupBpossible == true) {
				tabtest.this.flipper.setInAnimation(tabtest.this
						.inFromRightAnimation());
				tabtest.this.flipper.setOutAnimation(tabtest.this
						.outToLeftAnimation());
				tabtest.this.flipper.showNext();

				isSwipeToGroupApossible = true;
				isSwipeToGroupBpossible = false;
				final Button superEightA = (Button) findViewById(R.id.Button0001);
				final Button superEightB = (Button) findViewById(R.id.Button0002);

			
				superEightA.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.titlebackselected));
				superEightB.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.titleback));
				
			}

			// tabtest.this.flipper.
			break;

		case SimpleGestureFilter.SWIPE_RIGHT:

			if (isSwipeToGroupApossible == true
					&& isSwipeToGroupBpossible == false) {
				tabtest.this.flipper.setInAnimation(tabtest.this
						.inFromLeftAnimation());
				tabtest.this.flipper.setOutAnimation(tabtest.this
						.outToRightAnimation());
				tabtest.this.flipper.showPrevious();

				isSwipeToGroupApossible = false;
				isSwipeToGroupBpossible = true;
				
				final Button superEightA = (Button) findViewById(R.id.Button0001);
				final Button superEightB = (Button) findViewById(R.id.Button0002);

			
				superEightA.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.titleback));
				superEightB.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.titlebackselected));
				
				
			}

			break;
		}

	}

	public void onDoubleTap() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		ViewUtils.printView("menu", menu);
		menuOut = !menuOut;
		if (!menuOut) {
			menu.setVisibility(View.INVISIBLE);
		}
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
