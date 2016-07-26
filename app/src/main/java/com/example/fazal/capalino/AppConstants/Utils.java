package com.example.fazal.capalino.AppConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by asif on 2/24/2016.
 */
public class Utils {
    static Context context;
    public static final String TAG = "Utils";
    public static final String UserName = "UserName";
    public SharedPreferences preferences;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    SharedPreferences.Editor editor;
    public Utils(Context context) {

        Utils.context = context;
        preferences = context.getSharedPreferences("gcmdemo", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void savedata(String key,String val){
        editor.putString(key,val).commit();
    }

    public boolean saveArray(ArrayList<Integer> sKey)
    {

    /* sKey is an array */
        editor.putInt("Status_size", sKey.size());

        for(int i=0;i<sKey.size();i++)
        {
            editor.remove("Status_" + i);
            editor.putString("Status_" + i, String.valueOf(sKey.get(i)));
        }

        return editor.commit();
    }

    public ArrayList<Integer> loadArray(Context mContext)
    {
        ArrayList<Integer> list = new ArrayList<>();
        list.clear();
        int size = preferences.getInt("Status_size", 0);

        for(int i=0;i<size;i++)
        {
            list.add(Integer.valueOf(preferences.getString("Status_" + i, null)));
        }

        return list;
    }


    public String getdata(String key){
        String value = preferences.getString(key, "");
        if (value.isEmpty()) {
            Log.i(TAG, key + " not found.");
            return "";
        }
        return value;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {

            networkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!networkInfo.isAvailable()) {
                networkInfo = connectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            }
        }
        return networkInfo == null ? false : networkInfo.isConnected();
    }

    private static String LIST_SEPARATOR = ",";

    public static String convertListToString(Integer[] intList) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Integer str : intList) {
            stringBuffer.append(String.valueOf(str)).append(LIST_SEPARATOR);
        }

        // Remove last separator
        int lastIndex = stringBuffer.lastIndexOf(LIST_SEPARATOR);
        stringBuffer.delete(lastIndex, lastIndex + LIST_SEPARATOR.length() + 1);

        return stringBuffer.toString();
    }

    public static List<String> convertStringToList(String str) {
        return Arrays.asList(String.valueOf(str).split(LIST_SEPARATOR));
    }


}
