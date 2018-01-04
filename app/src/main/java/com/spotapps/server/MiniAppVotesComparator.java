package com.spotapps.server;

import com.spotapps.beans.MiniApp;

import java.util.Comparator;

/**
 * Created by tty on 02/01/2017.
 */
public class MiniAppVotesComparator implements Comparator<MiniApp> {
    @Override
    public int compare(MiniApp miniApp1, MiniApp miniApp2) {
        int miniApp1Rating = miniApp1.getUpVotes() + miniApp1.getDownVotes();
        int miniApp2Rating = miniApp2.getUpVotes() + miniApp2.getDownVotes();
        return miniApp2Rating - miniApp1Rating;
    }
}
