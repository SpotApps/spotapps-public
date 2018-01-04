package com.spotapps.beans;

/**
 * Created by tty on 4/6/2015.
 */
public class DefaultMiniApp implements MiniApp{

    private int upVotes = 0;
    private int downVotes = 0;
    private String id;
    private String name;
    private String description;
    private String icon;
    private String operation;
    private String params;

    @Override
    public int getUpVotes() {
        return upVotes;
    }

    public void addUpVotes(int upVotes) {
        this.upVotes += upVotes;
    }

    @Override
    public int getDownVotes() {
        return downVotes;
    }

    public void addDownVotes(int downVotes) {
        this.downVotes += downVotes;
    }

    private Spot spot;


    public DefaultMiniApp(String id, String name, String description, String icon, String operation, String params, Spot spot, Integer upVotes, Integer downVotes){
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.operation = operation;
        this.params = params;
        this.spot = spot;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getOperation() {
        return operation;
    }

    @Override
    public String getParams() {
        return params;
    }

    @Override
    public Spot getSpot() {
        return spot;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());
        return sb.toString();
    }
}
