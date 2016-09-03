package com.example.septiawanaji.stisbroadcast.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.septiawanaji.stisbroadcast.Objek.Absensi;
import com.example.septiawanaji.stisbroadcast.Objek.Maba;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji on 7/20/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABSE_VERSION = 11;
    private static final String DATABASE_NAME = "maba_db";

    private static final String TABEL_MABA = "tabel_maba";
    private static final String TABEL_ABSENSI_MABA= "tabel_absensi_maba";

    private static final String NAMA = "nama";
    private static final String NO = "no_pendaftaran";
    private static final String PATH_FOTO = "path_foto";
    private static final String ASAL_PROP = "asal_prop";
    private static final String NO_HP = "no_hp";
    private static final String EMAIL = "email";
    private static final String KELOMPOK = "kelompok";
    private static final String TANGGAL = "tanggal";
    private static final String JAM_KEDATANGAN = "jam_kedatangan";
    private static final String ID = "id";
    private static final String STATUS_UPLOAD = "status_upload";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_MABA);
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_ABSENSI_MABA);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MABA_TABLE = "CREATE TABLE "+ TABEL_MABA +" ("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
               +NO+" TEXT,"
                +PATH_FOTO+" TEXT,"
                +ASAL_PROP+" TEXT,"
                +NO_HP+" TEXT,"
                +EMAIL+" TEXT,"
                +KELOMPOK+" TEXT,"
                +NAMA+" TEXT)";
        db.execSQL(CREATE_MABA_TABLE);
        String CREATE_ABSENSI_TABLE = "CREATE TABLE "+TABEL_ABSENSI_MABA +" ("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +NO+" TEXT,"
                +TANGGAL+" TEXT,"
                +STATUS_UPLOAD+" TEXT,"
                +JAM_KEDATANGAN+" TEXT)";
        db.execSQL(CREATE_ABSENSI_TABLE);
    }

    public void insertRow(String no,String nama,String pathFoto,String asalProp,String noHP,String email, String kelompok){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NO,no);
        values.put(NAMA,nama);
        values.put(PATH_FOTO,pathFoto);
        values.put(ASAL_PROP,asalProp);
        values.put(NO_HP,noHP);
        values.put(EMAIL,email);
        values.put(KELOMPOK,kelompok);

        db.insert(TABEL_MABA, null, values);
        Log.d("Insert DB", db.toString());
        db.close();
    }

    public ArrayList<String> selectRow(String no){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> namaPath = new ArrayList<>();
        Cursor c  = db.rawQuery("SELECT " + NAMA+","+PATH_FOTO+ " FROM " + TABEL_MABA + " WHERE " + NO + "='" + no + "'", null);

        String namaMaba="";
        if(c!=null && c.moveToFirst()){
            namaPath.add(c.getString(0));
            namaPath.add(c.getString(1));
        }else{
            namaPath.clear();
        }
        Log.d("namaMaba", namaMaba);
        c.close();
        return namaPath;
    }

    public ArrayList<Maba> selecAllRows(){
        ArrayList<Maba> array = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + NO + ", " + NAMA + ", " + PATH_FOTO + ", " + ASAL_PROP + ", " + NO_HP + ", " + EMAIL + ", " + KELOMPOK + " FROM " + TABEL_MABA, null);
        Maba maba;
        if(c.moveToFirst()) {
            do {
                maba = new Maba();

                maba.setNomorPendaftaran(c.getString(0));
                maba.setNama(c.getString(1));
                maba.setPathFoto(c.getString(2));
                maba.setAsalProp(c.getString(3));
                maba.setNoHp(c.getString(4));
                maba.setEmail(c.getString(5));
                maba.setKelompok(c.getString(6));

                array.add(maba);
            }while(c.moveToNext());
            Log.d("SQLITE", array.toString());
        }
        return array;
    }

    public void deleteAllMaba(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABEL_MABA, null, null);
        db.delete(TABEL_ABSENSI_MABA,null,null);
    }

    public String cekRowSizeMaba(){
        String kosong="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c  = db.rawQuery("SELECT " + NAMA + " FROM " + TABEL_MABA , null);

        String namaMaba="";
        if(c!=null && c.moveToFirst()){
            kosong="ada";
        }
        return kosong;
    }

    public void insertAbsensi(String no,String tanggal,String jamKedatangan,String statusUpload){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NO,no);
        values.put(TANGGAL,tanggal);
        values.put(JAM_KEDATANGAN,jamKedatangan);
        values.put(STATUS_UPLOAD, statusUpload);

        db.insert(TABEL_ABSENSI_MABA, null, values);
        Log.d("Insert Absensi",db.toString());
        db.close();
    }

    public String selectWaktu(String no,String tanggal){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c  = db.rawQuery("SELECT " + JAM_KEDATANGAN + " FROM " + TABEL_ABSENSI_MABA + " WHERE " + NO + "='" + no+"' AND "+ TANGGAL +"='"+tanggal+"'", null);

        String jamKedatangan="";
        if(c!=null & c.moveToFirst()){
            jamKedatangan=c.getString(0);
        }else{
            jamKedatangan="";
        }
        Log.d("Waktunya " + no, jamKedatangan);
        return jamKedatangan;
    }

    public String selectStatusUpload(String no){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c  = db.rawQuery("SELECT " + STATUS_UPLOAD+ " FROM " + TABEL_ABSENSI_MABA + " WHERE " + NO + "='" + no+"'", null);

        String jamKedatangan="";
        if(c!=null & c.moveToFirst()){
            jamKedatangan=c.getString(0);
        }else{
            jamKedatangan="";
        }
        Log.d("Status Upload " + no, jamKedatangan);
        return jamKedatangan;
    }



    public ArrayList<Absensi> selectAllAbsensi(){
        ArrayList<Absensi> arrayAbsensi = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + NO + ", " + TANGGAL + ", " + JAM_KEDATANGAN + " FROM " + TABEL_ABSENSI_MABA, null);

        Absensi absensi;
        if(c.moveToFirst()) {
            do {
                absensi = new Absensi();
                absensi.setNomorPendaftaran(c.getString(0));
                absensi.setTanggal(c.getString(1));
                absensi.setWaktu(c.getString(2));


                arrayAbsensi.add(absensi);
            } while (c.moveToNext());
        }
        Log.d("Absensi Array",arrayAbsensi.toString());
        return arrayAbsensi;
    }

    public String cekRowSizeAbsensi(){
        String kosong="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c  = db.rawQuery("SELECT " + NO + " FROM " + TABEL_ABSENSI_MABA , null);

        if(c!=null && c.moveToFirst()){
            kosong="ada";
        }
        return kosong;
    }

    public void updateStatusUpload(String no,String tanggal){
        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(STATUS_UPLOAD,"Sudah Upload");
//        db.update(TABEL_ABSENSI_MABA, values, NO + "=" + no + " AND " + TANGGAL + "=" + tanggal, null);
        db.execSQL("UPDATE " + TABEL_ABSENSI_MABA + " SET " + STATUS_UPLOAD+"='Sudah Upload' WHERE "+NO+"='"+no+"' AND "+TANGGAL+"='"+tanggal+"'" );

    }

    public ArrayList<Absensi> selectStatusBelumUpload(){
        ArrayList<Absensi> arrayBelumUpload = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + NO + ", " + TANGGAL + ", " + JAM_KEDATANGAN + " FROM " + TABEL_ABSENSI_MABA+" WHERE "+STATUS_UPLOAD+"='Belum Upload'", null);

        Absensi absensi;
        if(c.moveToFirst()) {
            do {
                absensi = new Absensi();
                absensi.setNomorPendaftaran(c.getString(0));
                absensi.setTanggal(c.getString(1));
                absensi.setWaktu(c.getString(2));


                arrayBelumUpload.add(absensi);
            } while (c.moveToNext());
        }
        return arrayBelumUpload;
    }
}
