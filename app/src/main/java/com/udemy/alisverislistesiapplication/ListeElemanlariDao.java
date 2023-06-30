package com.udemy.alisverislistesiapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class ListeElemanlariDao {

    private SharedPreferences sp;
    private SharedPreferences.Editor e;

    int liste_id_disaridan;
    public ArrayList<ListeElemanlari> tumElemanlar(Veritabani vt){
        ArrayList<ListeElemanlari> listeElemanlariArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM listeElemanlari WHERE liste_id = " + ListeAdapter.liste_id_getir ,null);

        Log.e("liste_id dao ekranından",String.valueOf( ListeAdapter.liste_id_getir));

        while (c.moveToNext()){
            @SuppressLint("Range") ListeElemanlari l = new ListeElemanlari(c.getInt(c.getColumnIndex("eleman_id")),c.getString(c.getColumnIndex("eleman_ad"))
            ,c.getInt(c.getColumnIndex("liste_id")),c.getInt(c.getColumnIndex("eleman_checked")));
            listeElemanlariArrayList.add(l);
        }
        db.close();
        return listeElemanlariArrayList;
    }

    public ArrayList<ListeElemanlari> elemanAra(Veritabani vt,String aramaKelime){
        ArrayList<ListeElemanlari> listeElemanlariArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM listeElemanlari WHERE eleman_ad like '%"+aramaKelime+"%' AND liste_id = " + ListeAdapter.liste_id_getir,null);

        while (c.moveToNext()){
            @SuppressLint("Range") ListeElemanlari l = new ListeElemanlari(c.getInt(c.getColumnIndex("eleman_id")),c.getString(c.getColumnIndex("eleman_ad"))
                    ,c.getInt(c.getColumnIndex("liste_id")),c.getInt(c.getColumnIndex("eleman_checked")));
            listeElemanlariArrayList.add(l);
        }
        db.close();
        return listeElemanlariArrayList;
    }

    public void elemanSil(Veritabani vt, int eleman_id){
        SQLiteDatabase db = vt.getWritableDatabase();
        db.delete("listeElemanlari","eleman_id=?", new String[]{String.valueOf(eleman_id)});
        db.close();
    }
    public void elemanEkle(Veritabani vt, String eleman_ad,int liste_id,int eleman_checked){
        SQLiteDatabase db = vt.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("eleman_ad",eleman_ad);
        values.put("liste_id",ListeAdapter.liste_id_getir);
        values.put("eleman_checked",eleman_checked);

        db.insertOrThrow("listeElemanlari",null,values);
        db.close();
    }
    public void elemanGuncelle(Veritabani vt, int eleman_id,String eleman_ad){
        SQLiteDatabase db = vt.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("eleman_ad",eleman_ad);
        db.update("listeElemanlari",values,"eleman_id=?",new String[]{String.valueOf(eleman_id)});
        db.close();
    }

    public void elemanHepsiniSil(Veritabani vt){
        SQLiteDatabase db = vt.getWritableDatabase();
        db.execSQL("DELETE FROM " + "listeElemanlari" );

    }

    public ArrayList<ListeElemanlari> elemanCheckedTumu(Veritabani vt){
        ArrayList<ListeElemanlari> listeElemanlariArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM listeElemanlari where eleman_id = " + new ListeElemanlari().eleman_id(),null);

        while (c.moveToNext()){
            @SuppressLint("Range") ListeElemanlari l = new ListeElemanlari(c.getInt(c.getColumnIndex("eleman_id")),c.getInt(c.getColumnIndex("eleman_checked")));
            listeElemanlariArrayList.add(l);
        }
        db.close();
        return listeElemanlariArrayList;
    }

    public void elemanCheckedGuncelle(Veritabani vt, int eleman_id,int eleman_checked){
        SQLiteDatabase db = vt.getWritableDatabase();

        db.execSQL("UPDATE listeElemanlari SET eleman_checked =" + String.valueOf(eleman_checked) + " WHERE eleman_id = "+ String.valueOf(eleman_id));
        db.close();
    }
}
