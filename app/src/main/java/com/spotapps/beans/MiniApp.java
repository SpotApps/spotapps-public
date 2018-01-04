package com.spotapps.beans;

import java.io.Serializable;

/**
 * Created by tty on 4/5/2015.
 */
public interface MiniApp extends Serializable{

    String getId();

    String getName();

    String getIcon();

    String getDescription();

    String getOperation();

    String getParams();

    Spot getSpot();

    int getUpVotes();
    int getDownVotes();

    void addDownVotes(int votes);

    void addUpVotes(int votes);


}
