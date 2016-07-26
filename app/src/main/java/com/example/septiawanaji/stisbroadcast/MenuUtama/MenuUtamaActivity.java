package com.example.septiawanaji.stisbroadcast.MenuUtama;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.Koneksi.API;
import com.example.septiawanaji.stisbroadcast.Koneksi.ConvertParameter;
import com.example.septiawanaji.stisbroadcast.Koneksi.JSONParser;
import com.example.septiawanaji.stisbroadcast.ListMaba.DaftarMaba;
import com.example.septiawanaji.stisbroadcast.Objek.Absensi;
import com.example.septiawanaji.stisbroadcast.Objek.AtributName;
import com.example.septiawanaji.stisbroadcast.Objek.Maba;
import com.example.septiawanaji.stisbroadcast.Objek.StaticFinal;
import com.example.septiawanaji.stisbroadcast.Scan.DecoderActivity;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.Scan.HasilScan;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;
import com.example.septiawanaji.stisbroadcast.SplashScreen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Handler;

/**
 * Created by Septiawan Aji on 6/28/2016.
 */
public class MenuUtamaActivity extends AppCompatActivity {
    SessionManager sm;
    HashMap<String,String> hm;
    DatabaseHandler db;
    ImageButton daftarMaba,scannerAbsensi,absensiMaba,upload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sm= new SessionManager(getApplicationContext());
        hm = sm.getUserSession();
        db = new DatabaseHandler(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        daftarMaba = (ImageButton)findViewById(R.id.imageButtonDaftarMaba);
        scannerAbsensi = (ImageButton) findViewById(R.id.imageButtonScannerAbsensi);
        absensiMaba = (ImageButton)findViewById(R.id.imageButtonAbsensiMaba);
        upload = (ImageButton)findViewById(R.id.imageButtonUpload);

        if(db.cekRowSize()==""){

            daftarMaba.setBackgroundResource(R.color.input_daftar);
            scannerAbsensi.setImageResource(R.drawable.scan_black);
            absensiMaba.setImageResource(R.drawable.tugas_maba_black);
            upload.setImageResource(R.drawable.upload_black);

            daftarMaba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new getListMaba().execute();
                }
            });

            scannerAbsensi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        Toast.makeText(MenuUtamaActivity.this, "Download Daftar Maba Terlebih Dahulu", Toast.LENGTH_SHORT).show();

                }
            });

            absensiMaba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuUtamaActivity.this, "Download Daftar Maba Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            });

            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuUtamaActivity.this, "Download Daftar Maba Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            daftarMaba.setImageResource(R.drawable.maba_black);
            if(sm.getUploadSession()==null){
                Log.d("Lari","kesini");
                upload.setBackgroundResource(R.color.colorAccent);
                upload.setImageResource(R.drawable.upload_activ);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new uploadAbsensi().execute();
                    }
                });
            }else{
                upload.setImageResource(R.drawable.upload_black);
                upload.setBackgroundResource(R.color.abu);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuUtamaActivity.this, "Data Absensi Sudah Diupload", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            scannerAbsensi.setBackgroundResource(R.color.tulisan);
            absensiMaba.setBackgroundResource(R.color.colorPrimary);

            daftarMaba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MenuUtamaActivity.this, "Daftar Maba Telah Didownload", Toast.LENGTH_SHORT).show();
                }
            });

            scannerAbsensi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DecoderActivity.class);
                        startActivity(intent);
                }
            });

            absensiMaba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DaftarMaba.class);
                    startActivity(intent);
                    finish();
                }
            });




        }
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

    public boolean cekKoneksi() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class getListMaba extends AsyncTask<String,Void,String> {
        ProgressDialog pDialog;
        JSONObject json;
        JSONArray respon;
        Maba maba;

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        String tangkapError="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuUtamaActivity.this);
            pDialog.setMessage("Download Daftar Maba ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
            SessionManager sm = new SessionManager(getApplicationContext());
            HashMap<String,String> parameter = new HashMap<>();
            HashMap<String,String> pk = sm.getUserSession();
            Log.d("pk", pk.toString());
            parameter.put(AtributName.getKODE(), API.getGetListMaba());
            parameter.put(AtributName.getNimPk(),pk.get(AtributName.getNIM()));
            Log.d("par", parameter.toString());

            JSONParser jsonParser = new JSONParser();
            try{
                json = jsonParser.getJSONFromUrl(sm.getAlamatServer()+ ConvertParameter.getQuery(parameter));
                respon = json.getJSONArray(API.getRESPON());
                Log.d("Daftar Maba",respon.toString());
                for(int i=0;i<respon.length();i++){
                    HashMap<String,String> map = new HashMap<>();
                    maba = new Maba();
                    JSONObject c = respon.getJSONObject(i);

                    maba.setNama(c.getString(StaticFinal.getNAMA()));
                    maba.setNomorPendaftaran(c.getString(StaticFinal.getNomorPendaftaran()));
                    maba.setPathFoto(c.getString(StaticFinal.getPathFoto()));
                    maba.setAsalProp(c.getString(AtributName.getAsalProp()));
                    maba.setNoHp(c.getString(AtributName.getNoHp()));
                    maba.setEmail(c.getString(StaticFinal.getEMAIL()));

                    map.put(StaticFinal.getNAMA(), maba.getNama());
                    map.put(StaticFinal.getPathFoto(), maba.getPathFoto());
                    map.put(StaticFinal.getNomorPendaftaran(), maba.getNomorPendaftaran());
                    map.put(AtributName.getNoHp(),maba.getNoHp());
                    map.put(AtributName.getAsalProp(),maba.getAsalProp());
                    map.put(StaticFinal.getEMAIL(), maba.getEmail());


                    db.insertRow(maba.getNomorPendaftaran(), maba.getNama(), maba.getPathFoto(),maba.getAsalProp(),maba.getNoHp(), maba.getEmail());
                    arrayList.add(map);
                }

            }catch (Exception e){
                e.printStackTrace();
                tangkapError = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(tangkapError==""){
                Toast.makeText(getApplicationContext(), "Download Berhasil", Toast.LENGTH_SHORT).show();
                daftarMaba.setImageResource(R.drawable.maba_black);
                daftarMaba.setBackgroundResource(R.color.abu);

                upload.setBackgroundResource(R.color.colorAccent);
                upload.setImageResource(R.drawable.upload_activ);

                scannerAbsensi.setBackgroundResource(R.color.tulisan);
                scannerAbsensi.setImageResource(R.drawable.scan);

                absensiMaba.setBackgroundResource(R.color.colorPrimary);
                absensiMaba.setImageResource(R.drawable.tugas_maba);

                daftarMaba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MenuUtamaActivity.this, "Daftar Maba Telah Didownload", Toast.LENGTH_SHORT).show();
                    }
                });

                scannerAbsensi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DecoderActivity.class);
                        startActivity(intent);
                    }
                });

                absensiMaba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DaftarMaba.class);
                        startActivity(intent);
                    }
                });

                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new uploadAbsensi().execute();
                    }
                });
            }else{
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class uploadAbsensi extends AsyncTask <String,Void,String>{
        Absensi absensi;
        ProgressDialog pDialog;
        JSONObject json;
        JSONArray data;

        String tangkapError = "";
        String respon;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MenuUtamaActivity.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            absensi = new Absensi();

            ArrayList<Absensi> arrayAbsensi = db.selectAllAbsensi();
            pDialog.setMessage("Upload Data Absensi");
            for(int i = 0;i<arrayAbsensi.size();i++){
                HashMap<String,String> parameter = new HashMap<>();
                parameter.put(AtributName.getKODE(), AtributName.getUploadAbsensi());
                parameter.put(AtributName.getNomorPendaftaran(),arrayAbsensi.get(i).getNomorPendaftaran());
                parameter.put(AtributName.getTANGGAL(),arrayAbsensi.get(i).getTanggal());
                parameter.put(AtributName.getJamDatang(), arrayAbsensi.get(i).getWaktu());
                JSONParser jsonParser = new JSONParser();

                SessionManager sm = new SessionManager(getApplicationContext());

                try{
                    json = jsonParser.getJSONFromUrl(sm.getAlamatServer() + ConvertParameter.getQuery(parameter));
                    respon = json.getString(AtributName.getRESPON());
                    Log.d("Respon Insert",respon);
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
                if(!respon.equals(AtributName.getNOL())){
                    Toast.makeText(MenuUtamaActivity.this, "Input Berhasil", Toast.LENGTH_LONG).show();
                    sm.createUploadSession();


                    if(sm.getUploadSession()=="yes"){
                        upload.setImageResource(R.drawable.upload_black);
                        upload.setBackgroundResource(R.color.abu);
                        upload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MenuUtamaActivity.this, "Data Absensi Sudah Diupload", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Log.d("Lari", "kesini");
                        upload.setBackgroundResource(R.color.colorAccent);
                        upload.setImageResource(R.drawable.upload_activ);
                        upload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new uploadAbsensi().execute();
                            }
                        });
                    }
                }else{
                    Toast.makeText(MenuUtamaActivity.this, "Data Sudah Ada", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(MenuUtamaActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();

        }
    }
}
