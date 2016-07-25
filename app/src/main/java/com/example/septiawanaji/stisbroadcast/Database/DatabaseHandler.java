package com.example.septiawanaji.stisbroadcast.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Septiawan Aji on 7/20/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABSE_VERSION = 1;
    private static final String DATABASE_NAME = "maba_db";

    private static final String TABEL_MABA = "tabel_maba";
    private static final String TABEL_ABSENSI_MABA= "tabel_absensi_maba";

    private static final String NAMA = "nama";
    private static final String NO = "no";
    private static final String TANGGAL = "tanggal";
    private static final String JAM_KEDATANGAN = "jam_kedatangan";
    private static final String ID = "id";

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
                +NAMA+" TEXT)";
        db.execSQL(CREATE_MABA_TABLE);
        String CREATE_ABSENSI_TABLE = "CREATE TABLE "+TABEL_ABSENSI_MABA +" ("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +NO+" TEXT,"
                +NAMA+" TEXT,"
                +TANGGAL+" TEXT,"
                +JAM_KEDATANGAN+" TEXT)";
        db.execSQL(CREATE_ABSENSI_TABLE);
    }

    public void insertRow(String no,String nama){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NO,no);
        values.put(NAMA,nama);

        db.insert(TABEL_MABA, null, values);
        Log.d("Insert DB", db.toString());
        db.close();
    }

    public String selectRow(String no){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c  = db.rawQuery("SELECT " + NAMA + " FROM " + TABEL_MABA + " WHERE " + NO + "='" + no+"'", null);

        String namaMaba="";
        if(c!=null && c.moveToFirst()){
            namaMaba=c.getString(0);
        }else{
            namaMaba="";
        }
        Log.d("namaMaba", namaMaba);
        c.close();
        return namaMaba;
    }

    public void deleteAllMaba(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABEL_MABA, null, null);
    }

    public String cekRowSize(){
        String kosong="";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c  = db.rawQuery("SELECT " + NAMA + " FROM " + TABEL_MABA , null);

        String namaMaba="";
        if(c!=null && c.moveToFirst()){
            kosong="ada";
        }
        return kosong;
    }

    public void insertAbsensi(String no,String nama,String tanggal,String jamKedatangan){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NO,no);
        values.put(NAMA,nama);
        values.put(TANGGAL,tanggal);
        values.put(JAM_KEDATANGAN,jamKedatangan);

        db.insert(TABEL_ABSENSI_MABA, null, values);
        db.close();
    }

    public String selectWaktu(String no){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c  = db.rawQuery("SELECT " + JAM_KEDATANGAN + " FROM " + TABEL_ABSENSI_MABA + " WHERE " + NO + "=" + no, null);

        String jamKedatangan="";
        if(c.moveToFirst()){
            jamKedatangan=c.getString(0);
        }
        c.close();
        return jamKedatangan;
    }
}
