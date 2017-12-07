package jvpg.cgodin.qc.ca.projetpldl.entities;

import java.util.Date;

/**
 * Created by jodel on 2017-12-06.
 */

public class Utilisateur {

    /*this.id = id;
        this.courriel = courriel;
        this.motDePasse = motDePasse;
        this.alias = alias;
        this.avatar = avatar;
        this.actif = actif;
        this.date = date;
        */

    int id;
    String courriel;
    String motDePasse;
    String alias;
    String avatar;
    String actif;
    Date date;

    public Utilisateur() {

    }

    public Utilisateur(int id, String courriel, String motDePasse, String alias, String avatar, String actif, Date date) {
        this.id = id;
        this.courriel = courriel;
        this.motDePasse = motDePasse;
        this.alias = alias;
        this.avatar = avatar;
        this.actif = actif;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourriel() {
        return courriel;
    }

    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getActif() {
        return actif;
    }

    public void setActif(String actif) {
        this.actif = actif;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
