package com.laudev.android.scorekeeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int playerOneScore = 0;
    int playerTwoScore = 0;
    boolean increment = true;
    int amt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeIncrement(View view) {
        TextView incrementAmount = (TextView) findViewById(R.id.increment_amount);
        Button incToggle = (Button) findViewById(R.id.inc_toggle);
        if (increment) {
            increment = false;
            incrementAmount.setText("Tap each player to -" + amt);
            incToggle.setText("Decrease");

        } else {
            increment = true;
            incrementAmount.setText("Tap each player to +" + amt);
            incToggle.setText("Increase");
            
        }
    }

    public void changeAmt(View view) {
        if (amt == 1) {
            amt = 5;
        } else if (amt == 5) {
            amt = 10;
        } else if (amt == 10) {
            amt = 1;
        }
        TextView incrementAmount = (TextView) findViewById(R.id.increment_amount);
        if (increment) {
            incrementAmount.setText("Tap each player to +" + amt);
        } else {
            incrementAmount.setText("Tap each player to -" + amt);
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
