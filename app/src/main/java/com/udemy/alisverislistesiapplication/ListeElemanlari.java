package com.udemy.alisverislistesiapplication;

public class ListeElemanlari {

    private int eleman_id;
    private String eleman_ad;
    private int liste_id,eleman_checked;

    public ListeElemanlari(int eleman_id, int eleman_checked) {
        this.eleman_id = eleman_id;
        this.eleman_checked = eleman_checked;
    }

    public int eleman_checked() {
        return eleman_checked;
    }

    public ListeElemanlari setEleman_checked(int eleman_checked) {
        this.eleman_checked = eleman_checked;
        return this;
    }

    public ListeElemanlari() {
    }

    public ListeElemanlari(int eleman_id, String eleman_ad,int liste_id,int eleman_checked) {
        this.eleman_id = eleman_id;
        this.eleman_ad = eleman_ad;
        this.liste_id = liste_id;
        this.eleman_checked = eleman_checked;

    }

    public ListeElemanlari(int eleman_id, String eleman_ad,int liste_id) {
        this.eleman_id = eleman_id;
        this.eleman_ad = eleman_ad;
        this.liste_id = liste_id;

    }

    public int eleman_id() {
        return eleman_id;
    }

    public ListeElemanlari setEleman_id(int eleman_id) {
        this.eleman_id = eleman_id;
        return this;
    }

    public String eleman_ad() {
        return eleman_ad;
    }

    public ListeElemanlari setEleman_ad(String eleman_ad) {
        this.eleman_ad = eleman_ad;
        return this;
    }
}
