/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites.service;

import entites.Musiques;
import entites.Ticket;
import entites.Utilisateurs;
import static entites.service.UtilisateursFacadeREST.tickets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
@Path("musiques")
public class MusiquesFacadeREST extends AbstractFacade<Musiques> {

    @PersistenceContext(unitName = "ProjetPLDLPU")
    private EntityManager em;

    public MusiquesFacadeREST() {
        super(Musiques.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Musiques entity) {
        super.create(entity);
    }
    
    @GET
    @Path("creerMusique/{noTicket}/{chaineConfirmation}/{idUser}/{titre}/{artiste}/{musique}/{vignette}/{publique}/{active}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String creerMusique(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation, 
            @PathParam("idUser") Integer idUser, @PathParam("titre") String titre, @PathParam("artiste") String artiste,
            @PathParam("musique") String musique, @PathParam("vignette") String vignette,
            @PathParam("publique") boolean publique, @PathParam("active") boolean active) {
        String messageRetour = "empty";
        /*Utilisateurs util = null;
        Query q = em.createNamedQuery("Utilisateurs.findById");
        q.setParameter("id", idUser);
        
        try{
            util = (Utilisateurs) q.getSingleResult();
        }
        catch(Exception ex){
        }*/
        
        Ticket ticket = tickets.get(noTicket);
        
        //if(true){
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil()){
            Musiques musiqueAjout = new Musiques();
            musiqueAjout.setProprietaire(idUser);
            musiqueAjout.setTitre(titre);
            musiqueAjout.setArtiste(artiste);
            musiqueAjout.setMusique(musique);
            musiqueAjout.setVignette(vignette);
            musiqueAjout.setPublique(publique);
            musiqueAjout.setActive(active);
            musiqueAjout.setDate(new Date());
            em.persist(musiqueAjout);
            messageRetour = "Musique ajoutée";
        }
        else{
            messageRetour = "Musique n'a pa été ajoutée.";
        }
        
        return messageRetour;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Musiques entity) {
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
    public Musiques find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Musiques> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Musiques> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("musiquesPubliques")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Musiques> musiquesPubliques() {
        List<Musiques> musiques = new ArrayList<Musiques>();
        
        Query q = em.createNamedQuery("Musiques.findAll");
        List<Musiques> allMusiques = new ArrayList<Musiques>();
        
        try{
            allMusiques = (List<Musiques>) q.getResultList();
            for(int i = 0; i < allMusiques.size(); i++){
                if(allMusiques.get(i).getActive() == true && allMusiques.get(i).getPublique() == true){
                    musiques.add(allMusiques.get(i));
                }
            }
        }
        catch(Exception ex){
            
        }
        
        return musiques;
    }
    
    @GET
    @Path("consulterMusique/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Musiques musiquePublique(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation, 
            @PathParam("idUser") Integer idUser, @PathParam("idMusique") Integer idMusique) {
        Musiques musique = null;
        Musiques musiqueFound = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        try{
            musiqueFound = (Musiques) q.getSingleResult();
            /*if(musiqueFound != null){
                musique = musiqueFound;
            }*/
        }
        catch(Exception ex){
            
        }
        
        if(musiqueFound != null){
            Ticket ticket = tickets.get(noTicket);
        
            if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil() && musiqueFound.getProprietaire() == idUser){
                musique = musiqueFound;
                tickets.remove(noTicket);
            }
        }
        
        return musique;
    }
    
    @GET
    @Path("activerMusique/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}/{active}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String activerMusique(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation, 
            @PathParam("idUser") Integer idUser, @PathParam("idMusique") Integer idMusique, @PathParam("active") boolean active) {
        String messageRetour = "";
        Musiques musiqueFound = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        try{
            musiqueFound = (Musiques) q.getSingleResult();
        }
        catch(Exception ex){
            
        }
        
        if(musiqueFound != null){
            Ticket ticket = tickets.get(noTicket);
        
            if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil() && musiqueFound.getProprietaire() == idUser){
                musiqueFound.setActive(active);
                em.persist(musiqueFound);
                messageRetour = (active == true) ? "La musique est activée" : "La musique est désactivée";
                tickets.remove(noTicket);
            }
            else{
                messageRetour = "Erreur";
            }
        }
        else{
            messageRetour = "Erreur";
        }
        
        return messageRetour;
    }
    
    @GET
    @Path("publierMusique/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}/{publique}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public String publierMusique(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation, 
            @PathParam("idUser") Integer idUser, @PathParam("idMusique") Integer idMusique, @PathParam("publique") boolean publique) {
        String messageRetour = "";
        Musiques musiqueFound = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        try{
            musiqueFound = (Musiques) q.getSingleResult();
        }
        catch(Exception ex){
            
        }
        
        if(musiqueFound != null){
            Ticket ticket = tickets.get(noTicket);
        
            if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil() && musiqueFound.getProprietaire() == idUser){
                musiqueFound.setPublique(publique);
                em.persist(musiqueFound);
                messageRetour = (publique == true) ? "La musique est publique" : "La musique n'est pas publique";
                tickets.remove(noTicket);
            }
            else{
                messageRetour = "Erreur";
            }
        }
        else{
            messageRetour = "Erreur";
        }
        
        return messageRetour;
    }
    
    @GET
    @Path("modifierMusique/{noTicket}/{chaineConfirmation}/{Id}/{IdUtil}/{Titre}/{Artiste}/{Musique}/{Vignette}/{Publique}/{Active}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String modifierMusique(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,  
            @PathParam("Id") Integer id,@PathParam("IdUtil") Integer IdUtil, @PathParam("Titre") String titre, @PathParam("Artiste") String artiste, @PathParam("Musique") String musiqueUrl,
            @PathParam("Vignette") String vignette, @PathParam("Publique") boolean publique , @PathParam("Active") boolean active ) {
        String messageRetour = "";
        Musiques musique = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", id);
             
        try{
            musique = (Musiques) q.getSingleResult();
        }
        catch(Exception ex){
        }
       
        Ticket ticket = tickets.get(noTicket);
       
        String chaineToCompare = tickets.get(noTicket).getChaineConfirmation();
        if(IdUtil == musique.getProprietaire())
        {
            if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && IdUtil == ticket.getIdUtil()){            
            musique.setTitre(titre);
            musique.setArtiste(artiste);
            musique.setMusique(musiqueUrl);
            musique.setVignette(vignette);
            musique.setPublique(publique);
            musique.setActive(active);
            super.edit(musique);
            messageRetour = "La musique a été modifié";
            tickets.remove(noTicket);
            }
            else{
                messageRetour = "Erreur";
            }
        }
        else
        {
              messageRetour = "c'est pas votre music";
        }
       
        //super.edit(entity);
       
        return messageRetour;
        //return "wut";
    }
    
}
