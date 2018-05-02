package com.zombocom.fiend.myapplication;
import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;


import android.content.Context;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.provider.CalendarContract;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import java.util.Date;
import android.net.Uri;
import android.widget.TextView;
import android.view.View;
import android.content.ContentValues;




//Need to make it so that it can check events every 15 mins



public class MainActivity extends AppCompatActivity {


public long eventID = -1;
public Context mcontext;

@Override
protected void onResume() {


        super.onResume();
    long startMillis = System.currentTimeMillis();


    //This creates a cursor for going through the Instances calendar, which stores event IDs
    Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
            .buildUpon();
    //Start millis is the key here. We should track events that are happening at current time (startmillis)
    //and check for a certain amount of time in the future. Say, 15 mins
    ContentUris.appendId(eventsUriBuilder, startMillis);
    ContentUris.appendId(eventsUriBuilder, startMillis + 900000);
    Uri eventsUri = eventsUriBuilder.build();
    Cursor cursor = null;


    try {
        cursor = this.getContentResolver().query(eventsUri, INSTANCE_PROJECTION, null, null, CalendarContract.Instances.DTSTART + " ASC");
        // Use the cursor to step through the returned records

    } catch (SecurityException e) {
        Toast.makeText(getBaseContext(), "Security Exception, permissions are not found: " + e, Toast.LENGTH_LONG).show();
    } catch (java.lang.NullPointerException e) {
        Toast.makeText(getBaseContext(), "Null Pointer Exception", Toast.LENGTH_LONG).show();
    }


    try {

        while (cursor.moveToNext()) {
            String title = null;
            eventID = 0;
            long beginVal = 0;

            // Get the field values
            eventID = cursor.getLong(PROJECTION_ID_INDEX);
            beginVal = cursor.getLong(PROJECTION_BEGIN_INDEX);
            title = cursor.getString(PROJECTION_TITLE_INDEX);

            // Do something with the values.
            Log.i(DEBUG_TAG, "Event:  " + title);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(beginVal);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            //Toast.makeText(getBaseContext(), Long.toString(eventID), Toast.LENGTH_LONG).show();
            Log.i(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));

        }
    } catch (java.lang.NullPointerException e) {
        Toast.makeText(getBaseContext(), "Null Pointer Exception" + e, Toast.LENGTH_LONG).show();
    }


    //If event ID =-1, it means we did not find any events, and therefore event flag should be false
    if (eventID < 0) {
        //  Toast.makeText(getBaseContext(), "No events detected", Toast.LENGTH_LONG).show();
        eventFlag = false;
    } else {
        // Toast.makeText(getBaseContext(), "Event detected: " + eventID, Toast.LENGTH_LONG).show();
        eventFlag = true;
    }


}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        eventFlag = false;

        //Initialize the quiet zone flag
        withinQuietZone = false;



        Log.i(DEBUG_TAG, "  Test" );




        /*
        long eventID = 143;
        Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(uri);
        startActivity(intent);

        */

        //Requesting Permissions for Read Calendar

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }


        int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CALENDAR)) {


            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CALENDAR);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }

        int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {


            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }


        //Getting location manager
        LocationManager locationManager;
        Context mContext;
        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        //try catch block to get location data
        int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {


                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


            }
        } else {
            // Permission has already been granted
        }


//mintime is minimum time interval between location updates, in milliseconds



        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 20, locationListenerGPS);
            // locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerGPS);
        } catch (SecurityException e) {
            Toast.makeText(getBaseContext(), "Security Exception, permissions are not found: " + e, Toast.LENGTH_LONG).show();
        } catch (java.lang.NullPointerException e) {
            Toast.makeText(getBaseContext(), "Null Pointer Exception", Toast.LENGTH_LONG).show();
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        long startMillis = System.currentTimeMillis();


        //This creates a cursor for going through the Instances calendar, which stores event IDs
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
                .buildUpon();
        //Start millis is the key here. We should track events that are happening at current time (startmillis)
        //and check for a certain amount of time in the future. Say, 15 mins
        ContentUris.appendId(eventsUriBuilder, startMillis);
        ContentUris.appendId(eventsUriBuilder, startMillis + 900000);
        Uri eventsUri = eventsUriBuilder.build();
        Cursor cursor = null;


        try {
            cursor = this.getContentResolver().query(eventsUri, INSTANCE_PROJECTION, null, null, CalendarContract.Instances.DTSTART + " ASC");
            // Use the cursor to step through the returned records

        } catch (SecurityException e) {
            Toast.makeText(getBaseContext(), "Security Exception, permissions are not found: " + e, Toast.LENGTH_LONG).show();
        } catch (java.lang.NullPointerException e) {
            Toast.makeText(getBaseContext(), "Null Pointer Exception", Toast.LENGTH_LONG).show();
        }


        try {

            while (cursor.moveToNext()) {
                String title = null;
                eventID = 0;
                long beginVal = 0;

                // Get the field values
                eventID = cursor.getLong(PROJECTION_ID_INDEX);
                beginVal = cursor.getLong(PROJECTION_BEGIN_INDEX);
                title = cursor.getString(PROJECTION_TITLE_INDEX);

                // Do something with the values.
                Log.i(DEBUG_TAG, "Event:  " + title);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(beginVal);
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                //Toast.makeText(getBaseContext(), Long.toString(eventID), Toast.LENGTH_LONG).show();
                Log.i(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));

            }
        } catch (java.lang.NullPointerException e) {
            Toast.makeText(getBaseContext(), "Null Pointer Exception" + e, Toast.LENGTH_LONG).show();
        }


        //If event ID =-1, it means we did not find any events, and therefore event flag should be false
        if (eventID < 0) {
            //  Toast.makeText(getBaseContext(), "No events detected", Toast.LENGTH_LONG).show();
            eventFlag = false;
        } else {
            // Toast.makeText(getBaseContext(), "Event detected: " + eventID, Toast.LENGTH_LONG).show();
            eventFlag = true;
        }
        //Print out an event with ID eventID, if it exists


        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener phoneStateListener = new PhoneStateListener(){

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                //super.onCallStateChanged(state, incomingNumber);
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                String number = incomingNumber;
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Toast.makeText(getApplicationContext(), "Phone is Ringing", Toast.LENGTH_LONG);

                    Log.i(DEBUG_TAG, "Number:  "  + number);
                    if(number.equals("3177508923"))
                        audioManager.setStreamMute(AudioManager.STREAM_RING, false);

                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


    }






// Define a listener that responds to location updates
        LocationListener locationListenerGPS = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                 llmsg = "New Latitude: " + latitude + "New Longitude: " + longitude;
                //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();

                quietTimeFlag = false;

                    // Are we inside a quiet zone, i.e. a square or rectangle



                long startMillis = System.currentTimeMillis();


                //This creates a cursor for going through the Instances calendar, which stores event IDs
                Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI
                        .buildUpon();
                //Start millis is the key here. We should track events that are happening at current time (startmillis)
                //and check for a certain amount of time in the future. Say, 15 mins
                ContentUris.appendId(eventsUriBuilder, startMillis);
                ContentUris.appendId(eventsUriBuilder, startMillis + 900000);
                Uri eventsUri = eventsUriBuilder.build();
                Cursor cursor = null;


                try {
                    cursor = getApplicationContext().getContentResolver().query(eventsUri, INSTANCE_PROJECTION, null, null, CalendarContract.Instances.DTSTART + " ASC");
                    // Use the cursor to step through the returned records

                } catch (SecurityException e) {
                    Toast.makeText(getBaseContext(), "Security Exception, permissions are not found: " + e, Toast.LENGTH_LONG).show();
                } catch (java.lang.NullPointerException e) {
                    Toast.makeText(getBaseContext(), "Null Pointer Exception", Toast.LENGTH_LONG).show();
                }


                try {

                    while (cursor.moveToNext()) {
                        String title = null;
                        eventID = 0;
                        long beginVal = 0;

                        // Get the field values
                        eventID = cursor.getLong(PROJECTION_ID_INDEX);
                        beginVal = cursor.getLong(PROJECTION_BEGIN_INDEX);
                        title = cursor.getString(PROJECTION_TITLE_INDEX);

                        // Do something with the values.
                        Log.i(DEBUG_TAG, "Event:  " + title);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(beginVal);
                        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                        //Toast.makeText(getBaseContext(), Long.toString(eventID), Toast.LENGTH_LONG).show();
                        Log.i(DEBUG_TAG, "Date: " + formatter.format(calendar.getTime()));

                    }
                } catch (java.lang.NullPointerException e) {
                    Toast.makeText(getBaseContext(), "Null Pointer Exception" + e, Toast.LENGTH_LONG).show();
                }


                //If event ID =-1, it means we did not find any events, and therefore event flag should be false
                if (eventID < 0) {
                    //  Toast.makeText(getBaseContext(), "No events detected", Toast.LENGTH_LONG).show();
                    eventFlag = false;
                } else {
                    // Toast.makeText(getBaseContext(), "Event detected: " + eventID, Toast.LENGTH_LONG).show();
                    eventFlag = true;
                }


            // Jon's house
            boolean flag1 = false;
            boolean flag2 = false;

            double lat1 = location.getLatitude();
            double long1 = location.getLongitude();

            //Latitude increases going North
            //Longitude increases going East

            double JLat_bl = 33.867410;   // This is bot, left
            double JLong_bl = -118.181020;
            double JLat_tr = 33.867757 ;   // this is right, top
            double JLong_tr = -118.180658;



            // location of Falcon and Harding in LB
            double fh_lat=  33.867213;
            double fh_long = -118.174446;

           if ( ( (lat1 >= JLat_bl) && (lat1 < JLat_tr) )  && (long1 >= JLong_bl)  && (long1 <= JLong_tr) )     // then inside quiet zone
           {
               // quiet the phone
               //Toast.makeText(getBaseContext(), ("Inside quiet zone! Great!"), Toast.LENGTH_LONG).show();
               //withinQuietZone = true;
               flag1 = true;
           }
           else {
               //Toast.makeText(getBaseContext(), ("Outside quiet zone??!?? " + llmsg), Toast.LENGTH_LONG).show();
               //withinQuietZone = false;
               flag1 = false;
           }



           //CSU Dominguez Hills settings
           double DHLat_bl = 33.863610; //bot, 33.863610, left-118.255462
           double DHLong_bl = -118.255462;
           double DHlat_tr = 33.864320;  //top 33.864320, right -118.254340
            double DHlong_tr = -118.254340;

            if ( ( (lat1 >= DHLat_bl) && (lat1 < DHlat_tr) )  && (long1 >= DHLong_bl)  && (long1 < DHlong_tr) )     // then inside quiet zone
            {
                // quiet the phone

                //Toast.makeText(getBaseContext(), ("Inside quiet zone! Great!"), Toast.LENGTH_LONG).show();
               // withinQuietZone = true;
                flag2 = true;
            }
            else {
               // Toast.makeText(getBaseContext(), ("Outside quiet zone??!?? " + llmsg), Toast.LENGTH_LONG).show();
                //withinQuietZone = false;
                flag2 = false;
            }



            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);







               // Date currentTime = Calendar.getInstance().getTime();

                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);

               // Toast.makeText(getBaseContext(), (" " +currentHour), Toast.LENGTH_LONG).show();


                //Defining the Quiet Hours from Midnight to 7 AM, or 00 to 07
                if(currentHour>= 0 && currentHour<7)
                    quietTimeFlag = true;





                //Here are the big two events for muting the phone.
                //First, an if statement that checks the 3 mute conditions: location, time, event.
                //If any of these mute conditions are detected, print out which ones. Can be more than one.
                //If none are detected, simply stay unmuted. Printout is only for debug.

                //withinQuietZone is the main boolean. In second section, if withinQuietZone true, mute the phone settings.
                //If false, it should unmute the phone settings.
            if(flag1 || flag2 || eventFlag || quietTimeFlag)
            {
                withinQuietZone = true;

              if(eventFlag)
                   Toast.makeText(getBaseContext(), ("Event occurring."), Toast.LENGTH_LONG).show();
               if(quietTimeFlag)
                  Toast.makeText(getBaseContext(), ("Quiet hours detected."), Toast.LENGTH_LONG).show();
              if(!eventFlag && !quietTimeFlag)
                Toast.makeText(getBaseContext(), ("Inside quiet zone! Great!"), Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(), ("Outside quiet zone.  " + llmsg), Toast.LENGTH_LONG).show();
                withinQuietZone = false;
            }

            if(withinQuietZone) {




                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                audioManager.setStreamMute(AudioManager.STREAM_RING, true);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                 }
            else
                {
                audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
                audioManager.setStreamMute(AudioManager.STREAM_RING, false);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
                 }

            }





            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }


        };


// Register the listener with the Location Manager to receive location updates


    private static final String DEBUG_TAG = "MyActivity";
    public static final String[] INSTANCE_PROJECTION = new String[] {
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;



public boolean withinQuietZone;
public boolean eventFlag;
public String llmsg = "(0,0)";
public boolean quietTimeFlag;


@Override
    protected void onDestroy(){
        super.onDestroy();

        //Audio should unmute on closing the application.


        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        audioManager.setStreamMute(AudioManager.STREAM_RING, false);
         audioManager.setStreamMute(AudioManager.STREAM_MUSIC,  false);
    }

}


