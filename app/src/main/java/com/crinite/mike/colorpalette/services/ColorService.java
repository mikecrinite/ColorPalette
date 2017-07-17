package com.crinite.mike.colorpalette.services;

import android.graphics.Color;

/**
 * Created by Mike on 7/11/2017.
 */

public class ColorService {
    private static ColorService INSTANCE = null;

    public static ColorService getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ColorService();
        }
        return INSTANCE;
    }

    public int colorFromHex(String hex){
        return Color.parseColor(hex);
    }
}
