package com.laudev.android.scorekeeper;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by kevin on 3/12/16.
 */
public class ListViewSwipeDetector implements View.OnTouchListener {

    private float downY, upY, downX, upX;
    private Handler handler;
    private Runnable runnable;
    private Context context;
    private ViewConfiguration vc;
    private int mTouchSlop;
    private boolean checkScrollDirection = true;
    private boolean checkIfHold = true;
    private boolean isScrolling;
    private boolean isVerticalScrolling = false;
    private boolean isHorizontalScrolling = false;

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
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            // get initial Y value to compare distance moved
            case MotionEvent.ACTION_DOWN: {
                downY = event.getRawY();
                downX = event.getRawX();
            }

            // if initial Y value is greater than slop, intercept
            case MotionEvent.ACTION_MOVE: {
                upY = event.getRawY();
                upX = event.getRawX();
                float deltaY = downY - upY;
                float deltaX = downX - upX;

                if (checkIfHold) {
                    if (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop) {
                        checkIfHold = false;
                        isScrolling = true;
                    }
                }

                if (isScrolling) {
                    handler.removeCallbacks(runnable);
                    if (checkScrollDirection) {
                        if (Math.abs(deltaY) > Math.abs(deltaX)) {
                            isVerticalScrolling = true;
                        } else {
                            isHorizontalScrolling = true;
                        }
                        checkScrollDirection = false;
                    }
                }

                if (isVerticalScrolling) {
                    return true;
                } else if (isHorizontalScrolling) {
                    return false;
                }
            }

            case MotionEvent.ACTION_UP: {
                checkIfHold = true;
                checkScrollDirection = true;
            }
        }
        return false;
    }
}
