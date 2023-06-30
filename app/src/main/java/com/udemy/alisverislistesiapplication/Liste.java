package com.udemy.alisverislistesiapplication;

import java.io.Serializable;

public class Liste {
    private int liste_id;
    private String liste_ad;

    public Liste() {
    }

    public Liste(int liste_id, String liste_ad) {
        this.liste_id = liste_id;
        this.liste_ad = liste_ad;
    }

    public int getListe_id() {
        return liste_id;
    }

    public Liste setListe_id(int liste_id) {
        this.liste_id = liste_id;
        return this;
    }

    public String getListe_ad() {
        return liste_ad;
    }

    public Liste setListe_ad(String liste_ad) {
        this.liste_ad = liste_ad;
        return this;
    }
}
