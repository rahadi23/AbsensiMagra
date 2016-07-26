package com.example.septiawanaji.stisbroadcast.ListMaba;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.TextView;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.MenuUtama.MenuUtamaActivity;
import com.example.septiawanaji.stisbroadcast.Objek.Maba;
import com.example.septiawanaji.stisbroadcast.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Septiawan Aji on 7/26/2016.
 */
public class DaftarMaba extends AppCompatActivity {
    private ArrayList<Maba> arrayMaba;
    private DatabaseHandler db;
    private TextView tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_maba_grid);
        tanggal = (TextView)findViewById(R.id.keterangan_hari);
        tanggal.setText(getToday());



        db = new DatabaseHandler(getApplicationContext());
        arrayMaba = db.selecAllRows();
        GridView gv = (GridView)findViewById(R.id.gridView_maba);
        AdapterDaftarMaba adapterDaftarMaba = new AdapterDaftarMaba(DaftarMaba.this,R.layout.activity_daftar_maba_grid,arrayMaba);
        gv.setAdapter(adapterDaftarMaba);
    }

    public String getToday() {
        String day, month, tanggal;
        String tgl, bln, thn;
        Calendar calendar = Calendar.getInstance();

        tanggal = null;

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            day = "Senin";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            day = "Selasa";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            day = "Rabu";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            day = "Kamis";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            day = "Jumat";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            day = "Sabtu";
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            day = "Minggu";
        } else {
            day = "";
        }

        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            month = "Januari";
        } else if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
            month = "Februari";
        } else if (calendar.get(Calendar.MONTH) == Calendar.MARCH) {
            month = "Maret";
        } else if (calendar.get(Calendar.MONTH) == Calendar.APRIL) {
            month = "April";
        } else if (calendar.get(Calendar.MONTH) == Calendar.MAY) {
            month = "Mei";
        } else if (calendar.get(Calendar.MONTH) == Calendar.JUNE) {
            month = "Juni";
        } else if (calendar.get(Calendar.MONTH) == Calendar.JULY) {
            month = "Juli";
        } else if (calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
            month = "Agustus";
        } else if (calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
            month = "September";
        } else if (calendar.get(Calendar.MONTH) == Calendar.OCTOBER) {
            month = "Oktober";
        } else if (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) {
            month = "November";
        } else if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            month = "Desember";
        } else {
            month = "";
        }
            tanggal = day + ", " + calendar.get(Calendar.DAY_OF_MONTH) + " " + month + " " + calendar.get(Calendar.YEAR);


        return tanggal;
    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(getApplicationContext(),MenuUtamaActivity.class);
//        startActivity(i);
//        finish();
//    }
}
