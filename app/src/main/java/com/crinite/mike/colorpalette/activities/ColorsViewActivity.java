package com.crinite.mike.colorpalette.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crinite.mike.colorpalette.R;
import com.crinite.mike.colorpalette.objects.ColorAssociation;
import com.crinite.mike.colorpalette.services.ColorDictionaryService;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ColorsViewActivity extends AppCompatActivity {
    //Views
    FrameLayout content;

    //Fields

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_colors_view);

        content = (FrameLayout) findViewById(R.id.content);

        content.addView(dynamicList());
    }
    private LinearLayout dynamicList(){
        LinearLayout outer = new LinearLayout(getApplicationContext());
        LinearLayout inner;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        outer.setLayoutParams(params);
        outer.setOrientation(LinearLayout.VERTICAL);

        ArrayList<ColorAssociation> colorList = ColorDictionaryService.getInstance().getColorList();
        String name;
        int color;
        TextView txtName;
        ImageView imgCol;

        for(ColorAssociation ca : colorList){
            inner = generateNonDefaultLinearLayout();
            name = ca.getName();
            color = ca.getColor();

            txtName = new TextView(getApplicationContext());
            imgCol = new ImageView(getApplicationContext());

            txtName.setText(name);
            imgCol.setBackgroundColor(color);

            inner.addView(txtName);
            inner.addView(imgCol);

            outer.addView(inner);
        }

        return outer;
    }

    private LinearLayout generateNonDefaultLinearLayout(){
        LinearLayout result = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        result.setLayoutParams(params);
        result.setOrientation(LinearLayout.HORIZONTAL);

        return result;
    }
}
