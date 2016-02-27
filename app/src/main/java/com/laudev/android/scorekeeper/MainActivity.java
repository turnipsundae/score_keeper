package com.laudev.android.scorekeeper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    static final String PLAYER_ONE_SCORE = "playerOneScore";
    static final String PLAYER_TWO_SCORE = "playerTwoScore";
    static final String PLAYER_THREE_SCORE = "playerThreeScore";
    static final String PLAYER_FOUR_SCORE = "playerFourScore";

    int playerOneScore = 0;
    int playerTwoScore = 0;
    int playerThreeScore = 0;
    int playerFourScore = 0;
    boolean increment = true;
    int amt = 1;

    @Override
    public void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putInt(PLAYER_ONE_SCORE, playerOneScore);
        savedInstanceState.putInt(PLAYER_TWO_SCORE, playerTwoScore);
        savedInstanceState.putInt(PLAYER_THREE_SCORE, playerThreeScore);
        savedInstanceState.putInt(PLAYER_FOUR_SCORE, playerFourScore);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        playerOneScore = savedInstanceState.getInt(PLAYER_ONE_SCORE);
        playerTwoScore = savedInstanceState.getInt(PLAYER_TWO_SCORE);
        playerThreeScore = savedInstanceState.getInt(PLAYER_THREE_SCORE);
        playerFourScore = savedInstanceState.getInt(PLAYER_FOUR_SCORE);
        TextView p1_score = (TextView) findViewById(R.id.player_one_score);
        p1_score.setText("" + playerOneScore);
        TextView p2_score = (TextView) findViewById(R.id.player_two_score);
        p2_score.setText("" + playerTwoScore);
        TextView p3_score = (TextView) findViewById(R.id.player_three_score);
        p3_score.setText("" + playerThreeScore);
        TextView p4_score = (TextView) findViewById(R.id.player_four_score);
        p4_score.setText("" + playerFourScore);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout v = (LinearLayout) findViewById(R.id.player_one);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ChangePlayerName c = new ChangePlayerName();
                c.show(getFragmentManager(), "changePlayerName");
                return false;
            }
        });
    }

    public void ChangePlayerOneName (View view) {
        LinearLayout v = (LinearLayout) findViewById(R.id.player_one);
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ChangePlayerName c = new ChangePlayerName();
                c.show(getFragmentManager(), "changePlayerName");
                return false;
            }
        });
    }

    public static class ChangePlayerName extends DialogFragment {

        private EditText mEditText;

        public ChangePlayerName () {}


        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.edit_name, container);
            mEditText = (EditText) view.findViewById(R.id.edit_name);
            getDialog().setTitle(R.string.change_player_name);

            // Show soft keyboard automatically
            mEditText.requestFocus();
//            mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

//            mEditText.setOnEditorActionListener(getActivity());
            mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // do something updatePlayerName();
                        TextView w = (TextView) getActivity().findViewById(R.id.player_one_name);
                        w.setText(v.getText());
                        getDialog().dismiss();
                    }
                    return handled;
                }
            });

            return view;
        }
    }

    public void changeIncrement(View view) {
        Button incToggle = (Button) findViewById(R.id.inc_toggle);
        if (increment) {
            increment = false;
            incToggle.setText("-");
            incToggle.setBackgroundColor(Color.argb(255, 255, 214, 0));
            incToggle.setTextColor(Color.argb(255, 0, 0, 0));
        } else {
            increment = true;
            incToggle.setText("+");
            incToggle.setBackgroundColor(Color.argb(255, 0, 150, 136));
            incToggle.setTextColor(Color.argb(255, 255, 255, 255));
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
        chngAmt.setText("" + amt);
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
