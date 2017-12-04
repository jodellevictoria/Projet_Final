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
@Table(name = "musiques")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Musiques.findAll", query = "SELECT m FROM Musiques m")
    , @NamedQuery(name = "Musiques.findById", query = "SELECT m FROM Musiques m WHERE m.id = :id")
    , @NamedQuery(name = "Musiques.findByProprietaire", query = "SELECT m FROM Musiques m WHERE m.proprietaire = :proprietaire")
    , @NamedQuery(name = "Musiques.findByPublique", query = "SELECT m FROM Musiques m WHERE m.publique = :publique")
    , @NamedQuery(name = "Musiques.findByActive", query = "SELECT m FROM Musiques m WHERE m.active = :active")
    , @NamedQuery(name = "Musiques.findByDate", query = "SELECT m FROM Musiques m WHERE m.date = :date")})
public class Musiques implements Serializable {

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
    @Column(name = "Titre")
    private String titre;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "Artiste")
    private String artiste;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "Musique")
    private String musique;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "Vignette")
    private String vignette;
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

    public Musiques() {
    }

    public Musiques(Integer id) {
        this.id = id;
    }

    public Musiques(Integer id, int proprietaire, String titre, String artiste, String musique, String vignette, boolean publique, boolean active, Date date) {
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

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public String getMusique() {
        return musique;
    }

    public void setMusique(String musique) {
        this.musique = musique;
    }

    public String getVignette() {
        return vignette;
    }

    public void setVignette(String vignette) {
        this.vignette = vignette;
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
        if (!(object instanceof Musiques)) {
            return false;
        }
        Musiques other = (Musiques) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entites.Musiques[ id=" + id + " ]";
    }
    
}
