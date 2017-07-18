package com.crinite.mike.colorpalette.services;

import com.crinite.mike.colorpalette.activities.MainActivity;
import com.crinite.mike.colorpalette.objects.ColorAssociation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * The ColorDictionaryService contains a LinkedHashMap color names and their integer color value, as
 * designated by android's Color.ParseColor method. The service can be used to determine the closest
 * color to one provided for comparison.
 */
public class ColorDictionaryService {
    private static ColorDictionaryService INSTANCE = null;
    private ArrayList<ColorAssociation> colorList = new ArrayList<>();

    public ColorDictionaryService(){
        try {
            //Set up reader to read from wikipedia color file
            InputStreamReader is = new InputStreamReader(MainActivity.assMan.open("wikipedia_07_17_2017.txt"));
            BufferedReader br = new BufferedReader(is);

            // Fill up colorMap
            String line;
            String[] vals;
            String name;
            String hex;
            int value;
            while((line = br.readLine()) != null){
                vals = line.split(" : ");
                name = vals[0];
                hex = vals[1];
                value = ColorService.getInstance().colorFromHex(hex);
                colorList.add(new ColorAssociation(name, hex, value));
            }
            sortColorMap();

        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            System.out.println("The file is not at the provided path");
        }catch(IOException ioe){
            ioe.printStackTrace();
            System.out.println("There was an IO Exception, idk");
        }
    }

    public static ColorDictionaryService getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ColorDictionaryService();
        }
        return INSTANCE;
    }

    /**
     * Returns the name of the closest color to the one that is passed into the function
     * @param color Target color
     * @return The closest named color
     */
    public String closestColor(int color) {
        String closest = "none";
        int value, distance;
        int difference = 999999999; //difference between current color and provided color
        for(ColorAssociation ca : colorList){
            value = ca.getColor();
            distance = Math.abs(value - color);
            if(closest.equals("none")){
                difference = distance;
                closest = ca.getName();
            }else if(distance < difference){
                difference = distance;
                closest = ca.getName();
            }
            if(difference == 0){
                return closest;
            }
        }
        return closest;
    }

    /**
     * Returns the name of the closest color to the one that is passed into the function
     * @param color Target color
     * @return The closest named color
     */
    public String closestColor(String color){
        return closestColor(ColorService.getInstance().colorFromHex(color));
    }

    private void sortColorMap() {
        Collections.sort(colorList);
    }

    public ArrayList<ColorAssociation> getColorList() {
        return colorList;
    }

    public void setColorList(ArrayList<ColorAssociation> colorList) {
        this.colorList = colorList;
    }

}
