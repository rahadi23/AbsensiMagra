package com.example.septiawanaji.stisbroadcast.MenuUtama;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.Koneksi.API;
import com.example.septiawanaji.stisbroadcast.Koneksi.JSONParser;
import com.example.septiawanaji.stisbroadcast.ListMaba.DaftarMaba;
import com.example.septiawanaji.stisbroadcast.Objek.Absensi;
import com.example.septiawanaji.stisbroadcast.Objek.AtributName;
import com.example.septiawanaji.stisbroadcast.Objek.Maba;
import com.example.septiawanaji.stisbroadcast.Objek.Pk;
import com.example.septiawanaji.stisbroadcast.Objek.StaticFinal;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.Scan.DecoderActivity;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;
import com.example.septiawanaji.stisbroadcast.SplashScreen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Septiawan Aji on 6/28/2016.
 */
public class MenuUtamaActivity extends AppCompatActivity {
    SessionManager sm;
    HashMap<String,String> hm;
    DatabaseHandler db;
    ImageView daftarMaba,scannerAbsensi,absensiMaba,upload;
    LinearLayout layDaftarMaba, layScannerAbsensi, layAbsensiMaba, layUpload;
    PendingIntent pendingIntent;
    TextView namaPk, nimPk;

    AlarmManager uploadAlarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sm= new SessionManager(getApplicationContext());
        hm = sm.getUserSession();
        db = new DatabaseHandler(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        daftarMaba = (ImageView) findViewById(R.id.imageButtonDaftarMaba);
        scannerAbsensi = (ImageView) findViewById(R.id.imageButtonScannerAbsensi);
        absensiMaba = (ImageView) findViewById(R.id.imageButtonAbsensiMaba);
        upload = (ImageView) findViewById(R.id.imageButtonUpload);
        namaPk = (TextView)findViewById(R.id.nama_panitia);
        nimPk = (TextView)findViewById(R.id.nim_panitia);
        layDaftarMaba = (LinearLayout)findViewById(R.id.relativeLayout);
        layAbsensiMaba = (LinearLayout)findViewById(R.id.relativeLayout2);
        layScannerAbsensi = (LinearLayout)findViewById(R.id.relativeLayout3);
        layUpload = (LinearLayout)findViewById(R.id.relativeLayout4);

        Pk p = Pk.getINSTANCE();
        namaPk.setText(p.getNama());
        nimPk.setText(p.getNim());
//        //upload otomatis pada jam 23.00
//        Calendar uploadTime = Calendar.getInstance();
//
//        uploadTime.set(Calendar.HOUR_OF_DAY,23);
//        uploadTime.set(Calendar.MINUTE,1);
//        uploadTime.set(Calendar.SECOND,1);
//
//        uploadAlarm = (AlarmManager)getSystemService(ALARM_SERVICE);
//        Intent intentAlarm = new Intent(MenuUtamaActivity.this, UploadOtomatis.class);
//        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),123,intentAlarm,PendingIntent.FLAG_ONE_SHOT);
//        //setiap jam 23.10 buka UploadOtomatis.java
//
//        //testing 20 detik sekali
////                uploadAlarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (60 * 1000), pendingIntent);
//        Log.d("Milis cuk", String.valueOf(uploadTime.getTimeInMillis()));
//        uploadAlarm.set(AlarmManager.RTC_WAKEUP, uploadTime.getTimeInMillis(), pendingIntent);


        if(db.cekRowSizeMaba()==""){

//            daftarMaba.setBackgroundResource(R.color.input_daftar);
            scannerAbsensi.setImageResource(R.drawable.scan_black_new);
            layScannerAbsensi.setBackgroundResource(R.color.abu);
            absensiMaba.setImageResource(R.drawable.tugas_maba_black_new);
            layAbsensiMaba.setBackgroundResource(R.color.abu);
            upload.setImageResource(R.drawable.upload_black_new);
            layUpload.setBackgroundResource(R.color.abu);

            daftarMaba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cekKoneksi()){
                        new getListMaba().execute();
                    }else{
                        Toast.makeText(MenuUtamaActivity.this, R.string.no_koneksi, Toast.LENGTH_SHORT).show();
                    }

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

            daftarMaba.setImageResource(R.drawable.maba_black_new);
            layDaftarMaba.setBackgroundResource(R.color.abu);


                upload.setImageResource(R.drawable.upload_activ_new);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cekKoneksi()){
                            if(db.cekRowSizeAbsensi()==""){
                                Toast.makeText(MenuUtamaActivity.this, "Belum Melakukan Scanning", Toast.LENGTH_SHORT).show();
                            }else{
                                new uploadAbsensi().execute();new uploadAbsensi().execute();
                            }

                        }else{
                            Toast.makeText(MenuUtamaActivity.this, R.string.no_koneksi, Toast.LENGTH_SHORT).show();
                        }

                    }
                });



//            scannerAbsensi.setBackgroundResource(R.color.tulisan);
//            absensiMaba.setBackgroundResource(R.color.colorPrimary);

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
//                        finish();
                }
            });

            absensiMaba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DaftarMaba.class);
                    startActivity(intent);
//                    finish();
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
            showDialog();
        }

        return super.onOptionsItemSelected(item);

    }

    public void showDialog(){
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.pass_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText password = (EditText)dialogView.findViewById(R.id.password);

        dialogBuilder.setTitle("Logout");
        dialogBuilder.setMessage("Mau logout? Yakin? Bener? Ga nyesel? Nanti ilang lho database-nya? Serah deh..");
        dialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (password.getText().toString().equals("yoi")) {
                    sm.deleteSession();
                    db.deleteAllMaba();
                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MenuUtamaActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialogBuilder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.show();
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


//            parameter.put(AtributName.getNimPk(),pk.get(AtributName.getNIM()));
//            parameter.put(AtributName.getKODE(), API.getGetListMaba());
            ArrayList<String> rezkya = new ArrayList<>();
            rezkya.add(API.getGetListMaba());
            rezkya.add(pk.get(AtributName.getNIM()));



            Log.d("par", parameter.toString());

            JSONParser jsonParser = new JSONParser();
            try{
//                json = jsonParser.getJSONFromUrl(sm.getAlamatServer()+ ConvertParameter.getQuery(parameter));
                json = jsonParser.getJSONFromUrl(API.getAlamatUrl()+rezkya.get(0)+"/"+rezkya.get(1));
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
                    maba.setKelompok(c.getString(StaticFinal.getKELOMPOK()));

                    map.put(StaticFinal.getNAMA(), maba.getNama());
                    map.put(StaticFinal.getPathFoto(), maba.getPathFoto());
                    map.put(StaticFinal.getNomorPendaftaran(), maba.getNomorPendaftaran());
                    map.put(AtributName.getNoHp(),maba.getNoHp());
                    map.put(AtributName.getAsalProp(),maba.getAsalProp());
                    map.put(StaticFinal.getEMAIL(), maba.getEmail());
                    map.put(StaticFinal.getKELOMPOK(), maba.getKelompok());


                    db.insertRow(maba.getNomorPendaftaran(), maba.getNama(), maba.getPathFoto(),maba.getAsalProp(),maba.getNoHp(), maba.getEmail(), maba.getKelompok());
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
                daftarMaba.setImageResource(R.drawable.maba_black_new);
                layDaftarMaba.setBackgroundResource(R.color.abu);


                layUpload.setBackgroundResource(R.color.hijau);
                upload.setImageResource(R.drawable.upload_activ_new);

                layScannerAbsensi.setBackgroundResource(R.color.oranye);
                scannerAbsensi.setImageResource(R.drawable.scan_new);

                layAbsensiMaba.setBackgroundResource(R.color.merah);
                absensiMaba.setImageResource(R.drawable.tugas_maba_new);

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
//                        finish();
                    }
                });

                absensiMaba.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), DaftarMaba.class);
                        startActivity(intent);
//                        finish();
                    }
                });

                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(db.cekRowSizeAbsensi()==""){
                            Toast.makeText(getApplicationContext(), "Belum Melakukan Scanning", Toast.LENGTH_SHORT).show();
                        }else{
                            new uploadAbsensi().execute();
                        }
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
            pDialog.setMessage("Upload Data Absensi");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            absensi = new Absensi();

            ArrayList<Absensi> arrayAbsensi = db.selectStatusBelumUpload();

            ArrayList<String> arrayParameterGet = new ArrayList<>();
            for(int i = 0;i<arrayAbsensi.size();i++){
                HashMap<String,String> parameter = new HashMap<>();
//                parameter.put(AtributName.getJamDatang(), arrayAbsensi.get(i).getWaktu());
//                parameter.put(AtributName.getTANGGAL(),arrayAbsensi.get(i).getTanggal());
//                parameter.put(AtributName.getKODE(), AtributName.getUploadAbsensi());
//                parameter.put(AtributName.getNomorPendaftaran(),arrayAbsensi.get(i).getNomorPendaftaran());
                arrayParameterGet.add(AtributName.getUploadAbsensi());
                arrayParameterGet.add(arrayAbsensi.get(i).getNomorPendaftaran());
                arrayParameterGet.add(arrayAbsensi.get(i).getTanggal());
                arrayParameterGet.add(arrayAbsensi.get(i).getWaktu());
                JSONParser jsonParser = new JSONParser();

                SessionManager sm = new SessionManager(getApplicationContext());

                try{
//                    json = jsonParser.getJSONFromUrl(sm.getAlamatServer() + ConvertParameter.getQuery(arrayParameterGet));
                    json = jsonParser.getJSONFromUrl(API.getAlamatUrl()+arrayParameterGet.get(0)+"/"+arrayParameterGet.get(1)+"/"+arrayParameterGet.get(2)+"/"+arrayParameterGet.get(3));
                    respon = json.getString(AtributName.getRESPON());
                    db.updateStatusUpload(arrayAbsensi.get(i).getNomorPendaftaran(),arrayAbsensi.get(i).getTanggal());
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
                if(respon!="0"){
                    Toast.makeText(MenuUtamaActivity.this, "Upload Berhasil", Toast.LENGTH_LONG).show();
//                    sm.createUploadSession();
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
