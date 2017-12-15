package jvpg.cgodin.qc.ca.projetpldl.entities;

/**
 * Created by jodel on 2017-12-06.
 */

public class SearchBarItem {




    String titre;
    String musique;
    String vignette;
    String vignetteEncoded;

    public SearchBarItem() {
    }

    public SearchBarItem(String titre, String musique, String vignette, String vignetteEncoded) {
        this.titre = titre;
        this.musique = musique;
        this.vignette = vignette;
        this.vignetteEncoded = vignetteEncoded;
    }

    public String getVignetteEncoded() {
        return vignetteEncoded;
    }

    public void setVignetteEncoded(String vignetteEncoded) {
        this.vignetteEncoded = vignetteEncoded;
    }
    public String getTitre() {
        return titre;
    }

    public String getMusique() {
        return musique;
    }

    public String getVignette() {
        return vignette;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setMusique(String musique) {
        this.musique = musique;
    }

    public void setVignette(String vignette) {
        this.vignette = vignette;
    }

}