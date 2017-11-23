package entites;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-11-23T11:31:01")
@StaticMetamodel(Musiques.class)
public class Musiques_ { 

    public static volatile SingularAttribute<Musiques, Date> date;
    public static volatile SingularAttribute<Musiques, Integer> proprietaire;
    public static volatile SingularAttribute<Musiques, String> vignette;
    public static volatile SingularAttribute<Musiques, String> titre;
    public static volatile SingularAttribute<Musiques, String> artiste;
    public static volatile SingularAttribute<Musiques, String> musique;
    public static volatile SingularAttribute<Musiques, Boolean> active;
    public static volatile SingularAttribute<Musiques, Integer> id;
    public static volatile SingularAttribute<Musiques, Boolean> publique;

}