package com.example.warrantytracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import com.example.warrantytracker.database.AppDatabase;
import com.example.warrantytracker.database.Device;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import android.app.Service;

public class AddDevice extends AppCompatActivity {

    // manufacturers strings for autofill
    private static final String[] manufacturers = new String[] {
            "LG", "Samsung", "Dell"
    };

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    Uri newImage;
    String newImageString;
    boolean imageEdited = false;

    private NotificationManagerCompat notificationManager;

    //////////////////////////////////////////////
    // On create, loads add_device.xml layout
    // creates text inputs, creates buttons
    /////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device);

        AutoCompleteTextView editText = findViewById(R.id.manufacturerInput);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, manufacturers);
        editText.setAdapter(adapter);

        /////////////////////////////////////////////////////
        // date picker button
        ////////////////////////////////////////////////////
        intDatePicker();
        dateButton = findViewById(R.id.deviceTimeRemaining);
        dateButton.setText(getTodaysDate());

        ///////////////////////////////////////
        //Pulls fields from add_device.xml
        //
        //////////////////////////////////////
        final EditText deviceNameInput = findViewById(R.id.nameInput);
        final EditText deviceManufacturerInput = findViewById(R.id.manufacturerInput);
        final EditText deviceSerialInput = findViewById(R.id.serialInput);
        final Button deviceDateOfPurchaseInput = findViewById(R.id.deviceTimeRemaining);
        final Button linkButton = findViewById(R.id.linkButton);
        final TextView timeRemaining = findViewById(R.id.timeRemaining);
        final EditText warrantyMonths = findViewById(R.id.warrantyMonths);
        final EditText warrantyYears = findViewById(R.id.warrantyYears);

        deviceDateOfPurchaseInput.setText(makeDateString2(Calendar.getInstance()));
        linkButton.setVisibility(View.GONE);

        Button saveButton = findViewById(R.id.saveButton);

        ///////////////////////////////////////////////////////
        // Setup notification channel
        ///////////////////////////////////////////////////////
        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int warrantyMonthsVal = 0;
                int warrantyYearsVal = 0;
                if (warrantyMonths.getText().toString().equals("") && warrantyYears.getText().toString().equals("")) {
                    warrantyMonthsVal = 0;
                    warrantyYearsVal = 1;
                } else if (warrantyMonths.getText().toString().equals("")) {
                    warrantyMonthsVal = 0;
                    warrantyYearsVal = Integer.parseInt(warrantyYears.getText().toString());

                } else {
                    warrantyYearsVal = Integer.parseInt(warrantyYears.getText().toString());
                    warrantyMonthsVal = Integer.parseInt(warrantyMonths.getText().toString());
                }
                saveNewDevice(deviceNameInput.getText().toString(), deviceManufacturerInput.getText().toString(), deviceSerialInput.getText().toString(), deviceDateOfPurchaseInput.getText().toString(), timeRemaining.getText().toString(), warrantyMonthsVal, warrantyYearsVal);
                // display time remaining on device's recyclerview card
                //TextView dateOfPurchaseInput = findViewById(R.id.dateOfPurchaseInput);
                //dateOfPurchaseInput.setText(deviceDateOfPurchaseInput.getText().toString());
            }
        });

        // autofill input opens manufacturer website
        /*linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String manufacturer1 = "LG";
                String manufacturer2 = "Samsung";
                String manufacturer3 = "Dell";

                if (manufacturer1.equals("LG")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.lg.com/us/support/repair-service/schedule-repair-continued"));
                    startActivity(intent);
                } else if (manufacturer2.equals("Samsung")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.samsung.com/us/support/warranty/"));
                    startActivity(intent);
                } else  if (manufacturer3.equals("Dell")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dell.com/support/home/en-us?app=warranty"));
                    startActivity(intent);
                }
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
    private void saveNewDevice(String deviceName, String deviceManufacturer, String deviceSerial, String deviceDateOfPurchase, String timeRemaining, int warrantyMonths, int warrantyYears){
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        Device device = new Device();
        device.deviceName = deviceName;
        device.manufacturer = deviceManufacturer;
        device.deviceSerial = deviceSerial;
        device.deviceDateOfPurchase = deviceDateOfPurchase;
        device.timeRemaining = timeRemaining;
        device.warrantyMonths = warrantyMonths;
        device.warrantyYears = warrantyYears;
        if (imageEdited) {
            //SAVE URI
            device.deviceImage = extraPhotoURI.toString();
        }
        db.deviceDao().insertDevice(device);

        notificationSet(device);

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel1", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("description");
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void notificationSet (Device device) {
        final String CHANNEL_ID = "channel1";

        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), "notification set function", duration);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(device.deviceName)
                .setContentText("This device's warranty is ending soon!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        Notification notification = builder.build();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            toast.show();
            NotificationManagerCompat notificationManager2 = NotificationManagerCompat.from(this);
            notificationManager.notify(device.deviceID, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void scheduleNotification(Device device) {
        Calendar calendar2 = Calendar.getInstance();
        int years = device.warrantyYears;
        calendar2.add(Calendar.YEAR, years);
        int months = device.warrantyMonths;
        calendar2.add(Calendar.MONTH, months);
        int daysBetween = 365;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            daysBetween = (int) ChronoUnit.DAYS.between(Calendar.getInstance().getTime().toInstant(), calendar2.toInstant());

        }
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        intent.putExtra("titleExtra", device.deviceName);
        intent.putExtra("textExtra", "This item's warranty is ending soon!");
        intent.putExtra("idExtra", device.deviceID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
        Toast.makeText(getApplicationContext(), "Scheduled ", Toast.LENGTH_LONG).show();
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
                EditText warrantyMonths = findViewById(R.id.warrantyMonths);
                EditText warrantyYears = findViewById(R.id.warrantyYears);
                //int years = Integer.parseInt(warrantyYears.getText().toString());
                //calendar2.add(Calendar.YEAR, years);
                //int months = Integer.parseInt(warrantyMonths.getText().toString());
                //calendar2.add(Calendar.MONTH, months);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int daysBetween = (int) ChronoUnit.DAYS.between(Calendar.getInstance().getTime().toInstant(), calendar2.toInstant());
                    //TextView timeRemaining = findViewById(R.id.timeRemaining);
                    //timeRemaining.setText(daysBetween + " days remaining");
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

    private void timeRemaining(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Calendar calendar2 = Calendar.getInstance();
            calendar2.set(0,0,0);
            int daysBetween = (int) ChronoUnit.DAYS.between(Calendar.getInstance().getTime().toInstant(), calendar2.toInstant());
            TextView timeRemaining = findViewById(R.id.timeRemaining);
            timeRemaining.setText(daysBetween + " days remaining");
        }
        return;
    }

    ////////////////////////////////////
    // Format for the date shown
    // on the recycler list
    ///////////////////////////////////
    private String makeDateString(int day, int month, int year) {
        Calendar selected = Calendar.getInstance();
        selected.set(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date selectedDate = selected.getTime();
        String strDate = sdf.format(selectedDate);
        return strDate;
    }
    private String makeDateString2(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date selectedDate = calendar.getTime();
        String strDate = sdf.format(selectedDate);
        return strDate;
    }

    // transform the string above that you get turn it into a calendar object ^^
    private Calendar makeStringDate(String str_date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = (Date)sdf.parse(str_date);
        calendar.setTime(date);
        return calendar;
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