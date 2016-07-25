package com.example.septiawanaji.stisbroadcast.DaftarMaba;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.septiawanaji.stisbroadcast.Database.DatabaseHandler;
import com.example.septiawanaji.stisbroadcast.ImageLoader.ImageLoader;
import com.example.septiawanaji.stisbroadcast.Objek.AtributName;
import com.example.septiawanaji.stisbroadcast.Objek.StaticFinal;
import com.example.septiawanaji.stisbroadcast.R;
import com.example.septiawanaji.stisbroadcast.Scan.DecoderActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Septiawan Aji on 6/27/2016.
 */
public class AdapterDaftarMaba extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    ArrayList<HashMap<String,String>> daftarMaba;
    HashMap<String,String> resultp = new HashMap<>();
    private LayoutInflater inflater;

    ImageLoader imageLoader;

    public AdapterDaftarMaba(Context context,int layoutResourceId,ArrayList<HashMap<String,String>> daftarMaba){
        super(context,layoutResourceId,daftarMaba);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.daftarMaba = daftarMaba;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView namaMaba,noMaba;
        ImageView fotoMaba;
        inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  itemView = inflater.inflate(R.layout.adapter_maba,parent,false);

        resultp = daftarMaba.get(position);

        namaMaba = (TextView)itemView.findViewById(R.id.nama_maba);
        noMaba = (TextView)itemView.findViewById(R.id.no_maba);
        fotoMaba = (ImageView)itemView.findViewById(R.id.foto_maba);
        if(resultp.get(StaticFinal.getNAMA()).length()>15){
            namaMaba.setText(resultp.get(StaticFinal.getNAMA()).substring(0 ,15)+"...");
        }else{
            namaMaba.setText(resultp.get(StaticFinal.getNAMA()));
        }

        noMaba.setText(resultp.get(StaticFinal.getNomorPendaftaran()));


        imageLoader.DisplayImage(resultp.get(StaticFinal.getPathFoto()),fotoMaba);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultp = daftarMaba.get(position);
                Log.d("POSITION",Integer.toString(position));
                showDialog(resultp.get(StaticFinal.getNAMA()),resultp.get(AtributName.getNoHp()),resultp.get(AtributName.getAsalProp()),resultp.get(StaticFinal.getEMAIL()));
            }
        });
        return itemView;
    }

    public void showDialog(String nama,String noTelp,String asal,String email){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
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

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

}
