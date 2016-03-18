package com.laudev.android.scorekeeper;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.laudev.android.scorekeeper.ChangeNameDialog;
import com.laudev.android.scorekeeper.ListViewSwipeDetector;
import com.laudev.android.scorekeeper.Player;
import com.laudev.android.scorekeeper.PlayerAdapter;
import com.laudev.android.scorekeeper.R;
import com.laudev.android.scorekeeper.SelectedPlayer;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ChangeNameDialog.ChangeNameListener{

    public PlayerAdapter adapter;
    private ListView listViewPlayers;
    public ArrayList<Player> player_data;
    private ListViewSwipeDetector listViewSwipeDetector;
    public SelectedPlayer selectedPlayer;
    private Handler handler = new Handler();
    private Runnable mLongPressed = new Runnable() {
        public void run() {
            showChangeNameDialog();
        }
    };

    // Called when activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectedPlayer = new SelectedPlayer(0);

        // initialize List with 4 players
        player_data = new ArrayList<Player>();
        for (int i = 1; i < 5; i++) {
            player_data.add(new Player(R.drawable.red, "Player " + i, 0));
        }

        adapter = new PlayerAdapter(this,
                R.layout.simplerow, (ArrayList<Player>) player_data, handler, mLongPressed, selectedPlayer);

        listViewPlayers = (ListView)findViewById(R.id.playerList);

        // Inflate footerView, set On Click, add to ListView
        View footerView =  getLayoutInflater().inflate(R.layout.listview_footer_row, null);
        footerView.setOnClickListener(onClickAddPlayer);
        listViewPlayers.addFooterView(footerView);

        listViewSwipeDetector = new ListViewSwipeDetector(this, handler, mLongPressed);
        listViewPlayers.setOnTouchListener(listViewSwipeDetector);
        listViewPlayers.setAdapter(adapter);

    }

    public void showChangeNameDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ChangeNameDialog();
        dialog.show(getFragmentManager(), "ChangeNameDialog");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(ChangeNameDialog dialog) {
        // User touched the dialog's positive button
        String name = dialog.getTextInput();
        Player player = player_data.get(selectedPlayer.getPosition());
        player.name = name;
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    private View.OnClickListener onClickAddPlayer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // find next player number
            int nextPlayerNum = player_data.size() + 1;

            // add player to List
            player_data.add(new Player(R.drawable.red, "Player " + nextPlayerNum, 0));

            // update adapter
            adapter.notifyDataSetChanged();

            // scroll to bottom
            listViewPlayers.smoothScrollToPosition(listViewPlayers.getCount() - 1);
        }
    };

}
