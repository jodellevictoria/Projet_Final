package jvpg.cgodin.qc.ca.projetpldl.entities;

/**
 * Created by jodel on 2017-12-06.
 */

public class SearchBarItem {




    String titre;
    String musique;
    String vignette;

    public SearchBarItem() {
    }

    public SearchBarItem(String titre, String musique, String vignette) {
        this.titre = titre;
        this.musique = musique;
        this.vignette = vignette;
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
