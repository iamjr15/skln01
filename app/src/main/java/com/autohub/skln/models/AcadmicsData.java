package com.autohub.skln.models;

/**
 * Created by Vt Netzwelt
 */
public class AcadmicsData {
    private int color;
    private String classname;
    private int icon;

    public AcadmicsData(int color, String classname, int icon) {
        this.color = color;
        this.classname = classname;
        this.icon = icon;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getClassname() {
        return classname;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}