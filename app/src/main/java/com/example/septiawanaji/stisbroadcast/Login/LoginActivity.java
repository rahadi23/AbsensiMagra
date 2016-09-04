package com.example.septiawanaji.stisbroadcast.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Koneksi.API;
import com.example.septiawanaji.stisbroadcast.Koneksi.JSONParser;
import com.example.septiawanaji.stisbroadcast.Koneksi.Server;
import com.example.septiawanaji.stisbroadcast.MenuUtama.MenuUtamaActivity;
import com.example.septiawanaji.stisbroadcast.Objek.AtributName;
import com.example.septiawanaji.stisbroadcast.Objek.Pk;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.SessionManager.SessionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

//import com.example.septiawanaji.stisbroadcast.Register.RegisterActivity;

/**
 * Created by Septiawan Aji on 6/14/2016.
 */
public class LoginActivity extends AppCompatActivity {
    private EditText nomor_daftar,password;
    private Button next,daftar;

    SessionManager sessionManager;
    Pk user ;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        nomor_daftar = (EditText)findViewById(R.id.username_input);
        password = (EditText)findViewById(R.id.password_input);
        next = (Button)findViewById(R.id.next_1);
//        daftar = (Button)findViewById(R.id.prev_1);

        user = Pk.getINSTANCE();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setNim(nomor_daftar.getText().toString());
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] res = password.getText().toString().getBytes("UTF-8");
                    byte[] b = md.digest(res);

                    //Convert MD5 bytes to Hex
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < b.length; i++) {
                        sb.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
                    }

                    user.setPassword(sb.toString());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(user.getNim().isEmpty()){
                    Toast.makeText(LoginActivity.this, R.string.nomor_daftar_kosong, Toast.LENGTH_SHORT).show();
                }else{
                    new getStatusLogin().execute();
                }

            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if(id==R.id.action_settings){
//            showDialog();
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }

//    public void showDialog(){
//        server = new Server();
//        sessionManager = new SessionManager(getApplicationContext());
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View dialogView = inflater.inflate(R.layout.server_dialog, null);
//        dialogBuilder.setView(dialogView);
//
//        final EditText aturServer = (EditText)dialogView.findViewById(R.id.atur_server);
//        if(sessionManager.getAlamatServer() != null){
//            aturServer.setText(sessionManager.getAlamatServer());
//        }
//
//        dialogBuilder.setTitle("Atur Server");
//        dialogBuilder.setMessage("Masukan Alamat Server");
//        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (aturServer.getText().toString().isEmpty()) {
//                    Toast.makeText(LoginActivity.this, "Alamat Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
//                } else {
//                    sessionManager.deleteSession();
//                    sessionManager.saveUrlServer(aturServer.getText().toString());
//                }
//
//            }
//        });
//
//        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
//    }


    private class getStatusLogin extends AsyncTask<String,Void,String>{
        ProgressDialog pDialog;
        JSONObject json;
        JSONArray data;

        String tangkapError = "";
        String respon;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("Do", "inback");
            HashMap<String,String> parameter = new HashMap<>();
//            parameter.put(AtributName.getNIM(), user.getNim());
//            parameter.put(AtributName.getKODE(),AtributName.getGetStatusLogin());
//            parameter.put(AtributName.getPASSWORD(), user.getPassword());

            ArrayList<String> provita = new ArrayList<>();
            provita.add(AtributName.getGetStatusLogin());
            provita.add(user.getNim());
            provita.add(user.getPassword());

            Log.d("Parameter", parameter.toString());

            JSONParser jsonParser = new JSONParser();
            SessionManager sm = new SessionManager(getApplicationContext());

            try{
//                json = jsonParser.getJSONFromUrl(sm.getAlamatServer() + ConvertParameter.getQuery(parameter));
                json = jsonParser.getJSONFromUrl(API.getAlamatUrl()+provita.get(0)+"/"+provita.get(1)+"/"+provita.get(2));
                respon = json.getString(AtributName.getRESPON());
                Log.d("Respon",respon);
                if(!respon.equals(AtributName.getNOL())){
                    parameter.remove(AtributName.getKODE());
                    parameter.put(AtributName.getKODE(),AtributName.getGetNama());

                    ArrayList<String> atikaUkh = new ArrayList<>();
                    atikaUkh.add(AtributName.getGetNama());
                    atikaUkh.add(user.getNim());
                    atikaUkh.add(user.getPassword());
//                    json = jsonParser.getJSONFromUrl(sm.getAlamatServer() + ConvertParameter.getQuery(parameter));
                    json = jsonParser.getJSONFromUrl(API.getAlamatUrl()+atikaUkh.get(0)+"/"+atikaUkh.get(1)+"/"+atikaUkh.get(2));
                    data = json.getJSONArray(AtributName.getRESPON());
                    for(int i = 0;i<data.length();i++){
                        JSONObject c = data.getJSONObject(i);

                        user.setNama(c.getString(AtributName.getNAMA()));
                        user.setNim(c.getString(AtributName.getID()));
                    }
                }
//
            }catch (Exception e){
                tangkapError = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            sessionManager = new SessionManager(getApplicationContext());
            if (tangkapError == ("")) {
                if(respon.equals("1")){
                    sessionManager.createSessionLogin(user.getNim(), user.getNama());
//                    Toast.makeText(LoginActivity.this, user.getNama(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),MenuUtamaActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Username dan Password Salah", Toast.LENGTH_SHORT).show();
                }

            }else{
//                Log.e("SERVER ERROR", tangkapError);
                Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
            pDialog.dismiss();

        }
    }
}
