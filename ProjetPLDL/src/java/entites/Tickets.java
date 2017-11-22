package entites;
 
import java.util.Date;
 
 
 
//@XmlRootElement       //only needed if we also want to generate XML
public class Tickets {
 
    private String numTicket;      
    private String nom;    
    private String courriel;    
    private String motDePasse;
    private String captcha;
 
    public Tickets(String numTicket, String nom, String courriel, String motDePasse, String captcha) {
        this.numTicket = numTicket;
        this.nom = nom;
        this.motDePasse = motDePasse;
        this.captcha = captcha;
    }
    public Tickets() {        
    }
 
    public String getNumTicket() {
        return numTicket;
    }
 
    public String getNom() {
        return nom;
    }
 
    public String getCourriel() {
        return courriel;
    }
 
    public String getMotDePasse() {
        return motDePasse;
    }
 
    public String getCaptcha() {
        return captcha;
    }
 
    public void setNumTicket(String numTicket) {
        this.numTicket = numTicket;
    }
 
    public void setNom(String nom) {
        this.nom = nom;
    }
 
    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }
 
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
 
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}