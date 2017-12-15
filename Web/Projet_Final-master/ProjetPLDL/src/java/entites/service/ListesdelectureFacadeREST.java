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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
@Path("listesdelecture")
public class ListesdelectureFacadeREST extends AbstractFacade<Listesdelecture> {
 
    @PersistenceContext(unitName = "ProjetPLDLPU")
    private EntityManager em;
 
    public ListesdelectureFacadeREST() {
        super(Listesdelecture.class);
    }
 
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Listesdelecture entity) {
        super.create(entity);
    }
 
    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Listesdelecture entity) {
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
    public Listesdelecture find(@PathParam("id") Integer id) {
        return super.find(id);
    }
 
    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Listesdelecture> findAll() {
        return super.findAll();
    }
 
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Listesdelecture> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("publierListeDeLecture/{noTicket}/{chaineConfirmation}/{idUser}/{idListeDeLecture}/{publique}")
    @Produces({MediaType.APPLICATION_JSON})
    public String publierListeDeLecture(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("idListeDeLecture") Integer idListeDeLecture, @PathParam("publique") boolean publique) {

        String messageRetour = "";
        Listesdelecture listesdelecture = null;
        Query q = em.createNamedQuery("Listesdelecture.findById");
        q.setParameter("id", idListeDeLecture);
        try{
            listesdelecture = (Listesdelecture) q.getSingleResult();
        }
        catch(Exception ex){
           
        }
               
        if(listesdelecture != null){
            Ticket ticket = tickets.get(noTicket);
           
            if(listesdelecture.getProprietaire() == idUser)
            {
                if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil() ){
                    listesdelecture.setPublique(publique);
                    em.persist(listesdelecture);
                    messageRetour = (publique == true) ? "La liste de lecture est publique" : "La liste de lecture est privée";
                    tickets.remove(noTicket);
                }
                else
                {
                    messageRetour="erreur avec le ticket";
                }
            }
            else{
                messageRetour = "Cette liste de lecture ne vous appartient pas";
            }
        }
        else{
            messageRetour = "vous n'avez pas de liste de lecture";
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
    @Path("activerListeDeLecture/{noTicket}/{chaineConfirmation}/{idUser}/{idListeDeLecture}/{active}")
    @Produces({ MediaType.APPLICATION_JSON})
    public String activerListeDeLecture(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("idListeDeLecture") Integer idListeDeLecture, @PathParam("active") boolean active) {

        String messageRetour = "";
        Listesdelecture listesdelecture = null;
        Query q = em.createNamedQuery("Listesdelecture.findById");
        q.setParameter("id", idListeDeLecture);
        try{
            listesdelecture = (Listesdelecture) q.getSingleResult();
        }
        catch(Exception ex){
           
        }
       
        if(listesdelecture != null){
            Ticket ticket = tickets.get(noTicket);
           
            if(listesdelecture.getProprietaire() == idUser)
            {
                if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil() ){
                    listesdelecture.setActive(active);
                    em.persist(listesdelecture);
                    messageRetour = (active == true) ? "La liste de lecture est activée" : "La liste de lecture est désactivée";
                    tickets.remove(noTicket);
                }
                else
                {
                    messageRetour="erreur avec le ticket";
                }
            }
            else{
                messageRetour = "Cette liste de lecture ne vous appartient pas";
            }
        }
        else{
            messageRetour = "vous n'avez pas de liste de lecture";
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
    @Path("voirListeDeLectureALui/{noTicket}/{chaineConfirmation}/{idUtil}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON} )
    public List<Listesdelecture> voirListeDeLectureALui(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,@PathParam("idUtil") Integer idUtil) {
        //List<Musiques> musiquesInPlaylist = new ArrayList<Musiques>();

        List<Listesdelecture> listeDeLecture = new ArrayList<Listesdelecture>();
        Query q = em.createNamedQuery("Listesdelecture.findByProprietaire");
        q.setParameter("proprietaire", idUtil);
       
        listeDeLecture=null;
       
        try{
            listeDeLecture = (List<Listesdelecture>) q.getResultList();
        }
        catch(Exception ex){            
        }
               
        Ticket ticket = tickets.get(noTicket);
        if(listeDeLecture==null)
        {
            Listesdelecture l = new Listesdelecture(1,1,"vous n'avez pas de liste",true,true,new Date());  
            listeDeLecture= new ArrayList<Listesdelecture>();
            listeDeLecture.add(l);
        }
       
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUtil == ticket.getIdUtil())
        {
           tickets.remove(noTicket);
        }  
        else
        {
            Listesdelecture l = new Listesdelecture(1,1,"erreur avec le ticket",true,true,new Date());  
            listeDeLecture= new ArrayList<Listesdelecture>();
            listeDeLecture.add(l);
        }
        
        if(tickets.get(noTicket)!=null)
        {            
            tickets.get(noTicket).setNbEssais(tickets.get(noTicket).getNbEssais()+1);
            if(tickets.get(noTicket).getNbEssais()>=3)
            {
                tickets.remove(noTicket);
               // messageRetour="le ticket"+noTicket+"a été détruis apres vois 3 essais";
            }
        }
        
        return listeDeLecture;
    }
   
    @GET
    @Path("voirUneListeDeLectureALui/{noTicket}/{chaineConfirmation}/{idListe}/{idUtil}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON} )
    public  List<Musiques> voirUneListeDeLectureALui(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idListe") Integer idListe, @PathParam("idUtil") Integer idUtil) {
        //List<Musiques> musiquesInPlaylist = new ArrayList<Musiques>();

        
        
        List<ListesdelectureMusiques> listesdelectureMusiques = null;
        Query q3 = em.createNamedQuery("ListesdelectureMusiques.findByListeDeLecture");    
        q3.setParameter("listeDeLecture", idListe);
        try{
            listesdelectureMusiques = (List<ListesdelectureMusiques> ) q3.getResultList();          
        }
        catch(Exception ex){            
        } 
               
        Ticket ticket = tickets.get(noTicket);  
        
        List<Musiques> listeMusiques = null;
        listeMusiques = new ArrayList<Musiques>();
        
        if(listesdelectureMusiques!=null && listesdelectureMusiques.size()!=0)
        {
            
            for(int i=0;i<listesdelectureMusiques.size();i++)
            {
                Musiques musiqueFound = null;
                Query q = em.createNamedQuery("Musiques.findById");
                q.setParameter("id", listesdelectureMusiques.get(i).getListesdelectureMusiquesPK().getMusique());
                try{
                    musiqueFound = (Musiques) q.getSingleResult();
                }
                catch(Exception ex){

                }

                if(musiqueFound!=null)
                {
                    if((musiqueFound.getPublique()==true && musiqueFound.getActive()==true)|| musiqueFound.getProprietaire()==idUtil)
                    {                        
                        listeMusiques.add(musiqueFound);
                    }
                }
            }
        }
        else
        {
            Musiques m = new Musiques(-1,-1,"Cette liste de musique n'est pas disponible","null","null","null",true,true,new Date());
            listeMusiques.add(m);
        } 
        
        if(listeMusiques==null || listeMusiques.size()==0)
        {
            Musiques m = new Musiques(-1,-1,"Cette liste de musique ne contien pas de musiques","null","null","null",true,true,new Date());
            listeMusiques.add(m);
        }
              
        
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUtil == ticket.getIdUtil())
        {
            tickets.remove(noTicket);
        }
        else
        {
            listeMusiques.clear();
            Musiques m = new Musiques(-1,-1,"erreur ticket","null","null","null",true,true,new Date());
            listeMusiques.add(m);
        }
        
        
        
        
        
        if(tickets.get(noTicket)!=null)
        {            
            tickets.get(noTicket).setNbEssais(tickets.get(noTicket).getNbEssais()+1);
            if(tickets.get(noTicket).getNbEssais()>=3)
            {
                tickets.remove(noTicket);
                //messageRetour="le ticket"+noTicket+"a été détruis apres vois 3 essais";
            }
        }
        return listeMusiques;
    }
    
    @GET
    @Path("consulterMusiqueDansListe/{noTicket}/{chaineConfirmation}/{idUser}/{idMusique}")
    @Produces({ MediaType.APPLICATION_JSON})
    public Musiques consulterMusiqueDansListe(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation, 
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
        
            if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil()){
                if(musiqueFound.getProprietaire() == idUser){
                    musique = musiqueFound;
                    tickets.remove(noTicket);
                }
                else{
                    if(musiqueFound.getActive() && musiqueFound.getPublique()){
                        musique = musiqueFound;
                        tickets.remove(noTicket);
                    }
                }
            }
        }
        
        return musique;
    }
   
    @GET
    @Path("voirListeDeLectures")
    @Produces({ MediaType.APPLICATION_JSON})
    public List<Listesdelecture> voirListeDeLectures() {      
        em.getEntityManagerFactory().getCache().evictAll();
        
        List<Listesdelecture> listes = new ArrayList<Listesdelecture>();
        Query q = em.createNamedQuery("Listesdelecture.findByActive");
        q.setParameter("active", true);
        List<Listesdelecture> allListes = new ArrayList<Listesdelecture>();
        allListes=null;
        
        try{
            allListes = (List<Listesdelecture>) q.getResultList();            
        }
        catch(Exception ex){
            
        }
        
        for(int i = 0; i < allListes.size(); i++){
            if(/*allListes.get(i).getActive()==true && */allListes.get(i).getPublique()==true){
                listes.add(allListes.get(i));
            }
        }
        
        return listes;
    }
   
    @GET
    @Path("voirListeDeLecture/{idListe}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces({ MediaType.APPLICATION_JSON} )
    public List<Musiques> voirListeDeLecture(@PathParam("idListe") Integer idListe) {
        List<Musiques> musiquesInPlaylist = new ArrayList<Musiques>();

        List<ListesdelectureMusiques> listMusique = new ArrayList<ListesdelectureMusiques>();
        Query q = em.createNamedQuery("ListesdelectureMusiques.findMusiquesByListeDeLecture");
        q.setParameter("listeDeLecture", idListe);
       
        try{
            listMusique = (List<ListesdelectureMusiques>) q.getResultList();
        }
        catch(Exception ex){
           
        }
       
        List<ListesdelectureMusiquesPK> lmPKList = new ArrayList<ListesdelectureMusiquesPK>();
       
        //@NamedQuery(name = "Musiques.findById", query = "SELECT m FROM Musiques m WHERE m.id = :id")
       
        for(int i = 0; i < listMusique.size(); i++){
            
            ListesdelectureMusiquesPK lmPK = listMusique.get(i).getListesdelectureMusiquesPK();
            lmPKList.add(lmPK);
           
            int musiqueID = lmPK.getMusique();
            Musiques musique = new Musiques();
            Query query = em.createNamedQuery("Musiques.findById");
            query.setParameter("id", musiqueID);
 
            try{
                musique = (Musiques) query.getSingleResult();
            }
            catch(Exception ex){ 
            }
            if(musique!=null)
            {
                if(musique.getPublique()==true && musique.getActive()==true){
                    musiquesInPlaylist.add(musique);
                } 
            }
        }       
        return musiquesInPlaylist;
    }
   
    @GET
    @Path("creerListeDeLecture/{noTicket}/{chaineConfirmation}/{idUser}/{nom}/{publique}/{active}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String creerListeDeLecture(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUser") Integer idUser, @PathParam("nom") String nom,
            @PathParam("publique") boolean publique, @PathParam("active") boolean active) throws UnsupportedEncodingException {
        String messageRetour = "empty";
       
        Ticket ticket = tickets.get(noTicket);
       

        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUser == ticket.getIdUtil()){
            Listesdelecture listesdelecture = new Listesdelecture();
            listesdelecture.setProprietaire(idUser);
            listesdelecture.setNom(URLDecoder.decode(nom, "UTF-8"));
            listesdelecture.setPublique(publique);
            listesdelecture.setActive(active);
            listesdelecture.setDate(new Date());
            em.persist(listesdelecture);
            messageRetour = "la liste de lecture a eté ajoutée";
            tickets.remove(noTicket);

        }
        else{
            messageRetour = "la liste de lecture n'a pa été ajoutée.";
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
    @Path("modifierListeDeLecture/{noTicket}/{chaineConfirmation}/{Id}/{IdUtil}/{Nom}/{Publique}/{Active}")
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String modifierProfil(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,  
            @PathParam("Id") Integer id,@PathParam("IdUtil") Integer IdUtil, @PathParam("Nom") String nom, @PathParam("Publique") boolean publique , @PathParam("Active") boolean active ) throws UnsupportedEncodingException {
 
        String messageRetour = "";
        Listesdelecture listesdelecture = null;
        Query q = em.createNamedQuery("Listesdelecture.findById");
        q.setParameter("id", id);
             
        try{
            listesdelecture = (Listesdelecture) q.getSingleResult();
        }
        catch(Exception ex){
        }
       
        //String chaineToCompare = tickets.get(noTicket).getChaineConfirmation();
        Ticket ticket = tickets.get(noTicket);
         
        if(IdUtil == listesdelecture.getProprietaire())
        {
            if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && IdUtil == ticket.getIdUtil())
            {            
                listesdelecture.setNom(URLDecoder.decode(nom, "UTF-8"));
                listesdelecture.setPublique(publique);
                listesdelecture.setActive(active);
                super.edit(listesdelecture);
                messageRetour = "La liste a été modifié";
                tickets.remove(noTicket);

            }
            else{
                messageRetour = "Erreur avec le tickets";
            }
        }
        else
        {
            messageRetour = "c'est pas votre liste de lecture";
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