package nonexistent.safeu_12;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Timer extends AppCompatActivity {

    EditText timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationContext();
        setContentView(R.layout.timer);
        timer = (EditText) findViewById(R.id.manualTimer);
        timer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                //Set value typed in as int timerVal
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    String text = timer.getText().toString();
                    int timerVal = Integer.parseInt(text);

                    //Value must be 1-30 minutes
                    if(0 < timerVal && timerVal <= 30) {

                        //Create variable that can be accessed in HomeScreen.java
                        SharedPreferences sharedPref = getSharedPreferences("TimerPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(getString(R.string.userAssignedTimerVal), timerVal);
                        editor.commit();

                        Intent i = new Intent(Timer.this, HomeScreen.class);
                        startActivity(i);

                    }else {
                        //If value is not from 1-30, show error message
                        Toast toast = Toast.makeText(getApplicationContext(), "Please enter a number from 1 through 30", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                return false;
            }
        });
    }
}