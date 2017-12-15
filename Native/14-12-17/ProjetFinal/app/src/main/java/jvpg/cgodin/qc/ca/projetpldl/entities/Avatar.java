package jvpg.cgodin.qc.ca.projetpldl.entities;

/**
 * Created by jodel on 2017-12-15.
 */

public class Avatar {
    String avatar;
    String nom;
    int id;

    public Avatar(String avatar, String nom) {
        this.avatar = avatar;
        this.nom = nom;
    }

    public Avatar() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
