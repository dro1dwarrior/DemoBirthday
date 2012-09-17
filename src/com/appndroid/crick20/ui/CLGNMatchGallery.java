package com.appndroid.crick20.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Gallery;

public class CLGNMatchGallery extends Gallery
{
  GestureDetector mGD;
  GestureDetector.SimpleOnGestureListener mGL = new GestureDetector.SimpleOnGestureListener()
  {
    public boolean onFling(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
    {
      return super.onFling(paramAnonymousMotionEvent1, paramAnonymousMotionEvent2, paramAnonymousFloat1, paramAnonymousFloat2);
    }

    public boolean onScroll(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
    {
      return super.onScroll(paramAnonymousMotionEvent1, paramAnonymousMotionEvent2, paramAnonymousFloat1, paramAnonymousFloat2);
    }
  };

  public CLGNMatchGallery(Context paramContext)
  {
    super(paramContext);
  }

  public CLGNMatchGallery(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public CLGNMatchGallery(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private boolean isScrollingLeft(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2)
  {
    if (paramMotionEvent2.getX() > paramMotionEvent1.getX());
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  void createObj()
  {
    this.mGD = new GestureDetector(this.mGL);
  }

  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    if (isScrollingLeft(paramMotionEvent1, paramMotionEvent2));
    for (float f = 500.0F; ; f = -500.0F)
      return super.onFling(paramMotionEvent1, paramMotionEvent2, f, paramFloat2);
  }
}

/* Location:           F:\Workspace\cricbuzz\classes_dex2jar.jar
 * Qualified Name:     com.cricbuzz.android.CLGNMatchGallery
 * JD-Core Version:    0.6.1
 */