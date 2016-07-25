//package com.example.septiawanaji.stisbroadcast.Register;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioGroup;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.septiawanaji.stisbroadcast.Koneksi.API;
//import com.example.septiawanaji.stisbroadcast.Koneksi.ConvertParameter;
//import com.example.septiawanaji.stisbroadcast.Koneksi.JSONParser;
//import com.example.septiawanaji.stisbroadcast.Login.LoginActivity;
//import com.example.septiawanaji.stisbroadcast.Objek.StaticFinal;
//import com.example.septiawanaji.stisbroadcast.Objek.Pk;
//import com.example.septiawanaji.stisbroadcast.R;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.HashMap;
//
///**
// * Created by Septiawan Aji on 6/14/2016.
// */
//public class RegisterActivity extends AppCompatActivity {
//
//    private EditText nomor_pendaftaran,password,nama_lengkap,nomor_telp,email,alamat_lengkap;
//    private RadioGroup jenis_kelamin;
//    private Button daftar,pilih_provinsi;
//    private TextView hasil_pilih_prov;
//    AlertDialog.Builder provinsiDialog;
//
//
//    Spinner spinner;
//
//    String [] provinsiArray = {"Aceh","Sumatera Utara","Sumatera Barat",
//            "Riau","Kepulauan Riau","Jambi","Sumatera Selatan","Bangka Belitung",
//            "Bengkulu","Lampung","DKI Jakarta","Jawa Barat","Banten","Jawa Tengah","Daerah Istimewa Yogyakarta",
//            "Jawa Timur","Bali","Nusa Tenggara Barat","Nusa Tenggara Timur","Kalimantan Barat",
//            "Kalimantan Tengah","Kalimatan Selatan","Kalimantan Timur","Kalimantan Utara",
//            "Sulawesi Utara","Sulawesi Barat","Sulawesi Tengah","Sulawesi Tenggara","Sulawesi Selatan",
//            "Gorontalo","Maluku","Maluku Utara","Papua Barat","Papua"};
//
//
//
//
//    Pk user;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_daftar);
//
//        user = new Pk();
//        //Edittext
//        nomor_pendaftaran = (EditText)findViewById(R.id.input_nomor_daftar);
//        password = (EditText)findViewById(R.id.input_password_daftar);
//        nama_lengkap = (EditText)findViewById(R.id.input_nama_lengkap);
//        nomor_telp = (EditText)findViewById(R.id.no_telp);
//        email = (EditText)findViewById(R.id.email);
//        alamat_lengkap = (EditText)findViewById(R.id.alamat_lengkap_daftar);
//        //button
//        pilih_provinsi = (Button)findViewById(R.id.button_pilih_prov);
//        daftar = (Button)findViewById(R.id.daftar);
//        //textview
//        hasil_pilih_prov = (TextView)findViewById(R.id.pilih_provinsi);
//        //Radio
//        jenis_kelamin = (RadioGroup)findViewById(R.id.jenis_kelamin);
//
//
//        provinsiDialog = new AlertDialog.Builder(this);
//        provinsiDialog.setTitle(R.string.provinsi);
//
//
//        provinsiDialog.setItems(provinsiArray, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
//                for (int i = 0; i < 34; i++) {
//                    if (which == i) {
//                        user.setProvinsi(provinsiArray[i]);
//                    }
//                }
//
//                hasil_pilih_prov.setText(user.getProvinsi());
//            }
//        });
//
//        pilih_provinsi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                provinsiDialog.show();
//            }
//        });
//
//        daftar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                user.setNomor_daftar(nomor_pendaftaran.getText().toString());
//                user.setNama(nama_lengkap.getText().toString());
//                user.setNo_telp(nomor_telp.getText().toString());
//                user.setPassword(password.getText().toString());
//                user.setEmail(email.getText().toString());
//                user.setAlamat_lengkap(alamat_lengkap.getText().toString());
//
//                switch (jenis_kelamin.getCheckedRadioButtonId()){
//                    case R.id.laki_laki :
//                        user.setJenis_kelamin("Laki-Laki");
//                        break;
//                    case R.id.perempuan :
//                        user.setJenis_kelamin("Perempuan");
//                        break;
//                }
//
//
//                if(isNoEmpty(user.getNomor_daftar())){
//                    if(isNoEmpty(user.getPassword())){
//                        if(isNoEmpty(user.getNama())){
//                            if(isNoEmpty(user.getNo_telp())){
//                                if(isNoEmpty(user.getEmail())){
//                                    if(isNoEmpty(user.getAlamat_lengkap())){
//                                        new daftarKeServer().execute();
//                                    }else {
//                                        Toast.makeText(RegisterActivity.this, R.string.alamat_kosong, Toast.LENGTH_SHORT).show();
//                                    }
//                                }else{
//                                    Toast.makeText(RegisterActivity.this, R.string.email_kosong, Toast.LENGTH_SHORT).show();
//                                }
//                            }else{
//                                Toast.makeText(RegisterActivity.this, R.string.no_telp_kosong, Toast.LENGTH_SHORT).show();
//                            }
//                        }else{
//                            Toast.makeText(RegisterActivity.this, R.string.nama_kosong, Toast.LENGTH_SHORT).show();
//                        }
//                    }else{
//                        Toast.makeText(RegisterActivity.this, R.string.password_kosong, Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(RegisterActivity.this, R.string.nomor_daftar_kosong, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//    }
//
//    //validasi form jika isinya kosong
//    public final static boolean isNoEmpty(String str){
//        if(str.isEmpty()){
//            return false;
//        }
//        return true;
//    }
//
//    private class daftarKeServer extends AsyncTask <String,Void,String>{
//        ProgressDialog pDialog;
//        JSONObject json;
//        JSONArray data;
//
//        String tangkapError = "";
//        String respon;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(RegisterActivity.this);
//            pDialog.setMessage("Loading");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            HashMap<String,String> parameter = new HashMap<>();
//            parameter.put(API.getAtributeServer(),API.getGetRegisterStatus());
//            parameter.put(StaticFinal.getNomorDaftar(),user.getNomor_daftar());
//            parameter.put(StaticFinal.getPASSWORD(),user.getPassword());
//            parameter.put(StaticFinal.getNAMA(),user.getNama());
//            parameter.put(StaticFinal.getNoTelp(),user.getNo_telp());
//            parameter.put(StaticFinal.getEMAIL(),user.getEmail());
//            parameter.put(StaticFinal.getPROVINSI(),user.getProvinsi());
//            parameter.put(StaticFinal.getJenisKelamin(),user.getJenis_kelamin());
//            parameter.put(StaticFinal.getAlamatLengkap(),user.getAlamat_lengkap());
//
//            JSONParser jsonParser = new JSONParser();
//
//            try{
//                json = jsonParser.getJSONFromUrl(API.getAlamatUrl()+ ConvertParameter.getQuery(parameter));
//                respon = json.getString(API.getSTATUS());
//                Log.d("Status Daftar",json.toString());
//            }catch (Exception e){
//                tangkapError = e.getMessage();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if(tangkapError ==""){
//                if(!respon.equals(API.getNOL())){
//                    Toast.makeText(RegisterActivity.this, "Input Berhasil", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Toast.makeText(RegisterActivity.this, "Input Gagal", Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                Toast.makeText(RegisterActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//            pDialog.dismiss();
//        }
//    }
//
//}
