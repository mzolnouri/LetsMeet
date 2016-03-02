package zemmahi.youssef.letsmeet;

/**
 * Created by youssef on 24/02/2016.
 */
public class Group {
    private Integer id_=0;
    private String name_=null;

    public Group()
    {
        name_="sans";
    }


    public Group(String nom)
    {
        name_=nom;
        id_= nom.hashCode();
    }

    public String getGroupName()
    {
        return name_;
    }

    public void setGrouoName(String name)
    {
        name_=name;
        id_= name.hashCode();
    }

    public Integer getId()
    {
        return id_;
    }
    /* au cas ou l'on decide de creer differement l'id
    public void setId(int id)
    {
        id_=id;
    }*/
}
