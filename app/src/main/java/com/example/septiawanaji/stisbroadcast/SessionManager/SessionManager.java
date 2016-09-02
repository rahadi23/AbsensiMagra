package com.example.septiawanaji.stisbroadcast.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.septiawanaji.stisbroadcast.Objek.AtributName;

import java.util.HashMap;

/**
 * Created by Septiawan Aji on 6/17/2016.
 */
public class SessionManager {

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SessionManager(Context context){
        this.context  = context;
        sharedPreferences = context.getSharedPreferences(AtributName.getSESSION(),Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createSessionLogin (String nim,String nama){
        editor.putString(AtributName.getNIM(),nim);
       editor.putString(AtributName.getNAMA(),nama);
        editor.commit();
    }

    public void saveUrlServer(String url){
        editor.putString(AtributName.getAlamatServer(),"http://"+url+"/absensi/");
        editor.commit();
    }

    public void createUploadSession(){
        editor.putString("upload","yes");
        editor.commit();
    }

    public String getUploadSession(){
        return sharedPreferences.getString("upload",null);
    }

    public void deleteSession (){
        sharedPreferences.edit().clear().commit();
    }

    public HashMap<String,String> getUserSession(){
        HashMap<String,String> hm = new HashMap<>();
        hm.put(AtributName.getNIM(),sharedPreferences.getString(AtributName.getNIM(),null));
        hm.put(AtributName.getNAMA(), sharedPreferences.getString(AtributName.getNAMA(), null));
        return hm;
    }

    public String getAlamatServer(){
        return sharedPreferences.getString(AtributName.getAlamatServer(),null);
    }
}
