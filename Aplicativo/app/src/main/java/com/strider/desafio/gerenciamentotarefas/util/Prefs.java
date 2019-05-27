package com.strider.desafio.gerenciamentotarefas.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.IntegerRes;

/**
 * Created by amanda on 12/11/16.
 */

public class Prefs {
    public static final String PREF_ID = "gerenciamentotarefas";

    public static void setBoolean(Context context, String chave, boolean on){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(chave,on);
        editor.commit();
    }

    public static boolean getBoolean(Context context,String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        boolean b = pref.getBoolean(chave,true);
        return b;
    }

    public static void setInteger(Context context,String chave,int valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(chave,valor);
        editor.commit();
    }

    public static Integer getInteger(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        int i = pref.getInt(chave,0);
        return i;
    }

    public static void setFloat(Context context,String chave,float valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putFloat(chave,valor);
        editor.commit();
    }

    public static Float getFloat(Context context, String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        float i = pref.getFloat(chave,0);
        return i;
    }

    public static void setString(Context context,String chave,String valor){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(chave,valor);
        editor.commit();
    }

    public static String getString(Context context,String chave){
        SharedPreferences pref = context.getSharedPreferences(PREF_ID,0);
        String s = pref.getString(chave,"");
        return s;
    }
}
