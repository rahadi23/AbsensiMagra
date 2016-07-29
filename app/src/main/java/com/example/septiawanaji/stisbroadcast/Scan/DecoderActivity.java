package com.example.septiawanaji.stisbroadcast.Scan;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.MenuUtama.MenuUtamaActivity;
import com.example.septiawanaji.stisbroadcast.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Septiawan Aji on 7/7/2016.
 */
public class DecoderActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private TextView myTextView;
    private QRCodeReaderView mydecoderview;
    private Button ok,no,dec;
    private DatabaseHandler db;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);

        mydecoderview = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        mydecoderview.setOnQRCodeReadListener(this);
        myTextView = (TextView)findViewById(R.id.text);



    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        status = (TextView)findViewById(R.id.statusScan);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        db = new DatabaseHandler(getApplicationContext());
        if(db.selectRow(text)==""){
            status.setText("Maba Tidak Ada di Daftar");
        }else{
            if(db.selectWaktu(text,sdf.format(new Date()))==""){
                Intent intent = new Intent(getApplicationContext(),HasilScan.class);
                intent.putExtra("ID",text);
                startActivity(intent);
                finish();
            }else{
                status.setText("Absensi Maba Tersebut Sudah Diinput");
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MenuUtamaActivity.class);
        startActivity(i);
        finish();
    }


}
