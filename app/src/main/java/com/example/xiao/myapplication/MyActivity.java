package com.example.xiao.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;



public class MyActivity extends Activity {

    public static final String PREFS_NAME = "MyPrefsFile";
    Button btn;
    TextView textViewTime;
    long startTime = 0;


    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int sec = (int) (millis/1000);
            int min = sec / 60;
            sec = sec % 60;
            textViewTime.setText(String.format("%d:%02d", min, sec));
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("oncreate");
        System.out.println("savedstate");
        System.out.println(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String mode = settings.getString("mode", null);
        startTime = settings.getLong("startTime", 0);
        System.out.println("mode: " + mode);
        btn = (Button) findViewById(R.id.btn);
        textViewTime = (TextView) findViewById(R.id.viewTimeText);

        if (mode != null && mode.equals(getString(R.string.stop))) {
            btn.setText(R.string.stop);
            timerHandler.postDelayed(timerRunnable, 0);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.getText().equals(getString(R.string.stop))) {
                    timerHandler.removeCallbacks(timerRunnable);
                    btn.setText(R.string.start);
                } else {
                    startTime = System.currentTimeMillis();
                    btn.setText(R.string.stop);
                    timerHandler.postDelayed(timerRunnable, 0);
                }
            }
        });
    }

    static final String START_FROM = "startFrom";
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        System.out.println("onsaveinstancestate");
        System.out.println(startTime);
        savedInstanceState.putLong(START_FROM, startTime);
        System.out.println(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong("startTime", startTime);
        editor.putString("mode", (String) btn.getText());
        System.out.println((String) btn.getText());

        System.out.println("onStop");
        editor.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }




}
