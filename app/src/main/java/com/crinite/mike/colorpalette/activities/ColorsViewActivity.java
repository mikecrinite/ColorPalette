package com.crinite.mike.colorpalette.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crinite.mike.colorpalette.R;
import com.crinite.mike.colorpalette.objects.ColorAssociation;
import com.crinite.mike.colorpalette.services.ColorDictionaryService;
import com.crinite.mike.colorpalette.services.ColorService;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ColorsViewActivity extends AppCompatActivity implements View.OnClickListener {
    //Views
    FrameLayout content;

    //Fields
    boolean background = true;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

         setContentView(R.layout.activity_colors_view);

         content = (FrameLayout) findViewById(R.id.content);
         content.setOnClickListener(this);

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
        //View viewCol;

        for(ColorAssociation ca : colorList){
            inner = generateNonDefaultLinearLayout();
            name = ca.getName();
            color = ca.getColor();

            txtName = new TextView(getApplicationContext());
            //viewCol = new ImageView(getApplicationContext());

            txtName.setText(name);
            txtName.setTextColor(color);
            txtName.setTextSize(30);
            txtName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            //viewCol.setBackgroundColor(color);

            inner.addView(txtName);
            //inner.addView(viewCol);

            outer.addView(inner);
        }

        return outer;
    }

    private LinearLayout generateNonDefaultLinearLayout(){
        LinearLayout result = new LinearLayout(getApplicationContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        result.setLayoutParams(params);
        result.setOrientation(LinearLayout.HORIZONTAL);

        return result;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.content:
                swapBackgroundColor();
                break;
        }
    }

    private void swapBackgroundColor() {
        if(background) {
            content.setBackgroundColor(ColorService.getInstance().colorFromHex("#FFFFFF"));
        }else{
            content.setBackgroundColor(ColorService.getInstance().colorFromHex("#000000"));
        }
        background = !background;
    }
}
