package com.udemy.alisverislistesiapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Veritabani extends SQLiteOpenHelper {

    public Veritabani(@Nullable Context context) {
        super(context, "liste.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE liste (liste_id INTEGER PRIMARY KEY AUTOINCREMENT, liste_ad TEXT);");
        db.execSQL("CREATE TABLE listeElemanlari (eleman_id INTEGER PRIMARY KEY AUTOINCREMENT, eleman_ad TEXT," +
                "liste_id INTEGER,eleman_checked INTEGER,FOREIGN KEY(liste_id) REFERENCES liste(liste_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS liste");
        db.execSQL("DROP TABLE IF EXISTS listeElemanlari");

        onCreate(db);
    }
}
