package com.laudev.android.scorekeeper;

/**
 * Created by kevin on 3/17/16.
 */
public class SelectedPlayer {
    private int position;

    public SelectedPlayer (int pos) {
        this.position = pos;
    }

    public void setPosition (int pos) {
        position = pos;
    }

    public int getPosition () {
        return position;
    }

}