package com.udemy.alisverislistesiapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ListeAdapter extends RecyclerView.Adapter<ListeAdapter.CardTasarimTutucu> {

    private Context mContext;
    private List<Liste> listeList;
    private Veritabani vt;
    public static int liste_id_getir; // static yazdım çünkü baska classtan bunun valuesine erişemiyordum.


    public ListeAdapter(Context mContext, List<Liste> listeList, Veritabani vt) {
        this.mContext = mContext;
        this.listeList = listeList;
        this.vt = vt;
    }

    public ListeAdapter() {
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim, parent, false);
        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {
        final Liste liste = listeList.get(position);

        liste_id_getir = liste.getListe_id();

        SharedPreferences sp = mContext.getSharedPreferences("listeid",Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        holder.textViewListeAd.setText(liste.getListe_ad());

        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_sil:
                                Snackbar.make(holder.imageViewNokta, "Liste Silinsin mi ?"
                                        , Snackbar.LENGTH_SHORT).setAction("Evet", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        new ListeDao().listeSil(vt,liste.getListe_id());
                                        listeList = new ListeDao().tumListe(vt);
                                        notifyDataSetChanged();
                                        Toast.makeText(mContext, "Liste silindi!", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                                return true;
                            case R.id.action_guncelle:
                                alertGoster(liste);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        holder.listeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("liste card id: ", String.valueOf(liste.getListe_id()));
                int liste_id = liste.getListe_id();
                liste_id_getir = liste_id;
                Log.e("liste id getir ", String.valueOf(liste_id_getir));
                new ListeElemanlariDao().liste_id_disaridan = liste_id;
                liste_id_getir = liste_id;

                Intent intent = new Intent(mContext,MainActivity2.class);
                intent.putExtra("liste_id",liste_id);
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return listeList.size();
    }

    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        private TextView textViewListeAd;
        private ImageView imageViewNokta;
        private CardView listeCardView;
        private CheckBox checkBox;

        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);

            textViewListeAd = itemView.findViewById(R.id.textViewListeAd);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);
            listeCardView = itemView.findViewById(R.id.listeCardView);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBoxEleman);
        }
    }

    public void alertGoster(Liste liste) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View tasarim = layoutInflater.inflate(R.layout.alert_liste_tasarim, null); // tassrımı entegre etmek

        EditText editTextAlert = tasarim.findViewById(R.id.editTextListeAd);
        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);

        editTextAlert.setText(liste.getListe_ad());

        ad.setTitle("Liste Adı Güncelleme");
        ad.setView(tasarim);

        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editTextVeri = editTextAlert.getText().toString().trim();

                new ListeDao().listeGuncelle(vt,liste.getListe_id(),editTextVeri);
                Log.e("liste id",String.valueOf(liste.getListe_id()));
                listeList= new ListeDao().tumListe(vt);
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
}
