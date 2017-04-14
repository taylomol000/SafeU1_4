package nonexistent.safeu_12.TutorialPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import nonexistent.safeu_12.Credits;
import nonexistent.safeu_12.R;
import nonexistent.safeu_12.Settings;


public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
    }

    public void onCreditsClick(View v) {
        if (v.getId() == R.id.To_1) {
            Intent i = new Intent(Help.this, P2.class);
            startActivity(i);
        }
    }


}
