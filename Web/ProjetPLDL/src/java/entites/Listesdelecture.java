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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jodel
 */
@Entity
@Table(name = "listesdelecture")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listesdelecture.findAll", query = "SELECT l FROM Listesdelecture l")
    , @NamedQuery(name = "Listesdelecture.findById", query = "SELECT l FROM Listesdelecture l WHERE l.id = :id")
    , @NamedQuery(name = "Listesdelecture.findByProprietaire", query = "SELECT l FROM Listesdelecture l WHERE l.proprietaire = :proprietaire")
    , @NamedQuery(name = "Listesdelecture.findByPublique", query = "SELECT l FROM Listesdelecture l WHERE l.publique = :publique")
    , @NamedQuery(name = "Listesdelecture.findByActive", query = "SELECT l FROM Listesdelecture l WHERE l.active = :active")
    , @NamedQuery(name = "Listesdelecture.findByDate", query = "SELECT l FROM Listesdelecture l WHERE l.date = :date")})
public class Listesdelecture implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Proprietaire")
    private int proprietaire;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "Nom")
    private String nom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Publique")
    private boolean publique;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Active")
    private boolean active;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Listesdelecture() {
    }

    public Listesdelecture(Integer id) {
        this.id = id;
    }

    public Listesdelecture(Integer id, int proprietaire, String nom, boolean publique, boolean active, Date date) {
        this.id = id;
        this.proprietaire = proprietaire;
        this.nom = nom;
        this.publique = publique;
        this.active = active;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public boolean getPublique() {
        return publique;
    }

    public void setPublique(boolean publique) {
        this.publique = publique;
    }

    public boolean getActive() {
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
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Listesdelecture)) {
            return false;
        }
        Listesdelecture other = (Listesdelecture) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entites.Listesdelecture[ id=" + id + " ]";
    }
    
}
