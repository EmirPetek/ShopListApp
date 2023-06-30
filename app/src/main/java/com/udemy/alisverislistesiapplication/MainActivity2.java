package com.udemy.alisverislistesiapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar2;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private ArrayList<ListeElemanlari> listeElemanlariArrayList ;
    private ListeElemanlariAdapter adapter;
    private Veritabani vt;
    private Liste liste = new Liste();
    public int liste_id_getir;
    int listegetirm2 = ListeAdapter.liste_id_getir ;
    private CheckBox checkBox;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setTitle("Alışveriş Listesi");
        setSupportActionBar(toolbar2);

        rv = findViewById(R.id.rv);
        vt = new Veritabani(this);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        listeElemanlariArrayList = new ListeElemanlariDao().tumElemanlar(vt);

        adapter = new ListeElemanlariAdapter(this, listeElemanlariArrayList, vt);
        rv.setAdapter(adapter);

        fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertGoster();
            }
        });

        liste_id_getir = getIntent().getIntExtra("liste_id",0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu_eleman, menu);

        MenuItem menuItem = menu.findItem(R.id.action_arama);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        listeElemanlariArrayList = new ListeElemanlariDao().elemanAra(vt,newText);

        adapter = new ListeElemanlariAdapter(this, listeElemanlariArrayList, vt);
        rv.setAdapter(adapter);

        return false;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_hepsini_sil:
                hepsiniSil();
                return true;
            case R.id.action_hepsini_isaretle:
                hepsiniIsaretle();
                return true;
            case R.id.action_hepsini_birak:
                hepsiniBirak();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void alertGoster() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View tasarim = layoutInflater.inflate(R.layout.alert_eleman_tasarim, null); // tassrımı entegre etmek

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) EditText editTextElemanAdi = tasarim.findViewById(R.id.editTextElemanAdi);
        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Eleman Adı");
        ad.setView(tasarim);

        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String editTextListeAd = editTextElemanAdi.getText().toString().trim();

                new ListeElemanlariDao().elemanEkle(vt,editTextListeAd,liste_id_getir,0);
                listeElemanlariArrayList = new ListeElemanlariDao().tumElemanlar(vt);
                adapter = new ListeElemanlariAdapter(MainActivity2.this, listeElemanlariArrayList, vt);
                rv.setAdapter(adapter);
                Log.e("main a.2 liste id", String.valueOf(listegetirm2));
                Toast.makeText(MainActivity2.this, "Eleman Eklendi", Toast.LENGTH_SHORT).show();
            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity2.this, "İptal Edildi!", Toast.LENGTH_SHORT).show();
            }
        });

        ad.create().show();
    }

    public void hepsiniSil(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View tasarim = layoutInflater.inflate(R.layout.alert_tumunusil_eleman_tasarim, null); // tassrımı entegre etmek

        AlertDialog.Builder ad = new AlertDialog.Builder(this);

        ad.setTitle("Tüm elemanları sil");
        ad.setView(tasarim);

        ad.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SQLiteDatabase db = vt.getWritableDatabase();
                db.execSQL("DELETE FROM listeElemanlari WHERE liste_id = " + ListeAdapter.liste_id_getir );
                db.close();
                finish();
                Toast.makeText(MainActivity2.this, "Tüm elemanlar silindi!", Toast.LENGTH_SHORT).show();
            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity2.this, "İptal Edildi!", Toast.LENGTH_SHORT).show();
            }
        });

        ad.create().show();

    }

    @SuppressLint("Range")
    public void hepsiniIsaretle(){
        // seçili olma durumunu 1 yapar
        SQLiteDatabase db = vt.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eleman_checked",1);
        db.update("listeElemanlari",values,"liste_id=?",new String[]{String.valueOf(ListeAdapter.liste_id_getir)});
        db.close();
        // sayfayı refresh eder. overrideli olan şey animasyon olmadan yenilenmesini sağlıyor.
        Intent i = new Intent(MainActivity2.this,MainActivity2.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);
        Toast.makeText(MainActivity2.this, "Tüm elemanlar seçildi!", Toast.LENGTH_SHORT).show();

    }

    public void hepsiniBirak(){
        // seçili olma durumunu 0 yapar
        SQLiteDatabase db = vt.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eleman_checked",0);
        db.update("listeElemanlari",values,"liste_id=?",new String[]{String.valueOf(ListeAdapter.liste_id_getir)});
        db.close();
        // sayfayı refresh eder. overrideli olan şey animasyon olmadan yenilenmesini sağlıyor.
        Intent i = new Intent(MainActivity2.this,MainActivity2.class);
        finish();
        overridePendingTransition(0, 0);
        startActivity(i);
        overridePendingTransition(0, 0);
        Toast.makeText(MainActivity2.this, "Tüm seçimler kaldırıldı!", Toast.LENGTH_SHORT).show();
    }
}
