package com.laudev.android.scorekeeper;

/**
 * Created by kevin on 3/17/16.
 */
public class Player {
    public int icon;
    public String name;
    public int score;
    public Player(){
        super();
    }

    public Player(int icon, String name, int score) {
        super();
        this.icon = icon;
        this.name = name;
        this.score = score;
    }
}
