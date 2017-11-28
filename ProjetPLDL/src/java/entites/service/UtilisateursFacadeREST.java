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
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;
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
import sun.misc.BASE64Encoder;

/**
 *
 * @author jodel
 */
@Stateless
@Path("utilisateurs")
public class UtilisateursFacadeREST extends AbstractFacade<Utilisateurs> {

    public static HashMap<Integer, Ticket> tickets = new HashMap<Integer, Ticket>();
    
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
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
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
            tickets.put(noTicket, ticket);
            ticketReturn = new TicketToReturn(ticket.getNoTicket(),ticket.getCle());
        }
        else{
            retour = "-1";
            ticketReturn = new TicketToReturn(-1,"-1");
        }
        
        return ticketReturn;
    }

    
     @GET
    @Path("creer/{numTicket}/{captcha}")
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
 
               
                //placerUtilisateur(String courriel, String motDePasse, String alias, int avatar, boolean actif, Date date)
                
                
                int idUtilisateur = gestionnaireCommande.placerUtilisateur(listTickets.get(i).getCourriel(),listTickets.get(i).getMotDePasse(),listTickets.get(i).getNom(),listTickets.get(i).getAvatar(),true,date );
                
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
            flux="erreur, c'est pas le bon captcha ou pas le bon numero de ticket";
        }
       
        return flux;
    }
   
   
    @GET
    @Path("creer/{nom}/{courriel}/{motDePasse}/{avatar}")
    //@Produces(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    //@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.TEXT_PLAIN)
   
    public TicketCaptchaReturn creeUser(@PathParam("nom") String nom, @PathParam("courriel") String courriel, @PathParam("motDePasse") String motDePasse, @PathParam("avatar") int avatar  ) {      
       
        int width = 150;
        int height = 50;
        List arrayList = new ArrayList();
        String capcode = "abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMONOPQURSTUVWXYZ0123456789!@#$%&*";
        for (int i = 1; i < capcode.length() - 1; i++) {
            arrayList.add(capcode.charAt(i));
        }
        Collections.shuffle(arrayList);
        Iterator itr = arrayList.iterator();
        String s = "";
        String s2 = "";
        Object obj;
        while (itr.hasNext()) {
            obj = itr.next();
            s = obj.toString();
            s2 = s2 + s;
        }
        String s1 = s2.substring(0, 6);
        char[] s3 = s1.toCharArray();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        Font font = new Font("Georgia", Font.BOLD, 18);
        g2d.setFont(font);
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(rh);
        GradientPaint gp = new GradientPaint(0, 0, Color.red, 0, height / 2, Color.black, true);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(new Color(255, 153, 0));
        Random r = new Random();
        int index = Math.abs(r.nextInt()) % 5;
       
       
        String captcha2 = String.copyValueOf(s3);
        //request.getSession().setAttribute("captcha", captcha2);
        int x = 0;
        int y = 0;
        for (int i = 0; i < s3.length; i++) {
            x += 10 + (Math.abs(r.nextInt()) % 15);
            y = 20 + Math.abs(r.nextInt()) % 20;
            g2d.drawChars(s3, i, 1, x, y);
        }
        g2d.dispose();
        //response.setContentType("image/png");
       
       
       
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
 
        try {
            ImageIO.write(bufferedImage, "png", bos);
            byte[] imageBytes = bos.toByteArray();
 
            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);
 
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return imageString;
       
       
       
       
       
       
        Random xx = new Random();
        int n = xx.nextInt(1000000000);      
        String numTicket = n+"";      
        Tickets ticket =null ;
        String flux = "lol";
       
 
       
        //le client
        Query q = em.createNamedQuery("Utilisateurs.findByCourriel");
        q.setParameter("courriel", courriel);
        Utilisateurs utilisateurs = null;
        try{
            utilisateurs = (Utilisateurs) q.getSingleResult();
        }
        catch(Exception ex){  
           
        }
       
        //le client
        Query q2 = em.createNamedQuery("Utilisateurs.findByAlias");
        q2.setParameter("alias", nom);
        Utilisateurs utilisateurs2 = null;
        try{
            utilisateurs2 = (Utilisateurs) q2.getSingleResult();
        }
        catch(Exception ex){  
           
        }
        
     
       
       
        TicketCaptchaReturn ticketCaptchaReturn = null;
        if(utilisateurs==null && utilisateurs2==null && avatar<=36)
        {
            boolean boolTempo = true;  
            for(int i =0;i<listTickets.size();i++ )
            {
                if(listTickets.get(i).getCourriel().compareTo(courriel)==0 || listTickets.get(i).getNom().compareTo(nom)==0)
                {
                    boolTempo=false;
                    listTickets.remove(listTickets.get(i));
                    break;
                }
            }
           
            //flux = numTicket + nom + courriel + motDePasse + captcha2;
            String motDePasseMD5 = "";
            try{
                MessageDigest m=MessageDigest.getInstance("MD5");
                m.update(motDePasse.getBytes(),0,motDePasse.length());
                //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
                motDePasseMD5 = new BigInteger(1,m.digest()).toString(16);
                System.out.println(motDePasseMD5);
            }
            catch(Exception e){
 
            }
            //    public Tickets(String numTicket, String nom, String courriel, String motDePasse, String captcha) {
            ticket = new Tickets(numTicket,nom,courriel,motDePasseMD5,captcha2,avatar);
            listTickets.add(ticket);
            ticketCaptchaReturn = new TicketCaptchaReturn(ticket.getNumTicket(),ticket.getCaptcha(),imageString);
        }
        else
        {
            if(utilisateurs!=null)
            {
                ticketCaptchaReturn = new TicketCaptchaReturn("-1","-1","-1");
            }
            else if(utilisateurs2!=null)
            {
                ticketCaptchaReturn = new TicketCaptchaReturn("-2","-2","-2");
            } 
            else if(avatar>36){
                ticketCaptchaReturn = new TicketCaptchaReturn("-3","-3","-3");
            }
                
        }
       
        return ticketCaptchaReturn;    
        //return imageString;
    }
    
    @GET
    @Path("modifierProfil/{noTicket}/{chaineConfirmation}/{idUtil}/{courriel}/{motDePasse}/{alias}/{avatar}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.TEXT_PLAIN})
    public String modifierProfil(@PathParam("noTicket") Integer noTicket, @PathParam("chaineConfirmation") String chaineConfirmation,
            @PathParam("idUtil") Integer idUtil, @PathParam("courriel") String courriel, @PathParam("motDePasse") String motDePasse,
            @PathParam("alias") String alias, @PathParam("avatar") Integer avatar) {
        String messageRetour = "";
        Utilisateurs util = null;
        Query q = em.createNamedQuery("Utilisateurs.findById");
        q.setParameter("id", idUtil);
       
        try{
            util = (Utilisateurs) q.getSingleResult();
        }
        catch(Exception ex){
        }
       
       
        Ticket ticket = tickets.get(noTicket);
        if(ticket != null && ticket.getChaineConfirmation().equals(chaineConfirmation) && idUtil == ticket.getIdUtil()){
            util.setCourriel(courriel);
            util.setAlias(alias);
            String motDePasseMD5 = "";
            try{
                MessageDigest m=MessageDigest.getInstance("MD5");
                m.update(motDePasse.getBytes(),0,motDePasse.length());
                //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
                motDePasseMD5 = new BigInteger(1,m.digest()).toString(16);
            }
            catch(Exception e){
                
            }
                
            util.setMotDePasse(motDePasseMD5);
            util.setAvatar(avatar);
            super.edit(util);
            messageRetour = "L'utilisateur a été modifié";
            tickets.remove(noTicket);
        }
        else{
            messageRetour = "Erreur avec le ticket";
        }
        //super.edit(entity);
       
        return messageRetour;
        //return "asd";
    }
    
    @GET
    @Path("validerUtilisateur/{courriel}/{motDePasse}")
    //@Produces(MediaType.TEXT_PLAIN)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    //@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes(MediaType.TEXT_PLAIN)
    public String validerUtilisateur(@PathParam("courriel") String courriel, @PathParam("motDePasse") String motDePasse) {
        String retour = "";
        Query q = em.createNamedQuery("Utilisateurs.findByCourriel");
        q.setParameter("courriel", courriel);
                
        Utilisateurs util = null;
        try{
            util = (Utilisateurs) q.getSingleResult();
        }
        catch(Exception ex){
            
        }
        String mdpMD5 = "";
        try {
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(motDePasse.getBytes(),0,motDePasse.length());
            //System.out.println("MD5: "+new BigInteger(1,m.digest()).toString(16));
            mdpMD5 = new BigInteger(1,m.digest()).toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UtilisateursFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(util != null){
            if(util.getMotDePasse().equals(mdpMD5)){
                retour = "connexion établie";
            }
            else{
                retour = "Mot de passe invalide";
            }
        }
        else{
            retour = "courriel n'est pas dans la base";
        }
        
        return retour;
    }
    
}
