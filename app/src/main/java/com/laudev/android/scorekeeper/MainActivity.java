package com.laudev.android.scorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int playerOneScore = 0;
    int playerTwoScore = 0;
    boolean increment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeIncrement(View view) {
        TextView incrementAmount = (TextView) findViewById(R.id.increment_amount);
        if (increment) {
            increment = false;
            incrementAmount.setText("Tap each player to -1");
        } else {
            increment = true;
            incrementAmount.setText("Tap each player to +1");
        }
    }

    public void incPlayerOne (View view) {
        if (increment) {
            playerOneScore += 1;
        } else {
            playerOneScore -= 1;
        }
        displayPlayerOne(playerOneScore);
    }

    public void incPlayerTwo (View view) {
        if (increment) {
            playerTwoScore += 1;
        } else {
            playerTwoScore -= 1;
        }
        displayPlayerTwo(playerTwoScore);
    }

    private void displayPlayerOne (int score) {
        TextView view = (TextView) findViewById(R.id.player_one_score);
        view.setText("" + score);
    }

    private void displayPlayerTwo (int score) {
        TextView view = (TextView) findViewById(R.id.player_two_score);
        view.setText("" + score);
    }
}
