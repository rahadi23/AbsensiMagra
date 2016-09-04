package com.example.septiawanaji.stisbroadcast.ListMaba;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.ImageLoader.ImageLoader;
import com.example.septiawanaji.stisbroadcast.Objek.Maba;
import com.example.septiawanaji.stisbroadcast.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Septiawan Aji on 6/27/2016.
 */
public class AdapterDaftarMaba extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    ArrayList<Maba> daftarMaba;
    Maba resultp = new Maba();
    private LayoutInflater inflater;

    private DatabaseHandler db;

    ImageLoader imageLoader;

    public AdapterDaftarMaba(Context context,int layoutResourceId,ArrayList<Maba> daftarMaba){
        super(context,layoutResourceId,daftarMaba);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.daftarMaba = daftarMaba;
        imageLoader = new ImageLoader(context);
        db = new DatabaseHandler(context);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView namaMaba,noMaba,statusKehadiran;
        ImageView fotoMaba,tandaUpload;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  itemView = inflater.inflate(R.layout.adapter_maba,parent,false);

        resultp = daftarMaba.get(position);

        namaMaba = (TextView)itemView.findViewById(R.id.nama_maba);
        noMaba = (TextView)itemView.findViewById(R.id.no_maba);
        statusKehadiran = (TextView)itemView.findViewById(R.id.status_kehadiran);
        fotoMaba = (ImageView)itemView.findViewById(R.id.foto_maba);
        tandaUpload = (ImageView)itemView.findViewById(R.id.tandaUpload);

        if(resultp.getNama().length()>15){
            namaMaba.setText(resultp.getNama().substring(0, 15)+"...");
        }else{
            namaMaba.setText(resultp.getNama());
        }


        noMaba.setText(resultp.getNomorPendaftaran());
        Log.d("Status Adapter "+resultp.getNomorPendaftaran(),db.selectStatusUpload(resultp.getNomorPendaftaran(),sdf.format(new Date())));
        if(!db.selectStatusUpload(resultp.getNomorPendaftaran(),sdf.format(new Date())).equals("Sudah Upload")){
            tandaUpload.setImageResource(R.drawable.error);
        }else{
            tandaUpload.setImageResource(R.drawable.success);
        }

        if(db.selectWaktu(resultp.getNomorPendaftaran(),sdf.format(new Date()))==""){
            statusKehadiran.setText("Blm Hadir");
            statusKehadiran.setBackgroundResource(R.color.input_daftar );
        }else{
            statusKehadiran.setText("Hdr : "+db.selectWaktu(resultp.getNomorPendaftaran(),sdf.format(new Date())));
        }

        imageLoader.DisplayImage(resultp.getPathFoto(),fotoMaba);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = daftarMaba.get(position);
                Log.d("POSITION",Integer.toString(position));
                showDialog(resultp.getNama(),resultp.getNoHp(),resultp.getAsalProp(),resultp.getEmail(), resultp.getKelompok());
            }
        });
        return itemView;
    }

    public void showDialog(String nama,String noTelp,String asal,String email, String kelompok){
        final AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialView = inflater.inflate(R.layout.detail_maba_dialog, null);

        TextView namaMaba = (TextView)dialView.findViewById(R.id.namaMabaDialog);
        TextView noTelpMaba = (TextView)dialView.findViewById(R.id.noTelpMabaDialog);
        TextView asalMaba = (TextView)dialView.findViewById(R.id.asalMabaDialog);
        TextView emailMaba = (TextView)dialView.findViewById(R.id.emailMabaDialog);

        dialogBuilder.setView(dialView);
        dialogBuilder.setTitle("Detail Maba");
        namaMaba.setText("Nama : " + nama);
        noTelpMaba.setText("Telp : "+noTelp);
        asalMaba.setText("Asal : " + asal);
        emailMaba.setText("Email : "+ email);
        emailMaba.setText("Kelompok : "+ kelompok);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context, db.selectStatusUpload(resultp.getNomorPendaftaran()), Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

}
