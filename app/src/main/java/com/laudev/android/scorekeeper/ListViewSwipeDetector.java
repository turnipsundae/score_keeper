package com.laudev.android.scorekeeper;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by kevin on 3/12/16.
 */
public class ListViewSwipeDetector implements View.OnTouchListener {

    private final String LOG_TAG = ListViewSwipeDetector.class.getSimpleName();
    private float downY, upY, downX, upX;
    private Handler handler;
    private Runnable runnable;
    private Context context;
    private ViewConfiguration vc;
    private int mTouchSlop;
    private boolean checkScrollDirection;
    private boolean checkForHold;
    private boolean isScrolling;
    private boolean isVerticalScrolling;
    private boolean isHorizontalScrolling;

    /*
    *  used to handle scrolling action and intercept onLongPress
    *  from each ListView row onTouch
    *
    *  @param handler used for rows
    *  @param runnable used for 1 second delay
    */
    public ListViewSwipeDetector(Context context, Handler handler, Runnable runnable) {
        this.context = context;
        this.handler = handler;
        this.runnable = runnable;
        vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        checkScrollDirection = true;
        checkForHold = true;
        isScrolling = false;
        isVerticalScrolling = false;
        isHorizontalScrolling = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /*switch (event.getAction()) {

            // get initial Y value to compare distance moved
            case MotionEvent.ACTION_DOWN: {
                downY = event.getRawY();
                downX = event.getRawX();
                Log.v(LOG_TAG, "Action down X,Y:" + downX + "," + downY);
                return false;
            }

            // if initial Y value is greater than slop, intercept
            case MotionEvent.ACTION_MOVE: {
                upY = event.getRawY();
                upX = event.getRawX();
                float deltaY = downY - upY;
                float deltaX = downX - upX;
                Log.v(LOG_TAG, "Action MOVE");

                if (checkForHold) {
                    Log.v(LOG_TAG, "Check for Hold");
                    if (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop) {
                        checkForHold = false;
                        isScrolling = true;
                        Log.v(LOG_TAG, "scrolling");
                    }
                }

                if (isScrolling) {
                    handler.removeCallbacks(runnable);
                    if (checkScrollDirection) {
                        if (Math.abs(deltaY) > mTouchSlop) {
                            isVerticalScrolling = true;
                            Log.v(LOG_TAG, "is Vertical Scrolling");
                        } else if (Math.abs(deltaX) > mTouchSlop){
                            isHorizontalScrolling = true;
                            Log.v(LOG_TAG, "is Horizontal Scrolling");
                        }
                        checkScrollDirection = false;
                    }
                }

                if (isVerticalScrolling) {
                    return true;
                } else {
                    return false;
                }
            }

            case MotionEvent.ACTION_UP: {
                checkForHold = true;
                checkScrollDirection = true;
                isHorizontalScrolling = false;
                isScrolling = false;

                if (isVerticalScrolling) {
                    isVerticalScrolling = false;
                    return true;
                } else {
                    return false;
                }
            }
        }*/
        return false;
    }
}
