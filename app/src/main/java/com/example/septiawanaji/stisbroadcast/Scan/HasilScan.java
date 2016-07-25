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
    private String id,tanggal,waktu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_scan);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("ID");
        showDialog(id);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tanggal = sdf.format(new Date());

        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss");
        waktu = sdf1.format(new Date());

        Log.d("Tanggal",tanggal);
        Log.d("Waktu",waktu);
    }

    public void showDialog(String id){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialView = inflater.inflate(R.layout.scan_dialog, null);

        final DatabaseHandler db = new DatabaseHandler(getApplicationContext());


        dialogBuilder.setView(dialView);
        if(db.selectRow(id)==""){
            dialogBuilder.setTitle("Nama Maba Tidak Ada");
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), DecoderActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }else {
            dialogBuilder.setTitle(db.selectRow(id));
            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new uploadAbsensi().execute();

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(HasilScan.this, "Data Tidak Disimpan", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), DecoderActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

    private class uploadAbsensi extends AsyncTask <String,Void,String>{
        ProgressDialog pDialog;
        JSONObject json;
        JSONArray data;

        String tangkapError = "";
        String respon;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HasilScan.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> parameter = new HashMap<>();
            parameter.put(AtributName.getKODE(),AtributName.getUploadAbsensi());
            parameter.put(AtributName.getNomorPendaftaran(),id);
            parameter.put(AtributName.getTANGGAL(),tanggal);
            parameter.put(AtributName.getJamDatang(),waktu);
            JSONParser jsonParser = new JSONParser();

            SessionManager sm = new SessionManager(getApplicationContext());

            try{
                json = jsonParser.getJSONFromUrl(sm.getAlamatServer() + ConvertParameter.getQuery(parameter));
                respon = json.getString(AtributName.getRESPON());
                Log.d("Respon Insert",respon);
            }catch (Exception e){
                tangkapError = e.getMessage();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(tangkapError == ("")){
                if(!respon.equals(AtributName.getNOL())){
                    Intent intent = new Intent(getApplicationContext(),DecoderActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(HasilScan.this, "Input Berhasil", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(HasilScan.this, "Data Sudah Ada", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),DecoderActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else{
                Toast.makeText(HasilScan.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();
        }
    }
}
