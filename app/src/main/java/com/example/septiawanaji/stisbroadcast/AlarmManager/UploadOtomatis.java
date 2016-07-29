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
        ArrayList<Absensi> ab = db.selectAllAbsensi();
        if(ab.size()!=0){
            if(cekKoneksi()){
                new uploadAbsensi().execute();
            }else{
                Toast.makeText(context, R.string.no_koneksi, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, R.string.data_kosong_absensi, Toast.LENGTH_SHORT).show();
        }



    }

    public boolean cekKoneksi() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class uploadAbsensi extends AsyncTask<String,Void,String> {
        Absensi absensi;
        ProgressDialog pDialog;
        JSONObject json;
        JSONArray data;

        String tangkapError = "";
        String respon;

//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(context);
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }

        @Override
        protected String doInBackground(String... params) {
            absensi = new Absensi();
            Log.d("Alarm","Upload Otomatis");

            ArrayList<Absensi> arrayAbsensi = db.selectAllAbsensi();
            for(int i = 0;i<arrayAbsensi.size();i++){
                HashMap<String,String> parameter = new HashMap<>();
                parameter.put(AtributName.getKODE(), AtributName.getUploadAbsensi());
                parameter.put(AtributName.getNomorPendaftaran(),arrayAbsensi.get(i).getNomorPendaftaran());
                parameter.put(AtributName.getTANGGAL(),arrayAbsensi.get(i).getTanggal());
                parameter.put(AtributName.getJamDatang(), arrayAbsensi.get(i).getWaktu());
                JSONParser jsonParser = new JSONParser();

                SessionManager sm = new SessionManager(context);

                try{
                    json = jsonParser.getJSONFromUrl(sm.getAlamatServer() + ConvertParameter.getQuery(parameter));
                    respon = json.getString(AtributName.getRESPON());
                    Log.d("Respon Insert", respon);
                }catch (Exception e){
                    tangkapError = e.getMessage();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(tangkapError == ("")){
                if(respon!="0"){
                    Toast.makeText(context, "Input Berhasil", Toast.LENGTH_LONG).show();
                    sm.createUploadSession();
//                    Intent intent= new Intent();
//                    intent.setClassName(context,"MenuUtamaActivity.class");
//                    context.startActivity(intent);

//
//                    if(sm.getUploadSession()=="yes"){
//                        upload.setImageResource(R.drawable.upload_black);
//                        upload.setBackgroundResource(R.color.abu);
//                        upload.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(context, "Data Absensi Sudah Diupload", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }else {
//                        Log.d("Lari", "kesini");
//                        upload.setBackgroundResource(R.color.colorAccent);
//                        upload.setImageResource(R.drawable.upload_activ);
//                        upload.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                new uploadAbsensi().execute();
//                            }
//                        });
//                    }
                }else{
                    Toast.makeText(context, "Data Sudah Ada", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
//            pDialog.dismiss();

        }
    }
}
