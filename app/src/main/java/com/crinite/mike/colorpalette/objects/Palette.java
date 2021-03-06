package com.crinite.mike.colorpalette.objects;

import com.crinite.mike.colorpalette.services.ColorPaletteService;

/**
 * Palette object stores the main color as well as the shades and palette that are generated by
 * the ColorPaletteService each time a palette is requested
 *
 * @author Michael Crinite
 * @version 06/28/2017
 */
public class Palette {
    //Static ColorPaletteService instance
    private ColorPaletteService cgs = ColorPaletteService.getInstance();
    //Main color of image
    private String color = "#ffffff";
    //Generated shades of color
    private String shade0 = "#ffffff";
    private String shade1 = "#ffffff";
    private String shade2 = "#ffffff";
    private String shade3 = "#ffffff";
    //Generated palette colors
    private String pal0 = "#ffffff";
    private String pal1 = "#ffffff";
    private String pal2 = "#ffffff";
    private String pal3 = "#ffffff";

    public void populate(int colorCode){
        String[] arr = cgs.decode(colorCode);
        color = arr[0];
        pal0 = arr[1];
        pal1 = arr[2];
        pal2 = arr[3];
        pal3 = arr[4];
        shade0 = arr[5];
        shade1 = arr[6];
        shade2 = arr[7];
        shade3 = arr[8];
    }

    /**
     * Uses the ColorPaletteService to decode the color of the picture
     */
    public void populate(String photoPath){
        String[] arr = cgs.decode(photoPath);
        color = arr[0];
        pal0 = arr[1];
        pal1 = arr[2];
        pal2 = arr[3];
        pal3 = arr[4];
        shade0 = arr[5];
        shade1 = arr[6];
        shade2 = arr[7];
        shade3 = arr[8];
    }

    //Accessors and mutators TODO: Use the mutators in populate()
    public ColorPaletteService getCGS() {
        return cgs;
    }

    public void setColor(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setShade0(String color){
        shade0 = color;
    }
    public String getShade0() {
        return shade0;
    }

    public void setShade1(String color){
        shade1 = color;
    }

    public String getShade1() {
        return shade1;
    }

    public void setShade2(String color){
        shade2 = color;
    }

    public String getShade2() {
        return shade2;
    }

    public void setShade3(String color){
        shade3 = color;
    }

    public String getShade3() {
        return shade3;
    }

    public void setPal0(String color){
        pal0 = color;
    }

    public String getPal0() {
        return pal0;
    }

    public void setPal1(String color){
        pal1 = color;
    }

    public String getPal1() {
        return pal1;
    }

    public void setPal2(String color){
        pal2 = color;
    }

    public String getPal2() {
        return pal2;
    }

    public void setPal3(String color){
        pal3 = color;
    }

    public String getPal3() {
        return pal3;
    }

    public String saveString(){
        String result = "";
        result += color + "&";
        result += shade0 + "&";
        result += shade1 + "&";
        result += shade2 + "&";
        result += shade3 + "&";
        result += pal0 + "&";
        result += pal1 + "&";
        result += pal2 + "&";
        result += pal3;
        return result;
    }

    @Override
    public String toString(){
        String result = "";
        result += "color:" + color + "\n";
        result += "shade0:" + shade0 + "\n";
        result += "shade1:" + shade1 + "\n";
        result += "shade2:" + shade2 + "\n";
        result += "shade3:" + shade3 + "\n";
        result += "pal0:" + pal0 + "\n";
        result += "pal1:" + pal1 + "\n";
        result += "pal2:" + pal2 + "\n";
        result += "pal3:" + pal3;
        return result;
    }

}
