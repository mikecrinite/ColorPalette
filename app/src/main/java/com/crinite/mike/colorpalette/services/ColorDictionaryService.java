package com.crinite.mike.colorpalette.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

/**
 * The ColorDictionaryService contains a LinkedHashMap color names and their integer color value, as
 * designated by android's Color.ParseColor method. The service can be used to determine the closest
 * color to one provided for comparison.
 */
public class ColorDictionaryService {
    private static ColorDictionaryService INSTANCE = null;
    private LinkedHashMap<String, Integer> colorMap = new LinkedHashMap<>();

    {
        try {
            // Set up BufferedReader object
            File f = new File("app\\src\\main\\res\\other\\files\\wikipedia_07_11_2017.txt");
            System.out.println(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(new FileReader(f));

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
}
