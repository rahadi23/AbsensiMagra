package com.example.septiawanaji.stisbroadcast.AlarmManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.Koneksi.ConvertParameter;
import com.example.septiawanaji.stisbroadcast.Koneksi.JSONParser;
import com.example.septiawanaji.stisbroadcast.Objek.Absensi;
import com.example.septiawanaji.stisbroadcast.Objek.AtributName;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
    }

}
