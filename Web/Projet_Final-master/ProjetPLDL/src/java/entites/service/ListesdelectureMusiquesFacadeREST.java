/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites.service;
 
import entites.Listesdelecture;
import entites.ListesdelectureMusiques;
import entites.ListesdelectureMusiquesPK;
import entites.Musiques;
import entites.Ticket;
import static entites.service.UtilisateursFacadeREST.tickets;
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
import javax.ws.rs.core.PathSegment;
 
/**
 *
 * @author jodel
 */
@Stateless
@Path("listesdelecturemusiques")
public class ListesdelectureMusiquesFacadeREST extends AbstractFacade<ListesdelectureMusiques> {
 
    @PersistenceContext(unitName = "ProjetPLDLPU")
    private EntityManager em;
 
    private ListesdelectureMusiquesPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;listeDeLecture=listeDeLectureValue;musique=musiqueValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        entites.ListesdelectureMusiquesPK key = new entites.ListesdelectureMusiquesPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> listeDeLecture = map.get("listeDeLecture");
        if (listeDeLecture != null && !listeDeLecture.isEmpty()) {
            key.setListeDeLecture(new java.lang.Integer(listeDeLecture.get(0)));
        }
        java.util.List<String> musique = map.get("musique");
        if (musique != null && !musique.isEmpty()) {
            key.setMusique(new java.lang.Integer(musique.get(0)));
        }
        return key;
    }
 
    public ListesdelectureMusiquesFacadeREST() {
        super(ListesdelectureMusiques.class);
    }
 
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(ListesdelectureMusiques entity) {
        super.create(entity);
    }
 
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, ListesdelectureMusiques entity) {
        super.edit(entity);
    }
 
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        entites.ListesdelectureMusiquesPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }
 
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ListesdelectureMusiques find(@PathParam("id") PathSegment id) {
        entites.ListesdelectureMusiquesPK key = getPrimaryKey(id);
        return super.find(key);
    }
 
    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ListesdelectureMusiques> findAll() {
        return super.findAll();
    }
 
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<ListesdelectureMusiques> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("ajouterMusiqueALaListe/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}/{idListeDeMusique}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String ajouterMusiqueALaListe(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("idMusique") Integer idMusique, @PathParam("idListeDeMusique") Integer idListeDeMusique) {
        String messageRetour = "empty";
        Ticket ticket = tickets.get(noTicket);
       
       
       
        Musiques musique = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        try{
            musique = (Musiques) q.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
        Listesdelecture listesdelecture = null;
        Query q2 = em.createNamedQuery("Listesdelecture.findById");
        q2.setParameter("id", idListeDeMusique);
        try{
            listesdelecture = (Listesdelecture) q2.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
       
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil())
        {
            if(musique==null || (musique.getProprietaire() != idUser && (musique.getActive() == false || musique.getPublique()==false)))
            {
                messageRetour="musique introuvé ou inactive/privée";
            }
            else
            {
                if(listesdelecture==null || listesdelecture.getProprietaire() != idUser)
                {
                    messageRetour="liste de lecture introuvé ou ne vous appartien pas";
                }
                else
                {
                    ListesdelectureMusiquesPK listesdelectureMusiquesPK = new ListesdelectureMusiquesPK(idListeDeMusique,idMusique);
                    ListesdelectureMusiques listesdelectureMusiques = new ListesdelectureMusiques();
                    listesdelectureMusiques.setListesdelectureMusiquesPK(listesdelectureMusiquesPK);
                    listesdelectureMusiques.setDate(new Date());
                    em.persist(listesdelectureMusiques);
                    messageRetour = "la musique a été ajoutée a la liste";
                    tickets.remove(noTicket);
                }
            }
        }
        else{
            messageRetour = "erreur avec le ticket";
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
    @Path("supprimerMusiqueALaListe/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}/{idListeDeMusique}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String supprimerMusiqueALaListe(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("idMusique") Integer idMusique, @PathParam("idListeDeMusique") Integer idListeDeMusique) {

        String messageRetour = "empty";
        Ticket ticket = tickets.get(noTicket);
       
       
       
        Musiques musique = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        try{
            musique = (Musiques) q.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
        Listesdelecture listesdelecture = null;
        Query q2 = em.createNamedQuery("Listesdelecture.findById");
        q2.setParameter("id", idListeDeMusique);
        try{
            listesdelecture = (Listesdelecture) q2.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
       
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil())
        {
            if(musique==null || (musique.getActive() == false || musique.getPublique()==false))
            {
                messageRetour="musique introuvé ou inactive/privée";
            }
            else
            {
                if(listesdelecture==null || listesdelecture.getProprietaire() != idUser)
                {
                    messageRetour="liste de lecture introuvé ou ne vous appartient pas";
                }
                else
                {
                    ListesdelectureMusiquesPK listesdelectureMusiquesPK = new ListesdelectureMusiquesPK(idListeDeMusique,idMusique);
                   
                    List<ListesdelectureMusiques> listesdelectureMusiques = null;
                    Query q3 = em.createNamedQuery("ListesdelectureMusiques.findByListeDeLecture");    
                    q3.setParameter("listeDeLecture", idListeDeMusique);
                    try{
                        listesdelectureMusiques = (List<ListesdelectureMusiques> ) q3.getResultList();          
                    }
                    catch(Exception ex){            
                    }
                   
                   
                    boolean boolTempo=false;
                    if(listesdelectureMusiques==null)                                        
                    {
                         messageRetour = "la liste de lecture ne contien pas de musique";
                    }
                    else
                    {
                        for(int i = 0; i<listesdelectureMusiques.size();i++)
                        {
                            if(listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getMusique()==listesdelectureMusiquesPK.getMusique() &&
                                    listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getListeDeLecture()==listesdelectureMusiquesPK.getListeDeLecture())
                            {
                                boolTempo=true;
                                break;
                            }
                        }
                        if(boolTempo==true)
                        {
                            super.remove(super.find(listesdelectureMusiquesPK));
                            messageRetour = "la musique a été supprimée de la liste";
                            tickets.remove(noTicket);
                        }
                        else
                        {
                            messageRetour = "la musique n'appartient à aucune liste de lecture";
                        }
                    }
                }
            }
        }
        else{
            messageRetour = "erreur avec le ticket";
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
    @Path("changerMusiqueALaListe/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}/{idListeDeMusiqueOld}/{idListeDeMusiqueNew}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String changerMusiqueALaListe(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("idMusique") Integer idMusique, @PathParam("idListeDeMusiqueOld") Integer idListeDeMusiqueOld,
            @PathParam("idListeDeMusiqueNew") Integer idListeDeMusiqueNew) {

        String messageRetour = "empty";
        Ticket ticket = tickets.get(noTicket);
       
       
       
        Musiques musique = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        try{
            musique = (Musiques) q.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
        Listesdelecture listesdelecture = null;
        Query q2 = em.createNamedQuery("Listesdelecture.findById");
        q2.setParameter("id", idListeDeMusiqueOld);
        try{
            listesdelecture = (Listesdelecture) q2.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
       
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil())
        {
            if(musique==null || ((musique.getActive() == false || musique.getPublique()==false) && musique.getProprietaire() != idUser))
            {
                messageRetour="musique introuvée ou inactive/privée";
            }
            else
            {
                if(listesdelecture==null || listesdelecture.getProprietaire() != idUser)
                {
                    messageRetour="ancienne liste de lecture introuvée ou ne vous appartient pas";
                }
                else
                {
                    ListesdelectureMusiquesPK listesdelectureMusiquesPK = new ListesdelectureMusiquesPK(idListeDeMusiqueOld,idMusique);
                   
                    List<ListesdelectureMusiques> listesdelectureMusiques = null;
                    Query q3 = em.createNamedQuery("ListesdelectureMusiques.findByListeDeLecture");    
                    q3.setParameter("listeDeLecture", idListeDeMusiqueOld);
                    try{
                        listesdelectureMusiques = (List<ListesdelectureMusiques> ) q3.getResultList();          
                    }
                    catch(Exception ex){            
                    }
                   
                   
                    boolean boolTempo=false;
                    if(listesdelectureMusiques==null)                                        
                    {
                         messageRetour = "l'ancienne liste de lecture ne contient pas de musique";
                    }
                    else
                    {
                        for(int i = 0; i<listesdelectureMusiques.size();i++)
                        {
                            if(listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getMusique()==listesdelectureMusiquesPK.getMusique() &&
                                    listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getListeDeLecture()==listesdelectureMusiquesPK.getListeDeLecture())
                            {
                                boolTempo=true;
                                break;
                            }
                        }
                        if(boolTempo==true)
                        {
                           
                           
                            Listesdelecture listesdelecture2 = null;
                            Query q4 = em.createNamedQuery("Listesdelecture.findById");
                            q4.setParameter("id", idListeDeMusiqueNew);
                            try{
                                listesdelecture2 = (Listesdelecture) q4.getSingleResult();          
                            }
                            catch(Exception ex){            
                            }
                           
                            if(listesdelecture2==null || listesdelecture2.getProprietaire() != idUser)
                            {
                                messageRetour="la nouvelle liste de lecture n'existe pas ou ne vous appartient pas";
                            }
                            else
                            {
                                //JE DOIT TU ADD AVEC LA DATE ENPLUS????????
                                super.remove(super.find(listesdelectureMusiquesPK));
                                ListesdelectureMusiquesPK listesdelectureMusiquesPK2 = new ListesdelectureMusiquesPK(idListeDeMusiqueNew,idMusique);
                                ListesdelectureMusiques listesdelectureMusiques2 = new ListesdelectureMusiques();
                                listesdelectureMusiques2.setListesdelectureMusiquesPK(listesdelectureMusiquesPK2);
                                listesdelectureMusiques2.setDate(new Date());
                                em.persist(listesdelectureMusiques2);
                                messageRetour = "la musique a été ajoutée a la nouvelle liste";
                                tickets.remove(noTicket);
                            }                          
                           
                        }
                        else
                        {
                            messageRetour = "la musique n'appartient a aucune listede lecture";
                        }
                    }
                }
            }
        }
        else{
            messageRetour = "erreur avec le ticket";
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
    @Path("copierMusiqueAListe/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}/{idListeDeMusiqueOld}/{idListeDeMusiqueNew}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String copierMusiqueAListe(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("idMusique") Integer idMusique, @PathParam("idListeDeMusiqueOld") Integer idListeDeMusiqueOld,
            @PathParam("idListeDeMusiqueNew") Integer idListeDeMusiqueNew) {
        String messageRetour = "empty";
        Ticket ticket = tickets.get(noTicket);
       
       
       
        Musiques musique = null;
        Query q = em.createNamedQuery("Musiques.findById");
        q.setParameter("id", idMusique);
        try{
            musique = (Musiques) q.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
        Listesdelecture listesdelecture = null;
        Query q2 = em.createNamedQuery("Listesdelecture.findById");
        q2.setParameter("id", idListeDeMusiqueOld);
        try{
            listesdelecture = (Listesdelecture) q2.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
       
       
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil())
        {
            if(musique==null || ((musique.getActive() == false || musique.getPublique()== false) && musique.getProprietaire() != idUser))
            {
                messageRetour="musique introuvé ou inactive/privée";
            }
            else
            {
                if(listesdelecture==null || listesdelecture.getProprietaire() != idUser)
                {
                    messageRetour="ancienne liste de lecture introuvé ou ne vous appartient pas";
                }
                else
                {
                    ListesdelectureMusiquesPK listesdelectureMusiquesPK = new ListesdelectureMusiquesPK(idListeDeMusiqueOld,idMusique);
                   
                    List<ListesdelectureMusiques> listesdelectureMusiques = null;
                    Query q3 = em.createNamedQuery("ListesdelectureMusiques.findByListeDeLecture");    
                    q3.setParameter("listeDeLecture", idListeDeMusiqueOld);
                    try{
                        listesdelectureMusiques = (List<ListesdelectureMusiques> ) q3.getResultList();          
                    }
                    catch(Exception ex){            
                    }
                   
                   
                    boolean boolTempo=false;
                    if(listesdelectureMusiques==null)                                        
                    {
                         messageRetour = "l'ancienne liste de lecture ne contient pas de musique";
                    }
                    else
                    {
                        for(int i = 0; i<listesdelectureMusiques.size();i++)
                        {
                            if(listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getMusique()==listesdelectureMusiquesPK.getMusique() &&
                                    listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getListeDeLecture()==listesdelectureMusiquesPK.getListeDeLecture())
                            {
                                boolTempo=true;
                                break;
                            }
                        }
                        if(boolTempo==true)
                        {
                           
                           
                            Listesdelecture listesdelecture2 = null;
                            Query q4 = em.createNamedQuery("Listesdelecture.findById");
                            q4.setParameter("id", idListeDeMusiqueNew);
                            try{
                                listesdelecture2 = (Listesdelecture) q4.getSingleResult();          
                            }
                            catch(Exception ex){            
                            }
                           
                            if(listesdelecture2==null || listesdelecture2.getProprietaire() != idUser)
                            {
                                messageRetour="la nouvelle liste de lecture n'existe pas ou ne vous appartient pas";
                            }
                            else
                            {
                                ListesdelectureMusiquesPK listesdelectureMusiquesPK2 = new ListesdelectureMusiquesPK(idListeDeMusiqueNew,idMusique);
                                ListesdelectureMusiques listesdelectureMusiques2 = new ListesdelectureMusiques();
                                listesdelectureMusiques2.setListesdelectureMusiquesPK(listesdelectureMusiquesPK2);
                                listesdelectureMusiques2.setDate(new Date());
                                em.persist(listesdelectureMusiques2);
                                messageRetour = "la musique a été ajoutée a la nouvelle liste";
                                tickets.remove(noTicket);
                            }                          
                           
                        }
                        else
                        {
                            messageRetour = "la musique n'appartient a aucune listede lecture";
                        }
                    }
                }
            }
        }
        else{
            messageRetour = "erreur avec le ticket";
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
    @Path("copierListeeALaListe/{noTicket}/{chaineConfirmation}/{idUser}/{idListeDeMusique}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String copierListeeALaListe(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("idListeDeMusique") Integer idListeDeMusiqueOld) {

        String messageRetour = "empty";
        Ticket ticket = tickets.get(noTicket);
       
        Listesdelecture listesdelecture = null;
        Query q2 = em.createNamedQuery("Listesdelecture.findById");
        q2.setParameter("id", idListeDeMusiqueOld);
        try{
            listesdelecture = (Listesdelecture) q2.getSingleResult();          
        }
        catch(Exception ex){            
        }
       
        
        
         if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil())
        {
            if(listesdelecture==null)
            {
                messageRetour="ancienne liste de lecture introuvé ou ne vous appartient pas";
            }
            else
            {                
                List<ListesdelectureMusiques> listesdelectureMusiques = null;
                Query q3 = em.createNamedQuery("ListesdelectureMusiques.findByListeDeLecture");    
                q3.setParameter("listeDeLecture", idListeDeMusiqueOld);
                try{
                    listesdelectureMusiques = (List<ListesdelectureMusiques> ) q3.getResultList();          
                }
                catch(Exception ex){            
                }


                if(listesdelectureMusiques==null)                                        
                {
                     messageRetour = "la liste de lecture ne contient pas de musique";
                }
                else
                {
                    tickets.remove(noTicket);   
                    
                    Listesdelecture listesdelecture2 = new Listesdelecture();
                    listesdelecture2.setProprietaire(idUser);
                    listesdelecture2.setNom(listesdelecture.getNom());
                    listesdelecture2.setPublique(listesdelecture.getPublique());
                    listesdelecture2.setActive(listesdelecture.getActive());
                    listesdelecture2.setDate(new Date());
                    em.persist(listesdelecture2);
                    
                    
                    //JE DOIT TU ADD AVEC LA DATE ENPLUS????????
                    for(int i = 0; i<listesdelectureMusiques.size();i++)
                    {                                 
                        List<Listesdelecture> listeDeListesdelecture = null;
                        Query q4 = em.createNamedQuery("Listesdelecture.findAll");    
                        //q4.setParameter("listeDeLecture", idListeDeMusiqueOld);
                        try{
                            listeDeListesdelecture = (List<Listesdelecture> ) q4.getResultList();          
                        }
                        catch(Exception ex){            
                        }
                        
                        //messageRetour = listesdelecture2.getId()+"";                        
                        //messageRetour += listesdelecture2.getId()+";"+listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getMusique()+";plus gros;"+plusGrosID;
                        
                        ListesdelectureMusiquesPK listesdelectureMusiquesPK2 = new ListesdelectureMusiquesPK(listesdelecture2.getId(),listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getMusique());
                        ListesdelectureMusiques listesdelectureMusiques2 = new ListesdelectureMusiques();                        
                        //messageRetour+=listesdelectureMusiquesPK2.getListeDeLecture()+";"+listesdelectureMusiquesPK2.getMusique();
                        listesdelectureMusiques2.setListesdelectureMusiquesPK(listesdelectureMusiquesPK2);
                        listesdelectureMusiques2.setDate(new Date());
                        em.persist(listesdelectureMusiques2);                     
                        messageRetour = "les musique(s) a ont été ajoutée(s) a la nouvelle liste";
                                                                       
                    }                     
                }                
            }        
        }
        else
        {
            messageRetour = "erreur avec le ticket";
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
}