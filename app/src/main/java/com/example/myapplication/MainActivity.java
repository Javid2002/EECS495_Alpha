package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.ui.main.AlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.alan.alansdk.button.AlanButton;
import com.alan.alansdk.AlanConfig;
import com.alan.alansdk.AlanCallback;
import com.alan.alansdk.events.EventCommand;
import android.util.Log;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;


import com.example.myapplication.ui.main.SectionsPagerAdapter;
import com.example.myapplication.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AlanButton alanButton;

    private Button settingsBtn;

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        AlanConfig config = AlanConfig.builder().setProjectId("3561f2b97b11e44447dc158361f2aa152e956eca572e1d8b807a3e2338fdd0dc/stage").build();
        alanButton = findViewById(R.id.alan_button);
        alanButton.initWithConfig(config);

        AlanCallback alanCallback = new AlanCallback() {
            /// Handling commands from Alan Studio
            @Override
            public void onCommand(final EventCommand eventCommand) {
                try {
                    JSONObject command = eventCommand.getData();
                    String commandName = command.getJSONObject("data").getString("command");
                    Log.d("AlanButton", "onCommand: commandName: " + commandName);
                } catch (JSONException e) {
                    Log.e("AlanButton", e.getMessage());
                }
            }
        };

        /// Registering callbacks
        alanButton.registerCallback(alanCallback);


        //setContentView(R.layout.activity_main);


        settingsBtn = findViewById(R.id.idBtnSettings);

        // adding on click listener for our button.
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new intent to open settings activity.
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });
        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        }
    public void OnToggleClicked(View view) {
        long time;
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(MainActivity.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();

            // calendar is called to get current time in hour and minute
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver
            Intent intent = new Intent(this, AlarmReceiver.class);

            // we call broadcast using pendingIntent
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
            if (System.currentTimeMillis() > time) {
                // setting time as AM and PM
                if (Calendar.AM_PM == 0)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            // Alarm rings continuously until toggle button is turned off
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
            // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(MainActivity.this, "ALARM OFF", Toast.LENGTH_SHORT).show();
        }
    }

    }



//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        /// Defining the project key
//        AlanConfig config = AlanConfig.builder().setProjectId("3561f2b97b11e44447dc158361f2aa152e956eca572e1d8b807a3e2338fdd0dc/stage").build();
//        alanButton = findViewById(R.id.alan_button);
//        alanButton.initWithConfig(config);
//
//        AlanCallback alanCallback = new AlanCallback() {
//            /// Handling commands from Alan Studio
//            @Override
//            public void onCommand(final EventCommand eventCommand) {
//                try {
//                    JSONObject command = eventCommand.getData();
//                    String commandName = command.getJSONObject("data").getString("command");
//                    Log.d("AlanButton", "onCommand: commandName: " + commandName);
//                } catch (JSONException e) {
//                    Log.e("AlanButton", e.getMessage());
//                }
//            }
//        };
//
//        /// Registering callbacks
//        alanButton.registerCallback(alanCallback);
//    }

//    intent('Hello', p => {
//        p.play('Welcome Back User Javid');
//    });
//

//
//        intent(
//        'Who\'s there',
//        'What\'s your name',
//        p => {
//        p.play(
//        'My name is Javid.',
//        'It\'s Javid.',
//        );
//        },
//        );
//

//// You can also pass a list of patterns to the intent function.
//        const intentPatterns = [
//        'What is your favorite food',
//        'What food do you like',
//        ];
//
//        intent(intentPatterns, p => {
//        p.play('CPU time, yammy!');
//        });
//
//// Try: "What is your favorite food" or "What food do you like".
//

//
//        intent('(I will have|Get me) a coffee, please', p => {
//        p.play('Sorry, I don\'t have hands to brew it.');
//        });
//
//        intent(
//        '495', p=>{p.play('It is the best class');}
//
//        );
//
//        intent(
//        'Who is Professor David Chesney', p=>{p.play('He is the best professor ever');}
//
//        );
//
//// Try: "I will have a coffee, please" or "Get me a coffee, please".
//

//
//        intent('(Start|begin|take|) survey', p => {
//        p.play('(Sure.|OK.|) Starting a customer survey.');
//        });


