package com.example.septiawanaji.stisbroadcast.DaftarMaba;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.Koneksi.API;
import com.example.septiawanaji.stisbroadcast.Koneksi.ConvertParameter;
import com.example.septiawanaji.stisbroadcast.Koneksi.JSONParser;
import com.example.septiawanaji.stisbroadcast.MenuUtama.MenuUtamaActivity;
import com.example.septiawanaji.stisbroadcast.Objek.AtributName;
import com.example.septiawanaji.stisbroadcast.Objek.Maba;
import com.example.septiawanaji.stisbroadcast.Objek.StaticFinal;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Septiawan Aji on 6/27/2016.
 */
public class DaftarMaba extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_maba_grid);

        if(cekKoneksi()){
                new getListMaba().execute();
        }else{
            Toast.makeText(DaftarMaba.this, R.string.no_koneksi, Toast.LENGTH_SHORT).show();
        }

    }


    public boolean cekKoneksi() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private class getListMaba extends AsyncTask<String,Void,String>{
        ProgressDialog pDialog;
        JSONObject json;
        JSONArray respon;
        Maba maba;

        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        String tangkapError="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DaftarMaba.this);
            pDialog.setMessage("Loading");
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
            Log.d("pk",pk.toString());
            parameter.put(AtributName.getKODE(), API.getGetListMaba());
            parameter.put(AtributName.getNimPk(),pk.get(AtributName.getNIM()));
            Log.d("par",parameter.toString());

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


                    db.insertRow(maba.getNomorPendaftaran(), maba.getNama());
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

            if(arrayList.size()>0){
                GridView gv = (GridView)findViewById(R.id.gridView_maba);
                AdapterDaftarMaba adapterDaftarMaba = new AdapterDaftarMaba(DaftarMaba.this,R.layout.activity_daftar_maba_grid,arrayList);
                gv.setAdapter(adapterDaftarMaba);
                pDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Intent i = new Intent(getApplicationContext(),MenuUtamaActivity.class);
            startActivity(i);
            finish();
    }

}
