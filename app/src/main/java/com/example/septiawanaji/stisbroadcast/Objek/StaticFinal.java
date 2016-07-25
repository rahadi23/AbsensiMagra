package com.example.septiawanaji.stisbroadcast.Objek;

/**
 * Created by Septiawan Aji on 6/14/2016.
 */
public class StaticFinal {


    //user
    private static final String NOMOR_DAFTAR = "nomor_daftar";

    private static final String NAMA  = "nama";
    private static final String NO_TELP  = "no_telp";
    private static final String EMAIL  = "email";
    private static final String PASSWORD = "password";
    private static final String FIREBASE_TOKEN  = "firebase_token";
    private static final String ALAMAT_LENGKAP  = "alamat_lengkap";
    private static final String JENIS_KELAMIN  = "jenis_kelamin";
    private static final String PROVINSI  = "provinsi";
    private static final String TANGGAL_LAHIR  = "tanggal_lahir";
    private static final String PATH_FOTO  = "path_foto";

    private static final String NOMOR_PENDAFTARAN = "no_pendaftaran";




    //session manager
    private static final String SESSION  = "session";

    public static String getNIM() {
        return NOMOR_DAFTAR;
    }

    public static String getNAMA() {
        return NAMA;
    }


    public static String getPASSWORD() {
        return PASSWORD;
    }





    public static String getNomorDaftar() {
        return NOMOR_DAFTAR;
    }

    public static String getNoTelp() {
        return NO_TELP;
    }

    public static String getEMAIL() {
        return EMAIL;
    }

    public static String getFirebaseToken() {
        return FIREBASE_TOKEN;
    }

    public static String getAlamatLengkap() {
        return ALAMAT_LENGKAP;
    }

    public static String getJenisKelamin() {
        return JENIS_KELAMIN;
    }


    public static String getPROVINSI() {
        return PROVINSI;
    }

    public static String getTanggalLahir() {
        return TANGGAL_LAHIR;
    }

    public static String getSESSION() {
        return SESSION;
    }

    public static String getPathFoto() {
        return PATH_FOTO;
    }


    public static String getNomorPendaftaran() {
        return NOMOR_PENDAFTARAN;
    }
}
