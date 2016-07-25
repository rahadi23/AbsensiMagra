package com.example.septiawanaji.stisbroadcast.MenuUtama;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.septiawanaji.stisbroadcast.DaftarMaba.DaftarMaba;
import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.Scan.DecoderActivity;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;
import com.example.septiawanaji.stisbroadcast.SplashScreen;

import java.util.HashMap;

/**
 * Created by Septiawan Aji on 6/28/2016.
 */
public class MenuUtamaActivity extends AppCompatActivity {
    SessionManager sm;
    HashMap<String,String> hm;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sm= new SessionManager(getApplicationContext());
        hm = sm.getUserSession();
        db = new DatabaseHandler(getApplicationContext());
        ImageButton daftarMaba,scannerAbsensi,absensiMaba,lainLain;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        daftarMaba = (ImageButton)findViewById(R.id.imageButtonDaftarMaba);
        scannerAbsensi = (ImageButton) findViewById(R.id.imageButtonScannerAbsensi);
        absensiMaba = (ImageButton)findViewById(R.id.imageButtonAbsensiMaba);
        lainLain = (ImageButton)findViewById(R.id.imageButtonLainLain);


        daftarMaba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DaftarMaba.class);
                startActivity(intent);
                finish();
            }
        });

        scannerAbsensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.cekRowSize()==""){
                    Toast.makeText(MenuUtamaActivity.this, "Download Daftar Maba Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(getApplicationContext(), DecoderActivity.class);
                    startActivity(intent);
                }
            }
        });

        absensiMaba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuUtamaActivity.this, "Fungsi Dalam Proses Pengerjaan", Toast.LENGTH_SHORT).show();
            }
        });
        lainLain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuUtamaActivity.this, "Fungsi Dalam Proses Pengerjaan", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_setting_logout){
            sm.deleteSession();
            db.deleteAllMaba();
            Intent intent = new Intent(getApplicationContext(),SplashScreen.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);

    }
}
