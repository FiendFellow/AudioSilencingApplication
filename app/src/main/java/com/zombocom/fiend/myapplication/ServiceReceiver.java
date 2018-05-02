package com.zombocom.fiend.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Fiend on 4/27/2018.
 */

public class ServiceReceiver extends BroadcastReceiver {
    @Override

    public void onReceive(final Context context, Intent intent) {
        /*
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);

                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                System.out.println("incomingNumber : "+incomingNumber);
               // if(incomingNumber.equals())
                Log.i(DEBUG_TAG, "  " + incomingNumber);
                Toast.makeText(context, ("Phone number: " +incomingNumber), Toast.LENGTH_LONG).show();
                //amanager.setStreamMute(AudioManager.STREAM_RING, false);
                   // exceptionIncoming = false;
            }
        },PhoneStateListener.LISTEN_CALL_STATE);



       //amanager.setStreamMute(AudioManager.STREAM_RING, false);

    }
    private AudioManager amanager;
    private boolean exceptionIncoming = true;
    private static final String DEBUG_TAG = "MyActivity";
    */
    }
}
