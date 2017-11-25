/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entites.service;

import entites.Musiques;
import java.util.ArrayList;
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
    @Path("musiquePublique/{idMusique}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Musiques musiquePublique(@PathParam("idMusique") Integer idMusique) {
        Musiques musique = null;
        
        Query q = em.createNamedQuery("Musiques.findById");
        
        try{
            Musiques musiqueFound = (Musiques) q.getSingleResult();
            if(musiqueFound != null){
                if(musiqueFound.getActive() && musiqueFound.getPublique()){
                    musique = musiqueFound;
                }
            }
        }
        catch(Exception ex){
            
        }
        /*List<Musiques> musiques = new ArrayList<Musiques>();
        
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
            
        }*/
        
        return musique;
    }
}
