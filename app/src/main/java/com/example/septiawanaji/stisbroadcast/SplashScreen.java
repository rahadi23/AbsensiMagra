package com.example.septiawanaji.stisbroadcast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.septiawanaji.stisbroadcast.Login.LoginActivity;
import com.example.septiawanaji.stisbroadcast.MenuUtama.MenuUtamaActivity;
import com.example.septiawanaji.stisbroadcast.Objek.StaticFinal;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;

import java.util.HashMap;

/**
 * Created by Septiawan Aji on 6/28/2016.
 */public class SplashScreen extends AppCompatActivity {
    SessionManager sessionManager;
    HashMap<String, String> hm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        sessionManager = new SessionManager(getApplicationContext());
        hm = sessionManager.getUserSession();
        Log.d("HM ", hm.toString());
        Thread splash = new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    if (hm.get(StaticFinal.getNAMA()) == null) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MenuUtamaActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splash.start();
    }
}
