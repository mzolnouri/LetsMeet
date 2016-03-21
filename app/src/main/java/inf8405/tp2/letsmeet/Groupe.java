package inf8405.tp2.letsmeet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by youssef on 24/02/2016.
 */
public class Groupe {
    private String id_=new String();
    private String name_=null;
    private Map<String,Utilisateur> usersMap_ = new HashMap<String,Utilisateur>();
    private UUID uidFormat_ = UUID.fromString("91c83b36-e25c-11e5-9730-9a79f06e9478");
    private Rencontre rencontre_=new Rencontre();

    public Groupe()
    {
        id_=uidFormat_.randomUUID().toString();
        name_="sans";
        rencontre_=null;
    }

    public Groupe(String nom)
    {
        id_=uidFormat_.randomUUID().toString();
        name_=nom;
        rencontre_=null;
    }

    public String getGroupName()
    {
        return name_;
    }

    public void setGroupName(String name)
    {
        name_=name;
    }

    public String getId()
    {
        return id_;
    }

    public void setId(String id)
    {
        id_=id;
    }

    public Map<String, Utilisateur> getUsersMap() {
        return usersMap_;
    }

    public void setUsersMap(Map<String, Utilisateur> usersMap) {
        this.usersMap_ = usersMap;
    }

    public void deleteUser(String id)
    {
        usersMap_.remove(id);
    }
    public boolean addUser(Utilisateur user)
    {
        if(!usersMap_.containsKey(user.getId()))
        {
            usersMap_.put(user.getId(),user);
            return true;
        }
        return false;
    }

    public Rencontre getRencontre() {
        return rencontre_;
    }

    public boolean setRencontre(Rencontre rencontre) {
        if (rencontre_==null)
        {
            this.rencontre_ = rencontre;
            return true;
        }
        return false;
    }
}
