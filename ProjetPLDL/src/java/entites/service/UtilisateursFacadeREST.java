/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites.service;

import entites.Ticket;
import entites.TicketToReturn;
import entites.Utilisateurs;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author jodel
 */
@Stateless
@Path("utilisateurs")
public class UtilisateursFacadeREST extends AbstractFacade<Utilisateurs> {

    private HashMap<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
    
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
}
