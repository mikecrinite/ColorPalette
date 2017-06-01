package com.crinite.mike.colorpalette;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static int mode = -1;
    public static final int PICK_IMAGE = 1;

    //Widgets
    private Button btnTakePhoto;
    private Button btnUploadPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get widget References
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnUploadPhoto = (Button) findViewById(R.id.btnUploadPhoto);

        //Set up listeners
        btnTakePhoto.setOnClickListener(this);
        btnUploadPhoto.setOnClickListener(this);
    }

    /**
     * Click handler
     *
     * @param view Current view
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btnTakePhoto:
                mode = 0;
                Intent intent = new Intent(this, TakePhotoActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnUploadPhoto:
                mode = 1;
                //dispatchSelectImageIntent();
                Toast.makeText(this, "This feature is not yet implemented", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void dispatchSelectImageIntent(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, TakePhotoActivity.class);
            data.getData();
            intent.putExtra("mCurrentPhotoPath", "");
            startActivity(intent);
            finish();
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
