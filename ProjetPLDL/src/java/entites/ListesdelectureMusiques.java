/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jodel
 */
@Entity
@Table(name = "listesdelecture_musiques")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListesdelectureMusiques.findAll", query = "SELECT l FROM ListesdelectureMusiques l")
    , @NamedQuery(name = "ListesdelectureMusiques.findByListeDeLecture", query = "SELECT l FROM ListesdelectureMusiques l WHERE l.listesdelectureMusiquesPK.listeDeLecture = :listeDeLecture")
    , @NamedQuery(name = "ListesdelectureMusiques.findByMusique", query = "SELECT l FROM ListesdelectureMusiques l WHERE l.listesdelectureMusiquesPK.musique = :musique")
    , @NamedQuery(name = "ListesdelectureMusiques.findByDate", query = "SELECT l FROM ListesdelectureMusiques l WHERE l.date = :date")})
public class ListesdelectureMusiques implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ListesdelectureMusiquesPK listesdelectureMusiquesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public ListesdelectureMusiques() {
    }

    public ListesdelectureMusiques(ListesdelectureMusiquesPK listesdelectureMusiquesPK) {
        this.listesdelectureMusiquesPK = listesdelectureMusiquesPK;
    }

    public ListesdelectureMusiques(ListesdelectureMusiquesPK listesdelectureMusiquesPK, Date date) {
        this.listesdelectureMusiquesPK = listesdelectureMusiquesPK;
        this.date = date;
    }

    public ListesdelectureMusiques(int listeDeLecture, int musique) {
        this.listesdelectureMusiquesPK = new ListesdelectureMusiquesPK(listeDeLecture, musique);
    }

    public ListesdelectureMusiquesPK getListesdelectureMusiquesPK() {
        return listesdelectureMusiquesPK;
    }

    public void setListesdelectureMusiquesPK(ListesdelectureMusiquesPK listesdelectureMusiquesPK) {
        this.listesdelectureMusiquesPK = listesdelectureMusiquesPK;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (listesdelectureMusiquesPK != null ? listesdelectureMusiquesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ListesdelectureMusiques)) {
            return false;
        }
        ListesdelectureMusiques other = (ListesdelectureMusiques) object;
        if ((this.listesdelectureMusiquesPK == null && other.listesdelectureMusiquesPK != null) || (this.listesdelectureMusiquesPK != null && !this.listesdelectureMusiquesPK.equals(other.listesdelectureMusiquesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entites.ListesdelectureMusiques[ listesdelectureMusiquesPK=" + listesdelectureMusiquesPK + " ]";
    }
    
}
