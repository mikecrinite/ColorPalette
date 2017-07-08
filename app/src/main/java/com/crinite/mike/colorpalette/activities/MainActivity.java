package com.crinite.mike.colorpalette.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.crinite.mike.colorpalette.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Widgets
    private Button btnMakePalette;
    private Button btnViewPalette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get widget References
        btnMakePalette = (Button) findViewById(R.id.btnMakePalette);
        btnViewPalette = (Button) findViewById(R.id.btnViewPalette);

        //Set up listeners
        btnMakePalette.setOnClickListener(this);
        btnViewPalette.setOnClickListener(this);
    }

    /**
     * Click handler
     *
     * @param view Current view
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btnMakePalette:
                Intent intent = new Intent(this, MakePhotoPaletteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnViewPalette:
                Toast.makeText(this, "This feature is not yet implemented", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    /**
     * Inflates the Menu at res\menu\menu.xml
     * TODO: This doesn't work right now
     *
     * @param menu Autofilled
     * @return True
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
}
