package inf8405.tp2.letsmeet;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// INF8405 - Laboratoire 2
//Auteurs : Najib Arbaoui (1608366) && Youssef Zemmahi (1665843) && Zolnouri Mahdi (1593999)
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

    public void setRencontre(Rencontre rencontre) {

            this.rencontre_ = rencontre;
    }
}
