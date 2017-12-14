package jvpg.cgodin.qc.ca.projetpldl.entities;

import java.util.Date;

/**
 * Created by jodel on 2017-12-03.
 */

public class Musique {

    int id;
    int proprietaire;
    String titre;
    String artiste;
    String musique;
    String vignette;
    boolean publique;
    boolean active;
    Date date;

    public Musique(int id, int proprietaire, String titre, String artiste, String musique, String vignette, boolean publique, boolean active, Date date) {
        this.id = id;
        this.proprietaire = proprietaire;
        this.titre = titre;
        this.artiste = artiste;
        this.musique = musique;
        this.vignette = vignette;
        this.publique = publique;
        this.active = active;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getProprietaire() {
        return proprietaire;
    }

    public String getTitre() {
        return titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public String getMusique() {
        return musique;
    }

    public String getVignette() {
        return vignette;
    }

    public boolean isPublique() {
        return publique;
    }

    public boolean isActive() {
        return active;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Musique{" +
                "id=" + id +
                ", proprietaire=" + proprietaire +
                ", titre='" + titre + '\'' +
                ", artiste='" + artiste + '\'' +
                ", musique='" + musique + '\'' +
                ", vignette='" + vignette + '\'' +
                ", publique=" + publique +
                ", active=" + active +
                ", date=" + date +
                '}';
    }

    public Musique() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProprietaire(int proprietaire) {
        this.proprietaire = proprietaire;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public void setMusique(String musique) {
        this.musique = musique;
    }

    public void setVignette(String vignette) {
        this.vignette = vignette;
    }

    public void setPublique(boolean publique) {
        this.publique = publique;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
