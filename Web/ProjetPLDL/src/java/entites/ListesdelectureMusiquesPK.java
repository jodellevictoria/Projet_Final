/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jodel
 */
@Embeddable
public class ListesdelectureMusiquesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ListeDeLecture")
    private int listeDeLecture;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Musique")
    private int musique;

    public ListesdelectureMusiquesPK() {
    }

    public ListesdelectureMusiquesPK(int listeDeLecture, int musique) {
        this.listeDeLecture = listeDeLecture;
        this.musique = musique;
    }

    public int getListeDeLecture() {
        return listeDeLecture;
    }

    public void setListeDeLecture(int listeDeLecture) {
        this.listeDeLecture = listeDeLecture;
    }

    public int getMusique() {
        return musique;
    }

    public void setMusique(int musique) {
        this.musique = musique;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) listeDeLecture;
        hash += (int) musique;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListesdelectureMusiquesPK)) {
            return false;
        }
        ListesdelectureMusiquesPK other = (ListesdelectureMusiquesPK) object;
        if (this.listeDeLecture != other.listeDeLecture) {
            return false;
        }
        if (this.musique != other.musique) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entites.ListesdelectureMusiquesPK[ listeDeLecture=" + listeDeLecture + ", musique=" + musique + " ]";
    }
    
}
