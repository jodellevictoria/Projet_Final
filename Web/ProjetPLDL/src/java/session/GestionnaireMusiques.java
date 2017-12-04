/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entites.Utilisateurs;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 *
 * @author jodel
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestionnaireMusiques {
 
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
   
    @PersistenceContext(unitName = "ProjetPLDLPU")
    private EntityManager em;
    @Resource
    private SessionContext context;
 
   
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int placerUtilisateur(String courriel, String motDePasse, String alias, int avatar, boolean actif, Date date) {
       
        try {
            Utilisateurs utilisateur = ajouterUtilisateur(courriel, motDePasse, alias, avatar, actif, date);            
            return 1;
        } catch (Exception e) {
            context.setRollbackOnly();
            return 10;
        }
    }
 
    private Utilisateurs ajouterUtilisateur(String courriel, String motDePasse, String alias, int avatar, boolean actif, Date date) {
        Utilisateurs utilisateur = new Utilisateurs();
        utilisateur.setCourriel(courriel);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setAlias(alias);
        utilisateur.setAvatar(avatar);
        utilisateur.setAvatar(avatar);
        utilisateur.setActif(actif);
        utilisateur.setDate(date);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = factory.getValidator();        
        em.persist(utilisateur);
        return utilisateur;
    }
 
   
   
}