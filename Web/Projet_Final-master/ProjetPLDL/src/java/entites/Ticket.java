/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites;

/**
 *
 * @author jodel
 */
public class Ticket {
    int noTicket;
    String cle;
    String chaineConfirmation;
    int idUtil;
    int nbEssais;    

    public Ticket(int noTicket, String cle, String chaineConfirmation, int idUtil,int nbEssais) {
        this.noTicket = noTicket;
        this.cle = cle;
        this.chaineConfirmation = chaineConfirmation;
        this.idUtil = idUtil;
        this.nbEssais = nbEssais;
    }

    public Ticket() {
    }
    
    public int getIdUtil() {
        return idUtil;
    }

    public void setIdUtil(int idUtil) {
        this.idUtil = idUtil;
    }

    public int getNoTicket() {
        return noTicket;
    }

    public void setNoTicket(int noTicket) {
        this.noTicket = noTicket;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
    }

    public String getChaineConfirmation() {
        return chaineConfirmation;
    }

    public void setChaineConfirmation(String chaineConfirmation) {
        this.chaineConfirmation = chaineConfirmation;
    }
    
    public void setNbEssais(int nbEssais) {
        this.nbEssais = nbEssais;
    }

    public int getNbEssais() {
        return nbEssais;
    }
    
    
}
