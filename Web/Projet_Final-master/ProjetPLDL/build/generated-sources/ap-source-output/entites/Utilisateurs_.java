package entites;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-07T15:00:30")
@StaticMetamodel(Utilisateurs.class)
public class Utilisateurs_ { 

    public static volatile SingularAttribute<Utilisateurs, Date> date;
    public static volatile SingularAttribute<Utilisateurs, String> motDePasse;
    public static volatile SingularAttribute<Utilisateurs, String> alias;
    public static volatile SingularAttribute<Utilisateurs, Boolean> actif;
    public static volatile SingularAttribute<Utilisateurs, Integer> id;
    public static volatile SingularAttribute<Utilisateurs, Integer> avatar;
    public static volatile SingularAttribute<Utilisateurs, String> courriel;

}