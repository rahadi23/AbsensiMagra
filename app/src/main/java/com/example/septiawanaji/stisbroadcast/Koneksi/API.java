package com.example.septiawanaji.stisbroadcast.Koneksi;

/**
 * Created by Septiawan Aji on 6/27/2016.
 */
public class API {

    private static final String ALAMAT_URL = "http://192.168.43.144/rpl/koneksi.php?";
    private static final String ATRIBUTE_SERVER = "a";
    private static final String GET_REGISTER_STATUS = "getRegisterStatus";
    private static final String GET_STATUS_LOGIN = "getStatusLogin";
    private static final String GET_DATA_LOGIN = "getDataLogin";
    private static final String GET_LIST_MABA = "getListMaba";

    //server
    private static final String RESPON = "respon";
    private static final String STATUS_DAFTAR = "status_daftar";
    private static final String NOL = "0";
    private  static final String SATU = "1";
    private static final String POST = "post";
    private static final String GET = "get";



    public static String getAlamatUrl() {
        return ALAMAT_URL;
    }

    public static String getAtributeServer() {
        return ATRIBUTE_SERVER;
    }

    public static String getGetRegisterStatus() {
        return GET_REGISTER_STATUS;
    }

    public static String getGetStatusLogin() {
        return GET_STATUS_LOGIN;
    }

    public static String getGetDataLogin() {
        return GET_DATA_LOGIN;
    }

    public static String getRESPON() {
        return RESPON;
    }

    public static String getSTATUS() {
        return getStatusDaftar();
    }

    public static String getNOL() {
        return NOL;
    }

    public static String getSATU() {
        return SATU;
    }

    public static String getPOST() {
        return POST;
    }

    public static String getGET() {
        return GET;
    }

    public static String getGetListMaba() {
        return GET_LIST_MABA;
    }

    public static String getStatusDaftar() {
        return STATUS_DAFTAR;
    }
}
