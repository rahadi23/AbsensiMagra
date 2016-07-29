package com.example.septiawanaji.stisbroadcast.Scan;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.Koneksi.API;
import com.example.septiawanaji.stisbroadcast.Koneksi.ConvertParameter;
import com.example.septiawanaji.stisbroadcast.Koneksi.JSONParser;
import com.example.septiawanaji.stisbroadcast.MenuUtama.MenuUtamaActivity;
import com.example.septiawanaji.stisbroadcast.Objek.Absensi;
import com.example.septiawanaji.stisbroadcast.Objek.AtributName;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Septiawan Aji on 7/20/2016.
 */
public class HasilScan extends AppCompatActivity {
    private Absensi absensi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_scan);

        absensi = new Absensi();
        Bundle bundle = getIntent().getExtras();

        absensi.setNomorPendaftaran(bundle.getString("ID"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        absensi.setTanggal(sdf.format(new Date()));

        SimpleDateFormat sdf1 = new SimpleDateFormat("kk:mm:ss");
        absensi.setWaktu(sdf1.format(new Date()));

        showDialog(absensi.getNomorPendaftaran());

    }

    public void showDialog(String id){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialView = inflater.inflate(R.layout.scan_dialog, null);


        final DatabaseHandler db = new DatabaseHandler(HasilScan.this);
        Log.d("Maba bawah",absensi.getNomorPendaftaran()+absensi.getTanggal()+absensi.getWaktu());

        dialogBuilder.setView(dialView);

                dialogBuilder.setTitle(db.selectRow(id));
                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String statusUpload="Belum Upload";
                        db.insertAbsensi(absensi.getNomorPendaftaran(), absensi.getTanggal(), absensi.getWaktu(),statusUpload);
                        Toast.makeText(HasilScan.this, "Data Absensi Disimpan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HasilScan.this, DecoderActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                alertDialog.setCancelable(false);



    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(HasilScan.this,DecoderActivity.class);
        startActivity(i);
        finish();
    }
}
