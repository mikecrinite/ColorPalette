package com.crinite.mike.colorpalette;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.IOException;
import java.util.Random;

/**
 * Takes the average of each pixel in a scaled-down bitmap of the specified image
 * and provides a hexidecimal-formatted String representing the color for use elsewhere.
 *
 * @author Michael Crinite
 * @version 0.1
 */

public class ColorGrabber {
    private static ColorGrabber INSTANCE = null;

    /**
     * Ensures only a single instance ever exists
     *
     * @return The single instance of ColorGrabber
     */
    public static ColorGrabber getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ColorGrabber();
        }
        return INSTANCE;
    }

    /**
     * Creates an int array with the color at every pixel represented as an
     * integer
     *
     * @param filepath The path of the file to parse
     * @return An int array of pixel color values
     * @throws IOException If the filepath is not valid
     */
    private int[][] loadPixelsFromImage(String filepath) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                bitmap.getWidth()/10,
                bitmap.getHeight()/10,
                false);

        System.out.println(bitmap.getWidth() + " x " + bitmap.getHeight());

        try {
            int[][] colors = new int[bitmap.getWidth()][bitmap.getHeight()];

            for (int x = 0; x < bitmap.getWidth(); x++) {
                for (int y = 0; y < bitmap.getHeight(); y++) {
                    int pixel = bitmap.getPixel(x, y);
                    colors[x][y] = pixel;
                }
            }

            return colors;
        }catch(NullPointerException ne){
            System.out.println("NullPointerException again!");
            return new int[0][0];
        }
    }

    /**
     * Uses the return value from <code>loadPixelsFromImage</code> to determine the average
     * color and return it as an int
     * @param colors The array of colors representing the image
     * @return The average color as an integer
     */
    private int getAverageColor(int[][] colors){
        int length = colors.length;
        int depth = colors[0].length;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int pixelCount = 0;

        for(int x = 0; x <  length; x++){
            for(int y = 0; y < depth; y++){
                int i = colors[x][y];
                pixelCount++;
                redBucket += Color.red(i);
                greenBucket += Color.green(i);
                blueBucket += Color.blue(i);
            }
        }

        int avgred = redBucket / pixelCount;
        int avggrn = greenBucket / pixelCount;
        int avgblu = blueBucket / pixelCount;

        return Color.rgb(avgred,avggrn,avgblu);

    }

    /**
     * Utilizes the methods in this Class in order to return the properly-formatted
     * color of the image
     *
     * @param filepath The filepath where the target file resides
     * @return The color, formatted in hex
     */
    public String[] get(String filepath){
        try {
            int[][] colors = ColorGrabber.getInstance()
                    .loadPixelsFromImage(filepath);
            int decimalColor = ColorGrabber.getInstance().getAverageColor(colors);
            String[] palette = palette(decimalColor);
            palette[0] = "#" + Integer.toHexString(decimalColor).substring(2);
            return palette;
        }catch(IOException e){
            return new String[]{"#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF","#FFFFFF"};
        }

    }

    /**
     * Retrieves a color that complements the original color
     * @return The complementary color generated
     */
    public String[] palette(int color){
        String[] arr = new String[9];

        int color1, color2, color3, color4, color5, color6, color7, color8;
        color1 = mix(color); //color / 3;
        color2 = mix(color); //color / 2;
        color3 = mix(color); //color * 2;
        color4 = mix(color); //color * 3;
        color5 = shade(color,0);
        color6 = shade(color,0);
        color7 = shade(color,0);
        color8 = shade(color,0);

        arr[1] = "#" + Integer.toHexString(color1).substring(2);// = "#ffffff";
        arr[2] = "#" + Integer.toHexString(color2).substring(2);// = "#ffffff";
        arr[3] = "#" + Integer.toHexString(color3).substring(2);// = "#ffffff";
        arr[4] = "#" + Integer.toHexString(color4).substring(2);// = "#ffffff";
        arr[5] = "#" + Integer.toHexString(color5).substring(2);// = "#ffffff";
        arr[6] = "#" + Integer.toHexString(color6).substring(2);// = "#ffffff";
        arr[7] = "#" + Integer.toHexString(color7).substring(2);// = "#ffffff";
        arr[8] = "#" + Integer.toHexString(color8).substring(2);// = "#ffffff";
        return arr;
    }

    /**
     * Averages the color with that of a randomly-generated color to create a nice similar
     * color.
     *
     * @param color Primary color to mix
     * @return A color that looks aesthetically pleasing next to the primary color
     */
    private int mix(int color){
        //TODO: This works for aesthetics, but could benefit from more color theory
        Random random = new Random();
        int red = (random.nextInt(256) + Color.red(color)) / 2;
        int green = (random.nextInt(256) + Color.green(color)) / 2;
        int blue = (random.nextInt(256) + Color.blue(color)) / 2;

        return Color.rgb(red, green, blue);
    }

    /**
     *
     * @param color The color that will be used to generate a shade
     * @return The created shade
     */
    private int shade(int color, int offset){
        return color;
    }
}
