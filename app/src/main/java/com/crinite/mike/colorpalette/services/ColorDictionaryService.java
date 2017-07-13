package com.crinite.mike.colorpalette.services;

import com.crinite.mike.colorpalette.activities.MakePhotoPaletteActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * The ColorDictionaryService contains a LinkedHashMap color names and their integer color value, as
 * designated by android's Color.ParseColor method. The service can be used to determine the closest
 * color to one provided for comparison.
 */
public class ColorDictionaryService {
    private static ColorDictionaryService INSTANCE = null;
    private LinkedHashMap<String, Integer> colorMap = new LinkedHashMap<>();

    public ColorDictionaryService(){
        try {
            //Set up reader to read from wikipedia color file
            InputStreamReader is = new InputStreamReader(MakePhotoPaletteActivity.assMan.open("wikipedia_07_11_2017.txt"));
            BufferedReader br = new BufferedReader(is);

            // Fill up colorMap
            String line;
            String[] vals;
            String name;
            int value;
            while((line = br.readLine()) != null){
                vals = line.split(" : ");
                name = vals[0];
                value = ColorService.getInstance().colorFromHex(vals[1]);
                this.colorMap.put(name, value);
            }
            sortColorMap(); //TODO: Functionality

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
        int diff = -5;
        Set<String> keys = colorMap.keySet();
        for(String key : keys){
            value = colorMap.get(key);
            distance = Math.abs(color - value);
            if(diff != -5){
                if(distance < diff){
                    closest = key;
                    diff = distance;
                }
            }else{
                closest = key;
                diff = Math.abs(color - value);
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
        //TODO: Implement sorting the colorMap
    }
}
