package nonexistent.safeu_12;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import nonexistent.safeu_12.TutorialPages.Help;


/**
 * Created by matay on 2/16/2017.
 */

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }

    //Open Credits.java
    public void onCreditsClick(View v) {
        if (v.getId() == R.id.Credits) {
            Intent i = new Intent(Settings.this, Credits.class);
            startActivity(i);
        }
    }

    //Open Help.java
    public void onHelpClick(View v) {
        if (v.getId() == R.id.Help) {
            Intent i = new Intent(Settings.this, Help.class);
            startActivity(i);
        }
    }

    //Open Timer.java panel
    public void onTimerClick(View v) {
        if (v.getId() == R.id.Timer) {
            Intent i = new Intent(Settings.this, Timer.class);
            startActivity(i);
        }
    }
}

