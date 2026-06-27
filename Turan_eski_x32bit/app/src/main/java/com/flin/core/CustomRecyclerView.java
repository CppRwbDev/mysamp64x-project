package com.flinc.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerView
  extends RecyclerView
{
  public boolean vertical = true;
  
  public CustomRecyclerView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public int getScrollForRecycler()
  {
    return computeVerticalScrollOffset();
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if (vertical) {
      return super.onInterceptTouchEvent(paramMotionEvent);
    }
    return false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (vertical) {
      return super.onTouchEvent(paramMotionEvent);
    }
    return false;
  }
  
  public void setEnableScrolling(boolean paramBoolean)
  {
    vertical = paramBoolean;
  }
}
