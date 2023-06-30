package com.udemy.alisverislistesiapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ArrayList<Liste> listeArrayList;
    private ListeAdapter adapter;
    private Veritabani vt;
    private Liste liste = new Liste();
    private TextView textViewListeAd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Alışveriş Listesi");
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rv);
        vt = new Veritabani(this);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        listeArrayList = new ListeDao().tumListe(vt);

        adapter = new ListeAdapter(this, listeArrayList, vt);
        rv.setAdapter(adapter);


        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertGoster();
            }
        });

        textViewListeAd =  findViewById(R.id.textViewListeAd);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_ara);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit", query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("onQueryTextChange", newText);

        listeArrayList = new ListeDao().ListeAra(vt, newText);

        adapter = new ListeAdapter(this, listeArrayList, vt);
        rv.setAdapter(adapter);

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sil:
                alertTumunuSil();
                return true;
            case R.id.action_hakkimizda:
                alertHakkimizda();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void alertGoster() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View tasarim = layoutInflater.inflate(R.layout.alert_liste_tasarim, null); // tassrımı entegre etmek

        EditText editTextAlert = tasarim.findViewById(R.id.editTextListeAd);
        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Liste Adı");
        ad.setView(tasarim);

        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String editTextListeAd = editTextAlert.getText().toString().trim();

                new ListeDao().listeEkle(vt, editTextListeAd);
                listeArrayList = new ListeDao().tumListe(vt);
                adapter = new ListeAdapter(MainActivity.this, listeArrayList, vt);
                rv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Liste Eklendi", Toast.LENGTH_SHORT).show();
            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "İptal Edildi!", Toast.LENGTH_SHORT).show();
            }
        });

        ad.create().show();
    }

    public void alertTumunuSil() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View tasarim = layoutInflater.inflate(R.layout.alert_tumunusil_tasarim, null); // tassrımı entegre etmek

        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Listeleri Sil");
        ad.setView(tasarim);

        ad.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SQLiteDatabase db = vt.getWritableDatabase();
                db.execSQL("DELETE FROM liste");
                db.execSQL("DELETE FROM listeElemanlari");
                db.close();
                Intent i = new Intent(MainActivity.this,MainActivity.class);
                finish();
                overridePendingTransition(0, 0);
                startActivity(i);
                overridePendingTransition(0, 0);

                Toast.makeText(MainActivity.this, "Listeler Silindi!", Toast.LENGTH_SHORT).show();
            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "İptal Edildi!", Toast.LENGTH_SHORT).show();
            }
        });

        ad.create().show();
    }

    public void alertHakkimizda(){

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View tasarim = layoutInflater.inflate(R.layout.hakkimizda_tasarim, null); // tassrımı entegre etmek

        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Hakkımızda");
        ad.setView(tasarim);

        TextView textViewHakkimizda = tasarim.findViewById(R.id.textViewHakkimizda);
        textViewHakkimizda.setText("Bu uygulama Emir Petek tarafından geliştirilmiştir.\nHerhangi bir istek,öneri,şikayet için emirpetek2002@gmail.com ile\niletişime geçebilirsiniz.");
        textViewHakkimizda.setTextColor(Color.RED);
        ad.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        ad.create().show();

    }
}
