package com.example.septiawanaji.stisbroadcast.Scan;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.ImageLoader.ImageLoader;
import com.example.septiawanaji.stisbroadcast.Objek.Absensi;
import com.example.septiawanaji.stisbroadcast.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Septiawan Aji on 7/7/2016.
 */
public class DecoderActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView myTextView;
    private QRCodeReaderView mydecoderview;
    private Button ok,no,dec,simpan;
    private DatabaseHandler db;
    private TextView status, nama;
    private Absensi absensi;
    private ImageView imageView;
    private ImageLoader imageLoader;
    private DatabaseHandler database;
    private ArrayList<String> hasilNamaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
        myTextView = (TextView)findViewById(R.id.text);

        imageView = (ImageView) findViewById(R.id.cewekJalu);
        imageLoader = new ImageLoader(getApplicationContext());
        nama = (TextView) findViewById(R.id.namaMaba);
        simpan = (Button) findViewById(R.id.btnSimpan);
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        final Absensi absensi;

        status = (TextView)findViewById(R.id.statusScan);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        db = new DatabaseHandler(getApplicationContext());
        hasilNamaPath = db.selectRow(text);
        Log.d("HasilNama",hasilNamaPath.toString());
        if(hasilNamaPath.isEmpty()){
            status.setText(R.string.notfound);
            imageView.setImageResource(R.drawable.x);
            nama.setText("xxxxxx");

        }else{
            if(db.selectWaktu(text,sdf.format(new Date()))==""){
//                Intent intent = new Intent(getApplicationContext(),HasilScan.class);
//                intent.putExtra("ID",text);
//                startActivity(intent);
//                finish();

                mydecoderview.getCameraManager().stopPreview();
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialView = inflater.inflate(R.layout.scan_dialog, null);

                absensi = new Absensi();
                absensi.setNomorPendaftaran(text);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                absensi.setTanggal(sdf1.format(new Date()));
                SimpleDateFormat sdf2 = new SimpleDateFormat("kk:mm:ss");
                absensi.setWaktu(sdf2.format(new Date()));
//                status.setText(text+"\n"+absensi.getTanggal()+"\n"+absensi.getWaktu());
                status.setText(R.string.sukses);
                imageLoader.DisplayImage(hasilNamaPath.get(1), imageView);
                nama.setText(hasilNamaPath.get(0));

                Log.d("Maba bawah", absensi.getNomorPendaftaran() + absensi.getTanggal() + absensi.getWaktu());

                simpan.setVisibility(View.VISIBLE);
                simpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String statusUpload="Belum Upload";
                        db.insertAbsensi(absensi.getNomorPendaftaran(), absensi.getTanggal(), absensi.getWaktu(),statusUpload);
                        Toast.makeText(DecoderActivity.this, "Data Absensi Disimpan", Toast.LENGTH_SHORT).show();
                        mydecoderview.getCameraManager().startPreview();
                        simpan.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.x);
                        status.setText(R.string.belum);
                        nama.setText("xxxxxx");
                    }
                });

//                dialogBuilder.setView(dialView);
//
//                dialogBuilder.setTitle(db.selectRow(text));
//                dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        String statusUpload="Belum Upload";
//                        db.insertAbsensi(absensi.getNomorPendaftaran(), absensi.getTanggal(), absensi.getWaktu(),statusUpload);
//                        Toast.makeText(DecoderActivity.this, "Data Absensi Disimpan", Toast.LENGTH_SHORT).show();
////                        Intent intent = new Intent(HasilScan.this, DecoderActivity.class);
////                        startActivity(intent);
////                        finish();
//                        mydecoderview.getCameraManager().startPreview();
////                        new DecoderActivity();
//                    }
//                });
//                AlertDialog alertDialog = dialogBuilder.create();
//                alertDialog.show();
//                alertDialog.setCancelable(false);


//
//                DatabaseHandler db = new DatabaseHandler(this);
//                String statusUpload="Belum Upload";
//                db.insertAbsensi(absensi.getNomorPendaftaran(), absensi.getTanggal(), absensi.getWaktu(),statusUpload);
//                Toast.makeText(this, "Data Absensi Disimpan", Toast.LENGTH_SHORT).show();
            }else{
                status.setText(R.string.sudah);
                imageLoader.DisplayImage(hasilNamaPath.get(1), imageView);
                nama.setText(hasilNamaPath.get(0));
            }
        }
    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mydecoderview.getCameraManager().startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mydecoderview.getCameraManager().stopPreview();
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent i = new Intent(getApplicationContext(),MenuUtamaActivity.class);
//        startActivity(i);
//        finish();
//    }


}
