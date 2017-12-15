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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Base64;
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
import javax.xml.bind.DatatypeConverter;
import org.hibernate.validator.internal.util.logging.Log;

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
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String creerMusique(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation, 
            @PathParam("idUser") Integer idUser, @PathParam("titre") String titre, @PathParam("artiste") String artiste,
            @PathParam("musique") String musique, @PathParam("vignette") String vignette,
            @PathParam("publique") boolean publique, @PathParam("active") boolean active) throws UnsupportedEncodingException {
        //em.getEntityManagerFactory().getCache().evictAll();
        String messageRetour = "empty";

        Ticket ticket = tickets.get(noTicket);        
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil()){
            /*String vignetteTempo = vignette;
            vignetteTempo = vignetteTempo.replaceAll("Password1", "\\+");
            vignetteTempo = vignetteTempo.replaceAll("Password2", "/");
            vignetteTempo = vignetteTempo.replaceAll("Password3", "=");*/
            
            String vignettetoAdd = "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";
            Musiques musiqueAjout = new Musiques();
            musiqueAjout.setProprietaire(idUser);
            musiqueAjout.setTitre(URLDecoder.decode(titre, "UTF-8"));
            musiqueAjout.setArtiste(URLDecoder.decode(artiste, "UTF-8"));
            musiqueAjout.setMusique(URLDecoder.decode(musique, "UTF-8"));
            musiqueAjout.setVignette(getByteArrayFromImageURL(URLDecoder.decode(vignette, "UTF-8")));
            musiqueAjout.setPublique(publique);
            musiqueAjout.setActive(active);
            musiqueAjout.setDate(new Date());
            em.persist(musiqueAjout);
            messageRetour = "Musique ajoutée";
            tickets.remove(noTicket);
        }
        else{
            messageRetour = "Musique n'a pas été ajoutée.";
        }
        
        
        if(tickets.get(noTicket)!=null)
        {
            tickets.get(noTicket).setNbEssais(tickets.get(noTicket).getNbEssais()+1);
            if(tickets.get(noTicket).getNbEssais()>=3)
            {
                tickets.remove(noTicket);
                messageRetour="le ticket"+noTicket+"a été détruis apres vois 3 essais";
            }
        }      
        
        return messageRetour;
    }

    private String getByteArrayFromImageURL(String url) {

        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            InputStream is = ucon.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
            }
            baos.flush();
            return DatatypeConverter.printBase64Binary(baos.toByteArray());

            //return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            //return new String(baos.toByteArray(), Charset.defaultCharset()); 
        } catch (Exception e) {
            //Log.d("Error", e.toString());
        }
        return null;
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
    @Produces({MediaType.APPLICATION_JSON})
    public Musiques find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
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
    @Produces({ MediaType.APPLICATION_JSON})
    public List<Musiques> musiquesPubliques() {
        List<Musiques> musiques = new ArrayList<Musiques>();
        
        Query q = em.createNamedQuery("Musiques.findByPublique");
        q.setParameter("publique", true);
        List<Musiques> allMusiques = new ArrayList<Musiques>();
        
        try{
            allMusiques = (List<Musiques>) q.getResultList();
            for(int i = 0; i < allMusiques.size(); i++){
                if(allMusiques.get(i).getActive()){
                    musiques.add(allMusiques.get(i));
                }
            }
        }
        catch(Exception ex){
            
        }
        
        return musiques;
    }
    
    @GET
    @Path("musiquePublique/{idMusique}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Musiques musiquePublique(@PathParam("idMusique") Integer idMusique) {
        Musiques musiques = new Musiques();
        
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        
        try{
            musiques = (Musiques) q.getSingleResult();
            if(!musiques.getActive() || !musiques.getPublique()){
                musiques = null;
            }
        }
        catch(Exception ex){
            
        }
        
        return musiques;
    }   
    
    @GET
    @Path("consulterMusique/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}")
    @Produces({ MediaType.APPLICATION_JSON})
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
    @Produces({ MediaType.APPLICATION_JSON})
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
                messageRetour = "Erreur : avec la validation du ticket";
            }
        }
        else{
            messageRetour = "Erreur: le ticket n'existe pas";
        }
        
        if(tickets.get(noTicket)!=null)
        {
            tickets.get(noTicket).setNbEssais(tickets.get(noTicket).getNbEssais()+1);
            if(tickets.get(noTicket).getNbEssais()>=3)
            {
                tickets.remove(noTicket);
                messageRetour="le ticket"+noTicket+"a été détruis apres vois 3 essais";
            }
        }
        
        return messageRetour;
    }
    
    @GET
    @Path("publierMusique/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}/{publique}")
    @Produces({ MediaType.APPLICATION_JSON})
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
        
        if(musiqueFound != null)
        {
            if(idUser == musiqueFound.getProprietaire())
            {
                Ticket ticket = tickets.get(noTicket);        
                if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil() && musiqueFound.getProprietaire() == idUser)
                {
                    musiqueFound.setPublique(publique);
                    em.persist(musiqueFound);
                    messageRetour = (publique == true) ? "La musique est publique" : "La musique n'est pas publique";
                    tickets.remove(noTicket);
                }
                else
                {
                    messageRetour = "Erreur: erreur de validation du ticket";
                }
            }
            else
            {
                messageRetour = "c'est pas votre music";
            }      
        }
        else{
            messageRetour = "Erreur: la musique n'existe pas";
        }
        
        if(tickets.get(noTicket)!=null)
        {
            tickets.get(noTicket).setNbEssais(tickets.get(noTicket).getNbEssais()+1);
            if(tickets.get(noTicket).getNbEssais()>=3)
            {
                tickets.remove(noTicket);
                messageRetour="le ticket"+noTicket+"a été détruis apres vois 3 essais";
            }
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
       
        //String chaineToCompare = tickets.get(noTicket).getChaineConfirmation();
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
                messageRetour = "Erreur: ticket n'existe pas";
            }
        }
        else
        {
              messageRetour = "c'est pas votre music";
        }
       
        
        if(tickets.get(noTicket)!=null)
        {            
            tickets.get(noTicket).setNbEssais(tickets.get(noTicket).getNbEssais()+1);
            if(tickets.get(noTicket).getNbEssais()>=3)
            {
                tickets.remove(noTicket);
                messageRetour="le ticket"+noTicket+"a été détruis apres vois 3 essais";
            }
        }
        
        //super.edit(entity);
       
        return messageRetour;
        //return "wut";
    }
    
    @GET
    @Path("voirMusiquesALui/{noTicket}/{chaineConfirmation}/{idUtil}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON} )
    public List<Musiques> voirMusiquesALui(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,@PathParam("idUtil") Integer idUtil) {
        //List<Musiques> musiquesInPlaylist = new ArrayList<Musiques>();
        List<Musiques> musiques = new ArrayList<Musiques>();
        Query q = em.createNamedQuery("Musiques.findByProprietaire");
        q.setParameter("proprietaire", idUtil);
       
        musiques=null;
       
        try{
            musiques = (List<Musiques>) q.getResultList();
        }
        catch(Exception ex){            
        }
               
        Ticket ticket = tickets.get(noTicket);
        if(musiques==null)
        {
            Musiques l = new Musiques(1,1,"vous n'avez pas de liste","vous n'avez pas de liste","vous n'avez pas de liste","vous n'avez pas de liste",true,true,new Date());  
            musiques= new ArrayList<Musiques>();
            musiques.add(l);
        }
       
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUtil == ticket.getIdUtil())
        {
           tickets.remove(noTicket);
        }  
        else
        {
            Musiques l = new Musiques(1,1,"erreur avec le ticket","erreur avec le ticket","erreur avec le ticket","erreur avec le ticket",true,true,new Date());  
            musiques= new ArrayList<Musiques>();
            musiques.add(l);           
        }
        
        if(tickets.get(noTicket)!=null)
        {           
            tickets.get(noTicket).setNbEssais(tickets.get(noTicket).getNbEssais()+1);
            if(tickets.get(noTicket).getNbEssais()>=3)
            {
                tickets.remove(noTicket);                
            }
        }
        
        return musiques;
    }
    
}
