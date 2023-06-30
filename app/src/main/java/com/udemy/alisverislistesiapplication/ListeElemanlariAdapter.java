package com.udemy.alisverislistesiapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListeElemanlariAdapter extends RecyclerView.Adapter<ListeElemanlariAdapter.CardTasarimTutucu> {

    private Context mContext;
    private List<ListeElemanlari> listeElemanlariList;
    private Veritabani vt;
    public static int eleman_position;
    private ArrayList<ListeElemanlari> listeElemanlariArrayList ;
    private ListeElemanlariAdapter adapter;



    public ListeElemanlariAdapter(Context mContext, List<ListeElemanlari> listeElemanlariList, Veritabani vt) {
        this.mContext = mContext;
        this.listeElemanlariList = listeElemanlariList;
        this.vt = vt;
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        public ImageView imageViewNokta2;
        public CheckBox checkBox;
        public TextView textViewElemanAd;


        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);
            this.imageViewNokta2 = imageViewNokta2;
            this.checkBox = checkBox;
            textViewElemanAd = itemView.findViewById(R.id.textViewElemanAd);
            imageViewNokta2 = itemView.findViewById(R.id.imageViewNokta2);
            checkBox = itemView.findViewById(R.id.checkBoxEleman);
        }
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_eleman_tasarim, parent, false);
        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, @SuppressLint("RecyclerView") int position) {
        final ListeElemanlari listeElemanlari = listeElemanlariList.get(position);
            eleman_position = position; // positiona suppresslint eklendi
           holder.textViewElemanAd.setText(listeElemanlari.eleman_ad());

           holder.imageViewNokta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(mContext, holder.imageViewNokta2);
                    popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_sil:
                                    Snackbar.make(holder.imageViewNokta2, "Eleman Silinsin mi ?"
                                            , Snackbar.LENGTH_SHORT).setAction("Evet", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new ListeElemanlariDao().elemanSil(vt, listeElemanlari.eleman_id());
                                            listeElemanlariList = new ListeElemanlariDao().tumElemanlar(vt);
                                            notifyDataSetChanged();
                                            Log.e("eleman id",String.valueOf(listeElemanlari.eleman_id()));
                                            //Log.e("liste id eleman page: ", String.valueOf(new MainActivity2().liste_id_getir));
                                            Toast.makeText(mContext, "Eleman silindi!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                                    return true;
                                case R.id.action_guncelle:
                                    alertGoster(listeElemanlari);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });

           holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @SuppressLint("Range")
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if (isChecked) {

                       Log.e("cb state", "tıklandı");
                       new ListeElemanlariDao().elemanCheckedGuncelle(vt, listeElemanlari.eleman_id(), 1);
                       listeElemanlariArrayList = new ListeElemanlariDao().elemanCheckedTumu(vt);
                       //notifyDataSetChanged();
                       SQLiteDatabase db = vt.getWritableDatabase();
                       Cursor c = db.rawQuery("SELECT * FROM listeElemanlari WHERE liste_id = " + ListeAdapter.liste_id_getir,null);

                       while (c.moveToNext()){
                           int e_id = c.getInt(c.getColumnIndex("eleman_id"));
                           int e_chk = c.getInt(c.getColumnIndex("eleman_checked"));
                           Log.e("id and checked lea", String.valueOf(e_id + " " + e_chk));
                       }

                   }else {
                       new ListeElemanlariDao().elemanCheckedGuncelle(vt, listeElemanlari.eleman_id(), 0);
                       listeElemanlariArrayList = new ListeElemanlariDao().elemanCheckedTumu(vt);
                       //notifyDataSetChanged();
                       SQLiteDatabase db = vt.getWritableDatabase();
                       Cursor c = db.rawQuery("SELECT * FROM listeElemanlari WHERE liste_id = " + ListeAdapter.liste_id_getir,null);
                       while (c.moveToNext()){
                           int e_id = c.getInt(c.getColumnIndex("eleman_id"));
                           int e_chk = c.getInt(c.getColumnIndex("eleman_checked"));
                           Log.e("id and checked lea else", String.valueOf(e_id + " " + e_chk));
                       }
                   }
               }
           });

        {
            SQLiteDatabase db = vt.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM listeElemanlari ", null);
            while (c.moveToNext()) {
                @SuppressLint("Range") int e_id = c.getInt(c.getColumnIndex("eleman_id"));
                @SuppressLint("Range") int e_chk = c.getInt(c.getColumnIndex("eleman_checked"));
                Log.e("id and checked lea", String.valueOf(e_id + " " + e_chk));
                if (e_id == listeElemanlari.eleman_id() && e_chk == 1) {
                    holder.checkBox.setChecked(true);
                    // holder.textViewElemanAd.setTextColor(Color.RED);


                }
                if (e_id == listeElemanlari.eleman_id() && e_chk == 0) {
                    holder.checkBox.setChecked(false);
                    //holder.textViewElemanAd.setTextColor(Color.BLACK);

                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return listeElemanlariList.size();
    }

    public void alertGoster(ListeElemanlari listeElemanlari) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View tasarim = layoutInflater.inflate(R.layout.alert_liste_tasarim, null); // tassrımı entegre etmek

        EditText editTextAlert = tasarim.findViewById(R.id.editTextListeAd);
        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);

        editTextAlert.setText(listeElemanlari.eleman_ad());

        ad.setTitle("Eleman Adı Güncelleme");
        ad.setView(tasarim);

        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editTextVeri = editTextAlert.getText().toString().trim();

                new ListeElemanlariDao().elemanGuncelle(vt, listeElemanlari.eleman_id(), editTextVeri);
                listeElemanlariList = new ListeElemanlariDao().tumElemanlar(vt);
                notifyDataSetChanged();


                Toast.makeText(mContext, "İsim Değiştirildi", Toast.LENGTH_SHORT).show();
            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "İptal Edildi!", Toast.LENGTH_SHORT).show();
            }
        });

        ad.create().show();

    }

    public void checkboxStartState(){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View tasarim = layoutInflater.inflate(R.layout.card_eleman_tasarim, null); // tassrımı entegre etmek
        CheckBox checkBox = tasarim.findViewById(R.id.checkBoxEleman);
    }

}
