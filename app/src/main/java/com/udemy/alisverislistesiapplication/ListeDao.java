package com.udemy.alisverislistesiapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ListeDao {

    public ArrayList<Liste> tumListe(Veritabani vt){
        ArrayList<Liste> listeArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM liste",null);

        while (c.moveToNext()){
            @SuppressLint("Range") Liste l = new Liste(c.getInt(c.getColumnIndex("liste_id")),c.getString(c.getColumnIndex("liste_ad")));
            listeArrayList.add(l);
        }
            db.close();
        return listeArrayList;
    }

    public ArrayList<Liste> ListeAra(Veritabani vt,String aramaKelime){
        ArrayList<Liste> listeArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM liste WHERE liste_ad like '%"+aramaKelime+"%'",null);

        while (c.moveToNext()){
            @SuppressLint("Range") Liste l = new Liste(c.getInt(c.getColumnIndex("liste_id")),c.getString(c.getColumnIndex("liste_ad")));
            listeArrayList.add(l);
        }
        db.close();
        return listeArrayList;
    }

    public void listeSil(Veritabani vt, int liste_id){
        SQLiteDatabase db = vt.getWritableDatabase();
        db.delete("liste","liste_id=?", new String[]{String.valueOf(liste_id)});
    }
    public void listeEkle(Veritabani vt, String liste_ad){
        SQLiteDatabase db = vt.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("liste_ad",liste_ad);
        db.insertOrThrow("liste",null,values);
        db.close();
    }
    public void listeGuncelle(Veritabani vt, int liste_id,String liste_ad){
        SQLiteDatabase db = vt.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("liste_ad",liste_ad);
        db.update("liste",values,"liste_id=?",new String[]{String.valueOf(liste_id)});
        db.close();
    }

    public void listeHepsiniSil(Veritabani vt){
        SQLiteDatabase db = vt.getWritableDatabase();
        db.execSQL("DELETE FROM " + "liste" );
    }
}
