package jvpg.cgodin.qc.ca.projetpldl.entities;

import java.util.Date;

/**
 * Created by jodel on 2017-12-06.
 */

public class ListeDeLecture {

    /*this.id = id;
        this.proprietaire = proprietaire;
        this.nom = nom;
        this.publique = publique;
        this.active = active;
        this.date = date;*/

    int id;
    int proprietaire;
    String nom;
    boolean publique;
    boolean active;
    Date date;

    public ListeDeLecture() {
    }

    public ListeDeLecture(int id, int proprietaire, String nom, boolean publique, boolean active, Date date) {
        this.id = id;
        this.proprietaire = proprietaire;
        this.nom = nom;
        this.publique = publique;
        this.active = active;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(int proprietaire) {
        this.proprietaire = proprietaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isPublique() {
        return publique;
    }

    public void setPublique(boolean publique) {
        this.publique = publique;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ListeDeLecture{" +
                "id=" + id +
                ", proprietaire=" + proprietaire +
                ", nom='" + nom + '\'' +
                ", publique=" + publique +
                ", active=" + active +
                ", date=" + date +
                '}';
    }
}
