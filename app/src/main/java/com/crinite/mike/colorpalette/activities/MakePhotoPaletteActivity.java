package com.crinite.mike.colorpalette.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crinite.mike.colorpalette.R;
import com.crinite.mike.colorpalette.objects.Palette;
import com.crinite.mike.colorpalette.services.ColorDictionaryService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This activity should be created from the main activity screen when the user chooses
 * to take a picture with the app, rather than upload one from memory
 */
public class MakePhotoPaletteActivity extends AppCompatActivity implements View.OnClickListener {
    //Instance variables
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private String mCurrentPhotoPath;
    private Palette palette;

    //Services
    private ColorDictionaryService cds;

    //Other
    public static AssetManager assMan;

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

        //Set up asset manager
        assMan = getAssets();
        cds = ColorDictionaryService.getInstance();

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
     * Desclares intent to have the user select the image from a gallery
     */
    private void dispatchChoosePictureIntent(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if(intent.resolveActivity(getPackageManager()) != null){
            //Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch(IOException ex){
                //Error occured while creating the File
                System.out.println("Error occurred while creating the File");
            }
            // Continue only if the File was successfully created
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.crinite.mike.colorpalette",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
            }
        }
    }

    /**
     * Completes tasks that are waiting for a particular activity, i.e. a camera action
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
        }else if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(uri);
                Bitmap image = BitmapFactory.decodeStream(imageStream);

                /*
                 * This is sort of workaround-y
                 * Basically, since you can't easily get a filepath from the image
                 * gallery (why not? who knows...), we first create a file at the filepath
                 * just like we did when we took a picture, and then we just send the image to
                 * the file at that path
                 */
                FileOutputStream outputStream = new FileOutputStream(mCurrentPhotoPath);
                image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                setPic();
                setAllColors();
            }catch(IOException ioe){
                ioe.printStackTrace();
            }
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
        if(mCurrentPhotoPath != null && mCurrentPhotoPath.length() > 0) {
            palette.populate(mCurrentPhotoPath);
        }

        setColor(palette.getColor(), colorView);
        setColor(palette.getShade0(), colorView0);
        setColor(palette.getShade1(), colorView1);
        setColor(palette.getShade2(), colorView2);
        setColor(palette.getShade3(), colorView3);
        setColor(palette.getPal0(), colorView4);
        setColor(palette.getPal1(), colorView5);
        setColor(palette.getPal2(), colorView6);
        setColor(palette.getPal3(), colorView7);

        title.setText(cds.closestColor(palette.getColor()) + "~" + palette.getColor());
    }

    /**
     * Sets mImageView's image when mCurrentPhotoPath is set
     */
    private void setPic(){
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
     * Sets the background of mImageView to the hex color code provided
     *
     * @param hexcode Color to set background, formatted as a hex string
     */
    private void setPic(String hexcode){
        if(isValidHexcode(hexcode)) {
            mCurrentPhotoPath = null;
            mImageView.setImageBitmap(null);
            int color = Color.parseColor(hexcode);
            mImageView.setBackgroundColor(color);
            palette.populate(color);
            setAllColors();
        }else{
            Toast.makeText(this, "Invalid hex color code", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Sets the picture from a given bitmap
     * @param photo Bitmap to set
     */
    private void setPic(Bitmap photo){
        // Get dimensions of photo
        int photoW = photo.getWidth();
        int photoH = photo.getHeight();

        // Get height of imageview for scaling
        int targetH = mImageView.getHeight();
        int scaleFactor = photoH/targetH;

        // Get new dimensions
        photoW = photoW * scaleFactor;
        photoH = photoH * scaleFactor;

        mImageView.setImageBitmap(Bitmap.createScaledBitmap(photo, photoW, photoH, true));
    }

    /**
     * Validate hex with regular expression
     * @param hexcode hex for validation
     * @return True if valid hex, False if invalid hex
     */
    private boolean isValidHexcode(String hexcode) {
        Matcher matcher = Pattern.compile("^#([A-Fa-f0-9]{6})$").matcher(hexcode);
        //TODO: Investigate why inputting an invalid hex code does not cause this method to return false, thus defeating its purpose entirely
        return matcher.matches();
    }

    /**
     * Creates an AlertDialog to prompt the user for a hex color code string
     */
    private void promptForHexcode(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Input hexcode");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setPic(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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

    /**
     * Shows an AlertDialog to inform the user of best image-taking practices for the purpose
     * of this app
     */
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
     * Builds an AlertDialog in order to allow the user to select the method from which their
     * palette will be generated
     */
    private void showOptions(){
        AlertDialog.Builder optionsDialog = new AlertDialog.Builder(this);
        optionsDialog.setTitle("Choose source");
        optionsDialog.setCancelable(true);

        CharSequence options[] = new CharSequence[] {"Take a Photo", "Select From Gallery", "Input Hex Code"};
        optionsDialog.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which){
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        dispatchChoosePictureIntent();
                        break;
                    case 2:
                        promptForHexcode();
                        break;
                }
            }
        });
        optionsDialog.show();
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
                showOptions();
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
