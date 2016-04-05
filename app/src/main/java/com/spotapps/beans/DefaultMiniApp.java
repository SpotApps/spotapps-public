package com.spotapps.beans;

/**
 * Created by tty on 4/6/2015.
 */
public class DefaultMiniApp implements MiniApp{

    private String icon;
    private String description;
    private String operation;
    private String params;
    private Spot spot;

    public DefaultMiniApp(String icon, String description, String operation, String params, Spot spot){

        this.icon = icon;
        this.description = description;
        this.operation = operation;
        this.params = params;
        this.spot = spot;
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
        sb.append(getDescription());
        return sb.toString();
    }
}
