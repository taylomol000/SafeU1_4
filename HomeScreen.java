package nonexistent.safeu_12;

//for basic imports

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.app.PendingIntent;

import java.util.ArrayList;


//to access "userAssignedTimerVal" in Timer class
//for GPS
//for check and/or power buttons
//import com.google.android.gms.appindexing.AppIndex;
//for alert button


public class HomeScreen extends AppCompatActivity {

    private static String MIME_TEXT_PLAIN;
//    private final String MIME_TEXT_PLAIN = getString(R.string.explanation);
    LocationManager locationManager;
    LocationListener locationListener;
    ImageButton power;
    ImageButton check;
    TextView minsLeft;
    TextView mTextView;

    long timerTime;
    CountDownTimer countDownTimer = null;

    Vibrator vibe;

    private NfcAdapter mNfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        getApplicationContext();
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mTextView = (TextView) findViewById(R.id.textView_explanation);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mTextView.setText("NFC is disabled.");
        } else {
            mTextView.setText(R.string.explanation);
        }

        handleIntent(getIntent());

        //Grab time value for countDownTimer
        SharedPreferences sharedPref = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE);
        timerTime = sharedPref.getInt(getString(R.string.userAssignedTimerVal), 10);

        power = (ImageButton) findViewById(R.id.power);
        check = (ImageButton) findViewById(R.id.check);
        minsLeft = (TextView) findViewById(R.id.minsLeft);

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibe.cancel();
                //If the countDownTimer is running, turn it off and write "stopped"
                if (countDownTimer != null)
                    countDownTimer.cancel();
                minsLeft.setText("stopped");
            }
        });

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Cancel any vibrations created by this app
                vibe.cancel();

                //If the countDownTimer is running, turn it off
                if (countDownTimer != null)
                    countDownTimer.cancel();

                //create timer; set time (minutes)
                countDownTimer = new CountDownTimer(timerTime * 1000, 1000) { //Set to seconds for testing purposess

                    //When the timer is going, show a countdown for minutes and seconds
                    @Override
                    public void onTick(long milliseconds) {
                        long minutes = milliseconds / 60000;
                        long seconds = (milliseconds / 1000) % 60;
                        minsLeft.setText(getString(R.string.minutes) + (int) (minutes) + "  seconds:" + (int) (seconds));

                    }

                    //When the timer ends, ask if user is okay and start vibration pattern
                    @Override
                    public void onFinish() {
                        minsLeft.setText("Are You OK?");
                        long[] pattern = {100, 1000, 100, 2};
                        vibe.vibrate(pattern, 1);
                    }
                }
                //If "check" pressed, starts CountDownTimer
                .start();
            }
        });
    }

    public void onAlertClick(View view) {
        Toast sent = Toast.makeText(getApplicationContext(), "Alert Sent", Toast.LENGTH_SHORT);
        Toast permissions = Toast.makeText(getApplicationContext(), "Please change app permissions in settings", Toast.LENGTH_SHORT);

        //Try to send SMS in background; show success notification
        try {
            SmsManager.getDefault().sendTextMessage(getString(R.string.smsNumber), null,
                    getString(R.string.smsMessage), null, null);
            sent.show();

        //If SMS permission not granted, let user know
        } catch (Exception e) {
            permissions.show();
        }
    }

    //open Settings.java panel
    public void onSettingsClick(View v) {
        if (v.getId() == R.id.settings) {
            Intent i = new Intent(HomeScreen.this, Settings.class);
            startActivity(i);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /*
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
}