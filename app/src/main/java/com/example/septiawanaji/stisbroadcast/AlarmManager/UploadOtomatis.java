package com.example.septiawanaji.stisbroadcast.AlarmManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;

/**
 * Created by Septiawan Aji on 7/27/2016.
 */
public class UploadOtomatis extends BroadcastReceiver {
    Context context;
    SessionManager sm;
    DatabaseHandler db;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sm = new SessionManager(context);
        db = new DatabaseHandler(context);
        db.deleteAbsensiHariIni();
        Toast.makeText(context, "Hapus Absensi Berhasil", Toast.LENGTH_SHORT).show();
    }
}
