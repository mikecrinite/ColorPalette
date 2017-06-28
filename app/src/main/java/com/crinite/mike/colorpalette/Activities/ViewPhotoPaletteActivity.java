package com.crinite.mike.colorpalette.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crinite.mike.colorpalette.Objects.Palette;
import com.crinite.mike.colorpalette.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This activity should be created from the main activity screen when the user chooses
 * to take a picture with the app, rather than upload one from memory
 */
public class ViewPhotoPaletteActivity extends AppCompatActivity implements View.OnClickListener {
    //Instance variables
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Palette palette;

    //Widget references
    private ImageView mImageView;
    private TextView colorView;
    private View colorView0;
    private View colorView1;
    private View colorView2;
    private View colorView3;
    private View colorView4;
    private View colorView5;
    private View colorView6;
    private View colorView7;
    private TextView title;
    private FloatingActionButton fabPhoto;

    // store values in order to change orientation
    private SharedPreferences savedValues;

    /**
     * To be called when the activity is created
     * @param savedInstanceState SharedPreferences object for this activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        if(MainActivity.mode == 1) {
            mCurrentPhotoPath = getIntent().getStringExtra("mCurrentPhotoPath");
        }

        palette = new Palette();

        // decode preferences
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        //Get widget references
        mImageView = (ImageView) findViewById(R.id.mImageView);
        colorView = (TextView) findViewById(R.id.colorView);
        colorView0 = findViewById(R.id.colorView0); //Casting not necessary
        colorView1 = findViewById(R.id.colorView1); //findViewById returns a View object
        colorView2 = findViewById(R.id.colorView2);
        colorView3 = findViewById(R.id.colorView3);
        colorView4 = findViewById(R.id.colorView4);
        colorView5 = findViewById(R.id.colorView5);
        colorView6 = findViewById(R.id.colorView6);
        colorView7 = findViewById(R.id.colorView7);
        title = (TextView) findViewById(R.id.title);
        fabPhoto = (FloatingActionButton) findViewById(R.id.fabPhoto);

        //Set up listeners
        fabPhoto.setOnClickListener(this);
        fabPhoto.bringToFront();
        colorView0.setOnClickListener(this);
        colorView1.setOnClickListener(this);
        colorView2.setOnClickListener(this);
        colorView3.setOnClickListener(this);
        colorView4.setOnClickListener(this);
        colorView5.setOnClickListener(this);
        colorView6.setOnClickListener(this);
        colorView7.setOnClickListener(this);

        //Set colorView's size equal to its width
        colorView.setHeight(colorView.getWidth());

        showAlert();
    }

    /**
     * To be executed as the activity is paused for any reason.
     */
    @Override
    public void onPause(){
        /*
        // save the current state
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("mCurrentPhotoPath", mCurrentPhotoPath);
        editor.putString("color", color);
        editor.putString("shade0", shade0);
        editor.putString("shade1", shade1);
        editor.putString("shade2", shade2);
        editor.putString("shade3", shade3);
        editor.putString("pal0", pal0);
        editor.putString("pal1", pal1);
        editor.putString("pal2", pal2);
        editor.putString("pal3", pal3);
        editor.apply();
        */

        super.onPause();
    }

    /**
     * To be executed as the activity is resumed from pause.
     */
    @Override
    public void onResume(){
        super.onResume();

        /*
        // restore the instance variables
        mCurrentPhotoPath = savedValues.getString("mCurrentPhotoPath", "empty");
        color = savedValues.getString("color", "empty");
        shade0 = savedValues.getString("shade0", "empty");
        shade1 = savedValues.getString("shade1", "empty");
        shade2 = savedValues.getString("shade2", "empty");
        shade3 = savedValues.getString("shade3", "empty");
        pal0 = savedValues.getString("pal0", "empty");
        pal1 = savedValues.getString("pal1", "empty");
        pal2 = savedValues.getString("pal2", "empty");
        pal3 = savedValues.getString("pal3", "empty");

        setPic();
        setAllColors();
        */
    }


    /**
     * Declares intent to have the camera app take a picture
     */
    private void dispatchTakePictureIntent() {
        //TODO: Overlay the target area over the camera if possible?
        //TODO: Only use a small portion of the camera, to provide more accurate results
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                System.out.println("Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.crinite.mike.colorpalette",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Completes tasked that are waiting for a particular activity, i.e. a camera action
     * @param requestCode Automatically filled
     * @param resultCode Automatically filled
     * @param data Automatically filled
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Set the imageView pic and grab the color
            setPic();
            setAllColors();
            //grab();
        }
    }

    /**
     * Creates the file to store the captured image
     *
     * @return File containing the captured image
     * @throws IOException If the file cannot be created. Thrown by internal call
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Sets a target view's background color to the specified color
     *
     * @param color The color, in hex format, to set
     * @param view The view whose background should be set
     */
    private void setColor(String color, View view){
        if(!color.equals("empty")){
            view.setBackgroundColor(Color.parseColor(color));
        }
    }

    /**
     * Sets the color of all 9 fields
     */
    private void setAllColors(){
        palette.populate(mCurrentPhotoPath);

        setColor(palette.getColor(), colorView);
        setColor(palette.getShade0(), colorView0);
        setColor(palette.getShade1(), colorView1);
        setColor(palette.getShade2(), colorView2);
        setColor(palette.getShade3(), colorView3);
        setColor(palette.getPal0(), colorView4);
        setColor(palette.getPal1(), colorView5);
        setColor(palette.getPal2(), colorView6);
        setColor(palette.getPal3(), colorView7);

        title.setText(palette.getColor());
    }

    /**
     * Changes the image in the imageView to the captured image
     */
    private void setPic() {
        if(!mCurrentPhotoPath.equals("empty") && mCurrentPhotoPath != null){
            // Get the dimensions of the View
            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = photoH/targetH; //Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            mImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Invokes the scanner to add the taken picture to the gallery
     * For now, we're not going to use this because we expect most of the
     * pictures to be images that the user doesn't particularly want to view again.
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void showAlert(){
        //TODO: Make a setting to not show this again
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("For Most Accurate Results:");
        alert.setMessage("Make sure the object is evenly-lit. " +
                "\nTry to fill the screen with the object so that the " +
                "background does not affect the resultant color. ");
        alert.setPositiveButton("Got it!",null);
        alert.show();
    }

    /**
     * Click handler
     *
     * @param view Current view
     */
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.fabPhoto:
                dispatchTakePictureIntent();
                System.out.println(mCurrentPhotoPath);
                break;
            case R.id.colorView0:
                Toast.makeText(this, palette.getShade0(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.colorView1:
                Toast.makeText(this, palette.getShade1(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.colorView2:
                Toast.makeText(this, palette.getShade2(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.colorView3:
                Toast.makeText(this, palette.getShade3(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.colorView4:
                Toast.makeText(this, palette.getPal0(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.colorView5:
                Toast.makeText(this, palette.getPal1(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.colorView6:
                Toast.makeText(this, palette.getPal2(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.colorView7:
                Toast.makeText(this, palette.getPal3(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.new_game:
//                return true;
//            case R.id.help:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    /**
     * Inflates the Menu at res\menu\menu.xml
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