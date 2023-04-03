package com.example.warrantytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDevice extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    Uri newImage;
    String newImageString;
    boolean imageEdited = false;
    //////////////////////////////////////////////
    // On create, loads add_device.xml layout
    // creates text inputs, creates buttons
    /////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);

        /////////////////////////////////////////////////////
        // date picker button
        ////////////////////////////////////////////////////
        intDatePicker();
        dateButton = findViewById(R.id.dateOfPurchaseInput);
        dateButton.setText(getTodaysDate());

        ///////////////////////////////////////
        //Pulls fields from add_device.xml
        //
        //////////////////////////////////////
        final EditText deviceNameInput = findViewById(R.id.nameInput);
        final EditText deviceManufacturerInput = findViewById(R.id.manufacturerInput);
        final EditText deviceSerialInput = findViewById(R.id.serialInput);
        final Button deviceDateOfPurchaseInput = findViewById(R.id.dateOfPurchaseInput);
        final Button linkButton = findViewById(R.id.linkButton);
        final TextView timeRemaining = findViewById(R.id.timeRemaining);
        linkButton.setVisibility(View.GONE);

        Button saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNewDevice(deviceNameInput.getText().toString(), deviceManufacturerInput.getText().toString(), deviceSerialInput.getText().toString(), deviceDateOfPurchaseInput.getText().toString());
            }
        });

        /*Button linkButton = findViewById(R.id.linkButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // The Saleena pop-off section
                //String url = "https://www.lg.com/us/support/repair-service/schedule-repair-continued";
                //Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //startActivity(urlIntent);

            }
        }); */


        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchPhotoPicker();
            }
        });


    }
    ////////////////////////////////////////////////////////////////
    //takes device input pulled above and saves it to the database
    //
    /////////////////////////////////////////////////////////////////
    private void saveNewDevice(String deviceName, String deviceManufacturer, String deviceSerial, String deviceDateOfPurchase){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        Device device = new Device();
        device.deviceName = deviceName;
        device.manufacturer = deviceManufacturer;
        device.deviceSerial = deviceSerial;
        device.deviceDateOfPurchase = deviceDateOfPurchase;
        if (imageEdited) {
            //SAVE URI
            device.deviceImage = extraPhotoURI.toString();
        }
        db.deviceDao().insertDevice(device);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    /////////////////////////////////////
    // Sets the current date automatically
    // to the date text box.
    /////////////////////////////////////

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    /////////////////////////////////////
    // function for the date picker button
    //
    ////////////////////////////////////
    private void intDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //month = month + 1;
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(year , month, day);
                calendar2.add(Calendar.YEAR, 1);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int daysBetween = (int) ChronoUnit.DAYS.between(calendar.toInstant(), calendar2.toInstant());
                    TextView timeRemaining = findViewById(R.id.timeRemaining);
                    timeRemaining.setText(daysBetween + " days remaining");
                }

                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);

    }

    ////////////////////////////////////
    // Format for the date shown
    // on the recycler list
    ///////////////////////////////////
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default
        return "JAN";
    }


    public void openDatePicker(View view){
        datePickerDialog.show();
    }

    ///////////////////////////////////////////////////
    // UNFINISHED PHOTO PICKER
    //////////////////////////////////////////////////
    private File createImageFile() throws IOException {
        // Create an image file name
        String currentPhotoPath;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    Uri extraPhotoURI = null;
    private Uri setupCameraIntent() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);
            extraPhotoURI = photoURI;
            return photoURI;

        } else {
            return null;
        }
    }

    private void launchPhotoPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDevice.this);
        builder.setTitle("Where would you like to get the device image from?");
        // Add the buttons
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked Camera button
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, setupCameraIntent());
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)

            }
        });
        builder.setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Gallery button
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                return;
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageButton imageButton = findViewById(R.id.imageButton);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    if (extraPhotoURI != null) {
                        newImageString = extraPhotoURI.toString();
                        imageEdited = true;
                        imageButton.setImageURI(extraPhotoURI);
                    } else {
                        newImage = imageReturnedIntent.getData();
                        newImageString = newImage.toString();
                        imageEdited = true;
                        imageButton.setImageURI(newImage);
                    }
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    newImage = imageReturnedIntent.getData();
                    newImageString = newImage.toString();
                    imageEdited = true;
                    imageButton.setImageURI(newImage);
                }
                break;
        }
    }

}