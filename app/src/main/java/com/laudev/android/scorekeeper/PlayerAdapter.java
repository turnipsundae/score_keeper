package com.laudev.android.scorekeeper;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by kevin on 3/17/16.
 */
public class PlayerAdapter extends ArrayAdapter<Player> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Player> data;
    private Handler handler;
    private Runnable runnable;
    private SelectedPlayer selectedPlayer;

    public PlayerAdapter(Context context, int layoutResourceId, ArrayList<Player> data,
                         Handler handler, Runnable runnable, SelectedPlayer selectedPlayer) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.handler = handler;
        this.runnable = runnable;
        this.selectedPlayer = selectedPlayer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlayerHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PlayerHolder();
            holder.mainView = (LinearLayout) row.findViewById(R.id.player_object_mainview);
            holder.deleteView = (RelativeLayout) row.findViewById(R.id.player_object_deleteview);
            holder.txtIcon = (TextView)row.findViewById(R.id.txtIcon);
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtScore = (TextView)row.findViewById(R.id.txtScore);
            holder.changeScoreView = (LinearLayout) row.findViewById(R.id.change_score_view);
            holder.txtAdd = (TextView) holder.changeScoreView.findViewById(R.id.txt_add);
            holder.txtSubtract = (TextView) holder.changeScoreView.findViewById(R.id.txt_subtract);
            holder.stackofRows = (RelativeLayout) holder.mainView.getParent();

            row.setTag(holder);
        }
        else
        {
            holder = (PlayerHolder)row.getTag();
        }

        Player player = data.get(position);
        holder.txtName.setText(player.name);
        String iconTxt = "";
        if (player.name != null) {
            iconTxt = player.name.substring(0,1);
        }
        holder.txtIcon.setText(iconTxt);
        holder.txtScore.setText("" + player.score);

        // Recenter main view after non-swipe movements
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mainView.getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        holder.mainView.setLayoutParams(params);

        SwipeDetector swipeDetector = new SwipeDetector(holder, position, handler, runnable, selectedPlayer);
        SwipeDetector2 swipeDetector2 = new SwipeDetector2(holder, position, handler, runnable, selectedPlayer);


        // this works
        holder.txtName.setOnTouchListener(swipeDetector);
        holder.txtScore.setOnTouchListener(swipeDetector);
        holder.txtIcon.setOnTouchListener(swipeDetector);
        holder.txtAdd.setOnTouchListener(swipeDetector);
        holder.txtSubtract.setOnTouchListener(swipeDetector2);

        return row;
    }

    static class PlayerHolder
    {
        RelativeLayout deleteView;
        LinearLayout mainView;
        TextView txtIcon;
        TextView txtName;
        TextView txtScore;
        LinearLayout changeScoreView;
        TextView txtAdd;
        TextView txtSubtract;
        RelativeLayout stackofRows;
    }

    public class SwipeDetector implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 40; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX, downY, upY;
        private PlayerHolder holder;
        private int position;
        boolean isSingleTap;
        Handler handler;
        Runnable mLongPressed;
        SelectedPlayer selectedPlayer;
        private final String LOG_TAG = SwipeDetector.class.getSimpleName();
        private int mTouchSlop;
        private boolean checkScrollDirection;
        private boolean checkForHold;
        private boolean isScrolling;
        private boolean isVerticalScrolling;
        private boolean isHorizontalScrolling;


        public SwipeDetector(PlayerHolder h, int pos, Handler handler, Runnable runnable, SelectedPlayer selectedPlayer) {
            holder = h;
            position = pos;
            this.handler = handler;
            mLongPressed = runnable;
            this.selectedPlayer = selectedPlayer;
            ViewConfiguration vc = ViewConfiguration.get(context);
            mTouchSlop = vc.getScaledTouchSlop();
            isSingleTap = true;
            checkScrollDirection = true;
            checkForHold = true;
            isScrolling = false;
            isVerticalScrolling = false;
            isHorizontalScrolling = false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View stackOfRows = (View) holder.mainView.getParent();
            ListView listView = (ListView) stackOfRows.getParent();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN: {
                    Log.v(LOG_TAG, "Action down X:" + downX);
                    downX = event.getRawX();
                    downY = event.getRawY();
                    selectedPlayer.setPosition(position);
                    // start timer. if >1 sec go to Action Up
                    if (motionInterceptDisallowed) {
                        handler.postDelayed(mLongPressed, 1000);
                    }

                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getRawX();
                    upY = event.getRawY();
                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    Log.v(LOG_TAG, "Action MOVE");

                    if (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop) {
                        handler.removeCallbacks(null);
                    }

                    if (checkForHold) {
                        Log.v(LOG_TAG, "Check for Hold");
                        if (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop) {
                            isSingleTap = false;
                            checkForHold = false;
                            isScrolling = true;
                            handler.removeCallbacks(runnable);
                            Log.v(LOG_TAG, "scrolling");
                        }
                    }

                    if (isScrolling) {
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
                        motionInterceptDisallowed = false;
                    } else {
                        motionInterceptDisallowed = true;
                    }
                    listView.requestDisallowInterceptTouchEvent(motionInterceptDisallowed);

                    swipe(holder, -(int) deltaX, MIN_LOCK_DISTANCE);

                    return true;
                }

                case MotionEvent.ACTION_UP: {
                    upX = event.getRawX();
                    Log.v(LOG_TAG, "ACTION UP,DOWN X:" + upX + "," + downX);
                    float deltaX = upX - downX;

                    long downTime1 = event.getEventTime() - event.getDownTime();

                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // swipe right / left animation
                        Log.v(LOG_TAG, "swipe");
                        if (upX > downX) {
                            Log.v(LOG_TAG, "swipe right");
                            Animation anim = AnimationUtils.loadAnimation(
                                    getContext(), android.R.anim.slide_out_right
                            );
                            anim.setDuration(500);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    remove(getItem(position));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            holder.mainView.startAnimation(anim);
                        } else {
                            Log.v(LOG_TAG, "swipe left");
                            Animation anim = AnimationUtils.loadAnimation(
                                    getContext(), R.anim.slide_out_left
                            );
                            anim.setDuration(500);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    remove(getItem(position));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            holder.mainView.startAnimation(anim);
                        }
                    } else {
                        swipe(holder, 0);
                    }

                    if (downTime1 < 1000 && isSingleTap) {
                        handler.removeCallbacks(mLongPressed);
                        // find the position of the row within the ListView
                        int position = listView.getPositionForView(stackOfRows);
                        // find the Player data tied to the adapter
                        Player player = data.get(position);
                        // increase score
                        player.score += 1;
                        // update adapter
                        notifyDataSetChanged();
                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                    }

                    isSingleTap = true;
                    checkForHold = true;
                    checkScrollDirection = true;
                    isScrolling = false;
                    isHorizontalScrolling = false;
                    isVerticalScrolling = false;

                    return true;
                }

                case MotionEvent.ACTION_CANCEL: {
                    isSingleTap = true;
                    checkForHold = true;
                    checkScrollDirection = true;
                    isScrolling = false;
                    isHorizontalScrolling = false;
                    isVerticalScrolling = false;
                    return false;
                }
            }

            return true;
        }
    }

    public class SwipeDetector2 implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 40; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX, downY, upY;
        private PlayerHolder holder;
        private int position;
        boolean isSingleTap;
        Handler handler;
        Runnable mLongPressed;
        SelectedPlayer selectedPlayer;
        private final String LOG_TAG = SwipeDetector.class.getSimpleName();
        private int mTouchSlop;
        private boolean checkScrollDirection;
        private boolean checkForHold;
        private boolean isScrolling;
        private boolean isVerticalScrolling;
        private boolean isHorizontalScrolling;


        public SwipeDetector2(PlayerHolder h, int pos, Handler handler, Runnable runnable, SelectedPlayer selectedPlayer) {
            holder = h;
            position = pos;
            this.handler = handler;
            mLongPressed = runnable;
            this.selectedPlayer = selectedPlayer;
            ViewConfiguration vc = ViewConfiguration.get(context);
            mTouchSlop = vc.getScaledTouchSlop();
            isSingleTap = true;
            checkScrollDirection = true;
            checkForHold = true;
            isScrolling = false;
            isVerticalScrolling = false;
            isHorizontalScrolling = false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View stackOfRows = (View) holder.mainView.getParent();
            ListView listView = (ListView) stackOfRows.getParent();
            String behavior = "No Action";

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN: {
                    Log.v(LOG_TAG, "Action down X:" + downX);
                    downX = event.getRawX();
                    downY = event.getRawY();
                    selectedPlayer.setPosition(position);
                    // start timer. if >1 sec go to Action Up
                    handler.postDelayed(mLongPressed, 1000);
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getRawX();
                    upY = event.getRawY();
                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    Log.v(LOG_TAG, "Action MOVE");

                    if (checkForHold) {
                        Log.v(LOG_TAG, "Check for Hold");
                        if (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop) {
                            isSingleTap = false;
                            checkForHold = false;
                            isScrolling = true;
                            handler.removeCallbacks(runnable);
                            Log.v(LOG_TAG, "scrolling");
                        }
                    }

                    if (isScrolling) {
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
                        listView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        listView.requestDisallowInterceptTouchEvent(true);
                    }

                    swipe(holder, -(int) deltaX, MIN_LOCK_DISTANCE);

                    return true;
                }

                case MotionEvent.ACTION_UP: {
                    upX = event.getRawX();
                    Log.v(LOG_TAG, "ACTION UP,DOWN X:" + upX + "," + downX);
                    float deltaX = upX - downX;

                    long downTime1 = event.getEventTime() - event.getDownTime();

                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        // swipe right / left animation
                        Log.v(LOG_TAG, "swipe");
                        if (upX > downX) {
                            Log.v(LOG_TAG, "swipe right");
                            Animation anim = AnimationUtils.loadAnimation(
                                    getContext(), android.R.anim.slide_out_right);
                            anim.setDuration(500);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    remove(getItem(position));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            holder.mainView.startAnimation(anim);
                        } else {
                            Log.v(LOG_TAG, "swipe left");
                            Animation anim = AnimationUtils.loadAnimation(
                                    getContext(), R.anim.slide_out_left);
                            anim.setDuration(500);
                            anim.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    remove(getItem(position));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            holder.mainView.startAnimation(anim);
                        }
                    } else {
                        swipe(holder, 0);
                    }

                    if (downTime1 < 1000 && isSingleTap) {
                        behavior = "Single Tap";
                        handler.removeCallbacks(mLongPressed);
                        // find the position of the row within the ListView
                        int position = listView.getPositionForView(stackOfRows);
                        // find the Player data tied to the adapter
                        Player player = data.get(position);
                        // increase score
                        player.score -= 1;
                        // update adapter
                        notifyDataSetChanged();
                    } else {
                        swipe(holder, 0);
                    }

                    if (listView != null) {
                        listView.requestDisallowInterceptTouchEvent(false);
                    }

                    isSingleTap = true;
                    checkForHold = true;
                    checkScrollDirection = true;
                    isScrolling = false;
                    isHorizontalScrolling = false;
                    isVerticalScrolling = false;


                    /*if (isVerticalScrolling) {
                        isVerticalScrolling = false;
                        return true;
                    } else {
                        return false;
                    }*/

                    return true;
                }

                case MotionEvent.ACTION_CANCEL: {
                    return false;
                }
            }

            return true;
        }
    }

    private void swipe (PlayerHolder holder, int distance) {
        View animationView = holder.mainView;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
        params.rightMargin = -distance;
        params.leftMargin = distance;
        animationView.setLayoutParams(params);
    }

    private void swipe (PlayerHolder holder, int distance, int minDistance) {
        View animationView = holder.mainView;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();

        if (Math.abs(distance) > Math.abs(minDistance)) {
            params.rightMargin = -distance;
            params.leftMargin = distance;
            animationView.setLayoutParams(params);
        }
    }

}
