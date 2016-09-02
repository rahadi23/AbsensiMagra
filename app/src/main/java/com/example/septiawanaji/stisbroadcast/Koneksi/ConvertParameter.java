package com.example.septiawanaji.stisbroadcast.Koneksi;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Septiawan Aji on 6/18/2016.
 */
public class ConvertParameter {
    public static String getQuery(HashMap<String ,String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for(Map.Entry<String ,String > entry : params.entrySet()){
            if(first)
                first = false;
            else
                result.append("/");
//            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
//            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        Log.d("QUERY ", result.toString());
        return result.toString();
    }
}
