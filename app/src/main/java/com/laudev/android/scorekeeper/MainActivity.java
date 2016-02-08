package com.laudev.android.scorekeeper;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int playerOneScore = 0;
    int playerTwoScore = 0;
    int playerThreeScore = 0;
    int playerFourScore = 0;
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
            incToggle.setBackgroundColor(Color.argb(255, 229, 57, 53));
        } else {
            increment = true;
            incrementAmount.setText("Tap each player to +" + amt);
            incToggle.setText("Increase");
            incToggle.setBackgroundColor(Color.argb(255, 67, 160, 71));
        }
    }

    public void changeAmt(View view) {
        Button chngAmt = (Button) findViewById(R.id.change_amt);
        if (amt == 1) {
            amt = 5;
        } else if (amt == 5) {
            amt = 10;
        } else if (amt == 10) {
            amt = 1;
        }
        chngAmt.setText("By " + amt);

        TextView incrementAmount = (TextView) findViewById(R.id.increment_amount);
        if (increment) {
            incrementAmount.setText("Tap each player to +" + amt);
        } else {
            incrementAmount.setText("Tap each player to -" + amt);
        }
    }

    public void incPlayerOne (View view) {
        if (increment) {
            playerOneScore += amt;
        } else {
            playerOneScore -= amt;
        }
        displayPlayerOne(playerOneScore);
    }

    public void incPlayerTwo (View view) {
        if (increment) {
            playerTwoScore += amt;
        } else {
            playerTwoScore -= amt;
        }
        displayPlayerTwo(playerTwoScore);
    }

    public void incPlayerThree (View view) {
        if (increment) {
            playerThreeScore += amt;
        } else {
            playerThreeScore -= amt;
        }
        displayPlayerThree(playerThreeScore);
    }

    public void incPlayerFour (View view) {

        if (increment) {
            playerFourScore += amt;
        } else {
            playerFourScore -= amt;
        }
        displayPlayerFour(playerFourScore);
    }

    public void resetAll (View view) {
        playerOneScore = 0;
        playerTwoScore = 0;
        playerThreeScore = 0;
        playerFourScore = 0;
        TextView p1 = (TextView) findViewById(R.id.player_one_score);
        p1.setText("" + playerOneScore);
        TextView p2 = (TextView) findViewById(R.id.player_two_score);
        p2.setText("" + playerTwoScore);
        TextView p3 = (TextView) findViewById(R.id.player_three_score);
        p3.setText("" + playerThreeScore);
        TextView p4 = (TextView) findViewById(R.id.player_four_score);
        p4.setText("" + playerFourScore);
    }

    private void displayPlayerOne (int score) {
        TextView view = (TextView) findViewById(R.id.player_one_score);
        view.setText("" + score);
    }

    private void displayPlayerTwo (int score) {
        TextView view = (TextView) findViewById(R.id.player_two_score);
        view.setText("" + score);
    }

    private void displayPlayerThree (int score) {
        TextView view = (TextView) findViewById(R.id.player_three_score);
        view.setText("" + score);
    }

    private void displayPlayerFour (int score) {
        TextView view = (TextView) findViewById(R.id.player_four_score);
        view.setText("" + score);
    }
}
