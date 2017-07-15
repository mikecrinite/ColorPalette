package com.crinite.mike.colorpalette.objects;

import android.support.annotation.NonNull;

/**
 * Created by Mike on 7/14/2017.
 */

public class ColorAssociation implements Comparable{
    private String name;
    private String hex;
    private Integer color;

    public ColorAssociation(String name, String hex){
        this.name = name;
        this.hex = hex;
    }

    public ColorAssociation(String name, int color){
        this.name = name;
        this.color = color;
    }

    public ColorAssociation(String name, String hex, int color){
        this.name = name;
        this.hex = hex;
        this.color = color;
    }

    public String getName(){
        return name;
    }

    public String getHex(){
        return hex;
    }

    public int getColor(){
        return color;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHex(String hex){
        this.hex = hex;
    }

    public void setColor(int color){
        this.color = color;
    }

    @Override
    public String toString(){
        String str = "";
        if(name != null) {
            str += "Name: " + name + "\n";
        }

        if(hex != null) {
            str += "Hex: " + hex + "\n";
        }

        if(color != null) {
            str += "Color Value: " + color + "\n";
        }
        return str;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(o instanceof ColorAssociation) {
            ColorAssociation ca = (ColorAssociation) o;
            return Integer.compare(color, ca.getColor());
        }else{
            return 0;
        }
    }
}
