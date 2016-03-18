package com.laudev.android.scorekeeper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtName = (TextView)row.findViewById(R.id.txtName);
            holder.txtScore = (TextView)row.findViewById(R.id.txtScore);
            holder.stackofRows = (RelativeLayout) holder.mainView.getParent();

            row.setTag(holder);
        }
        else
        {
            holder = (PlayerHolder)row.getTag();
        }

        Player player = data.get(position);
        holder.txtName.setText(player.name);
        holder.imgIcon.setImageResource(player.icon);
        holder.txtScore.setText("" + player.score);

        // Recenter main view after non-swipe movements
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mainView.getLayoutParams();
        params.rightMargin = 0;
        params.leftMargin = 0;
        holder.mainView.setLayoutParams(params);

        SwipeDetector swipeDetector = new SwipeDetector(holder, position, handler, runnable, selectedPlayer);


        // this works
        holder.txtName.setOnTouchListener(swipeDetector);
        holder.txtScore.setOnTouchListener(swipeDetector);
        holder.imgIcon.setOnTouchListener(swipeDetector);

        return row;
    }

    static class PlayerHolder
    {
        RelativeLayout deleteView;
        LinearLayout mainView;
        ImageView imgIcon;
        TextView txtName;
        TextView txtScore;
        RelativeLayout stackofRows;
    }

    public class SwipeDetector implements View.OnTouchListener {

        private static final int MIN_DISTANCE = 300;
        private static final int MIN_LOCK_DISTANCE = 40; // disallow motion intercept
        private boolean motionInterceptDisallowed = false;
        private float downX, upX;
        private PlayerHolder holder;
        private int position;
        boolean isSingleTap = true;
        Handler handler;
        Runnable mLongPressed;
        SelectedPlayer selectedPlayer;

        public SwipeDetector(PlayerHolder h, int pos, Handler handler, Runnable runnable, SelectedPlayer selectedPlayer) {
            holder = h;
            position = pos;
            this.handler = handler;
            mLongPressed = runnable;
            this.selectedPlayer = selectedPlayer;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View stackOfRows = (View) holder.mainView.getParent();
            ListView listView = (ListView) stackOfRows.getParent();
            String behavior = "No Action";

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getRawX();
                    selectedPlayer.setPosition(position);
                    // start timer. if >1 sec go to Action Up
                    handler.postDelayed(mLongPressed, 1000);
                    return true; // allow other events like Click to be processed
                }

                case MotionEvent.ACTION_MOVE: {
                    upX = event.getRawX();

                    float deltaX = downX - upX;

                    if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listView != null && !motionInterceptDisallowed) {
                        motionInterceptDisallowed = true;
                        listView.requestDisallowInterceptTouchEvent(motionInterceptDisallowed);
                        handler.removeCallbacks(mLongPressed);
                        isSingleTap = false;

                    }
                    swipe(holder, -(int) deltaX, MIN_LOCK_DISTANCE);

                    return true;
                }

                case MotionEvent.ACTION_UP: {
                    upX = event.getRawX();
                    float deltaX = upX - downX;

                    long downTime1 = event.getEventTime() - event.getDownTime();

                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        behavior = "Swipe";
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            remove(getItem(position));
                            notifyDataSetChanged();
                        } else {
                            swipe(holder, 0);
                        }
                    } else if (downTime1 < 1000 && isSingleTap) {
                        behavior = "Single Tap";
                        handler.removeCallbacks(mLongPressed);

                        // find the position of the row within the ListView
                        int position = listView.getPositionForView(stackOfRows);

                        // find the Player data tied to the adapter
                        Player player = data.get(position);

                        // increase score
                        player.score += 1;

                        // update adapter
                        notifyDataSetChanged();
                    } else {
                        swipe(holder, 0);
                    }

                    if (listView != null) {
                        motionInterceptDisallowed = false;
                        listView.requestDisallowInterceptTouchEvent(motionInterceptDisallowed);
                    }

                    Toast.makeText(context, "Downtime: " + downTime1 + " Behavior: " + behavior, Toast.LENGTH_SHORT).show();

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
