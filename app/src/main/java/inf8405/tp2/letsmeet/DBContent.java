package inf8405.tp2.letsmeet;

import android.util.ArrayMap;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.TemplatesHandler;

import okhttp3.internal.Util;

/**
 * Created by youssef on 05/03/2016.
 */
public class DBContent {

    // la map contient les utilisateurs qui appartiennent au groupe auquel appartient l'utilisateur
    private Map<String,Utilisateur> userMap_ = new HashMap<String,Utilisateur>();
    private Map<String,Preference> preferencesMap_ = new HashMap<String,Preference>();
    private Map<String,Groupe> groupsMap_ = new HashMap<String,Groupe>();
    private Map<String,Position> positionsMap_ = new HashMap<String,Position>();
    private String actualGroupId_= "aff7a903-2d99-4bd1-b16b-69a2193d3284";
    private String actualUserId_=new String();
    // seul attribut commun permettant de verifier la reponse a la requette http a l'interieur d'un thread
    private boolean flagForResponses = false;
    private String responseStr = new String();

    // instance du singleton
    private static DBContent instance_ = null;

    /**
     * Constructeur private
     */
    private DBContent ()
    {

    }

    /**
     * Cette fonction permet de retourner l'instance du singleton
     * @return
     */
    public static DBContent getInstance()
    {
        if(instance_==null)
        {
            instance_ = new DBContent();
        }
        return instance_;
    }

    /**
     * Cette classe permet de detruire l'instance du singleton
     */
    public static void destroyInstance()
    {
        instance_=null;
    }

    /**
     * Cette fonction permet d'ajouter une preference a l'utilisateur actuel
     * @param priorite
     * @param adresse
     * @return boolean true si ajoute, false sinon
     */

    public boolean addPreference(String priorite, String adresse)
    {
        if(!priorite.contentEquals(Constants.highPriority)&&!priorite.contentEquals(Constants.mediumPriority)&&!priorite.contentEquals(Constants.lowPriority) && !priorite.contentEquals(Constants.DefaultPriority))
            return false;
        Preference preference = new Preference(adresse,priorite,actualUserId_);
        userMap_.get(actualUserId_).addPreferences(preference);
        return true;
    }

    /**
     * Fonction permettant dechanger deux preferences deja existantes
     * @param x index du 1er element  ( commence par 0)
     * @param y index du 2eme element
     */
    public void switchUserPreferences(int x,int y)
    {
        userMap_.get(actualUserId_).switchPreferencesPriorities(x, y);
    }

    /**
     * Cette fonction permet d'attribuer un objet de type rencontre a l'utilisateur actuel
     * @param lieu
     * @param description
     * @param date
     * @return boolean si la rencontre a ete attribue a l'utilisateur actuel
     */
    public boolean creerRencontre(String lieu, String description, String date)
    {
        if (!groupsMap_.containsKey(actualGroupId_))
            return false;

        return groupsMap_.get(actualGroupId_).setRencontre(new Rencontre(description, actualUserId_, actualGroupId_, date));
    }

    /**
     * cette fonction permet de recuperer la recontre du groupe actuel, si la rencontre nest pas creer essaye de la recupere de la base de donnee
     * @return objet de type rencontre ou null si aucune rencontre nest cree
     */
    public Rencontre getActualGroupeRencontre()
    {
        if(groupsMap_.get(actualGroupId_).getRencontre()==null)
        {
            Rencontre rencontre=getRencontreFromRemoteContent(actualGroupId_);
            if(rencontre==null)
            {
                return null;
            }
            else {
                groupsMap_.get(actualGroupId_).setRencontre(rencontre);
            }
        }
        return groupsMap_.get(actualGroupId_).getRencontre();
    }

    /**
     * Cette fonction permet de recuperer les informations de la rencontre du groupe identifie par idDuGroupe
     * @param idDuGroupe
     * @return un objet de type rencontre sinon null ( en cas de probleme)
     */
    public Rencontre getRencontreFromRemoteContent(final String idDuGroupe)
    {
        final Rencontre[] rencontre = new Rencontre[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    rencontre[0] =Parseur.ParseJsonToRencontre(DBConnexion.getRequest("http://najibarbaoui.com/najib/rencontrebygroupe.php?id_groupe="+idDuGroupe));
                } catch (JSONException e) {
                    e.printStackTrace();
                    rencontre[0]=null;
                } catch (IOException e) {
                    e.printStackTrace();
                    rencontre[0]=null;
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return rencontre[0];
    }
    public void AjouterActiviteUtilisateur()
    {


    }

    /**
     * Cette fonction recupere la rencontre selon le groupeId si la rencontre nest pas en local il la recupere et la set au local
     * @param groupid
     * @return objet de type rencontre
     */
    public Rencontre getRencontreByGroupeId(String groupid)
    {
        if(groupsMap_.containsKey(groupid))
        {
            if(groupsMap_.get(groupid).getRencontre()==null)
            {
                groupsMap_.get(groupid).setRencontre(getRencontreFromRemoteContent(groupid));
            }
        }
        else
        {
            return getRencontreFromRemoteContent(groupid);
        }

        return groupsMap_.get(groupid).getRencontre();
    }


    /**
     * Cette fonction permet de recuperer l'utilisateur actuel
     * @return un objet de type utilisateur ou null si la map na pas ete instancie
     */
    public Utilisateur getActualUser()
    {
        if (userMap_.containsKey(actualUserId_))
          return userMap_.get(actualUserId_);
        else
            return null;
    }

    /**
     * cette fonction permet de recuperer le groupe actuel
     * @return un objet de type Groupe ou null si la map n'a pas ete instancie
     */
    public Groupe getActualGroup()
    {
        if(groupsMap_.containsKey(actualGroupId_))
        return groupsMap_.get(actualGroupId_);
        else
            return null;
    }

    /**
     * Cette fonction permet d'ajouter un nouvel utilisateur a la base de donnee
     * @param NUtilisateur
     * @return retourne l'information si l'utilisateur est ajoute ou pas
     */
    public String CreerNouvelUtilisateur(final Utilisateur NUtilisateur )
    {
        responseStr=Constants.UserNotAdded;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // todo password enregistre localement est dangereux, voir solution alternative
                try {
                    Log.d("CreerNouvelUtilisateur", "cest mon test a moi");
                    // reponse true ou false du cote serveur
                    String reponsePost = DBConnexion.postRequest("http://najibarbaoui.com/najib/insert_utilisateur.php", Parseur.ParseUserToJsonFormat(NUtilisateur));
                    if(reponsePost.contentEquals("1"))
                    {
                        responseStr=Constants.UserAdded;
                        userMap_.put(NUtilisateur.getId(),NUtilisateur);
                        actualUserId_=NUtilisateur.getId();
                        actualGroupId_=NUtilisateur.getGroupeId();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;

    }

    /**
     * Cette fonction permet l'authentification d'un utilisateur en utilisant son courriel et mot de passe
     * @param courriel
     * @param password
     * @return si l'email est errone, le password est errone ou si l'acces est valide
     */
    // authentification envoit une requette au serveur le serveur renvoit une reponse positive ou negative
    // si positive, renvoit les infos de l<utilisateur sinon renvoit quelle est le probleme
    public String authentification(final String courriel, final String password)
    {;
        responseStr=Constants.WrongEmail;
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Log.d("authentification", "c<est mon test a moi");
                    String reponsePost= DBConnexion.postRequest("http://najibarbaoui.com/najib/ouvrirsession.php",Parseur.ParseAuthentificationInfoToJsonFormat(courriel, password));

                    if(reponsePost.contentEquals("0"))
                    {
                        responseStr=Constants.WrongPassword;
                    }
                    else if(!reponsePost.contentEquals("1"))
                    {
                        responseStr=Constants.AccessGranted;
                        Utilisateur utilisateurActuel;
                        utilisateurActuel = Parseur.ParseJsonToUser(reponsePost);
                        actualUserId_=utilisateurActuel.getId();
                        actualGroupId_=utilisateurActuel.getGroupeId();
                        userMap_.put(actualUserId_, utilisateurActuel);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    // fonction de mise a jour de la position de l'utilisateur actuel dans la BD

    /**
     * Cette fonction permet de mettre a jour la position de l'utilisateur actuel dans la base de donnee
     */
    public void UpdateRemotePosition()
    {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                Log.d("UpdatePosition", "c mon test a moi");
                try {
                    DBConnexion.postRequest("http://najibarbaoui.com/najib/update_position.php",
                            Parseur.ParsePositionToJsonFormat(userMap_.get(actualUserId_).getPosition()));

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * cette fonction recupere la liste de tout les utilisateurs
     * @return
     */
    public  Map<String,Utilisateur> getAllUsers()
    {
        // le clear au cas ou la map contient deja klke chose
        final List<Map<String,Utilisateur>> maps = new ArrayList<Map<String,Utilisateur>>();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Users test", "c mon test a moi");
                try{
                    maps.add(Parseur.ParseToUsersMap(DBConnexion.getRequest("http://najibarbaoui.com/najib/utilisateurs.php"))) ;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return maps.get(0);
    }

    /**
     * Cette fonction recupere les informations d'un utilisateur selon son id
     * @param idUser
     * @return objet de type utilisateur
     */

    public Utilisateur getUserById(final String idUser)
    {
        // verification si l'utilisateur nest pas deja la
        if( userMap_.containsKey(idUser))
        {
            return userMap_.get(idUser);
        }
        else
        {
            final Utilisateur[] user = new Utilisateur[1];
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        user[0] = Parseur.ParseJsonToUser(DBConnexion.getRequest("http://najibarbaoui.com/najib/utilisateur.php?id_utilisateur="
                        + idUser));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return user[0];
        }
    }

    /**
     *  recuperation des informations d'un groupe a partir de l'id d'un utilisateur de ce groupe
     * @param userId
     * @return Objet de type groupe
     */

    public Groupe getGroupdInformationFromUserId(final String userId)
    {
        final Groupe[] groupe = new Groupe[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String reponse = DBConnexion.getRequest("http://najibarbaoui.com/najib/groupebyidutilisateur.php?id_utilisateur="+
                            userId);
                    groupe[0] =Parseur.ParseJsonToGroup(reponse);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return groupe[0];
    }

    /**
     * fonction qui permet de recuperer les informations de tout les groupes
     * @return
     */
    public  Map<String, Groupe> getAllGroupsInformations()
    {
       // au cas ou
        groupsMap_.clear();
        Thread GroupsThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Groups test", "c mon test a moi");
                DBConnexion con=new DBConnexion();
                try {
                    groupsMap_ = Parseur.ParseToGroupeMap(con.getRequest("http://najibarbaoui.com/najib/groupe.php"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        GroupsThread.start();
        try {
            GroupsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return groupsMap_;
    }

    /**
     *  cette fonction permet de creer un groupe dans la base de donnees, id est donnee dans le constructeur
     * @param nom , nom donnee au groupe
     * @return une constante indiquant si le groupe a ete ajoute ou pas
     */
    public String AddNewGroupToRemoteContent(String nom)
    {
        responseStr=Constants.GroupNotAdded;
        final Groupe group = new Groupe(nom);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String reponse = DBConnexion.postRequest("http://najibarbaoui.com/najib/insert_groupe.php",
                            Parseur.ParseGroupToJsonFormat(group));
                    if(reponse.contentEquals("1"))
                    {
                        responseStr=Constants.GroupAdded;
                        actualGroupId_=group.getId();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return responseStr;
    }

    /**
     * Ajout des preferences de l'utilisateur actuel
     * @param user
     */
    // ajout des preferences a la db
    public void addPreferencesToRemoteContent(final Utilisateur user)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!userMap_.containsKey(actualUserId_))
                    {
                        userMap_=getAllUsers();
                    }
                    String preferencesStr = Parseur.ParsePreferencesToJsonFormat(userMap_.get(actualUserId_));
                    DBConnexion.postRequest("http://najibarbaoui.com/najib/insert_preferences.php",preferencesStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * cette fonction permet de recuperer les informations de tout les utilisateurs du groupe dont l'id est idGroupe
     * @param idGroupe
     * @return map contenant comme cle l id del'utilisateur et valeur l objet utilisateur
     */
    //
    public  Map<String,Utilisateur> GetUsersFromGroup(final String idGroupe)
    {
        // le clear au cas ou
        final List<Map<String,Utilisateur>> maps = new ArrayList<Map<String,Utilisateur>>();
        Thread UsersThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Users test", "c mon test a moi");
                try{
                    maps.add(Parseur.ParseToUsersMap(DBConnexion.getRequest("http://najibarbaoui.com/najib/utilisateurbygroupe.php?id_groupe="
                            + idGroupe)));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        UsersThread.start();
        try {
            UsersThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return maps.get(0);
    }

    /**
     * Cette fonction mmet a jour les informations de l'utilisateur actuel sur la base de donnees
     */
    public void updateUserInformationInRemoteContent()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   String temp = DBConnexion.postRequest("http://najibarbaoui.com/najib/update_utilisateur.php",Parseur.ParseUserToJsonFormat(userMap_.get(actualUserId_)));
                    Log.d("okkkkkkaa",temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     */
    public void mettreAjourPositionsMembresDuGroupe()
    {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                // todo right url
                String url = "";
                try {
                    url+="?groupe_idgroupe=" + URLEncoder.encode(actualGroupId_,"UTF-8");
                    Map<String, Position> newPositions= new HashMap<String, Position>();

                    try {
                        newPositions = Parseur.ParseToPositionsMap(DBConnexion.getRequest(url));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    for(Map.Entry<String, Utilisateur> entry : userMap_.entrySet())
                    {
                        if(newPositions.containsKey(entry.getValue().getPositionId()))
                        {
                            entry.getValue().setPosition(newPositions.get(entry.getValue().getPositionId()));
                        }
                        else
                        {
                            Log.d("Probelem","when updating users position");
                        }
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recuperation de la rencontre du groupe dont l id est celui dans l'argument
     * @param idGroupe
     * @return rencontre du groupe
     */
    public Rencontre recupererLaRencontreGroupeBD(final String idGroupe)
    {
        final Rencontre[] rencontre = new Rencontre[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String reponse = DBConnexion.getRequest("http://najibarbaoui.com/najib/rencontrebygroupe.php?id_groupe="
                    + idGroupe);
                    rencontre[0] =Parseur.ParseJsonToRencontre(reponse);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return rencontre[0];
    }

    /**
     * TODO
     */
    public void SynchronizeLocalPreferencesFromRemoteContent()
    {
        Thread PreferencesThread = new Thread(new Runnable() {
            public void run() {
                Log.d("Preferences test ", "c mon test a moi");
                DBConnexion con=new DBConnexion();
                try{
                    // TODO set the right url
                    preferencesMap_ = Parseur.ParseToPreferencesMap(con.getRequest("http://najibarbaoui.com/najib"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        PreferencesThread.start();
        try {
            PreferencesThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fonction de recuperation de la position d'un utilisateur
     * @param userid
     * @return position de l'utilisateur associe au userid
     */
    public  Position GetUserPosition(final String userid)
    {
        final Position[] position = new Position[1];
        Thread PositionsThread= new Thread(new Runnable() {
            public void run() {
                Log.d("Positions test", "c mon test a moi");
                try{
                    if (!userMap_.containsKey(userid))// 0661359033// 661903420
                    {
                       userMap_=getAllUsers();
                    }
                    // TODO set the right url
                    position[0] = Parseur.ParseToPosition(DBConnexion.getRequest("http://najibarbaoui.com/najib/position.php?id_position=" +
                            userMap_.get(userid).getPositionId()));
                    if (userMap_.containsKey(userid))
                    {
                        userMap_.get(userid).setPosition(position[0]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        PositionsThread.start();
        try {
            PositionsThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return position[0];
    }

    void uploadActivitiesToRemoteContent()
    {
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DBConnexion.postRequest("http://najibarbaoui.com/najib/insert_calendrier.php", Parseur.ParseActivitiesToJsonFormat(userMap_.get(actualUserId_)));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public Map<String, Utilisateur> getUserMap() {
        return userMap_;
    }

    public void setUserMap_(Map<String, Utilisateur> userMap) {
        this.userMap_ = userMap;
    }

    public Map<String, Preference> getPreferencesMap() {
        return preferencesMap_;
    }

    public void setPreferencesMap(Map<String, Preference> preferencesMap) {
        this.preferencesMap_ = preferencesMap;
    }

    public Map<String, Groupe> getGroupsMap() {
        return groupsMap_;
    }

    public void setGroupsMap(Map<String, Groupe> groupsMap) {
        this.groupsMap_ = groupsMap;
    }

    public Map<String, Position> getPositionsMap() {
        return positionsMap_;
    }

    public void setPositionsMap(Map<String, Position> positionsMap) {
        this.positionsMap_ = positionsMap;
    }

    public String getActualGroupId() {
        return actualGroupId_;
    }

    public void setActualGroupId(String actualGroupId) {
        this.actualGroupId_ = actualGroupId;
    }

    public String getActualUserId() {
        return actualUserId_;
    }

    public void setActualUserId(String actualUserId_) {
        this.actualUserId_ = actualUserId_;
    }
}
