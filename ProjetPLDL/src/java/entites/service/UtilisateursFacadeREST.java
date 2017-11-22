/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites.service;

import entites.Ticket;
import entites.TicketCaptchaReturn;
import entites.TicketToReturn;
import entites.Tickets;
import entites.Utilisateurs;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import session.GestionnaireUtilisateur;

/**
 *
 * @author jodel
 */
@Stateless
@Path("utilisateurs")
public class UtilisateursFacadeREST extends AbstractFacade<Utilisateurs> {

    private HashMap<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
    
    @EJB
    private GestionnaireUtilisateur gestionnaireCommande;
   
    public ArrayList<Tickets> listTickets=new ArrayList<Tickets>();
    
    @PersistenceContext(unitName = "ProjetPLDLPU")
    private EntityManager em;

    public UtilisateursFacadeREST() {
        super(Utilisateurs.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Utilisateurs entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Utilisateurs entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Utilisateurs find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Utilisateurs> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Utilisateurs> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    @GET
    @Path("GetTicket/{Utilisateur}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON} )
    public TicketToReturn getTicket(@PathParam("Utilisateur") String utilisateur){
        String retour = "";
        Query q = em.createNamedQuery("Utilisateurs.findByCourriel");
        q.setParameter("courriel", utilisateur);
        
        Ticket ticket = null;
        TicketToReturn ticketReturn = null;
        
        Utilisateurs util = null;
        try{
            util = (Utilisateurs) q.getSingleResult();
        }
        catch(Exception ex){
            
        }
        
        if(util != null){
           
            //retour = util.getMotDePasse();

            Random r = new Random();
            int Low = 0;
            int High = 1000;
            int cle = r.nextInt(High-Low) + Low;
            retour += Integer.toString(cle);                
            int idUser = util.getId();
            String mdp = util.getMotDePasse();
            //retour = mdp;
            
            String avConvert = Integer.toString(cle) + mdp;
            retour += "," + avConvert;
            String confirmation = "";
            String confirmationMD5 = "";
            try {
                MessageDigest m=MessageDigest.getInstance("MD5");
                m.update(avConvert.getBytes(),0,avConvert.length());
                //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
                confirmationMD5 = new BigInteger(1,m.digest()).toString(16);
                
                confirmation = confirmationMD5;
                retour += "," + confirmation;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UtilisateursFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            int noTicket = tickets.size() + 1;
            retour = Integer.toString(noTicket);
            // public Ticket(int noTicket, String cle, String chaineConfirmation, int idUtil) {
            ticket = new Ticket(noTicket, Integer.toString(cle), confirmationMD5, idUser);
            ticketReturn = new TicketToReturn(ticket.getNoTicket(),ticket.getCle());
        }
        else{
            retour = "-1";
        }
        
        return ticketReturn;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    } 
    
    
    @GET
    @Path("créer/{numTicket}/{captcha}")
    //@Produces(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    //@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.TEXT_PLAIN)
    public String confirmer(@PathParam("numTicket") String numTicket, @PathParam("captcha") String captcha) {
       
        boolean boolTempo=false;
        String flux = "";
               
        for(int i =0;i<listTickets.size();i++ )
        {
            if(listTickets.get(i).getNumTicket().compareTo(numTicket)==0 && listTickets.get(i).getCaptcha().compareTo(captcha)==0 )
            {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                System.out.println(dateFormat.format(date));
               
                //placerUtilisateur(String courriel, String motDePasse, String alias, int avatar, boolean actif, Date date)
                int idUtilisateur = gestionnaireCommande.placerUtilisateur(listTickets.get(i).getCourriel(),listTickets.get(i).getMotDePasse(),listTickets.get(i).getNom(),1,true,date );
                listTickets.remove(listTickets.get(i));
                boolTempo=true;
                break;
            }
        }
       
        if(boolTempo==true)
        {
            flux="l'utilisateur à été ajouter";
        }
        else
        {
            flux="l'utilisateur à été ajouter";
        }
       
        return "";
    }
   
   
    @GET
    @Path("créer/{nom}/{courriel}/{motDePasse}")
    //@Produces(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    //@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.TEXT_PLAIN)
   
    public TicketCaptchaReturn creeUser(@PathParam("nom") String nom, @PathParam("courriel") String courriel, @PathParam("motDePasse") String motDePasse) {
       
        Random x = new Random();
        int n = x.nextInt(1000000000);
       
        String numTicket = n+"";      
        Tickets ticket =null ;
       
       
        int length = 7 + (Math.abs(x.nextInt()) % 3);
    StringBuffer captchaStringBuffer = new StringBuffer();
    for (int i = 0; i < length; i++) {
            int baseCharNumber = Math.abs(x.nextInt()) % 62;
            int charNumber = 0;
            if (baseCharNumber < 26) {
                charNumber = 65 + baseCharNumber;
            }
            else if (baseCharNumber < 52){
                charNumber = 97 + (baseCharNumber - 26);
            }
            else {
                charNumber = 48 + (baseCharNumber - 52);
            }
            captchaStringBuffer.append((char)charNumber);
    }
    String captcha = captchaStringBuffer.toString();
       
       
        //le client
        Query q = em.createNamedQuery("Utilisateurs.findByCourriel");
        q.setParameter("courriel", courriel);
        Utilisateurs utilisateurs = null;
        try{
            utilisateurs = (Utilisateurs) q.getSingleResult();
           
            if(utilisateurs==null)
            {
                ticket = null;
            }
            else
            {
                boolean boolTempo = true;                
                for(int i =0;i<listTickets.size();i++ )
                {
                    if(listTickets.get(i).getCourriel().compareTo(courriel)==0)
                    {
                        boolTempo=false;
                        listTickets.get(i).setCaptcha(captcha);
                        break;
                    }
                }
                if(boolTempo==true)
                {
                    ticket = new  Tickets(numTicket,nom,courriel,motDePasse,captcha);
                }                
            }
        }
        catch(Exception ex){            
        }
       
        TicketCaptchaReturn ticketCaptchaReturn = null;
       
        if(ticket==null)
        {
            ticketCaptchaReturn = new TicketCaptchaReturn("-1",ticket.getCaptcha());            
        }
        else
        {
            listTickets.add(ticket);
            ticketCaptchaReturn = new TicketCaptchaReturn(ticket.getNumTicket(),ticket.getCaptcha());            
        }        
        return ticketCaptchaReturn;
       
    }
}
